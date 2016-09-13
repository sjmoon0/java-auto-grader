/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stevedev.cs1grader;

import java.util.ArrayList;

/**
 *
 * @author steve
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
