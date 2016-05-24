/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica;

import com.apica.constants.StringConstants;
import com.apica.loadtest.execution.JobExecutorService;
import com.apica.loadtest.execution.JobStatusRequest;
import com.apica.loadtest.execution.JobStatusResponse;
import com.apica.loadtest.execution.LoadtestJobSummaryRequest;
import com.apica.loadtest.execution.LoadtestJobSummaryResponse;
import com.apica.loadtest.execution.LtpApiLoadtestJobExecutorService;
import com.apica.loadtest.execution.PerformanceSummary;
import com.apica.loadtest.execution.RunLoadtestJobResult;
import com.apica.loadtest.execution.StartJobByPresetResponse;
import com.apica.loadtest.execution.TransmitJobRequestArgs;
import com.apica.loadtest.thresholds.Threshold;
import com.apica.loadtest.thresholds.ThresholdEvaluationResult;
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
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        List<Threshold> thresholds = new ArrayList<Threshold>();
        //code omitted for the time being, need to collect thresholds later
        final String apiToken = taskContext.getConfigurationMap().get(StringConstants.API_TOKEN_KEY);
        final String scenarioFile = taskContext.getConfigurationMap().get(StringConstants.SCENARIO_FILE_NAME_KEY);
        final String preset = taskContext.getConfigurationMap().get(StringConstants.PRESET_KEY);

        final BuildLogger buildLogger = taskContext.getBuildLogger();
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
             if (presetResponse.getTestInstanceId() < 1)
                {
                    buildLogger.addBuildLogEntry("The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset.");
                    serverSideValidationOk = false;
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
            buildLogger.addBuildLogEntry("Validation done, continuing with the job initiation...");
            if (!thresholds.isEmpty())
            {
                buildLogger.addBuildLogEntry("Threshold values: \r\n");
                for (Threshold threshold : thresholds)
                {
                    buildLogger.addBuildLogEntry(threshold.toString());
                }
            }

            RunLoadtestJobResult runLoadtestJob = runLoadtestJob(buildLogger, thresholds, preset, scenarioFile, apiToken);
            boolean res = runLoadtestJob.isSuccess();
            PerformanceSummary performanceSummary = runLoadtestJob.getPerformanceSummary();
            if (performanceSummary != null)
            {
                //build.addAction(new LoadTestSummary(build, performanceSummary, loadtestBuilderModel.getPresetName()));
            }
            if (res)
            {
                return TaskResultBuilder.newBuilder(taskContext).success().build();
            } else
            {
                return TaskResultBuilder.newBuilder(taskContext).failed().build();
            }
        }
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
        }
        catch (Exception ex)
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
