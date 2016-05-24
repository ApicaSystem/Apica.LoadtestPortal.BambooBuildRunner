/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.reports;

import com.atlassian.bamboo.build.PlanResultsAction;
import com.atlassian.bamboo.buildqueue.manager.AgentManager;
import com.atlassian.bamboo.chains.ChainResultsSummaryImpl;
import com.atlassian.bamboo.chains.ChainStageResult;
import com.atlassian.bamboo.resultsummary.BuildResultsSummary;

/**
 *
 * @author andras.nemes
 */
public class ViewLoadtestReport extends PlanResultsAction
{
    @Override
    public String execute() throws Exception
    {
        String result = super.execute();
        
        return result;
    }
}
