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
import static com.stevedev.cs1grader.Assignment.getInstance;
import static com.stevedev.cs1grader.Assignment.grade;
import java.util.ArrayList;

/**
 * Requirements specific to Lab 8
 * @author steve
 */
public class Lab8 extends Assignment{
    private static final String PACKAGE_NAME = "parkinglot";
    private static final String MAIN_CLASS = "Tester";
    private static ParsedClass cClass;
    private static ParsedClass cfClass;
    private static ParsedClass plClass;
    private static ParsedClass tClass;
    private static Object cObject;
    private static Object cObjectwParams;
    private static Object cfObject;
    private static Object plObject;
    private static Object plObjectwParams;
    private static Object tObject;
    //private static boolean usingNetbeans = true;
    
    public static void main(String[] args){
        Lab8 lab = new Lab8();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 && requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkMethodSyntax());
            requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements, args.length>0?Boolean.parseBoolean(args[0]):false);
        
    }
    
    public Lab8(){
        super("Lab",8,true);
        tObject = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        Class<?>[] cArgTypes = {String.class,String.class,String.class,double.class,int.class};
        Object[] cObs= {"BROWN","Subaru","Forrester",12345.67,2015};
        cObjectwParams = getInstance(PACKAGE_NAME+".Car",cArgTypes,cObs);
        cObject = getInstance(PACKAGE_NAME+".Car",null,null);
        cfObject = getInstance(PACKAGE_NAME+".CarFactory",null,null);
        Class<?>[] plArgTypes = {int.class};
        Object[] plObs= {10};
        plObjectwParams = getInstance(PACKAGE_NAME+".ParkingLot",plArgTypes,plObs);
        plObject = getInstance(PACKAGE_NAME+".ParkingLot",null,null);
        tClass = JavaFileParser.parse(PACKAGE_NAME+"/"+MAIN_CLASS);
        cClass = JavaFileParser.parse(PACKAGE_NAME+"/Car");
        cfClass = JavaFileParser.parse(PACKAGE_NAME+"/CarFactory");
        plClass = JavaFileParser.parse(PACKAGE_NAME+"/ParkingLot");
        if(tClass==null || cClass==null || cfClass==null || plClass==null){
            System.err.println("Classes not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(cClass, "color",
                "^private( )+String( )+color( )*;$",2));
        reqs.add(checkFieldExists(cClass, "make",
                "^private( )+String( )+make( )*;$",2));
        reqs.add(checkFieldExists(cClass, "model",
                "^private( )+String( )+model( )*;$",2));
        reqs.add(checkFieldExists(cClass, "odometer",
                "^private( )+double( )+odometer( )*;$",2));
        reqs.add(checkFieldExists(cClass, "year",
                "^private( )+int( )+year( )*;$",3));
        return reqs;
    }
    
    public ArrayList<Requirement> checkConstructorSyntax(){
        ArrayList<Requirement> constructorReqs=new ArrayList();
        constructorReqs.add(checkConstructorComments(cClass, "Car",
                new String[]{},
                "^/\\*\\*.*(N|n)o (P|p)arameter (C|c)onstructor.*\\*/$",
                1));
        constructorReqs.add(checkConstructorComments(cClass, "Car",
                new String[]{"String","String","String","double","int"},
                "^/\\*\\*.*(C|c)onstructor.*@param color.*@param make.*@param model.*@param odometer.*@param year.*\\*/$",
                1));
        return constructorReqs;
    }
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(cClass, "getMake",
                new String[]{},
                "^/\\*\\*.*@return.*(M|m)ake.*\\*/$",
                "^public( )+String( )+getMake( )*\\(( )*\\).*$",
                "^.*return( )+make.*$",
                2));
        methodReqs.addAll(checkMethodCorrectness(cClass, "getModel",
                new String[]{},
                "^/\\*\\*.*@return.*(M|m)odel.*\\*/$",
                "^public( )+String( )+getModel( )*\\(( )*\\).*$",
                "^.*return( )+model.*$",
                2));
        methodReqs.addAll(checkMethodCorrectness(cClass, "getColor",
                new String[]{},
                "^/\\*\\*.*@return.*(C|c)olor.*\\*/$",
                "^public( )+String( )+getColor( )*\\(( )*\\).*$",
                "^.*return( )+color.*$",
                2));
        methodReqs.addAll(checkMethodCorrectness(cClass, "getYear",
                new String[]{},
                "^/\\*\\*.*@return.*(Y|y)ear.*\\*/$",
                "^public( )+String( )+getYear( )*\\(( )*\\).*$",
                "^.*return( )+year.*$",
                2));
        methodReqs.addAll(checkMethodCorrectness(cClass, "getOdometer",
                new String[]{},
                "^/\\*\\*.*@return.*(O|o)dometer.*\\*/$",
                "^public( )+String( )+getOdometer( )*\\(( )*\\).*$",
                "^.*return( )+odometer.*$",
                2));
        methodReqs.addAll(checkMethodCorrectness(cClass, "repaint",
                new String[]{"String"},
                "^/\\*\\*.*@param.*(C|c)olor.*\\*/$",
                "^public( )+void( )+repaint( )*\\(( )*String( )*.*\\).*$",
                "^.*color( )*\\=.*$",
                2));
        methodReqs.addAll(checkMethodCorrectness(cClass, "resetMileage",
                new String[]{},
                "^/\\*\\*.*(S|s)et( )+(O|odometer).*\\*/$",
                "^public( )+void( )+repaint( )*\\(( )*String( )*.*\\).*$",
                "^.*color( )*\\=.*$",
                2));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        methodReqs.add(checkMethodReturnValue(cObject,"getMake",
                null,
                null,
                String.class,
                "Toyota",2));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        
        String mainOutput = "^.*default car: 2000 BLACK Toyota Camry with 0.0 miles.*"
                            + "first car: \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with \\d+\\.\\d+ miles.*"
                            + "(U|u)pdated car: \\d\\d\\d\\d WHITE [a-zA-Z]+ [a-zA-Z]+ with 0.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2000 BLACK Toyota Camry with 0.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2013 GREEN Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2010 RED Toyota Corolla with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2002 RED Chevy Malibu with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2014 BLACK Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2009 BLUE Chevy Malibu with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2007 RED Chevy Corvette with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2011 BLACK Ford Focus with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2008 GREEN Chevy Malibu with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            //Reset odometers
                            + "\\|\\-.*"
                            + "\\| 2000 BLACK Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2013 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2010 RED Toyota Corolla with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2002 RED Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2014 BLACK Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2009 BLUE Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2007 RED Chevy Corvette with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2011 BLACK Ford Focus with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2008 GREEN Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            // Paint Toyotas green
                            + "\\|\\-.*"
                            + "\\| 2000 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2013 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2010 GREEN Toyota Corolla with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2002 RED Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2014 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2009 BLUE Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2007 RED Chevy Corvette with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2011 BLACK Ford Focus with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2008 GREEN Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            //Remove red cars
                            + "\\|\\-.*"
                            + "\\| 2000 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2013 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2010 GREEN Toyota Corolla with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| null.*"
                            + "\\|\\-.*"
                            + "\\| 2014 GREEN Toyota Camry with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2009 BLUE Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| null.*"
                            + "\\|\\-.*"
                            + "\\| 2011 BLACK Ford Focus with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| 2008 GREEN Chevy Malibu with 0\\.0 miles.*"
                            + "\\|\\-.*"
                            + "\\| \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with 0\\.0 miles.*"
                            + "\\|\\-.*$";
        methodReqs.add(assertSystemOutputRegex(tObject,
                "main", new Class[]{String[].class}, new Object[]{new String[]{}},
                mainOutput, 10));
        return methodReqs;
    }
}
