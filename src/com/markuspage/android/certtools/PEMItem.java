/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.markuspage.android.certtools;

/**
 *
 * @author markus
 */
public class PEMItem {

    private String title; 
    private byte[] data;
    
    PEMItem(String title, byte[] data) {
        this.title = title;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
    
}
