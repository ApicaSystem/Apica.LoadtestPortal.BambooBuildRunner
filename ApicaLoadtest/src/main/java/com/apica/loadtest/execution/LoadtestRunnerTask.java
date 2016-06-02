/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.execution;

import com.amazonaws.util.json.Jackson;
import com.apica.constants.StringConstants;
import com.apica.loadtest.thresholds.LoadtestThresholdMetric;
import com.apica.loadtest.thresholds.LoadtestThresholdMetricFactory;
import com.apica.loadtest.thresholds.Threshold;
import com.apica.loadtest.thresholds.ThresholdDirection;
import com.apica.loadtest.thresholds.ThresholdDirectionFactory;
import com.apica.loadtest.thresholds.ThresholdEvaluationResult;
import com.apica.loadtest.thresholds.ThresholdViewModel;
import com.apica.loadtest.thresholds.ThresholdViewModelListContainer;
import com.apica.loadtest.validation.JobParameterValidationService;
import com.apica.loadtest.validation.LoadtestJobParameterValidationService;
import com.apica.loadtest.validation.PresetResponse;
import com.apica.loadtest.validation.RunnableFileResponse;
import com.apica.ssl.LtpWebApiHostnameVerifier;
import com.apica.ssl.LtpWebApiTrustManager;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.util.concurrent.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author andras.nemes
 */
public class LoadtestRunnerTask implements TaskType
{

    private final JobExecutorService jobExecutorService;
    private final JobParameterValidationService parameterValidationService;
    private long executionStartDateMillis;

    public LoadtestRunnerTask()
    {
        this(new LtpApiLoadtestJobExecutorService(new LtpWebApiHostnameVerifier(), new LtpWebApiTrustManager()),
                new LoadtestJobParameterValidationService(new LtpWebApiHostnameVerifier(), new LtpWebApiTrustManager()));
    }

    public LoadtestRunnerTask(JobExecutorService jobExecutorService, JobParameterValidationService parameterValidationService)
    {
        this.jobExecutorService = jobExecutorService;
        this.parameterValidationService = parameterValidationService;
    }

    @NotNull
    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException
    {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        executionStartDateMillis = new Date().getTime();

        taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("baseUrl", taskContext.getConfigurationMap().get("baseUrl"));

        List<Threshold> thresholds = new ArrayList<>();
        //code omitted for the time being, need to collect thresholds later
        final String apiToken = taskContext.getConfigurationMap().get(StringConstants.API_TOKEN_KEY);
        taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("apiToken", apiToken);
        final String scenarioFile = taskContext.getConfigurationMap().get(StringConstants.SCENARIO_FILE_NAME_KEY);
        final String preset = taskContext.getConfigurationMap().get(StringConstants.PRESET_KEY);
        final String thresholdsVmJson = taskContext.getConfigurationMap().get(StringConstants.THRESHOLDS_KEY);
        String jsonThresholds = taskContext.getConfigurationMap().get(StringConstants.THRESHOLDS_KEY);
        if (jsonThresholds != null)
        {
            ThresholdViewModelListContainer thresholdsContainer = Jackson.fromJsonString(jsonThresholds, ThresholdViewModelListContainer.class);
            List<ThresholdViewModel> thresholdViewModels = thresholdsContainer.getThresholdViewModels();
            thresholdViewModels.stream().map((thresholdViewModel) ->
            {
                ThresholdDirection thresholdDirection = ThresholdDirectionFactory.getThresholdDirection(thresholdViewModel.getDirection());
                LoadtestThresholdMetric metric = LoadtestThresholdMetricFactory.getMetric(thresholdViewModel.getMetric());
                Threshold t = new Threshold(metric, thresholdDirection, thresholdViewModel.getValue());
                return t;
            }).forEach((t) ->
            {
                thresholds.add(t);
            });
        }

        buildLogger.addBuildLogEntry("Apica Loadtest starting...");
        buildLogger.addBuildLogEntry("Validating job parameters...");
        buildLogger.addBuildLogEntry("Testing preset name: ".concat(preset));
        buildLogger.addBuildLogEntry("Testing load test file name: ".concat(scenarioFile));

        boolean clientSideValidationOk = true;

        if (StringUtils.isEmpty(apiToken) || StringUtils.isBlank(apiToken))
        {
            buildLogger.addBuildLogEntry("You must provide a valid LTP authentication token");
            clientSideValidationOk = false;
        }

        if (StringUtils.isEmpty(preset) || StringUtils.isBlank(preset))
        {
            buildLogger.addBuildLogEntry("You must provide a valid preset name");
            clientSideValidationOk = false;
        }

        if (StringUtils.isEmpty(scenarioFile) || StringUtils.isBlank(scenarioFile))
        {
            buildLogger.addBuildLogEntry("You must provide a valid scenario name");
            clientSideValidationOk = false;
        }

        boolean serverSideValidationOk = true;
        if (clientSideValidationOk) //if client side validation is fine then continue with the server side validation
        {
            PresetResponse presetResponse = this.parameterValidationService.checkPreset(apiToken, preset);
            if (!presetResponse.isPresetExists())
            {
                buildLogger.addBuildLogEntry("Cannot find this preset: ".concat(preset).concat(". "));
                if (presetResponse.getException() != null && !presetResponse.getException().equals(""))
                {
                    buildLogger.addBuildLogEntry("Exception while checking preset: ".concat(presetResponse.getException()));
                }
                serverSideValidationOk = false;
            } else //validate test instance id
            {
                if (presetResponse.getTestInstanceId() < 1)
                {
                    buildLogger.addBuildLogEntry("The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset.");
                    serverSideValidationOk = false;
                }
                else
                {
                    taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("presetTestInstanceId", Integer.toString(presetResponse.getTestInstanceId()));
                }
            }

            RunnableFileResponse runnableFileResponse = parameterValidationService.checkRunnableFile(apiToken, scenarioFile);
            if (!runnableFileResponse.isFileExists())
            {
                if (runnableFileResponse.getException() != null && !runnableFileResponse.getException().equals(""))
                {
                    buildLogger.addBuildLogEntry("Exception while checking load test file: ".concat(runnableFileResponse.getException()));
                    serverSideValidationOk = false;
                } else
                {
                    buildLogger.addBuildLogEntry("No such load test file found: ".concat(scenarioFile));
                    serverSideValidationOk = false;
                }
            }
        }

        if (clientSideValidationOk && serverSideValidationOk)
        {
            taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("presetName", preset);
            taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("scenario", scenarioFile);
            buildLogger.addBuildLogEntry("Validation done, continuing with the job initiation...");
            if (!thresholds.isEmpty())
            {
                buildLogger.addBuildLogEntry("Threshold values:");
                thresholds.stream().forEach((threshold) ->
                {
                    buildLogger.addBuildLogEntry(threshold.toString());
                });
            }

            RunLoadtestJobResult runLoadtestJob = runLoadtestJob(buildLogger, thresholds, preset, scenarioFile, apiToken);
            boolean res = runLoadtestJob.isSuccess();
            PerformanceSummary performanceSummary = runLoadtestJob.getPerformanceSummary();
            if (performanceSummary != null)
            {
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("startDateUtc", new Date(executionStartDateMillis).toString());
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("totalPassedLoops", Integer.toString(performanceSummary.getTotalPassedLoops()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("totalFailedLoops", Integer.toString(performanceSummary.getTotalFailedLoops()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("averageNetworkThroughput", Double.toString(performanceSummary.getAverageNetworkThroughput()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("networkThroughputUnit", performanceSummary.getNetworkThroughputUnit());
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("averageSessionTimePerLoop", Double.toString(performanceSummary.getAverageSessionTimePerLoop()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("averageResponseTimePerLoop", Double.toString(performanceSummary.getAverageResponseTimePerLoopMs()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("webTransactionRate", Double.toString(performanceSummary.getWebTransactionRate()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("averageResponseTimePerPage", Double.toString(performanceSummary.getAverageResponseTimePerPageMs()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("totalHttpCalls", Integer.toString(performanceSummary.getTotalHttpCalls()));                
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("averageNetworkConnectTime", Integer.toString(performanceSummary.getAverageNetworkConnectTime()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("totalTransmittedBytes", Long.toString(performanceSummary.getTotalTransmittedBytes()));
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("linkToDetails", runLoadtestJob.getLinkToResultDetails());
            }
            if (res)
            {
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("finished", "true");
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("hasResults", "true");
                return TaskResultBuilder.newBuilder(taskContext).success().build();
            } else
            {
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("finished", "true");
                taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("hasResults", "false");
                return TaskResultBuilder.newBuilder(taskContext).failed().build();
            }
        }
        taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("finished", "true");
        taskContext.getBuildContext().getBuildResult().getCustomBuildData().put("hasResults", "false");
        return TaskResultBuilder.newBuilder(taskContext).failed().build();

    }

    private RunLoadtestJobResult runLoadtestJob(BuildLogger buildLogger, List<Threshold> thresholds, String loadtestPresetName,
            String loadtestFileName, String authToken)
    {
        RunLoadtestJobResult res = new RunLoadtestJobResult();
        boolean success = true;
        try
        {
            TransmitJobRequestArgs transmitJobRequestArgs = new TransmitJobRequestArgs();
            transmitJobRequestArgs.setFileName(loadtestFileName);
            transmitJobRequestArgs.setApiToken(authToken);
            transmitJobRequestArgs.setPresetName(loadtestPresetName);
            StartJobByPresetResponse startByPresetResponse = this.jobExecutorService.transmitJob(transmitJobRequestArgs);
            if (startByPresetResponse.getJobId() > 0)
            {
                int jobId = startByPresetResponse.getJobId();
                buildLogger.addBuildLogEntry("Successfully inserted job. Job id: ".concat(Integer.toString(jobId)));
                JobStatusRequest jobStatusRequest = new JobStatusRequest();
                jobStatusRequest.setJobId(jobId);
                jobStatusRequest.setApiToken(authToken);
                JobStatusResponse jobStatus = jobExecutorService.checkJobStatus(jobStatusRequest);
                logJobStatus(jobStatus, buildLogger);
                while (!jobStatus.isJobCompleted())
                {
                    jobStatus = jobExecutorService.checkJobStatus(jobStatusRequest);
                    logJobStatus(jobStatus, buildLogger);
                    try
                    {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex)
                    {
                    }
                }
                if (jobStatus.isCompleted() && !jobStatus.isJobFaulted())
                {
                    buildLogger.addBuildLogEntry("Job has finished normally. Will now attempt to retrieve some job statistics.");
                    LoadtestJobSummaryRequest summaryRequest = new LoadtestJobSummaryRequest();
                    summaryRequest.setJobId(jobId);
                    summaryRequest.setApiToken(authToken);
                    LoadtestJobSummaryResponse summaryResponse = jobExecutorService.getJobSummaryResponse(summaryRequest);
                    res.setPerformanceSummary(summaryResponse.getPerformanceSummary());
                    res.setLinkToResultDetails(summaryResponse.getLinkToTestResults());
                    logJobSummary(summaryResponse, buildLogger);
                    if (!thresholds.isEmpty())
                    {
                        boolean atLeastOneThresholdBroken = false;
                        buildLogger.addBuildLogEntry("Evaluating threshold values...");
                        for (Threshold threshold : thresholds)
                        {
                            ThresholdEvaluationResult evaluate = threshold.evaluate(summaryResponse.getPerformanceSummary());
                            buildLogger.addBuildLogEntry(evaluate.getReport());
                            if (evaluate.isThresholdBroken())
                            {
                                atLeastOneThresholdBroken = true;
                            }
                        }
                        if (atLeastOneThresholdBroken)
                        {
                            buildLogger.addBuildLogEntry("Job has finished with at least one broken threshold.");
                            success = false;
                        }
                    }
                } else if (jobStatus.isJobFaulted())
                {
                    buildLogger.addBuildLogEntry("Job has finished with an error: ".concat(jobStatus.getStatusMessage()));
                    success = false;
                }
            } else
            {
                buildLogger.addBuildLogEntry(startByPresetResponse.getException());
                success = false;
            }
        } catch (Exception ex)
        {
            buildLogger.addBuildLogEntry(ex.getMessage());
            success = false;
        }
        res.setSuccess(success);
        return res;
    }

    private void logJobStatus(JobStatusResponse jobStatusResponse, BuildLogger buildLogger)
    {
        if (jobStatusResponse.getException().equals(""))
        {
            buildLogger.addBuildLogEntry(jobStatusResponse.toString());
        } else
        {
            buildLogger.addBuildLogEntry("Exception when retrieving job status: ".concat(jobStatusResponse.getException()));
        }
    }

    private void logJobSummary(LoadtestJobSummaryResponse jobSummaryResponse, BuildLogger buildLogger)
    {
        if (jobSummaryResponse.getException().equals(""))
        {
            buildLogger.addBuildLogEntry(jobSummaryResponse.toString());
        } else
        {
            buildLogger.addBuildLogEntry("Exception when retrieving job summary statistics: ".concat(jobSummaryResponse.getException()));
        }
    }
}
