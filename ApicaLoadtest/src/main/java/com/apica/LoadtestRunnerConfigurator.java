/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica;

import com.amazonaws.util.json.Jackson;
import com.apica.constants.StringConstants;
import com.apica.loadtest.thresholds.LoadtestThresholdMetric;
import com.apica.loadtest.thresholds.LoadtestThresholdMetricFactory;
import com.apica.loadtest.thresholds.Threshold;
import com.apica.loadtest.thresholds.ThresholdDirection;
import com.apica.loadtest.thresholds.ThresholdDirectionFactory;
import com.apica.loadtest.thresholds.ThresholdViewModel;
import com.apica.loadtest.thresholds.ThresholdViewModelListContainer;
import com.apica.loadtest.validation.JobParameterValidationService;
import com.apica.loadtest.validation.LoadtestJobParameterValidationService;
import com.apica.loadtest.validation.PresetResponse;
import com.apica.loadtest.validation.RunnableFileResponse;
import com.apica.ssl.LtpWebApiHostnameVerifier;
import com.apica.ssl.LtpWebApiTrustManager;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.util.concurrent.NotNull;
import com.atlassian.util.concurrent.Nullable;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.atlassian.bamboo.task.BuildTaskRequirementSupport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author andras.nemes
 */
public class LoadtestRunnerConfigurator extends AbstractTaskConfigurator implements BuildTaskRequirementSupport
{

    private final String apiTokenKey = StringConstants.API_TOKEN_KEY;
    private final String presetKey = StringConstants.PRESET_KEY;
    private final String scenarioFileKey = StringConstants.SCENARIO_FILE_NAME_KEY;
    private final String thresholdsKey = StringConstants.THRESHOLDS_KEY;
    private final JobParameterValidationService parameterValidationService;
    private ApplicationProperties applicationProperties;
    private final String thresholdValuePrefix = "threshold_value_";
    private final String thresholdDirectionPrefix = "threshold_direction_";
    private final String thresholdMetricPrefix = "threshold_metric_";
    private static final Logger log = Logger.getLogger(LoadtestRunnerConfigurator.class);

    public LoadtestRunnerConfigurator()
    {
        this(new LoadtestJobParameterValidationService(new LtpWebApiHostnameVerifier(), new LtpWebApiTrustManager()));
    }

    public LoadtestRunnerConfigurator(JobParameterValidationService parameterValidationService)
    {
        this.parameterValidationService = parameterValidationService;
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, 
            @Nullable final TaskDefinition previousTaskDefinition)
    {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(apiTokenKey, params.getString(apiTokenKey));
        config.put(presetKey, params.getString(presetKey));
        config.put(scenarioFileKey, params.getString(scenarioFileKey));
        config.put("baseUrl", this.applicationProperties.getBaseUrl(UrlMode.ABSOLUTE));  
        List<Threshold> thresholds = getThresholdsFromParams(params);
        ThresholdViewModelListContainer thresholdsContainer = new ThresholdViewModelListContainer();
        thresholdsContainer.setFromThresholds(thresholds);
        config.put(thresholdsKey, Jackson.toJsonString(thresholdsContainer));
        log.info("LOADTEST PLUGIN ADDED");
        return config;
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection)
    {
        super.validate(params, errorCollection);        
        try
        {
            getThresholdsFromParams(params);
        }
        catch (Exception ex)
        {
            errorCollection.addError(scenarioFileKey, ex.getMessage());
        }
        final String tokenProvidedByUser = params.getString(apiTokenKey);
        final String presetProvidedByUser = params.getString(presetKey);
        final String scenarioNameProvidedByUser = params.getString(scenarioFileKey);
        boolean clientSideValidationOk = true;

        if (StringUtils.isEmpty(tokenProvidedByUser) || StringUtils.isBlank(tokenProvidedByUser))
        {
            errorCollection.addError(apiTokenKey, "You must provide a valid LTP authentication token");
            clientSideValidationOk = false;
        }

        if (StringUtils.isEmpty(presetProvidedByUser) || StringUtils.isBlank(presetProvidedByUser))
        {
            errorCollection.addError(presetKey, "You must provide a valid preset name");
            clientSideValidationOk = false;
        }

        if (StringUtils.isEmpty(scenarioNameProvidedByUser) || StringUtils.isBlank(scenarioNameProvidedByUser))
        {
            errorCollection.addError(scenarioFileKey, "You must provide a valid scenario name");
            clientSideValidationOk = false;
        }

        if (clientSideValidationOk) //if client side validation is fine then continue with the server side validation
        {
            PresetResponse presetResponse = this.parameterValidationService.checkPreset(tokenProvidedByUser, presetProvidedByUser);
            if (!presetResponse.isPresetExists())
            {
                errorCollection.addError(presetKey, "Cannot find this preset: ".concat(presetProvidedByUser).concat(". "));
                if (presetResponse.getException() != null && !presetResponse.getException().equals(""))
                {
                    errorCollection.addError(presetKey, "Exception while checking preset: ".concat(presetResponse.getException()));
                }
            } else //validate test instance id
             if (presetResponse.getTestInstanceId() < 1)
                {
                    errorCollection.addError(presetKey, "The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset.");
                }

            RunnableFileResponse runnableFileResponse = parameterValidationService.checkRunnableFile(tokenProvidedByUser, scenarioNameProvidedByUser);
            if (!runnableFileResponse.isFileExists())
            {
                if (runnableFileResponse.getException() != null && !runnableFileResponse.getException().equals(""))
                {
                    errorCollection.addError(scenarioFileKey, "Exception while checking load test file: ".concat(runnableFileResponse.getException()));
                } else
                {
                    errorCollection.addError(scenarioFileKey, "No such load test file found: ".concat(scenarioNameProvidedByUser));
                }
            }
        }
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context)
    {
        super.populateContextForCreate(context);
        generateThresholdsForAllOperations(context);
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForEdit(context, taskDefinition);
        context.put(apiTokenKey, taskDefinition.getConfiguration().get(apiTokenKey));
        context.put(presetKey, taskDefinition.getConfiguration().get(presetKey));
        context.put(scenarioFileKey, taskDefinition.getConfiguration().get(scenarioFileKey));
        generateThresholdsForAllOperations(context);
        String jsonThresholds = taskDefinition.getConfiguration().get(thresholdsKey);
        List<Integer> selectedThresholdsList = new ArrayList();
        if (jsonThresholds != null)
        {
            ThresholdViewModelListContainer thresholdsContainer = Jackson.fromJsonString(jsonThresholds, ThresholdViewModelListContainer.class);
            List<ThresholdViewModel> thresholds = thresholdsContainer.getThresholdViewModels();
            if (thresholds != null && !thresholds.isEmpty())
            {
                for (int i = 0; i < thresholds.size(); i++)
                {
                    selectedThresholdsList.add(i);
                    double numericValue = thresholds.get(i).getValue();
                    context.put(thresholdValuePrefix.concat(Integer.toString(i)), numericValue);
                    context.put(thresholdDirectionPrefix.concat(Integer.toString(i)), thresholds.get(i).getDirection());
                    context.put(thresholdMetricPrefix.concat(Integer.toString(i)), thresholds.get(i).getMetric());
                }
            }
        }
        context.put("selectedThresholdIndices", selectedThresholdsList);
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForView(context, taskDefinition);
        context.put(apiTokenKey, taskDefinition.getConfiguration().get(apiTokenKey));
        context.put(presetKey, taskDefinition.getConfiguration().get(presetKey));
        context.put(scenarioFileKey, taskDefinition.getConfiguration().get(scenarioFileKey));
    }
    
    private void generateThresholdsForAllOperations(@NotNull Map<String, Object> context)
    {
        Map<String, String> thresholdDirectionMap = new LinkedHashMap();
        List<ThresholdDirection> thresholdDirections = ThresholdDirectionFactory.getThresholdDirections();
        thresholdDirections.stream().forEach((thresholdDirection) ->
        {
            thresholdDirectionMap.put(thresholdDirection.getValue(), thresholdDirection.getDescription());
        });
        context.put("thresholdDirections", thresholdDirectionMap);
        
        Map<String, String> thresholdMetricsMap = new LinkedHashMap();
        List<LoadtestThresholdMetric> allMetrics = LoadtestThresholdMetricFactory.getAllMetrics();
        allMetrics.stream().forEach((metric) ->
        {
            thresholdMetricsMap.put(metric.getValue(), metric.getDescription());
        });
        context.put("thresholdMetrics", thresholdMetricsMap);
    }
    
    private List<Threshold> getThresholdsFromParams(@NotNull final ActionParametersMap params)
    {
        Map<String, Object> parameters = params.getParameters();
        Set<String> parameterKeys = parameters.keySet();
        List<Threshold> thresholds = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String parameterKey : parameterKeys)
        {
            if (parameterKey.startsWith(thresholdValuePrefix))
            {
                int currentIndex = Integer.parseInt(parameterKey.replace(thresholdValuePrefix, ""));
                String thresholdValueRaw = params.getString(parameterKey);
                double thresholdValue = Double.parseDouble(thresholdValueRaw);
                String correspondingMetricRawValue = params.getString(thresholdMetricPrefix.concat(Integer.toString(currentIndex)));
                String correspondingDirectionRawValue = params.getString(thresholdDirectionPrefix.concat(Integer.toString(currentIndex)));
                LoadtestThresholdMetric metric = LoadtestThresholdMetricFactory.getMetric(correspondingMetricRawValue);
                ThresholdDirection thresholdDirection = ThresholdDirectionFactory.getThresholdDirection(correspondingDirectionRawValue);
                Threshold t = new Threshold(metric, thresholdDirection, thresholdValue);
                thresholds.add(t);
            }
        }
        return thresholds;
    }

    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators
    public void setApplicationProperties(ApplicationProperties applicationProperties)
    {
        this.applicationProperties  =applicationProperties;
    }
}
