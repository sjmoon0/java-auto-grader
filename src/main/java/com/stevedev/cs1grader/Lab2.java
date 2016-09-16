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
            requirements.addAll(lab.checkMethods());
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
    
    
    public ArrayList<Requirement> checkMethods(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"main","^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$","^public( )+static( )+void( )+main( )*\\(( )*String( )*\\[( )*\\]( )+args( )*\\)$","^\\{.*greetCustomer( )*\\(( )*\\)( )*;.*checkoutCustomer( )*\\(( )*\\)( )*;.*\\}$",3));
        return methodReqs;
    }
}
