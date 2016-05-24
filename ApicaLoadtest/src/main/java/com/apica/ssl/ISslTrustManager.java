/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.ssl;

import javax.net.ssl.TrustManager;

/**
 *
 * @author andras.nemes
 */
public interface ISslTrustManager
{
    public TrustManager[] certificatesToTrust();
}
