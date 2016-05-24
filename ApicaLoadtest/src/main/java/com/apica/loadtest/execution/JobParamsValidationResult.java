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
public class JobParamsValidationResult
{
    private boolean allParamsPresent;
    private int presetTestInstanceId;
    private String exceptionSummary;
    private String authTokenException;
    private String scenarioFileException;
    private String presetNameException;

    public int getPresetTestInstanceId()
    {
        return presetTestInstanceId;
    }

    public void setPresetTestInstanceId(int presetTestInstanceId)
    {
        this.presetTestInstanceId = presetTestInstanceId;
    }
    
    public String getAuthTokenException()
    {
        return authTokenException;
    }

    public void setAuthTokenException(String authTokenException)
    {
        this.authTokenException = authTokenException;
    }

    public String getScenarioFileException()
    {
        return scenarioFileException;
    }

    public void setScenarioFileException(String scenarioFileException)
    {
        this.scenarioFileException = scenarioFileException;
    }

    public String getPresetNameException()
    {
        return presetNameException;
    }

    public void setPresetNameException(String presetNameException)
    {
        this.presetNameException = presetNameException;
    }
    
    public boolean isAllParamsPresent()
    {
        return allParamsPresent;
    }

    public void setAllParamsPresent(boolean allParamsPresent)
    {
        this.allParamsPresent = allParamsPresent;
    }

    public String getExceptionSummary()
    {
        return exceptionSummary;
    }

    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionSummary = exceptionMessage;
    }
}
