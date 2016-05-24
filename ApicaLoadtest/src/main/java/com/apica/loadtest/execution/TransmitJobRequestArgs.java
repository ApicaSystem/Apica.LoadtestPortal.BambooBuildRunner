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
public class TransmitJobRequestArgs
{
    private String fileName;
    private String presetName;
    private String apiToken;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public void setPresetName(String presetName)
    {
        this.presetName = presetName;
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
