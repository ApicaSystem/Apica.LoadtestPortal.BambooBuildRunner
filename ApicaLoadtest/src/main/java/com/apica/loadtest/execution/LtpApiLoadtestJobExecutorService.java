/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.execution;

import com.amazonaws.util.json.Jackson;
import com.apica.LtpWebApiServiceBase;
import com.apica.ssl.ISslHostnameVerifier;
import com.apica.ssl.ISslTrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author andras.nemes
 */
public class LtpApiLoadtestJobExecutorService extends LtpWebApiServiceBase implements JobExecutorService
{ 

    public LtpApiLoadtestJobExecutorService(ISslHostnameVerifier sslHostnameVerifier, ISslTrustManager sslTrustManager)
    {
        super(sslHostnameVerifier, sslTrustManager);
    }

    @Override
    public StartJobByPresetResponse transmitJob(TransmitJobRequestArgs transmitJobArgs)
    {
        StartJobByPresetResponse resp = new StartJobByPresetResponse();
        String startByPresetUriExtension = "selfservicejobs/preset";
        String apiToken = transmitJobArgs.getApiToken();
        try
        {
            super.initSSL();
            URI uri = new URI(super.getScheme(), null, super.getBaseLtpApiUri(),
                    getPort(), getSeparator().concat(getUriVersion()).concat(getSeparator())
                    .concat(startByPresetUriExtension), null, null);
            URL url = uri.toURL();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setRequestMethod("POST");
            httpsCon.setConnectTimeout(60000);
            httpsCon.setRequestProperty(getTokenHeaderName(), apiToken);
            httpsCon.setRequestProperty("Content-Type", "application/json");
            resp.setException("");
            resp.setJobId(-1);
            TransmitJobPresetArgs presetArgs
                    = new TransmitJobPresetArgs(transmitJobArgs.getPresetName(), transmitJobArgs.getFileName());
            String jsonifiedPresetArgs = Jackson.toJsonPrettyString(presetArgs);
            httpsCon.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(httpsCon.getOutputStream());
            wr.writeBytes(jsonifiedPresetArgs);
            wr.flush();
            wr.close();
            int responseCode = httpsCon.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }

                resp = Jackson.fromJsonString(response.toString(), StartJobByPresetResponse.class);
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp.setException(response.toString());
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            }
        }catch (Exception ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    @Override
    public JobStatusResponse checkJobStatus(JobStatusRequest jobStatusRequest)
    {
        JobStatusResponse resp = new JobStatusResponse();
        resp.setException("");
        String jobStatusExtension = "selfservicejobs";
        int jobId = jobStatusRequest.getJobId();
        String apiToken = jobStatusRequest.getApiToken();
        try
        {
            super.initSSL();
            URI uri = new URI(super.getScheme(), null, super.getBaseLtpApiUri(),
                    getPort(), getSeparator().concat(getUriVersion()).concat(getSeparator())
                    .concat(jobStatusExtension).concat(getSeparator()).concat(Integer.toString(jobId)), null, null);
            URL url = uri.toURL();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setRequestMethod("GET");
            httpsCon.setConnectTimeout(60000);
            httpsCon.setRequestProperty(getTokenHeaderName(), apiToken);
            int responseCode = httpsCon.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp = Jackson.fromJsonString(response.toString(), JobStatusResponse.class);
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp.setException(response.toString());
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            }
        } catch (Exception ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    @Override
    public LoadtestJobSummaryResponse getJobSummaryResponse(LoadtestJobSummaryRequest summaryRequest)
    {
        LoadtestJobSummaryResponse resp = new LoadtestJobSummaryResponse();
        resp.setException("");
        String summaryEndpoint = "summary";
        String jobStatusExtension = "selfservicejobs";
        int jobId = summaryRequest.getJobId();
        String apiToken = summaryRequest.getApiToken();
        try
        {
            URI uri = new URI(getScheme(), null, getBaseLtpApiUri(), getPort(),
                            (getSeparator()).concat(getUriVersion()).concat(getSeparator()).concat(jobStatusExtension)
                            .concat(getSeparator()).concat(Integer.toString(jobId).concat(getSeparator())
                                    .concat(summaryEndpoint)), null, null);
            URL url = uri.toURL();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setRequestMethod("GET");
            httpsCon.setConnectTimeout(60000);
            httpsCon.setRequestProperty(getTokenHeaderName(), apiToken);
            int responseCode = httpsCon.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp = Jackson.fromJsonString(response.toString(), LoadtestJobSummaryResponse.class);
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsCon.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp.setException(response.toString());
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            }
        } catch (Exception ex)
        {
            resp.setException(ex.getMessage());
        }
        
        return resp;
    }
    
}
