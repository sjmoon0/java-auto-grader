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
 * Requirements specific to Lab 4
 * @author steve
 */
public class Lab4 extends Assignment{
    private static final String PACKAGE_NAME ="business";
    private static final String MAIN_CLASS = "BusinessRunner";
    private static ParsedClass brClass;
    private static ParsedClass wcClass;
    private static ParsedClass bsClass;
    private static Object brObj;
    private static Object wcObj;
    private static Object bsObj;
    
    public static void main(String[] args){
        Lab4 lab = new Lab4();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 &&requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodSyntax());
            requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);
    }
    
    public Lab4(){
        super("Lab",4,false);
        brObj = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        wcObj = getInstance(PACKAGE_NAME+".WonkyCalculator",null,null);
        bsObj = getInstance(PACKAGE_NAME+".BananaStand",null,null);
        brClass = JavaFileParser.parse(PACKAGE_NAME+"/"+MAIN_CLASS);
        wcClass = JavaFileParser.parse(PACKAGE_NAME+"/WonkyCalculator");
        bsClass = JavaFileParser.parse(PACKAGE_NAME+"/BananaStand");
        if(brClass==null || wcClass ==null||bsClass==null){
            System.err.println("Class was not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(bsClass, "EMPLOYEE_UN",
                "^public( )+static( )+final( )+String( )+EMPLOYEE_UN( )*=( )*\""+getUsername()+"\"( )*;$",3));
        reqs.add(checkFieldExists(bsClass,"BANANA_COST",
                "^public( )+static( )+final( )+double( )+BANANA_COST( )*=( )*((\\d)+|(\\d)+\\.(\\d)*)( )*;$",3));
        return reqs;
    }
    
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(bsClass,"greetCustomer",
                new String[] {"String"},
                "^/\\*\\*.*@param( )+customerName.*\\*/$",
                "^public( )+void( )+greetCustomer( )*\\(( )*String( )+customerName( )*\\)( )*throws( )+IllegalArgumentException$",
                "^\\{.*if( )*\\(( )*customerName\\.length\\(( )*\\)( )*\\=\\=( )*0( )*\\)( )*\\{.*throw( )+new( )+IllegalArgumentException.*\\}.*println.*customerName.*EMPLOYEE_UN.*System\\.out\\.println.*BANANA_COST.*\\}$",3));
        methodReqs.addAll(checkMethodCorrectness(wcClass,"multiply",
                new String[] {"double","double"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(p|P)roduct.*$",
                "^public( )+static( )+double( )+multiply( )*\\(( )*double( )+num1( )*,( )*double( )+num2( )*\\)$",
                "^\\{.*(num1( )*\\*( )*num2|num2( )*\\*( )*num1).*\\}$",2));
        // BusinessRunner main correct objects?
        methodReqs.add(checkMethodBodyContains(brClass, "main",
                new String[] {"String[]"},
                "creation of objects",
                "^.*BananaStand( )+bs( )*\\=( )*new( )+BananaStand( )*\\(( )*\\)( )*;.*WonkyCalculator( )+wc( )*\\=( )*new( )+WonkyCalculator( )*\\(( )*\\)( )*;.*Scanner( )+console( )*\\=( )*new( )+Scanner( )*\\(( )*System\\.in( )*\\)( )*;.*$",
                6));
        // BusinessRunner proper try/catch?
        methodReqs.add(checkMethodBodyContains(brClass, "main",
                new String[] {"String[]"},
                "proper try with 3 catch blocks",
                //"^.*?"+"try( )*?\\{.*?\\}"+"catch( )*?\\(( )*?NumberFormatException.*?\\)( )*?\\{.*?\\}"/*+"catch( )*\\(( )*IllegalArgumentException.*\\)( )*\\{.*\\}"+"catch( )*\\(( )*InputMismatchException.*\\)( )*\\{.*\\}"*/+".*?$",
                "^.*try.*catch( )*\\(( )*NumberFormatException.*\\).*catch( )*\\(( )*IllegalArgumentException.*\\).*catch( )*\\(( )*InputMismatchException.*\\)( )*.*$",
                10));
        // BusinessRunner proper method calls?
            //Integer.parseInt
        methodReqs.add(checkMethodBodyContains(brClass, "main",
                new String[] {"String[]"},
                "proper Integer.parseInt call",
                "^.*try.*int( )+numCustomers( )*\\=( )*Integer\\.parseInt( )*\\(( )*args\\[0\\]( )*\\).*catch.*$",
                6));
            //bs.greetCustomer
        methodReqs.add(checkMethodBodyContains(brClass, "main",
                new String[] {"String[]"},
                "proper bs.greetCustomer call",
                "^.*try.*else.*for.*bs\\.greetCustomer( )*\\(( )*console\\.next\\(( )*\\)( )*\\).*catch.*$",
                6));
            //printf
        methodReqs.add(checkMethodBodyContains(brClass, "main",
                new String[] {"String[]"},
                "proper printf call",
                "^.*try.*else.*for.*out\\.printf( )*\\(( )*\".*%\\.2f%n.*\"( )*,( )*wc\\.multiply\\(( )*numBananas( )*,( )*BananaStand\\.BANANA_COST( )*\\)( )*\\).*catch.*$",
                8));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        methodReqs.add(checkMethodReturnType(wcObj,"multiply",
                new Class[]{double.class,double.class},
                new Object[]{2.0,3.0},
                double.class,
                6.0,3));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        String mainOutput= "^What is your name\\?\\R"+
                "Hello, George, my name is "+getUsername()+"\\R"+
                "Each banana costs \\$(\\d+|\\d+.\\d*)\\R"+
                "How many bananas would you like\\?\\R"+
                "Your total comes to \\$(\\d+|\\d+.\\d*)\\R"+
                "What is your name\\?\\R"+
                "Hello, Michael, my name is "+getUsername()+"\\R"+
                "Each banana costs \\$(\\d+|\\d+.\\d*)\\R"+
                "How many bananas would you like\\?\\R"+
                "Your total comes to \\$(\\d+|\\d+.\\d*)\\R"+
                "What is your name\\?\\R"+
                "Hello, Bluth, my name is "+getUsername()+"\\R"+
                "Each banana costs \\$(\\d+|\\d+.\\d*)\\R"+
                "How many bananas would you like\\?\\R"+
                "Input not an integer"
                ;
        methodReqs.add(assertSystemOutputRegex(brObj,
                "main",new Class[] {String[].class},new Object[] {new String[]{"3"}},
                mainOutput,25));
        return methodReqs;
    }
}
