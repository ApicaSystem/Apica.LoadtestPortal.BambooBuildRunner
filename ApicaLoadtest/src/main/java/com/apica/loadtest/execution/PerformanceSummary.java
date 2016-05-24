/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.execution;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andras.nemes
 */
public class PerformanceSummary
{
    @JsonProperty("Total passed loops")
    private int totalPassedLoops;
    @JsonProperty("Total failed loops")
    private int totalFailedLoops;
    @JsonProperty("Average network throughput")
    private double averageNetworkThroughput;
    @JsonProperty("Avg. network throughput unit of measurement")
    private String networkThroughputUnit;
    @JsonProperty("Average session time per loop (s)")
    private double averageSessionTimePerLoop;
    @JsonProperty("Average response time per loop (s)")
    private double averageResponseTimePerLoop;
    @JsonProperty("Web transaction rate (Hits/s)")
    private double webTransactionRate;
    @JsonProperty("Average response time per page (s)")
    private double averageResponseTimePerPage;
    @JsonProperty("Total http(s) calls")
    private int totalHttpCalls;
    @JsonProperty("Avg network connect time (ms)")
    private int averageNetworkConnectTime;
    @JsonProperty("Total transmitted bytes")
    private long totalTransmittedBytes;

    public int getTotalPassedLoops()
    {
        return totalPassedLoops;
    }

    public int getTotalFailedLoops()
    {
        return totalFailedLoops;
    }

    public double getAverageNetworkThroughput()
    {
        return averageNetworkThroughput;
    }

    public String getNetworkThroughputUnit()
    {
        return networkThroughputUnit;
    }

    public double getAverageSessionTimePerLoop()
    {
        return averageSessionTimePerLoop;
    }

    public double getAverageResponseTimePerLoopMs()
    {
        return averageResponseTimePerLoop * 1000.0;
    }

    public double getWebTransactionRate()
    {
        return webTransactionRate;
    }

    public double getAverageResponseTimePerPageMs()
    {
        return averageResponseTimePerPage * 1000.0;
    }

    public int getTotalHttpCalls()
    {
        return totalHttpCalls;
    }

    public int getAverageNetworkConnectTime()
    {
        return averageNetworkConnectTime;
    }

    public long getTotalTransmittedBytes()
    {
        return totalTransmittedBytes;
    }
}
