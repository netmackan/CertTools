package com.markuspage.android.certtools;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CertToolsActivity extends ListActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        final List<PEMItem> items = new ArrayList<PEMItem>();
        
        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, 
                items));
        
        Intent intent = getIntent();
        if (intent.getData() == null) {
            items.add(new PEMItem("(No certificates)", null));
            // TODO: Request file manager intent
        } else {
              ListView lv = getListView();
              lv.setTextFilterEnabled(true);

              lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                  
                    Toast.makeText(getApplicationContext(), 
                            ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                  
                    Intent myIntent = new Intent(view.getContext(), DisplayCertActivity.class);
                    myIntent.putExtra("certbytes", items.get(position).getData());
                    startActivityForResult(myIntent, 0);
                    
                }
              });

            System.out.println("Intent: " + intent);
            Uri uri = intent.getData();
            File file = new File(uri.getPath());
            setTitle(file.getName());
          
            List<Certificate> certs;
            try {
                certs = CertTools.getCertsFromPEM(new FileInputStream(file));
                int index = 0;
                for (final Certificate b : certs) {
                    index++;
                    final byte[] bytes = b.getEncoded();
                    items.add(new PEMItem("(" + index + ") " + CertTools.getName(b), bytes));
                }
            } catch (FileNotFoundException ex) {
                TextView tv = new TextView(this);
                setContentView(tv);
                tv.setText("File not found: \n" + ex.getLocalizedMessage());
                Logger.getLogger(CertToolsActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                TextView tv = new TextView(this);
                tv.setText("Error: \n" + ex.getLocalizedMessage());
                setContentView(tv);
                Logger.getLogger(CertToolsActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CertificateException ex) { // TODO move into for loop
                TextView tv = new TextView(this);
                tv.setText("Error: \n" + ex.getLocalizedMessage());
                setContentView(tv);
                Logger.getLogger(CertToolsActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
