/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.execution;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andras.nemes
 */
public class StartJobByPresetResponse
{
    @JsonProperty("exception")
    private String exception;
    @JsonProperty("jobid")
    private int jobId;
    
    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }
}
