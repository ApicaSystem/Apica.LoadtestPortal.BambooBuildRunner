/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.thresholds;

import com.apica.loadtest.execution.PerformanceSummary;

/**
 *
 * @author andras.nemes
 */
public abstract class LoadtestThresholdMetric
{
    private final String value;
    private final String description;
    private final String unitOfMeasurementSymbol;

    public LoadtestThresholdMetric(String value, String description, String unitOfMeasurementSymbol)
    {
        this.value = value;
        this.description = description;
        this.unitOfMeasurementSymbol = unitOfMeasurementSymbol;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUnitOfMeasurementSymbol()
    {
        return unitOfMeasurementSymbol;
    }
    
    public boolean isMatch(String shortDescription)
    {
        return value.equalsIgnoreCase(shortDescription);
    }
    
    public abstract double extractActualValueFrom(PerformanceSummary performanceSummary);
}
