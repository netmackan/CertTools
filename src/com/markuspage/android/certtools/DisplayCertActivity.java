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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 *
 * @author Markus Kilås
 */
public class DisplayCertActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        Intent intent = getIntent();
        System.out.println("intent: " + intent);
       
        
        byte[] certbytes = intent.getByteArrayExtra("certbytes");
        
        TextView tv = new TextView(this);
        tv.setVerticalScrollBarEnabled(true);
        tv.setMovementMethod(new ScrollingMovementMethod());
        try {
            Certificate cert = CertTools.getCert(certbytes);
            setTitle(CertTools.getName(cert));
            tv.setText(cert.toString());
        } catch (CertificateException ex) {
            tv.setText("Error: " + ex.getLocalizedMessage());
        }
        setContentView(tv);
        
    }
}
