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

import static com.stevedev.cs1grader.Assignment.getUsername;
import java.util.ArrayList;

/**
 * Contains requirements specific to Lab3
 * @author steve
 */
public class Lab3 extends Assignment {
    private static final String MAIN_CLASS = "WonkyCalculator";
    private static ParsedClass parsedClass;
    private static Object testObj;
    
    public static void main(String[] args)
    {
        Lab3 lab = new Lab3();
        ArrayList<Requirement> requirements = checkInitialConditions(MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 &&requirements.get(1).getReceived()>0)
        {
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodSyntax());
            requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);
    }
    
    public Lab3()
    {
        super("Lab",3,false);
        testObj = getInstance(MAIN_CLASS,null,null);
        parsedClass = JavaFileParser.parse(MAIN_CLASS);
        if(parsedClass==null){
            System.err.println("Class was not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(parsedClass,"UN",
                "^public( )+static( )+final( )+String( )+UN( )*=( )*\""+getUsername()+"\"( )*;$",3));
        return reqs;
    }
    
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
//        methodReqs.addAll(checkMethodCorrectness(parsedClass,"main",
//                new String[] {"String[]"},
//                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
//                "^public( )+static( )+void( )+main( )*\\(( )*String( )*\\[( )*\\]( )+args( )*\\)$",
//                "",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"add",
                new String[] {"double","double"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(s|S)um.*$",
                "^public( )+static( )+double( )+add( )*\\(( )*double( )+num1( )*,( )*double( )+num2( )*\\)$",
                "^\\{.*(num1( )*\\+( )*num2|num2( )*\\+( )*num1).*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"add",
                new String[] {"int","int"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(s|S)um.*$",
                "^public( )+static( )+int( )+add( )*\\(( )*int( )+num1( )*,( )*int( )+num2( )*\\)$",
                "^\\{.*(num1( )*\\+( )*num2|num2( )*\\+( )*num1).*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"subtract",
                new String[] {"double","double"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(d|D)ifference.*$",
                "^public( )+static( )+double( )+subtract( )*\\(( )*double( )+num1( )*,( )*double( )+num2( )*\\)$",
                "^\\{.*num1( )*\\-( )*num2.*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"subtract",
                new String[] {"int","int"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(d|D)ifference.*$",
                "^public( )+static( )+int( )+subtract( )*\\(( )*int( )+num1( )*,( )*int( )+num2( )*\\)$",
                "^\\{.*num1( )*\\-( )*num2.*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"multiply",
                new String[] {"double","double"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(p|P)roduct.*$",
                "^public( )+static( )+double( )+multiply( )*\\(( )*double( )+num1( )*,( )*double( )+num2( )*\\)$",
                "^\\{.*(num1( )*\\*( )*num2|num2( )*\\*( )*num1).*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"multiply",
                new String[] {"int","int"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(p|P)roduct.*$",
                "^public( )+static( )+int( )+multiply( )*\\(( )*int( )+num1( )*,( )*int( )+num2( )*\\)$",
                "^\\{.*(num1( )*\\*( )*num2|num2( )*\\*( )*num1).*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"modulo",
                new String[] {"int","int"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(r|R)emainder.*$",
                "^public( )+static( )+int( )+modulo( )*\\(( )*int( )+num1( )*,( )*int( )+num2( )*\\)$",
                "^\\{.*num1( )*%( )*num2.*\\}$",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"divideIntToDouble",
                new String[] {"int","int"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*(q|Q)uotient.*$",
                "^public( )+static( )+double( )+divideIntToDouble( )*\\(( )*int( )+num1( )*,( )*int( )+num2( )*\\)$",
                "^\\{.*(\\(( )*double( )*\\)( )*num1( )*/( )*num2|num1( )*/( )*\\(( )*double( )*\\)( )*num2).*\\}",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"power",
                new String[] {"double","int"},
                "^/\\*\\*.*@param( )+base.*@param( )+.*exponent.*@return.*(p|P)ower.*$",
                "^public( )+static( )+double( )+power( )*\\(( )*double( )+base( )*,( )*int( )+exponent( )*\\)$",
                "^\\{.*double( )+result( )*=( )*1( )*;.*for.*\\{.*result( )*\\*.*base.*\\}.*return( )+result.*\\}",2));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"concatenate",
                new String[] {"String","String"},
                "^/\\*\\*.*@param( )+str1.*@param( )+.*str2.*@return.*$",
                "^public( )+static( )+String( )+concatenate( )*\\(( )*String( )+str1( )*,( )*String( )+str2( )*\\)$",
                "^\\{.*str1( )*\\+( )*str2.*\\}$",2));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        methodReqs.add(checkMethodReturnType(testObj,"add",
                new Class[]{double.class,double.class},
                new Object[]{2,3},
                double.class,
                5.0,2));
        methodReqs.add(checkMethodReturnType(testObj,"add",
                new Class[]{int.class,int.class},
                new Object[]{2,3},
                int.class,
                5,2));
        methodReqs.add(checkMethodReturnType(testObj,"subtract",
                new Class[]{double.class,double.class},
                new Object[]{2.0,3.0},
                double.class,
                -1.0,2));
        methodReqs.add(checkMethodReturnType(testObj,"subtract",
                new Class[]{int.class,int.class},
                new Object[]{2,3},
                int.class,
                -1,2));
        methodReqs.add(checkMethodReturnType(testObj,"multiply",
                new Class[]{double.class,double.class},
                new Object[]{2.0,3.0},
                double.class,
                6.0,2));
        methodReqs.add(checkMethodReturnType(testObj,"multiply",
                new Class[]{int.class,int.class},
                new Object[]{2,3},
                int.class,
                6,2));
        methodReqs.add(checkMethodReturnType(testObj,"modulo",
                new Class[]{int.class,int.class},
                new Object[]{5,2},
                int.class,
                1,2));
        methodReqs.add(checkMethodReturnType(testObj,"divideIntToDouble",
                new Class[]{int.class,int.class},
                new Object[]{5,2},
                double.class,
                2.5,2));
        methodReqs.add(checkMethodReturnType(testObj,"power",
                new Class[]{double.class,int.class},
                new Object[]{2.0,3},
                double.class,
                8.0,2));
        methodReqs.add(checkMethodReturnType(testObj,"concatenate",
                new Class[]{String.class,String.class},
                new Object[]{"abc","def"},
                int.class,
                "abcdef",2));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        String nl = "\\R";
        String mainOutput= "^Welcome to "+getUsername()+"'s Wonky Calculator"+nl+
                "Please input your first integer:"+nl+
                "Please input your second integer:"+nl+
                "Please input your first decimal:"+nl+
                "Please input your second decimal:"+nl+
                "Please input your first String:"+nl+
                "Please input your second String:"+nl+
                "Sum of doubles \\= 3\\.\\d+"+nl+
                "Sum of ints \\= 9"+nl+
                "Difference of doubles \\= 1\\.\\d+"+nl+
                "Difference of ints \\= 1"+nl+
                "Product of doubles \\= 3\\.\\d\\d+"+nl+
                "Product of ints \\= 20"+nl+
                "Remainder of ints \\= 1"+nl+
                "Decimal quotient of ints \\= 1\\.\\d\\d+"+nl+
                "2.4 to the power of 5 \\= 79\\.\\d+"+nl+
                "Concatenation of Strings \\= abcdef$"
                ;
        methodReqs.add(assertSystemOutputRegex(testObj,
                "main",new Class[] {String[].class},new Object[] {new String[]{""}},
                mainOutput,10));
        return methodReqs;
    }
}
