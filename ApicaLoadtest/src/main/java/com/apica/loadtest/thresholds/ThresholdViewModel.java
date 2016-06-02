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
public class ThresholdViewModel
{
    private double value;
    private String direction;
    private String metric;

    public double getValue()
    {
        return value;
    }

    public String getDirection()
    {
        return direction;
    }

    public String getMetric()
    {
        return metric;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public void setMetric(String metric)
    {
        this.metric = metric;
    }
}
