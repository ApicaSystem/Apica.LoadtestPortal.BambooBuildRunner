/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.reports;

import com.atlassian.bamboo.build.ViewBuildResults;
import com.atlassian.bamboo.resultsummary.ResultsSummary;

/**
 *
 * @author andras.nemes
 */
public class BuildInfoAction extends ViewBuildResults
{

    @Override
    public String doExecute() throws Exception
    {
        String superResult = super.doExecute();
        return INPUT;
    }

    @Override
    public String doDefault() throws Exception
    {
        super.doExecute(); // to populate all the stuff
        if (getUser() == null)
        {
            return ERROR;
        }
        return INPUT;
    }
}
