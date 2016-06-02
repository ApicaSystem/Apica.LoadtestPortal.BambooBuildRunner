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
public class ThresholdViewModelListContainer
{
    private List<ThresholdViewModel> thresholdViewModels;

    public ThresholdViewModelListContainer()
    {
        this.thresholdViewModels = new ArrayList<>();
    }
    
    public List<ThresholdViewModel> getThresholdViewModels()
    {
        return thresholdViewModels;
    }

    public void setThresholdViewModels(List<ThresholdViewModel> thresholdViewModels)
    {
        this.thresholdViewModels = thresholdViewModels;
    }
    
    public void setFromThresholds(List<Threshold> thresholds)
    {
        thresholds.stream().forEach((threshold) ->
        {
            ThresholdViewModel vm = new ThresholdViewModel();
            vm.setDirection(threshold.getDirection().getValue());
            vm.setMetric(threshold.getMetric().getValue());
            vm.setValue(threshold.getNumericValue());
            this.thresholdViewModels.add(vm);
        });
    }
}
