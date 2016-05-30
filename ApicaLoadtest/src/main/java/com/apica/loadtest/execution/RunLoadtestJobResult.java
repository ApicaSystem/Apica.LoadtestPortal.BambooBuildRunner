/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.execution;

/**
 *
 * @author andras.nemes
 */
public class RunLoadtestJobResult
{
    private PerformanceSummary performanceSummary;
    private String linkToResultDetails;
    private boolean success;

    public PerformanceSummary getPerformanceSummary()
    {
        return performanceSummary;
    }

    public String getLinkToResultDetails()
    {
        return linkToResultDetails;
    }

    public void setLinkToResultDetails(String linkToResultDetails)
    {
        this.linkToResultDetails = linkToResultDetails;
    }
    
    public void setPerformanceSummary(PerformanceSummary performanceSummary)
    {
        this.performanceSummary = performanceSummary;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    
}
