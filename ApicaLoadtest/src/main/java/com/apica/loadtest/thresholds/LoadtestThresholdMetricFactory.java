/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.thresholds;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andras.nemes
 */
public class LoadtestThresholdMetricFactory
{
    public static List<LoadtestThresholdMetric> getAllMetrics()
    {
        List<LoadtestThresholdMetric> list = new ArrayList<LoadtestThresholdMetric>();
        list.add(new AverageResponseTimePerPageMetric());
        list.add(new FailedLoopsRateMetric());
        return list;
    }
    
    public static LoadtestThresholdMetric getMetric(String shortDescription)
    {
        List<LoadtestThresholdMetric> allMetrics = getAllMetrics();
        for (LoadtestThresholdMetric metric : allMetrics)
        {
            if (metric.isMatch(shortDescription))
            {
                return metric;
            }
        }
        
        return allMetrics.get(0);
    }
}
