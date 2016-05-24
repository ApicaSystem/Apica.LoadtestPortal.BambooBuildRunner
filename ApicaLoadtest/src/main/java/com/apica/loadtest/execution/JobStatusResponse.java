/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.execution;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author andras.nemes
 */
public class JobStatusResponse
{
    @JsonProperty("id")
    private int jobId;
    @JsonProperty("message")
    private String statusMessage;
    @JsonProperty("completed")
    private boolean completed;
    @JsonProperty("error")
    private boolean jobFaulted;
    @JsonProperty("exception")
    private String exception;
    @JsonProperty("aborted")
    private boolean aborted;
    @JsonProperty("active")
    private boolean active;

    public boolean isAborted()
    {
        return aborted;
    }

    public void setAborted(boolean aborted)
    {
        this.aborted = aborted;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
    
    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public boolean isJobFaulted()
    {
        return jobFaulted;
    }

    public void setJobFaulted(boolean jobFaulted)
    {
        this.jobFaulted = jobFaulted;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public boolean isJobCompleted()
    {
        return jobFaulted || completed;
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Job message: ").append(getStatusMessage().trim());;
            return sb.toString();
        } catch (Exception ex)
        {
            return "Could not read job status response properties.";
        }
    }
}
