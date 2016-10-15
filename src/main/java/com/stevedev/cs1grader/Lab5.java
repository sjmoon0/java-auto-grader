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
 * Requirements specific to Lab5
 * @author steve
 * @version 2016.10.15
 */
public class Lab5 extends Assignment{
    private static final String PACKAGE_NAME = "yahtzee";
    private static final String MAIN_CLASS = "Yahtzee";
    private static ParsedClass yClass;
    private static ParsedClass dClass;
    private static Object yObject;
    private static Object dObject;
    private static boolean usingNetbeans = true;
    
    public static void main(String[] args){
        Lab5 lab = new Lab5();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 && requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodSyntax());
            //requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements, args.length>0?Boolean.parseBoolean(args[0]):false);
        
    }
    
    public Lab5(){
        super("Lab",5,true);
        yObject = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        dObject = getInstance(PACKAGE_NAME+".Dice",null,null);
        yClass = JavaFileParser.parse(PACKAGE_NAME+"/"+MAIN_CLASS);
        dClass = JavaFileParser.parse(PACKAGE_NAME+"/Dice");
        if(yClass==null || dClass==null){
            System.err.println("Classes not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(yClass, "UN",
                "^public( )+static( )+final( )+String( )+UN( )*=( )*\""+getUsername()+"\"( )*;$",3));
        reqs.add(checkFieldExists(dClass, "DIE",
                "^public( )+static( )+final( )+Random( )+DIE( )*=( )*new( )+Random( )*\\(( )*1234567890( )*\\)( )*;$",4));
        return reqs;
    }
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.add(checkMethodBodyContains(yClass, "main",
                new String[] {"String[]"},
                "Ex.5: proper variable declarations",
                "^.*int( )+score( )*=( )*0( )*;.*int( )+play( )*=( )*1( )*;.*int( )+currentRoll( )*=( )*1( )*;.*while.*$",
                5));
        methodReqs.add(checkMethodBodyContains(yClass, "main",
                new String[] {"String[]"},
                "Ex.6: while loop condition",
                "^.*currentRoll.*while( )*\\(( )*(play( )*\\<( )*14|play( )*\\<=( )*13)( )*\\).*$",
                5));
        methodReqs.add(checkMethodBodyContains(yClass, "main",
                new String[] {"String[]"},
                "Ex.10: Scanner object after call to welcome",
                "^.*welcome( )*\\(( )*\\).*Scanner( )+console( )*=( )*new( )* Scanner( )*\\(( )*System.in( )*\\).*while.*$",
                5));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        String welcomeOutput = "Hello, welcome to "+getUsername()+"'s Yahtzee game!\\R"+
                "Press Enter to roll 5 dice.";
        methodReqs.add(assertSystemOutputRegex(yObject, 
                "welcome", null,null,
                welcomeOutput,5));
        return methodReqs;
    }
}
