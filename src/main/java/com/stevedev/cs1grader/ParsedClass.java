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
 * Object that contains static text from a single java file for each gradable part of a class.
 * @author steve
 * @version 2016.09.17
 */
public class ParsedClass {
    private String name;
    private String introComments;
    private ArrayList<String> fields;
    private ArrayList<ParsedMethod> methods;
    
    public ParsedClass(String n, String i, ArrayList<String> f, ArrayList<ParsedMethod> m){
        name = n;
        introComments = i;
        fields = f;
        methods = m;
    }
    
    public String getName(){
        return name;
    }
    
    public String getComments(){
        return introComments;
    }
    
    public ArrayList<String> getFields(){
        return fields;
    }
    
    public ArrayList<ParsedMethod> getMethods(){
        return methods;
    }
}
