/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andras.nemes
 */
public class PresetResponse
{
    @JsonProperty("presetexists")
    private boolean presetExists;
    @JsonProperty("exception")
    private String exception;
    @JsonProperty("testinstanceid")
    private int testInstanceId;

    public int getTestInstanceId()
    {
        return testInstanceId;
    }

    public void setTestInstanceId(int testInstanceId)
    {
        this.testInstanceId = testInstanceId;
    }

    public boolean isPresetExists()
    {
        return presetExists;
    }

    public void setPresetExists(boolean presetExists)
    {
        this.presetExists = presetExists;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }
}
