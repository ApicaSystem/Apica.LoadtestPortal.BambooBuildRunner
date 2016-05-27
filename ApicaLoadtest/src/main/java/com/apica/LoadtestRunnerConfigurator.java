/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica;

import com.apica.constants.StringConstants;
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
import com.atlassian.bamboo.task.TaskContextHelperService;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;

/**
 *
 * @author andras.nemes
 */
public class LoadtestRunnerConfigurator extends AbstractTaskConfigurator implements BuildTaskRequirementSupport
{

    private final String apiTokenKey = StringConstants.API_TOKEN_KEY;
    private final String presetKey = StringConstants.PRESET_KEY;
    private final String scenarioFileKey = StringConstants.SCENARIO_FILE_NAME_KEY;
    private final JobParameterValidationService parameterValidationService;
    private ApplicationProperties applicationProperties;

    public LoadtestRunnerConfigurator()
    {
        this(new LoadtestJobParameterValidationService(new LtpWebApiHostnameVerifier(), new LtpWebApiTrustManager()));
    }

    public LoadtestRunnerConfigurator(JobParameterValidationService parameterValidationService)
    {
        this.parameterValidationService = parameterValidationService;
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition)
    {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(apiTokenKey, params.getString(apiTokenKey));
        config.put(presetKey, params.getString(presetKey));
        config.put(scenarioFileKey, params.getString(scenarioFileKey));
        config.put("baseUrl", this.applicationProperties.getBaseUrl(UrlMode.ABSOLUTE));        
        return config;
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection)
    {
        super.validate(params, errorCollection);

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
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForEdit(context, taskDefinition);
        context.put(apiTokenKey, taskDefinition.getConfiguration().get(apiTokenKey));
        context.put(presetKey, taskDefinition.getConfiguration().get(presetKey));
        context.put(scenarioFileKey, taskDefinition.getConfiguration().get(scenarioFileKey));
    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition)
    {
        super.populateContextForView(context, taskDefinition);
        context.put(apiTokenKey, taskDefinition.getConfiguration().get(apiTokenKey));
        context.put(presetKey, taskDefinition.getConfiguration().get(presetKey));
        context.put(scenarioFileKey, taskDefinition.getConfiguration().get(scenarioFileKey));
    }

    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators
    public void setApplicationProperties(ApplicationProperties applicationProperties)
    {
        this.applicationProperties  =applicationProperties;
    }
}
