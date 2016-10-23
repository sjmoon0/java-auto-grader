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
 * Requirements specific to Lab6
 * @author steve
 * @version 2016.10.23
 */
public class Lab6 extends Assignment{
    private static final String PACKAGE_NAME = "yahtzee";
    private static final String MAIN_CLASS = "Yahtzee";
    private static ParsedClass yClass;
    private static ParsedClass dClass;
    private static ParsedClass eClass;
    private static Object yObject;
    private static Object yObject2;
    private static Object dObject;
    private static Object eObject;
    private static boolean usingNetbeans = true;
    
    public static void main(String[] args){
        Lab6 lab = new Lab6();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 && requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodSyntax());
            requirements.addAll(lab.checkMethodReturns());
            //requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements, args.length>0?Boolean.parseBoolean(args[0]):false);
        
    }
    
    public Lab6(){
        super("Lab",6,true);
        yObject = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        yObject2 = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        dObject = getInstance(PACKAGE_NAME+".Dice",null,null);
        eObject = getInstance(PACKAGE_NAME+".Engine",null,null);
        yClass = JavaFileParser.parse(PACKAGE_NAME+"/"+MAIN_CLASS);
        dClass = JavaFileParser.parse(PACKAGE_NAME+"/Dice");
        eClass = JavaFileParser.parse(PACKAGE_NAME+"/Engine");
        if(yClass==null || dClass==null){
            System.err.println("Classes not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(yClass, "UN",
                "^public( )+static( )+final( )+String( )+UN( )*=( )*\""+getUsername()+"\"( )*;$",2));
        reqs.add(checkFieldExists(dClass, "DIE",
                "^public( )+static( )+final( )+Random( )+DIE( )*=( )*new( )+Random( )*\\(( )*1234567890( )*\\)( )*;$",2));
        return reqs;
    }
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(yClass, "sanitizeInput",
                new String[]{"String"},
                "^/\\*\\*.*@param.*input.*@return.*\\*/$",
                "^public( )+static( )+String( )+sanitizeInput( )*\\(( )*String( )*input( )*\\).*$",
                "^.*String( )*result.*for.*input\\.charAt( )*\\(.*\\).*(\\=\\=( )*' '( )*\\|\\|( )*.*\\=\\=( )*'.t'.*|"
                        + ".*\\=\\=( )*'.t'( )*\\|\\|.*\\=\\=( )*' ').*"
                        + "if.*!( )*Character\\.isDigit\\(.*\\).*throw.*IllegalArgumentException.*"
                        + "if.*(0.*\\|\\|.*7|7.*\\|\\|.*0).*throw.*IllegalArgumentException.*"
                        + "return.*$",
                5));
        String makeplayString = "^.*hand( )*\\=( )*sanitizeInput\\(( )*hand( )*\\).*"
                + "Engine( )+e( )*\\=( )*new( )*Engine.*"
                + "if.*equals.*\"(A|a)\".*return( )+e\\.checkCount( )*\\(( )*hand( )*,( )*1( )*\\).*"
                + "else( )+if.*equals.*\"(B|b)\".*return( )+e\\.checkCount( )*\\(( )*hand( )*,( )*2( )*\\).*"
                + "else( )+if.*equals.*\"(C|c)\".*return( )+e\\.checkCount( )*\\(( )*hand( )*,( )*3( )*\\).*"
                + "else( )+if.*equals.*\"(D|d)\".*return( )+e\\.checkCount( )*\\(( )*hand( )*,( )*4( )*\\).*"
                + "else( )+if.*equals.*\"(E|e)\".*return( )+e\\.checkCount( )*\\(( )*hand( )*,( )*5( )*\\).*"
                + "else( )+if.*equals.*\"(F|f)\".*return( )+e\\.checkCount( )*\\(( )*hand( )*,( )*6( )*\\).*"
                + "else( )+if.*equals.*\"(G|g)\".*return( )+e\\.checkThreeOfAKind( )*\\(( )*hand( )*\\).*"
                + "else( )+if.*equals.*\"(H|h)\".*return( )+e\\.checkFourOfAKind( )*\\(( )*hand( )*\\).*"
                + "else( )+if.*equals.*\"(I|i)\".*return( )+e\\.checkFullHouse( )*\\(( )*hand( )*\\).*"
                + "else( )+if.*equals.*\"(J|j)\".*return( )+e\\.checkSmallStraight( )*\\(( )*hand( )*\\).*"
                + "else( )+if.*equals.*\"(K|k)\".*return( )+e\\.checkLargeStraight( )*\\(( )*hand( )*\\).*"
                + "else( )+if.*equals.*\"(L|l)\".*return( )+e\\.checkYahtzee( )*\\(( )*hand( )*\\).*"
                + "else( )+if.*equals.*\"(M|m)\".*return( )+e\\.checkChance( )*\\(( )*hand( )*\\).*"
                + "else.*throw( )+new( )+IllegalArgumentException.*";
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
        String promptOutput = "Choose one of the following options:\\R"+
                "A:( )*Count 1s(\\t)+G:( )*Three of a kind\\R"+
                "B:( )*Count 2s(\\t)+H:( )*Four of a kind\\R"+
                "C:( )*Count 3s(\\t)+I:( )*Full house\\R"+
                "D:( )*Count 4s(\\t)+J:( )*Small straight\\R"+
                "E:( )*Count 5s(\\t)+K:( )*Large straight\\R"+
                "F:( )*Count 6s(\\t)+L:( )*Yahtzee\\R"+
                "(\\t)+M:( )*Chance";
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
        methodReqs.add(checkMethodReturnType(yObject2,"reroll",
                new Class[]{String.class,String.class},
                new Object[]{"123","1 2 3 4 5"},
                String.class,
                "4 5 4 3 6",5));
        methodReqs.add(checkMethodReturnType(yObject2,"reroll",
                new Class[]{String.class,String.class},
                new Object[]{"1  2  3","1 2 3 4 5"},
                String.class,
                "4 5 1 6 3",5));
        methodReqs.add(checkMethodReturnType(yObject2,"reroll",
                new Class[]{String.class,String.class},
                new Object[]{"2 3 4","5 4 3 2 1"},
                String.class,
                "5 1 6 3 2",5));
        methodReqs.add(checkMethodReturnType(yObject2,"reroll",
                new Class[]{String.class,String.class},
                new Object[]{"2 3 4 15","5 4 3 2 1"},
                String.class,
                "4 6 6 6 6",5));
        methodReqs.add(checkMethodReturnType(yObject2,"reroll",
                new Class[]{String.class,String.class},
                new Object[]{"3","3 4 3 2 1"},
                String.class,
                "4 3 2 1 4",5));
        methodReqs.add(checkMethodReturnType(eObject,"checkCount",
                new Class[]{String.class,int.class},
                new Object[]{"1 2 3 4 5",5},
                int.class,
                0,5));
        methodReqs.add(checkMethodReturnType(eObject,"checkThreeOfAKind",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                1,4));
        methodReqs.add(checkMethodReturnType(eObject,"checkFourOfAKind",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                2,4));
        methodReqs.add(checkMethodReturnType(eObject,"checkFullHouse",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                3,4));
        methodReqs.add(checkMethodReturnType(eObject,"checkSmallStraight",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                4,4));
        methodReqs.add(checkMethodReturnType(eObject,"checkLargeStraight",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                5,4));
        methodReqs.add(checkMethodReturnType(eObject,"checkYahtzee",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                6,4));
        methodReqs.add(checkMethodReturnType(eObject,"checkChance",
                new Class[]{String.class},
                new Object[]{"1 2 3 4 5"},
                int.class,
                7,4));
        return methodReqs;
    }
}
