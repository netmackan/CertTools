/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author markus
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
