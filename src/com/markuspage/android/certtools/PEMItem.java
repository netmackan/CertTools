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

/**
 *
 * @author Markus Kilås
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
