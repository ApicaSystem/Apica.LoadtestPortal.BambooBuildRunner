/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.reports;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;
import java.util.Map;

/**
 *
 * @author andras.nemes
 */
public class BuildLoadtestReportingCondition implements Condition
{

    private PlanManager planManager;
    private ResultsSummaryManager resultsSummaryManager;

    public void init(Map<String, String> map) throws PluginParseException
    {

    }

    public boolean shouldDisplay(Map<String, Object> context)
    {
        String planKeyDescr = context.get("planKey") == null ? null
                : (String) context.get("planKey");
        String buildNumber = context.get("buildNumber") == null ? null
                : (String) context.get("buildNumber");
        if (planKeyDescr == null || buildNumber == null)
        {
            return false;
        }
        PlanKey planKey = PlanKeys.getPlanKey(planKeyDescr);

        Plan plan = planManager.getPlanByKey(planKey);
        if (plan == null)
        {
            return false;
        }

        PlanResultKey planResultKey = PlanKeys.getPlanResultKey(plan.getPlanKey(), Integer.parseInt(buildNumber));
        ResultsSummary resultsSummary = resultsSummaryManager.getResultsSummary(planResultKey);

        if (resultsSummary == null)
        {
            return false;
        }
        
        return resultsSummary.isFinished();
    }

    public void setPlanManager(PlanManager planManager)
    {
        this.planManager = planManager;
    }

    public void setResultsSummaryManager(ResultsSummaryManager resultsSummaryManager)
    {
        this.resultsSummaryManager = resultsSummaryManager;
    }
}
