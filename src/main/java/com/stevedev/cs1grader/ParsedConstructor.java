/*
 * Copyright (C) 2016 steve
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.stevedev.cs1grader;

import java.util.ArrayList;

/**
 *
 * @author steve
 */
public class ParsedConstructor {
    private String name;
    private String comments;
    private String header;
    private ArrayList<String> params;
    private String body;
    
    public ParsedConstructor(String n,String c, String h, ArrayList<String> p, String b){
        name = n;
        comments = c;
        header = h;
        params = p;
        body = b;
    }
    public String getName(){
        return name;
    }
    public String getComments(){
        return comments;
    }
    public String getHeader(){
        return header;
    }
    /**
     * 
     * @return String versions of Parameter types that the method takes
     */
    public ArrayList<String> getParams(){
        return params;
    }
    public String getBody(){
        return body;
    }
}
