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
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);
    }
    
    public Lab3()
    {
        super("Lab",3);
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
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"main",
                new String[] {"String[]"},
                "^/\\*\\*([^\\*]|\\*(?!/))*?.*?\\*/$",
                "^public( )+static( )+void( )+main( )*\\(( )*String( )*\\[( )*\\]( )+args( )*\\)$",
                "",3));
        methodReqs.addAll(checkMethodCorrectness(parsedClass,"add",
                new String[] {"double"},
                "^/\\*\\*.*@param( )+num1.*@param( )+.*num2.*@return.*sum.*$",
                "^public( )+static( )+double( )+add( )*\\(( )*double( )+num1( )*,( )*double( )+num2( )*\\)$",
                "^\\{.*(num1( )*\\+( )*num2|num2( )*\\+( )*num1).*\\}$",3));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        methodReqs.add(assertSystemOutputRegex(testObj,
                "main",new Class[] {String[].class},new Object[] {new String[]{""}},
                "^$",5));
        return methodReqs;
    }
}
