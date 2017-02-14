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
                4));
        methodReqs.add(checkMethodBodyContains(yClass, "main",
                new String[] {"String[]"},
                "Ex.10: Scanner object after call to welcome",
                "^.*welcome( )*\\(( )*\\).*Scanner( )+console( )*=( )*new( )* Scanner( )*\\(( )*System.in( )*\\).*while.*$",
                3));
        methodReqs.add(checkMethodBodyContains(yClass, "main",
                new String[] {"String[]"},
                "Ex.12: call to prompt in while loop",
                "^.*while.*prompt( )*\\(( )*currentRoll( )*==( )*3( )*\\).*$",
                3));
        methodReqs.addAll(checkMethodCorrectness(dClass, "rollDie",
                new String[] {},
                "^/\\*\\*.*@return.*\\*/$",
                "^public( )+static( )+int( )+rollDie( )*\\(( )*\\).*$",
                "^.*DIE\\.nextInt( )*\\(( )*6( )*\\)( )*\\+( )*1.*;.*$",
                3));
        methodReqs.addAll(checkMethodCorrectness(dClass, "rollNumDice",
                new String[]{"int"},
                "^/\\*\\*.*@param( )+num.*@return.*\\*/$",
                "^public( )+static( )+String( )+rollNumDice( )*\\(( )*int( )*num( )*\\)$",
                "^.*while.*rollDie.*return.*$",
                4));
        methodReqs.add(checkMethodBodyContains(yClass, "main",
                new String[] {"String[]"},
                "Ex.17: set hand variable to rollFiveDice",
                "^.*String( )+hand( )*=( )*Dice\\.rollFiveDice( )*\\(( )*\\).*while.*$",
                3));
        String makeplayString = "^.*if.*equals.*\"A\".*return( )+1.*"
                + "else( )+if.*equals.*\"(B|b)\".*return( )+2.*"
                + "else( )+if.*equals.*\"(C|c)\".*return( )+3.*"
                + "else( )+if.*equals.*\"(D|d)\".*return( )+4.*"
                + "else( )+if.*equals.*\"(E|e)\".*return( )+5.*"
                + "else( )+if.*equals.*\"(F|f)\".*return( )+6.*"
                + "else( )+if.*equals.*\"(G|g)\".*return( )+7.*"
                + "else( )+if.*equals.*\"(H|h)\".*return( )+8.*"
                + "else( )+if.*equals.*\"(I|i)\".*return( )+9.*"
                + "else( )+if.*equals.*\"(J|j)\".*return( )+10.*"
                + "else( )+if.*equals.*\"(K|k)\".*return( )+11.*"
                + "else( )+if.*equals.*\"(L|l)\".*return( )+12.*"
                + "else( )+if.*equals.*\"(M|m)\".*return( )+13.*";
        methodReqs.addAll(checkMethodCorrectness(yClass, "makePlay",
                new String[]{"String","String"},
                "^/\\*\\*.*@param( )+choice.*@param( )+hand.*@return.*\\*/$",
                "^public( )+static( )+int( )+makePlay( )*\\(( )*String( )+choice( )*,( )*String( )+hand( )*\\)",
                makeplayString,
                5));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        String welcomeOutput = "Hello, welcome to "+getUsername()+"'s Yahtzee game!\\R"+
                "Press Enter to roll 5 dice";
        methodReqs.add(assertSystemOutputRegex(yObject, 
                "welcome", null,null,
                welcomeOutput,3));
        String promptOutput = "Choose one of the following options:\\R"+
                "A:( )*Count 1s(\\t)+G:( )*Three of a kind\\R"+
                "B:( )*Count 2s(\\t)+H:( )*Four of a kind\\R"+
                "C:( )*Count 3s(\\t)+I:( )*Full house\\R"+
                "D:( )*Count 4s(\\t)+J:( )*Small straight\\R"+
                "E:( )*Count 5s(\\t)+K:( )*Large straight\\R"+
                "F:( )*Count 6s(\\t)+L:( )*Yahtzee\\R"+
                "(\\t)+M:( )*Chance";
        methodReqs.add(assertSystemOutputRegex(yObject, 
                "prompt", new Class[] {boolean.class},new Object[]{true},
                "^"+promptOutput,5));
        methodReqs.add(assertSystemOutputRegex(yObject, 
                "prompt", new Class[] {boolean.class},new Object[]{false},
                "^Enter the numbers you wish to reroll, or\\R"+promptOutput,5));
        String mainOutput = "^"+welcomeOutput+".*"
                +promptOutput+".*IllegalArgumentException.*"
                +promptOutput+".*Please try again.*"
                +promptOutput+".*Turn #1( )*=( )*13.*Total Score( )*=( )*13.*"
                +promptOutput+".*Turn #2( )*=( )*12.*Total Score( )*=( )*25.*"
                +promptOutput+".*Turn #3( )*=( )*11.*Total Score( )*=( )*36.*"
                +promptOutput+".*Turn #4( )*=( )*10.*Total Score( )*=( )*46.*"
                +promptOutput+".*Turn #5( )*=( )*9.*Total Score( )*=( )*55.*"
                +promptOutput+".*Turn #6( )*=( )*8.*Total Score( )*=( )*63.*"
                +promptOutput+".*Turn #7( )*=( )*7.*Total Score( )*=( )*70.*"
                +promptOutput+".*Turn #8( )*=( )*6.*Total Score( )*=( )*76.*"
                +promptOutput+".*Turn #9( )*=( )*5.*Total Score( )*=( )*81.*"
                +promptOutput+".*Turn #10( )*=( )*4.*Total Score( )*=( )*85.*"
                +promptOutput+".*Turn #11( )*=( )*3.*Total Score( )*=( )*88.*"
                +promptOutput+".*Turn #12( )*=( )*2.*Total Score( )*=( )*90.*"
                +"Roll.*results( )*:( )*1 2 3 4 5.*"+promptOutput+".*Turn #13( )*=( )*1.*Total Score( )*=( )*91.*";
        methodReqs.add(assertSystemOutputRegex(yObject,
                "main", new Class[]{String[].class}, new Object[]{new String[]{}},
                mainOutput, 25));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        return methodReqs;
    }
}
