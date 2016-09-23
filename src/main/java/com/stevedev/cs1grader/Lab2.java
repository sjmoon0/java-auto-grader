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
 * Contains requirements specific to Lab 2
 * @author steve
 * @version 2016.09.17
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
            requirements.addAll(lab.checkMethodSyntax());
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
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(parsedClass,"EMPLOYEE_UN",
                "^public( )+static( )+final( )+String( )+EMPLOYEE_UN( )*=( )*\""+getUsername()+"\"( )*;$",3));
        reqs.add(checkFieldExists(parsedClass,"BANANA_COST",
                "^public( )+static( )+final( )+double( )+BANANA_COST( )*=( )*((\\d)+|(\\d)+\\.(\\d)*)( )*;$",3));
        return reqs;
    }
    
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"main",
                new String[] {""},
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+main( )*\\(( )*String( )*\\[( )*\\]( )+args( )*\\)$",
                "^\\{.*greetCustomer( )*\\(( )*\\)( )*;.*checkoutCustomer( )*\\(( )*\\)( )*;.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"greetCustomer",
                new String[] {""},
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+greetCustomer( )*\\(( )*\\)$",
                "^\\{.*String( )+customerName.*println.*EMPLOYEE_UN.*System\\.out\\.println.*BANANA_COST.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"checkoutCustomer",
                new String[] {""},
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+checkoutCustomer( )*\\(( )*\\)$",
                "^\\{.*System\\.out\\.print.*calculateBananaCostLoop.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"calculateBananaCost",
                new String[] {""},
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+calculateBananaCost( )*\\(( )*\\)$",
                "^\\{.*int( )+numBananas( )*\\=( )*10( )*;.*System\\.out\\.println( )*\\(( )*(numBananas( )*\\*( )*BANANA_COST|BANANA_COST( )*\\*( )*numBananas)( )*\\).*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"calculateBananaCostLoop",
                new String[] {""},
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
