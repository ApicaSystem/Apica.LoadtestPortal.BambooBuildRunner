/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.reports;

import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.chains.ChainResultsSummaryImpl;
import com.atlassian.bamboo.chains.ChainStageResult;
import com.atlassian.bamboo.resultsummary.BuildResultsSummary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author andras.nemes
 */
public class BuildLoadtestSummary extends ViewBuildResults
{

    private String baseUrl = "None";
    private String groupId = "None";
    private String artifactId = "None";
    private String testHasFinished = "N/A";
    private String testHasResult = "N/A";
    private final String defaultNotAvailableMessage = "Not available, check the logs for possible failures";
    private String presetName;
    private String scenario;
    private String startDateUtc;
    private String totalPassedLoops;
    private String totalFailedLoops;
    private String averageNetworkThroughput;
    private String networkThroughputUnit;
    private String averageSessionTimePerLoop;
    private String averageResponseTimePerLoop;
    private String webTransactionRate;
    private String averageResponseTimePerPage;
    private String totalHttpCalls;
    private String averageNetworkConnectTime;
    private String totalTransmittedBytes;
    private String linkToDetails;

    @Override
    public String doExecute() throws Exception
    {
        String superResult = super.doExecute();

        setGroupId("com.apica");
        setArtifactId("ApicaLoadtest");
        presetName = defaultNotAvailableMessage;
        scenario = defaultNotAvailableMessage;
        startDateUtc = defaultNotAvailableMessage;
        totalPassedLoops = defaultNotAvailableMessage;
        totalFailedLoops = defaultNotAvailableMessage;
        averageNetworkThroughput = defaultNotAvailableMessage;
        networkThroughputUnit = defaultNotAvailableMessage;
        averageSessionTimePerLoop = defaultNotAvailableMessage;
        averageResponseTimePerLoop = defaultNotAvailableMessage;
        webTransactionRate = defaultNotAvailableMessage;
        averageResponseTimePerPage = defaultNotAvailableMessage;
        totalHttpCalls = defaultNotAvailableMessage;
        averageNetworkConnectTime = defaultNotAvailableMessage;
        totalTransmittedBytes = defaultNotAvailableMessage;
        linkToDetails = defaultNotAvailableMessage;
        ChainResultsSummaryImpl chainResults = (ChainResultsSummaryImpl) this.getResultsSummary();

        List<ChainStageResult> resultList = chainResults.getStageResults();
        for (ChainStageResult chainResult : resultList)
        {
            Set<BuildResultsSummary> resultSet = chainResult.getBuildResults();
            Iterator<BuildResultsSummary> iter = resultSet.iterator();
            while (iter.hasNext())
            {
                BuildResultsSummary brs = iter.next();
                Map<String, String> customBuildData = brs.getCustomBuildData();
                if (customBuildData.containsKey("baseUrl"))
                {
                    setBaseUrl(customBuildData.get("baseUrl"));
                }
                if (customBuildData.containsKey("finished"))
                {
                    testHasFinished = customBuildData.get("finished");
                }
                if (customBuildData.containsKey("hasResults"))
                {
                    testHasResult = customBuildData.get("hasResults");
                }
                if (customBuildData.containsKey("presetName"))
                {
                    presetName = customBuildData.get("presetName");
                }
                if (customBuildData.containsKey("scenario"))
                {
                    scenario = customBuildData.get("scenario");
                }
                if (customBuildData.containsKey("startDateUtc"))
                {
                    startDateUtc = customBuildData.get("startDateUtc");
                }
                if (customBuildData.containsKey("totalPassedLoops"))
                {
                    totalPassedLoops = customBuildData.get("totalPassedLoops");
                }
                if (customBuildData.containsKey("totalFailedLoops"))
                {
                    totalFailedLoops = customBuildData.get("totalFailedLoops");
                }
                if (customBuildData.containsKey("averageNetworkThroughput"))
                {
                    averageNetworkThroughput = customBuildData.get("averageNetworkThroughput");
                }
                if (customBuildData.containsKey("networkThroughputUnit"))
                {
                    networkThroughputUnit = customBuildData.get("networkThroughputUnit");
                }
                if (customBuildData.containsKey("averageSessionTimePerLoop"))
                {
                    averageSessionTimePerLoop = customBuildData.get("averageSessionTimePerLoop");
                }
                if (customBuildData.containsKey("averageResponseTimePerLoop"))
                {
                    averageResponseTimePerLoop = customBuildData.get("averageResponseTimePerLoop");
                }
                if (customBuildData.containsKey("webTransactionRate"))
                {
                    webTransactionRate = customBuildData.get("webTransactionRate");
                }
                if (customBuildData.containsKey("averageResponseTimePerPage"))
                {
                    averageResponseTimePerPage = customBuildData.get("averageResponseTimePerPage");
                }
                if (customBuildData.containsKey("totalHttpCalls"))
                {
                    totalHttpCalls = customBuildData.get("totalHttpCalls");
                }
                if (customBuildData.containsKey("averageNetworkConnectTime"))
                {
                    averageNetworkConnectTime = customBuildData.get("averageNetworkConnectTime");
                }
                if (customBuildData.containsKey("totalTransmittedBytes"))
                {
                    totalTransmittedBytes = customBuildData.get("totalTransmittedBytes");
                }
                if (customBuildData.containsKey("linkToDetails"))
                {
                    linkToDetails = customBuildData.get("linkToDetails");
                }
            }
        }

        return INPUT;
    }

    @Override
    public String doDefault() throws Exception
    {
        super.doExecute(); // to populate all the stuff
        if (getUser() == null)
        {
            return ERROR;
        }
        return INPUT;

    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(String artifactId)
    {
        this.artifactId = artifactId;
    }

    public String getTestHasFinished()
    {
        return testHasFinished;
    }

    public String getTestHasResult()
    {
        return testHasResult;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public String getScenario()
    {
        return scenario;
    }

    public String getStartDateUtc()
    {
        return startDateUtc;
    }

    public String getTotalPassedLoops()
    {
        return totalPassedLoops;
    }

    public String getTotalFailedLoops()
    {
        return totalFailedLoops;
    }

    public String getAverageNetworkThroughput()
    {
        return averageNetworkThroughput;
    }

    public String getNetworkThroughputUnit()
    {
        return networkThroughputUnit;
    }

    public String getAverageSessionTimePerLoop()
    {
        return averageSessionTimePerLoop;
    }

    public String getAverageResponseTimePerLoop()
    {
        return averageResponseTimePerLoop;
    }

    public String getWebTransactionRate()
    {
        return webTransactionRate;
    }

    public String getAverageResponseTimePerPage()
    {
        return averageResponseTimePerPage;
    }

    public String getTotalHttpCalls()
    {
        return totalHttpCalls;
    }

    public String getAverageNetworkConnectTime()
    {
        return averageNetworkConnectTime;
    }

    public String getTotalTransmittedBytes()
    {
        return totalTransmittedBytes;
    }

    public String getLinkToDetails()
    {
        return linkToDetails;
    }
}
