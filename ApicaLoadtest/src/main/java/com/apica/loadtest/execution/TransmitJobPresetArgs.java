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
public class TransmitJobPresetArgs
{
    @JsonProperty("PresetName")
    private final String presetName;
    @JsonProperty("RunnableFileName")
    private final String runnableFileName;

    public TransmitJobPresetArgs(String presetName, String runnableFileName)
    {
        this.presetName = presetName;
        this.runnableFileName = runnableFileName;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public String getRunnableFileName()
    {
        return runnableFileName;
    }
}
