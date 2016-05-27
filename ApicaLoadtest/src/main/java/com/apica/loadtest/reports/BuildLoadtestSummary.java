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
    private final String NL = System.lineSeparator();

    @Override
    public String doExecute() throws Exception
    {
        String superResult = super.doExecute();

        setGroupId("com.apica");
        setArtifactId("ApicaLoadtest");
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

    
}
