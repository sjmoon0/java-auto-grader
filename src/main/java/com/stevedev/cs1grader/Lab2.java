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
public class Lab2 extends Assignment
{
    private static final String MAIN_CLASS = "BananaStand";
    private static ParsedClass parsedClass;
    private static Object testObj;
    
    public static void main(String[] args)
    {
        Lab2 lab = new Lab2();
        ArrayList<Requirement> requirements = checkInitialConditions(MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 &&requirements.get(1).getReceived()>0)
        {
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodsSyntax());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);
    }

    public Lab2()
    {
        super("Lab",2);
        testObj = getInstance(MAIN_CLASS,null,null);
        parsedClass = JavaFileParser.parse(MAIN_CLASS);
        if(parsedClass==null){
            System.err.println("Class was not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        String EMPLOYEE_UN = "^public( )+static( )+final( )+String( )+EMPLOYEE_UN( )*=( )*\""+getUsername()+"\"( )*;$";
        String BANANA_COST = "^public( )+static( )+final( )+double( )+BANANA_COST( )*=( )*((\\d)+|(\\d)+\\.(\\d)*)( )*;$";
        String[] requiredFields = {EMPLOYEE_UN,BANANA_COST};
        return checkFieldsExist(parsedClass,requiredFields,3);
    }
    
    
    public ArrayList<Requirement> checkMethodsSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"main",
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+main( )*\\(( )*String( )*\\[( )*\\]( )+args( )*\\)$",
                "^\\{.*greetCustomer( )*\\(( )*\\)( )*;.*checkoutCustomer( )*\\(( )*\\)( )*;.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"greetCustomer",
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+greetCustomer( )*\\(( )*\\)$",
                "^\\{.*String( )+customerName.*println.*EMPLOYEE_UN.*System\\.out\\.println.*BANANA_COST.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"checkoutCustomer",
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+checkoutCustomer( )*\\(( )*\\)$",
                "^\\{.*System\\.out\\.print.*calculateBananaCostLoop.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"calculateBananaCost",
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+calculateBananaCost( )*\\(( )*\\)$",
                "^\\{.*int( )+numBananas( )*\\=( )*10( )*;.*System\\.out\\.println( )*\\(( )*(numBananas( )*\\*( )*BANANA_COST|BANANA_COST( )*\\*( )*numBananas)( )*\\).*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"calculateBananaCostLoop",
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+calculateBananaCostLoop( )*\\(( )*\\)$",
                "^\\{.*int( )+numBananas( )*\\=( )*10( )*;.*double( )+sum.*for.*\\{.*sum.*\\+.*BANANA_COST.*\\}.*System\\.out\\.println( )*\\(( )*sum( )*\\).*\\}$",3));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        methodReqs.add(assertSystemOutputRegex(testObj,
                "greetCustomer",null,null,
                "^Hello [a-zA-Z]*, my name is "+getUsername()+"\\REach banana costs \\$(\\d+|\\d+.\\d*)$",5));
        methodReqs.add(assertSystemOutputRegex(testObj,
                "checkoutCustomer",null,null,
                "^Thank you for shopping, your total is \\$(\\d+|\\d+.\\d*)$",5));
        methodReqs.add(assertSystemOutputRegex(testObj,
                "calculateBananaCost",null,null,
                "^(\\d+|\\d+.\\d*)$",5));
        methodReqs.add(assertSystemOutputRegex(testObj,
                "calculateBananaCostLoop",null,null,
                "^(\\d+|\\d+.\\d*)$",5));
        methodReqs.add(assertSystemOutputRegex(testObj,
                "main",new Class[] {String[].class},new Object[] {new String[]{""}},
                "^Hello [a-zA-Z]*, my name is "+getUsername()+"\\REach banana costs \\$(\\d+|\\d+.\\d*)\\RThank you for shopping, your total is \\$(\\d+|\\d+.\\d*)$",5));
        return methodReqs;
    }
}
