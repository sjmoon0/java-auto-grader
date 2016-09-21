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

/**
 * Object that contains the static text associated with a single field in the class
 * @author steve
 * @version 2016.09.20
 */
public class ParsedField {
    private String name;
    private String type;
    private String value;
    private String declaration;
    
    public ParsedField(String n, String t, String v, String d){
        name = n;
        type = t;
        value = v;
        declaration = d;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public String getValue(){
        return value;
    }
    
    public String getDeclaration(){
        return declaration;
    }
}
