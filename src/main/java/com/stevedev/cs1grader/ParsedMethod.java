/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stevedev.cs1grader;

/**
 *
 * @author steve
 */
public class ParsedMethod {
    private String name;
    private String comments;
    private String header;
    private String body;
    
    public ParsedMethod(String n,String c, String h, String b){
        name = n;
        comments = c;
        header = h;
        body = b;
    }
    
    public boolean commentsContainRegex(){
        return false;
    }
    public boolean headerContainsRegex(){
        return false;
    }
    public boolean bodyContainsRegex(){
        return false;
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
    public String getBody(){
        return body;
    }
}
