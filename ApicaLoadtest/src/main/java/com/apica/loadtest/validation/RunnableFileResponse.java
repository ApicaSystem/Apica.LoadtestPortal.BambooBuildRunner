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
public class RunnableFileResponse
{
    @JsonProperty("fileexists")
    private boolean fileExists;
    @JsonProperty("exception")
    private String exception;

    public boolean isFileExists()
    {
        return fileExists;
    }

    public void setFileExists(boolean fileExists)
    {
        this.fileExists = fileExists;
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
