/*
 *  Copyright (C) 2011, 2012 Markus Kilås
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

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main Activity showing the list of certificates.
 * 
 * @author Markus Kilås
 */
public class CertToolsActivity extends ListActivity
{
    private static final int PICKFILE_RESULT_CODE = 1;
    
    private final List<PEMItem> items = new ArrayList<PEMItem>();
    
    private static final PEMItem NO_CERTIFICATES = new PEMItem("(No certificates)", null);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, 
                items));
        createFromIntent(getIntent());
    }
   
    private void createFromIntent(Intent intent) {
        items.clear();
        if (intent.getData() == null) {
            items.add(NO_CERTIFICATES);
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
            
            ContentResolver cr = getContentResolver();
            Uri uri = intent.getData();
            File file = new File(uri.getPath());
            setTitle(file.getName());
          
            List<Certificate> certs;
            try {
                try {
                    certs = CertTools.getCertsFromPEM(cr.openInputStream(uri));
                } catch (IOException ex) {
                    Logger.getLogger(CertToolsActivity.class.getName()).log(Level.INFO, "Not a PEM: " + ex.getMessage());
                    certs = Collections.singletonList(CertTools.getCert(cr.openInputStream(uri)));
                }
                int index = 0;
                for (final Certificate b : certs) {
                    index++;
                    final byte[] bytes = b.getEncoded();
                    items.add(new PEMItem("(" + index + ") " + CertTools.getName(b), bytes));
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CertToolsActivity.class.getName()).log(Level.SEVERE, null, ex);
                items.add(new PEMItem("File not found: \n" + ex.getLocalizedMessage(), null));
            } catch (IOException ex) {
                Logger.getLogger(CertToolsActivity.class.getName()).log(Level.SEVERE, null, ex);
                items.add(new PEMItem("Error: \n" + ex.getLocalizedMessage(), null));
            } catch (CertificateException ex) { // TODO move into for loop
                Logger.getLogger(CertToolsActivity.class.getName()).log(Level.SEVERE, null, ex);
                items.add(new PEMItem("Error: \n" + ex.getLocalizedMessage(), null));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        System.out.println("Created options menu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open:
                Toast.makeText(this, "Open certificate file", Toast.LENGTH_SHORT).show();
                
                Intent intentBrowseFiles = new Intent(Intent.ACTION_GET_CONTENT);
                intentBrowseFiles.setType("*/*");
                intentBrowseFiles.addCategory(Intent.CATEGORY_OPENABLE);
                
                startActivityForResult(intentBrowseFiles, PICKFILE_RESULT_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE: 
                if(resultCode==RESULT_OK){
                    createFromIntent(data);
                    getListView().invalidateViews();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
    
    
}
