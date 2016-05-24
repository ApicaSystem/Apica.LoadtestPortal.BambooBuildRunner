/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.thresholds;

/**
 *
 * @author andras.nemes
 */
public abstract class ThresholdDirection
{
    private final String value;
    private final String description;

    public ThresholdDirection(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }
    
    public boolean isMatch(String shortDescription)
    {
        return this.value.equalsIgnoreCase(shortDescription);
    }
    
    public abstract boolean thresholdBroken(double threshold, double actual);
}
