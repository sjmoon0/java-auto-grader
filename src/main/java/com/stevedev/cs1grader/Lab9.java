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
 * Requirements specific to Lab 9
 * @author steve
 */
public class Lab9 extends Assignment{
    private static final String PACKAGE_NAME = "parkinglot";
    private static final String MAIN_CLASS = "Tester";
    private static ParsedClass cClass;
    private static ParsedClass plClass;
    private static ParsedClass teClass;
    private static ParsedClass trClass;
    private static ParsedClass vClass;
    private static Object cObject;
    private static Object cObjectwParams;
   // private static Object plObject;
   // private static Object plObjectwParams;
    private static Object teObject;
    private static Object trObject;
    private static Object trObjectwParams;
    
    public static void main(String[] args){
        Lab9 lab = new Lab9();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 && requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkConstructorSyntax());
            requirements.addAll(lab.checkMethodSyntax());
            requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements, args.length>0?Boolean.parseBoolean(args[0]):false);
        
    }
    
    public Lab9(){
        super("Lab",9,true);
        teObject = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        Class<?>[] cArgTypes = {String.class,String.class,String.class,double.class,int.class,boolean.class,double.class};
        Object[] cObs= {"BROWN","Subaru","Forrester",12345.67,2015,true,6.5};
        cObjectwParams = getInstance(PACKAGE_NAME+".Car",cArgTypes,cObs);
        cObject = getInstance(PACKAGE_NAME+".Car",null,null);
        Class<?>[] trArgTypes = {String.class,String.class,String.class,double.class,int.class,double.class,boolean.class,int.class};
        Object[] trObs= {"PURPLE","Dodge","RAM",12345.67,2015,6.5,false,5};
        trObjectwParams = getInstance(PACKAGE_NAME+".Truck",trArgTypes,trObs);
        trObject = getInstance(PACKAGE_NAME+".Truck",null,null);
        Class<?>[] plArgTypes = {int.class};
        Object[] plObs= {10};
//        plObjectwParams = getInstance(PACKAGE_NAME+".ParkingLot",plArgTypes,plObs);
//        plObject = getInstance(PACKAGE_NAME+".ParkingLot",null,null);
        teClass = JavaFileParser.parse(PACKAGE_NAME+"/"+MAIN_CLASS);
        cClass = JavaFileParser.parse(PACKAGE_NAME+"/Car");
        trClass = JavaFileParser.parse(PACKAGE_NAME+"/Truck");
        plClass = JavaFileParser.parse(PACKAGE_NAME+"/ParkingLot");
        vClass = JavaFileParser.parse(PACKAGE_NAME+"/Vehicle");
        if(trClass==null || teClass==null || cClass==null || plClass==null || vClass==null){
            System.err.println("Classes not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(vClass, "color",
                "^protected( )+String( )+color( )*;$",2));
        reqs.add(checkFieldExists(vClass, "make",
                "^protected( )+String( )+make( )*;$",2));
        reqs.add(checkFieldExists(vClass, "model",
                "^protected( )+String( )+model( )*;$",2));
        reqs.add(checkFieldExists(vClass, "odometer",
                "^protected( )+double( )+odometer( )*;$",2));
        reqs.add(checkFieldExists(vClass, "year",
                "^protected( )+int( )+year( )*;$",2));
        reqs.add(checkFieldExists(cClass, "allWheelDrive",
                "^private( )+boolean( )+allWheelDrive( )*;$",2));
        reqs.add(checkFieldExists(cClass, "trunkSize",
                "^private( )+double( )+trunkSize( )*;$",2));
        reqs.add(checkFieldExists(trClass, "bedLength",
                "^private( )+double( )+bedLength( )*;$",2));
        reqs.add(checkFieldExists(trClass, "fourWheelDrive",
                "^private( )+boolean( )+fourWheelDrive( )*;$",2));
        reqs.add(checkFieldExists(trClass, "numTires",
                "^private( )+int( )+numTires( )*;$",2));
        reqs.add(checkFieldExists(plClass, "lot",
                "^private( )+Vehicle\\[\\]( )+lot( )*;$",2));
        return reqs;
    }
    
    public ArrayList<Requirement> checkConstructorSyntax(){
        ArrayList<Requirement> constructorReqs=new ArrayList();
        constructorReqs.add(checkConstructorComments(vClass, "Vehicle",
                new String[]{},
                "^/\\*\\*.*(N|n)o (P|p)arameter (C|c)onstructor.*\\*/$",
                1));
        constructorReqs.add(checkConstructorComments(vClass, "Vehicle",
                new String[]{"String","String","String","double","int"},
                "^/\\*\\*.*(C|c)onstructor.*@param color.*@param make.*@param model.*@param odometer.*@param year.*\\*/$",
                1));
        constructorReqs.add(checkConstructorComments(cClass, "Car",
                new String[]{},
                "^/\\*\\*.*(N|n)o (P|p)arameter (C|c)onstructor.*\\*/$",
                1));
        constructorReqs.add(checkConstructorComments(cClass, "Car",
                new String[]{"String","String","String","double","int","boolean","double"},
                "^/\\*\\*.*(C|c)onstructor.*@param color.*@param make.*@param model.*@param odometer.*@param year.*@param allWheelDrive.*@param trunkSize.*\\*/$",
                1));
        constructorReqs.add(checkConstructorComments(trClass, "Truck",
                new String[]{},
                "^/\\*\\*.*(N|n)o (P|p)arameter (C|c)onstructor.*\\*/$",
                1));
        constructorReqs.add(checkConstructorComments(trClass, "Truck",
                new String[]{"String","String","String","double","int","double","boolean","int"},
                "^/\\*\\*.*(C|c)onstructor.*@param color.*@param make.*@param model.*@param odometer.*@param year.*@param bedLength.*@param fourWheelDrive.*@param numTires.*\\*/$",
                1));
        return constructorReqs;
    }
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.addAll(checkMethodCorrectness(vClass, "getMake",
                new String[]{},
                "^/\\*\\*.*@return.*(M|m)ake.*\\*/$",
                "^public( )+String( )+getMake( )*\\(( )*\\).*$",
                "^.*return( )+make.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(vClass, "getModel",
                new String[]{},
                "^/\\*\\*.*@return.*(M|m)odel.*\\*/$",
                "^public( )+String( )+getModel( )*\\(( )*\\).*$",
                "^.*return( )+model.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(vClass, "getColor",
                new String[]{},
                "^/\\*\\*.*@return.*(C|c)olor.*\\*/$",
                "^public( )+String( )+getColor( )*\\(( )*\\).*$",
                "^.*return( )+color.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(vClass, "getYear",
                new String[]{},
                "^/\\*\\*.*@return.*(Y|y)ear.*\\*/$",
                "^public( )+int( )+getYear( )*\\(( )*\\).*$",
                "^.*return( )+year.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(vClass, "getOdometer",
                new String[]{},
                "^/\\*\\*.*@return.*((O|o)dometer|(M|m)ileage).*\\*/$",
                "^public( )+double( )+getOdometer( )*\\(( )*\\).*$",
                "^.*return( )+odometer.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(cClass, "isAWD",
                new String[]{},
                "^/\\*\\*.*@return.*((D|d)rive|AWD|awd).*\\*/$",
                "^public( )+boolean( )+isAWD( )*\\(( )*\\).*$",
                "^.*return( )+allWheelDrive.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(cClass, "isGoodInSnow",
                new String[]{},
                "^/\\*\\*.*@return.*((G|g)ood|(S|s)now).*\\*/$",
                "^public( )+boolean( )+isGoodInSnow( )*\\(( )*\\).*$",
                "^.*return( )+(isAWD|allWheelDrive).*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(cClass, "getTrunkSize",
                new String[]{},
                "^/\\*\\*.*@return.*(S|s)ize.*\\*/$",
                "^public( )+double( )+getTrunkSize( )*\\(( )*\\).*$",
                "^.*return( )+trunkSize.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(trClass, "is4WD",
                new String[]{},
                "^/\\*\\*.*@return.*((D|d)rive|4WD|4wd).*\\*/$",
                "^public( )+boolean( )+is4WD( )*\\(( )*\\).*$",
                "^.*return( )+fourWheelDrive.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(trClass, "isGoodInSnow",
                new String[]{},
                "^/\\*\\*.*@return.*((G|g)ood|(S|s)now).*\\*/$",
                "^public( )+boolean( )+isGoodInSnow( )*\\(( )*\\).*$",
                "^.*return( )+(is4WD|fourWheelDrive).*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(trClass, "getBedLength",
                new String[]{},
                "^/\\*\\*.*@return.*(L|l)ength.*\\*/$",
                "^public( )+double( )+getBedLength( )*\\(( )*\\).*$",
                "^.*return( )+bedLength.*$",
                1));
        methodReqs.addAll(checkMethodCorrectness(trClass, "getNumTires",
                new String[]{},
                "^/\\*\\*.*@return.*(T|t)ire.*\\*/$",
                "^public( )+int( )+getNumTires( )*\\(( )*\\).*$",
                "^.*return( )+numTires.*$",
                1));
        methodReqs.add(checkMethodBodyContains(plClass, "fillLot",
                new String[]{},
                "CarFactory method call inside a for loop",
                "^.*for( )*\\(int.*;.*;.*\\).*lot\\[.*\\]( )*\\=( )*CarFactory.generateCar\\(( )*\\).*$",
                3));
        methodReqs.add(checkMethodBodyContains(plClass, "fillLot",
                new String[]{},
                "TruckFactory method call inside a for loop",
                "^.*for( )*\\(int.*;.*;.*\\).*lot\\[.*\\]( )*\\=( )*TruckFactory.generateTruck\\(( )*\\).*$",
                3));
        methodReqs.add(checkMethodBodyContains(plClass, "paintToyotasGreen",
                new String[]{},
                "Updated foreach loop",
                "^.*for.*\\(( )*Vehicle.*:.*\\).*\\{.*repaint.*\\}.*$",
                1));
        methodReqs.add(checkMethodBodyContains(plClass, "resetAllOdometers",
                new String[]{},
                "Updated foreach loop",
                "^.*for.*\\(( )*Vehicle.*:.*\\).*\\{.*resetMileage.*\\}.*$",
                1));
        methodReqs.add(checkMethodBodyContains(plClass, "removeRedCars",
                new String[]{},
                "Check if color is \"RED\" AND object is instanceof Car",
                "^.*for( )*\\(int.*;.*;.*\\).*if.*(\"RED\".*&&.*instanceof( )+Car|instanceof( )+Car.*&&.*\"RED\").*$",
                3));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        
        //Check car objects
        methodReqs.add(checkMethodReturnValue(cObject,"isAWD",
                null,
                null,
                boolean.class,
                false,1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"isAWD",
                null,
                null,
                boolean.class,
                true,1));
        methodReqs.add(checkMethodReturnValue(cObject,"isGoodInSnow",
                null,
                null,
                boolean.class,
                false,1));
        methodReqs.add(checkMethodReturnValue(cObject,"compareTo",
                new Class[]{Object.class},
                new Object[]{cObject},
                int.class,
                0,3));
        methodReqs.add(checkMethodReturnValue(cObject,"equals",
                new Class[]{Object.class},
                new Object[]{cObject},
                boolean.class,
                true,3));
        methodReqs.add(checkMethodReturnValue(cObject,"equals",
                new Class[]{Object.class},
                new Object[]{cObjectwParams},
                boolean.class,
                false,3));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"isGoodInSnow",
                null,
                null,
                boolean.class,
                true,1));
        methodReqs.add(checkMethodReturnValue(cObject,"getMake",
                null,
                null,
                String.class,
                "Toyota",1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"getMake",
                null,
                null,
                String.class,
                "Subaru",1));
        methodReqs.add(checkMethodReturnValue(cObject,"getModel",
                null,
                null,
                String.class,
                "Camry",1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"getModel",
                null,
                null,
                String.class,
                "Forrester",1));
        methodReqs.add(checkMethodReturnValue(cObject,"getColor",
                null,
                null,
                String.class,
                "BLACK",1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"getColor",
                null,
                null,
                String.class,
                "BROWN",1));
        methodReqs.add(checkMethodReturnValue(cObject,"getOdometer",
                null,
                null,
                double.class,
                0.0,1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"getOdometer",
                null,
                null,
                double.class,
                12345.67,1));
        methodReqs.add(checkMethodReturnValue(cObject,"getYear",
                null,
                null,
                int.class,
                2000,1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"getYear",
                null,
                null,
                int.class,
                2015,1));
        methodReqs.add(checkMethodReturnValue(cObject,"getTrunkSize",
                null,
                null,
                double.class,
                5.0,1));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"getTrunkSize",
                null,
                null,
                double.class,
                6.5,1));
        methodReqs.add(checkMethodReturnValue(cObject,"toString",
                null,
                null,
                String.class,
                "2WD 2000 BLACK Toyota Camry with 0.0 miles",2));
        methodReqs.add(checkMethodReturnValue(cObjectwParams,"toString",
                null,
                null,
                String.class,
                "AWD 2015 BROWN Subaru Forrester with 12345.67 miles",2));
        
        //Check truck objects
        methodReqs.add(checkMethodReturnValue(trObject,"getMake",
                null,
                null,
                String.class,
                "Toyota",2));
        methodReqs.add(checkMethodReturnValue(trObjectwParams,"getMake",
                null,
                null,
                String.class,
                "Dodge",2));
        methodReqs.add(checkMethodReturnValue(trObject,"compareTo",
                new Class[]{Object.class},
                new Object[]{trObject},
                int.class,
                0,3));
        methodReqs.add(checkMethodReturnValue(trObject,"equals",
                new Class[]{Object.class},
                new Object[]{trObject},
                boolean.class,
                true,3));
        methodReqs.add(checkMethodReturnValue(trObject,"equals",
                new Class[]{Object.class},
                new Object[]{trObjectwParams},
                boolean.class,
                false,3));
        methodReqs.add(checkMethodReturnValue(trObject,"is4WD",
                null,
                null,
                boolean.class,
                true,1));
        methodReqs.add(checkMethodReturnValue(trObjectwParams,"is4WD",
                null,
                null,
                boolean.class,
                false,1));
        methodReqs.add(checkMethodReturnValue(trObject,"isGoodInSnow",
                null,
                null,
                boolean.class,
                true,1));
        methodReqs.add(checkMethodReturnValue(trObjectwParams,"isGoodInSnow",
                null,
                null,
                boolean.class,
                false,1));
        methodReqs.add(checkMethodReturnValue(trObject,"getBedLength",
                null,
                null,
                double.class,
                8.0,1));
        methodReqs.add(checkMethodReturnValue(trObjectwParams,"getBedLength",
                null,
                null,
                double.class,
                6.5,1));
        methodReqs.add(checkMethodReturnValue(trObject,"toString",
                null,
                null,
                String.class,
                "4WD 2000 BLACK Toyota Tacoma with 0.0 miles",2));
        methodReqs.add(checkMethodReturnValue(trObjectwParams,"toString",
                null,
                null,
                String.class,
                "2WD 2015 PURPLE Dodge RAM with 12345.67 miles",2));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodOutput(){
        ArrayList<Requirement> methodReqs = new ArrayList();
        
        String mainOutput = "^.*(M|m)y (C|c)ar: (2|A)WD \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with \\d+\\.\\d+ miles.*"
                            + "(M|m)y (T|t)ruck: 4WD 1900 RED Ford F250 with 100000.0 miles.*"
                            + "(I|i)s (M|m)y (T|t)ruck (G|g)ood (I|i)n (S|s)now\\? true.*"
                            + "\\|\\-.*"
                            + "\\| (2|A)WD \\d\\d\\d\\d [a-zA-Z]+ [a-zA-Z]+ [a-zA-Z]+ with \\d+\\.\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2002 GREEN GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2013 GREEN Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2009 RED GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2008 RED Ford Focus with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 4WD 2003 BLACK Ford F350 with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2014 BLACK Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2004 RED Ford F150 with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| AWD 2003 GREEN Chevy Malibu with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 4WD 1900 RED Ford F250 with \\d+\\.\\d+ miles.*"
                            + "\\|\\-.*"
                
                
                            + "\\|\\-.*"
                            + "\\| 4WD 1900 RED Ford F250 with \\d+\\.\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2002 GREEN GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 4WD 2003 BLACK Ford F350 with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| AWD 2003 GREEN Chevy Malibu with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2004 RED Ford F150 with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2008 RED Ford Focus with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2009 RED GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2013 GREEN Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2014 BLACK Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                
                            + "\\|\\-.*"
                            + "\\| 4WD 1900 RED Ford F250 with \\d+\\.\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2002 GREEN GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 4WD 2003 BLACK Ford F350 with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| AWD 2003 GREEN Chevy Malibu with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2004 RED Ford F150 with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| null.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2009 RED GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2013 GREEN Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2014 BLACK Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*";
        methodReqs.add(assertSystemOutputRegex(teObject,
                "main", new Class[]{String[].class}, new Object[]{new String[]{}},
                mainOutput, 10));
        return methodReqs;
    }
}
