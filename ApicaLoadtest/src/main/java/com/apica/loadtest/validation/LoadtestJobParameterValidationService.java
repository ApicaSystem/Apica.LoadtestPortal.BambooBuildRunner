/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.loadtest.validation;

import com.amazonaws.util.json.Jackson;
import com.apica.LtpWebApiServiceBase;
import com.apica.ssl.ISslHostnameVerifier;
import com.apica.ssl.ISslTrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang.Validate;

/**
 *
 * @author andras.nemes
 */
public class LoadtestJobParameterValidationService extends LtpWebApiServiceBase implements JobParameterValidationService
{

    public LoadtestJobParameterValidationService(ISslHostnameVerifier sslHostnameVerifier, ISslTrustManager sslTrustManager)
    {
        super(sslHostnameVerifier, sslTrustManager);
    }

    @Override
    public PresetResponse checkPreset(String apiToken, String presetName)
    {
        PresetResponse presetResponse = new PresetResponse();
        Validate.notNull(apiToken);
        Validate.notNull(presetName);
        Validate.notEmpty(apiToken);
        Validate.notEmpty(presetName);
        presetResponse.setException("");
        try
        {
            super.initSSL();
            String presetUriExtension = "selfservicepresets";
            URI uri = new URI(super.getScheme(), null, super.getBaseLtpApiUri(), getPort(), getSeparator().concat(getUriVersion()).concat(getSeparator())
                    .concat(presetUriExtension), ("presetName=").concat(presetName.replace("+", "SPECIAL_CHAR_PLUS")), null);
            URL url = uri.toURL();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setRequestMethod("GET");
            httpsCon.setConnectTimeout(60000);
            httpsCon.setRequestProperty(getTokenHeaderName(), apiToken);
            int responseCode = httpsCon.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpsCon.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                try
                {
                    presetResponse = Jackson.fromJsonString(response.toString(), PresetResponse.class);
                } catch (Exception ex)
                {
                    presetResponse.setException(ex.getMessage());
                }
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            } else
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpsCon.getErrorStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }

                presetResponse.setException(response.toString());
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            }
        } catch (Exception ex)
        {
            presetResponse.setException(ex.getMessage());
        }
        return presetResponse;
    }

    @Override
    public RunnableFileResponse checkRunnableFile(String apiToken, String fileName)
    {
        Validate.notNull(apiToken);
        Validate.notNull(fileName);
        Validate.notEmpty(apiToken);
        Validate.notEmpty(fileName);
        String fileUriExtension = "selfservicefiles";
        RunnableFileResponse runnableFileResponse = new RunnableFileResponse();
        runnableFileResponse.setException("");
        if (!fileIsClass(fileName) && !fileIsZip(fileName))
        {
            runnableFileResponse.setException("Load test file name must be either a .class or .zip file.");
        } else
        {
            try
            {
                super.initSSL();
                URI uri = new URI(super.getScheme(), null, super.getBaseLtpApiUri(), getPort(),
                        getSeparator().concat(getUriVersion()).concat(getSeparator())
                        .concat(fileUriExtension).concat(getSeparator()).concat(fileName), null, null);
                URL url = uri.toURL();
                HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
                httpsCon.setRequestMethod("GET");
                httpsCon.setConnectTimeout(60000);
                httpsCon.setRequestProperty(getTokenHeaderName(), apiToken);
                int responseCode = httpsCon.getResponseCode();
                if (responseCode < 300)
                {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(httpsCon.getInputStream()));

                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    try
                    {
                        runnableFileResponse = Jackson.fromJsonString(response.toString(), RunnableFileResponse.class);
                    } catch (Exception ex)
                    {
                        runnableFileResponse.setException(ex.getMessage());
                    }
                    try
                    {
                        in.close();
                    } catch (IOException ex)
                    {
                    }
                } else
                {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(httpsCon.getErrorStream()));

                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }

                    runnableFileResponse.setException(response.toString());
                    try
                    {
                        in.close();
                    } catch (IOException ex)
                    {
                    }
                }
            } catch (Exception ex)
            {
                runnableFileResponse.setException(ex.getMessage());
            }
        }
        return runnableFileResponse;
    }

    private boolean fileIsZip(String fileName)
    {
        return fileName.endsWith(".zip");
    }

    private boolean fileIsClass(String fileName)
    {
        return fileName.endsWith(".class");
    }

}
