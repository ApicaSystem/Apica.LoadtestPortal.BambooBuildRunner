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
public class Threshold
{
    private final LoadtestThresholdMetric metric;
    private final ThresholdDirection direction;
    private final double numericValue;

    public Threshold(LoadtestThresholdMetric metric, ThresholdDirection direction, double numericValue)
    {
        this.metric = metric;
        this.direction = direction;
        this.numericValue = numericValue;
    }

    public LoadtestThresholdMetric getMetric()
    {
        return metric;
    }

    public ThresholdDirection getDirection()
    {
        return direction;
    }

    public double getNumericValue()
    {
        return numericValue;
    }
    
    public ThresholdEvaluationResult evaluate(PerformanceSummary performanceSummary)
    {
        ThresholdEvaluationResult res = new ThresholdEvaluationResult();
        double actualValue = metric.extractActualValueFrom(performanceSummary);
        boolean broken = direction.thresholdBroken(numericValue, actualValue);
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("If ")
                .append(metric.getDescription().toLowerCase())
                .append(" is ").append(direction.getDescription())
                .append(" ").append(numericValue).append(metric.getUnitOfMeasurementSymbol())
                .append(" then test fails. Outcome: ").append(actualValue).append(metric.getUnitOfMeasurementSymbol())
                .append(" Threshold broken: ").append(broken);
        res.setThresholdBroken(broken);
        res.setReport(reportBuilder.toString());
        return res;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("If ").append(metric.getDescription().toLowerCase()).append(" is ").append(direction.getDescription())
                .append(" ").append(Double.toString(numericValue))
                .append(metric.getUnitOfMeasurementSymbol())
                .append(" then mark test as failed");

        return sb.toString();
    }
}
