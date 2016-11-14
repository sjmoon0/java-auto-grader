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

import static com.stevedev.cs1grader.Assignment.checkInitialConditions;
import static com.stevedev.cs1grader.Assignment.grade;
import java.util.ArrayList;

/**
 * Requirements specific to Lab 7
 * @author steve
 */
public class Lab7 extends Assignment{
    private static final String PACKAGE_NAME = "yahtzee";
    private static final String MAIN_CLASS = "Yahtzee";
    private static ParsedClass yClass;
    private static ParsedClass dClass;
    private static ParsedClass eClass;
    private static Object yObject;
    private static Object dObject;
    private static Object eObject;
    
    public static void main(String[] args){
        Lab7 lab = new Lab7();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 && requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodSyntax());
            requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements, args.length>0?Boolean.parseBoolean(args[0]):false);
        
    }
    
    public Lab7(){
        super("Lab",7,true);
        yObject = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
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
        reqs.add(checkFieldExists(yClass, "choicesUsed",
                "^private( )+static( )+boolean\\[\\]( )+choicesUsed( )*=( )*new( )+boolean( )*\\[( )*13( )*\\]( )*;$",3));
        return reqs;
    }
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.add(checkMethodComments(eClass, "checkCount",
                new String[]{"String","int"},
                "^/\\*\\*.*(C|c)ount.*times.*hand.*@param( )+hand.*@param( )+targetNum.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkThreeOfAKind",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(T|t)hree.*(K|k)ind.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkFourOfAKind",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(F|f)our.*(K|k)ind.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkFullHouse",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(F|f)ull.*(H|h)ouse.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkSmallStraight",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(S|s)mall.*(S|s)traight.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkLargeStraight",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(L|l)arge.*(S|s)traight.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkYahtzee",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(Y|y)ahtzee.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "checkChance",
                new String[]{"String"},
                "^/\\*\\*.*(C|c)heck.*(C|c)hance.*@param( )+hand.*@return.*(P|p)oints.*\\*/$",
                2));
        methodReqs.add(checkMethodComments(eClass, "diceTotal",
                new String[]{"int[]"},
                "^/\\*\\*.*@param( )+hand.*@return.*\\*/$",
                2));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        methodReqs.add(checkMethodReturnValue(eObject,"checkCount",
                new Class[]{String.class,int.class},
                new Object[]{"12345",1},
                int.class,
                1,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkCount",
                new Class[]{String.class,int.class},
                new Object[]{"15545",5},
                int.class,
                15,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkCount",
                new Class[]{String.class,int.class},
                new Object[]{"62264",1},
                int.class,
                0,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkThreeOfAKind",
                new Class[]{String.class},
                new Object[]{"54321"},
                int.class,
                0,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkThreeOfAKind",
                new Class[]{String.class},
                new Object[]{"54525"},
                int.class,
                21,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkThreeOfAKind",
                new Class[]{String.class},
                new Object[]{"22222"},
                int.class,
                10,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkFourOfAKind",
                new Class[]{String.class},
                new Object[]{"33333"},
                int.class,
                15,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkFourOfAKind",
                new Class[]{String.class},
                new Object[]{"33131"},
                int.class,
                0,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkFourOfAKind",
                new Class[]{String.class},
                new Object[]{"44144"},
                int.class,
                17,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkFullHouse",
                new Class[]{String.class},
                new Object[]{"44114"},
                int.class,
                25,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkFullHouse",
                new Class[]{String.class},
                new Object[]{"24114"},
                int.class,
                0,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkFullHouse",
                new Class[]{String.class},
                new Object[]{"11111"},
                int.class,
                0,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkSmallStraight",
                new Class[]{String.class},
                new Object[]{"34121"},
                int.class,
                30,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkSmallStraight",
                new Class[]{String.class},
                new Object[]{"33214"},
                int.class,
                30,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkSmallStraight",
                new Class[]{String.class},
                new Object[]{"12356"},
                int.class,
                0,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkLargeStraight",
                new Class[]{String.class},
                new Object[]{"33214"},
                int.class,
                0,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkLargeStraight",
                new Class[]{String.class},
                new Object[]{"53214"},
                int.class,
                40,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkLargeStraight",
                new Class[]{String.class},
                new Object[]{"56324"},
                int.class,
                40,4));
        methodReqs.add(checkMethodReturnValue(eObject,"checkYahtzee",
                new Class[]{String.class},
                new Object[]{"11112"},
                int.class,
                0,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkYahtzee",
                new Class[]{String.class},
                new Object[]{"33333"},
                int.class,
                50,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkChance",
                new Class[]{String.class},
                new Object[]{"33333"},
                int.class,
                15,3));
        methodReqs.add(checkMethodReturnValue(eObject,"checkChance",
                new Class[]{String.class},
                new Object[]{"63136"},
                int.class,
                19,3));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        String promptOutput = "Choose one of the following options:\\R"+
                "A:( )*Count 1s(\\t)+G:( )*Three of a kind\\R"+
                "B:( )*Count 2s(\\t)+H:( )*Four of a kind\\R"+
                "C:( )*Count 3s(\\t)+I:( )*Full house\\R"+
                "D:( )*Count 4s(\\t)+J:( )*Small straight\\R"+
                "E:( )*Count 5s(\\t)+K:( )*Large straight\\R"+
                "F:( )*Count 6s(\\t)+L:( )*Yahtzee\\R"+
                "(\\t)+M:( )*Chance";
        String mainOutput = "^.*(is( )+not( )+an( )+option.*){12}(S|c)core.*63.*$";
        
        methodReqs.add(assertSystemOutputRegex(yObject,
                "main", new Class[]{String[].class}, new Object[]{new String[]{}},
                mainOutput, 10));
        return methodReqs;
    }
}
