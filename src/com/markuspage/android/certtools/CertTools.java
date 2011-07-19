/*
 *  Copyright (C) 2011 Markus Kilås
 * 
 *  This file is part of CertTools.
 *
 *  CertTools is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CertTools is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CertTools.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */
package com.markuspage.android.certtools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncyastle.util.encoders.Base64;

/**
 * Utility methods.
 * 
 * @author Markus Kilås
 */
public class CertTools {

    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";
    public static final String BEGIN_TRUSTED_CERTIFICATE = "-----BEGIN TRUSTED CERTIFICATE-----";
    public static final String END_TRUSTED_CERTIFICATE = "-----END TRUSTED CERTIFICATE-----";
    
    public static List<Certificate> getCertsFromPEM(InputStream in)
            throws IOException, CertificateException {

        List<Certificate> ret = new ArrayList<Certificate>();
        BufferedReader bufRdr = null;
        
        try {
            bufRdr = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufRdr.readLine()) != null) {
                if (line.equals(BEGIN_CERTIFICATE) 
                        || line.equals(BEGIN_TRUSTED_CERTIFICATE)) {
                    continue;
                }
                ByteArrayOutputStream ostr = new ByteArrayOutputStream();
                PrintStream opstr = new PrintStream(ostr);
                opstr.print(line);
                
                while ((line = bufRdr.readLine()) != null && !(line.equals(CertTools.END_CERTIFICATE) || line.equals(CertTools.END_TRUSTED_CERTIFICATE))) {
                    opstr.print(line);
                    System.out.println("Got line: " + line);
                }
                if (line == null) {
                    throw new IOException("Missing end boundary");
                }
                byte[] certbuf = Base64.decode(ostr.toByteArray());

                Certificate cert = CertTools.getCert(certbuf);
                ret.add(cert);
            }
        } finally {
            if (bufRdr != null) {
                bufRdr.close();
            }
        }
        return ret;
    }
    
    public static Certificate getCert(byte[] bytes) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(new ByteArrayInputStream(bytes));
    }
    
    public static String getName(Certificate b) {
        final String result;
        if (b instanceof X509Certificate) {
            final X509Certificate x509 = (X509Certificate) b;
            final String dn = x509.getSubjectX500Principal().getName();
            if (dn.contains("CN=")) {
                int start = dn.indexOf("CN=");
                int end = dn.indexOf(",", start);
                if (end == -1) {
                    end = dn.length();
                }
                result = dn.substring(start, end);
            } else {
                result = dn;
            }
        } else {
            result = "Unknown certificate";
        }
        return result;
    }
}
