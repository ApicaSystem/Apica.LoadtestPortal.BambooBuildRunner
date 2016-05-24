/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica;

import com.apica.ssl.ISslHostnameVerifier;
import com.apica.ssl.ISslTrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.jsoup.helper.Validate;

/**
 *
 * @author andras.nemes
 */
public abstract class LtpWebApiServiceBase
{
    private final String baseLtpApiUri;
    private final String uriVersion;
    private final String separator;
    private final String scheme;
    private final int port;
    private final String tokenHeaderName;
    private final ISslHostnameVerifier sslHostnameVerifier;
    private final ISslTrustManager sslTrustManager;    
    
    public LtpWebApiServiceBase(ISslHostnameVerifier sslHostnameVerifier, ISslTrustManager sslTrustManager)
    {
        baseLtpApiUri = "api-ltp.apicasystem.com";
        uriVersion = "prod";
        separator = "/";
        scheme = "https";
        port = 443;
        tokenHeaderName = "x-apica-auth-token";
        Validate.notNull(sslHostnameVerifier);
        Validate.notNull(sslTrustManager);
        this.sslHostnameVerifier = sslHostnameVerifier;
        this.sslTrustManager = sslTrustManager;
    }

    public String getBaseLtpApiUri()
    {
        return baseLtpApiUri;
    }

    public String getUriVersion()
    {
        return uriVersion;
    }

    public String getSeparator()
    {
        return separator;
    }

    public String getScheme()
    {
        return scheme;
    }

    public int getPort()
    {
        return port;
    }

    public String getTokenHeaderName()
    {
        return tokenHeaderName;
    }
    
    public void initSSL() throws NoSuchAlgorithmException, KeyManagementException
    {
        TrustManager[] trustAllCertificates = this.sslTrustManager.certificatesToTrust();
        HostnameVerifier trustAllHostnames = this.sslHostnameVerifier.hostnamesToTrust();
        System.setProperty("jsse.enableSNIExtension", "false");
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCertificates, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
    }
    
}
