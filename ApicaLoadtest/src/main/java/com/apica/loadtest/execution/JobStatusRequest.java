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
public class JobStatusRequest
{
    private int jobId;
    private String apiToken;

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getApiToken()
    {
        return apiToken;
    }

    public void setApiToken(String apiToken)
    {
        this.apiToken = apiToken;
    }
}
