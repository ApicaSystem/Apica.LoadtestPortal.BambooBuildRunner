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
public class BuildLoadtestTrend extends ViewBuildResults
{
    private String trendsUrl;
    private String baseUrl = "None";
    private String groupId = "None";
    private String artifactId = "None";
    private final String webPortalRoot = "https://loadtest.apicasystem.com/";
    private final String loadtestPortalCiController = "ContinuousIntegrationTeamCity";
    
    @Override
    public String doExecute() throws Exception
    {
        groupId = "com.apica";
        artifactId = "ApicaLoadtest";
        String superResult = super.doExecute();
        ChainResultsSummaryImpl chainResults = (ChainResultsSummaryImpl) this.getResultsSummary();
        String presetName = "N/A";
        int presetTestInstance = 0;
        String authToken = "";
        List<ChainStageResult> resultList = chainResults.getStageResults();
        for (ChainStageResult chainResult : resultList)
        {
            Set<BuildResultsSummary> resultSet = chainResult.getBuildResults();
            Iterator<BuildResultsSummary> iter = resultSet.iterator();
            while (iter.hasNext())
            {
                BuildResultsSummary brs = iter.next();
                Map<String, String> customBuildData = brs.getCustomBuildData();
                if (customBuildData.containsKey("presetName"))
                {
                    presetName = customBuildData.get("presetName");
                }
                if (customBuildData.containsKey("presetTestInstanceId"))
                {
                    presetTestInstance = Integer.parseInt(customBuildData.get("presetTestInstanceId"));
                }
                if (customBuildData.containsKey("apiToken"))
                {
                    authToken = customBuildData.get("apiToken");
                }
                if (customBuildData.containsKey("baseUrl"))
                {
                    baseUrl = customBuildData.get("baseUrl");
                }
            }
        }
        
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(webPortalRoot)
                .append(loadtestPortalCiController)
                .append("/").append(presetTestInstance)
                .append("/").append(authToken);
        trendsUrl = urlBuilder.toString();
        
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

    public String getTrendsUrl()
    {
        return trendsUrl;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }
}
