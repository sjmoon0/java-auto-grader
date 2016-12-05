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
 * Requirements specific to Lab 10
 * @author steve
 */
public class Lab10 extends Assignment{
    private static final String PACKAGE_NAME = "parkinglot";
    private static final String MAIN_CLASS = "Tester";
    private static ParsedClass plClass;
    private static ParsedClass teClass;
    private static Object plObject;
    private static Object plObjectwParams;
    private static Object teObject;
    
    public static void main(String[] args){
        Lab10 lab = new Lab10();
        ArrayList<Requirement> requirements = checkInitialConditions(PACKAGE_NAME+"."+MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 && requirements.get(1).getReceived()>0){
            requirements.addAll(lab.checkFields());
            requirements.addAll(lab.checkConstructorSyntax());
            requirements.addAll(lab.checkMethodSyntax());
            //requirements.addAll(lab.checkMethodReturns());
            requirements.addAll(lab.checkMethodOutput());
        }
        grade(requirements, args.length>0?Boolean.parseBoolean(args[0]):false);
        
    }
    
    public Lab10(){
        super("Lab",10,true);
        teObject = getInstance(PACKAGE_NAME+"."+MAIN_CLASS,null,null);
        Class<?>[] plArgTypes = {int.class};
        Object[] plObs= {10};
        plObjectwParams = getInstance(PACKAGE_NAME+".ParkingLot",plArgTypes,plObs);
        plObject = getInstance(PACKAGE_NAME+".ParkingLot",null,null);
        teClass = JavaFileParser.parse(PACKAGE_NAME+"/"+MAIN_CLASS);
        plClass = JavaFileParser.parse(PACKAGE_NAME+"/ParkingLot");
        if(teClass==null || plClass==null){
            System.err.println("Classes not parsed correctly");
            System.exit(0);
        }
    }
    
    public ArrayList<Requirement> checkFields(){
        ArrayList<Requirement> reqs = new ArrayList();
        reqs.add(checkFieldExists(plClass, "lot",
                "^private( )+ArrayList\\<Vehicle\\>( )+lot( )*;$",5));
        reqs.add(checkFieldExists(plClass, "numSpots",
                "^private( )+int( )+numSpots( )*;$",5));
        return reqs;
    }
    
    public ArrayList<Requirement> checkConstructorSyntax(){
        ArrayList<Requirement> constructorReqs=new ArrayList();
        constructorReqs.add(checkConstructorBodyContains(plClass,"ParkingLot",
                new String[]{},
                "no parameters and properly initializes variables",
                //"^.*lot( )*\\=( )*new( )+ArrayList\\<Vehicle\\>.*numSpots( )*\\=( )*5.*$",
                "^.*(lot( )*\\=( )*new( )+ArrayList\\<Vehicle\\>.*numSpots( )*\\=( )*5|numSpots( )*\\=( )*5.*lot( )*.*lot( )*\\=( )*new( )+ArrayList\\<Vehicle\\>).*$",
                5));
        constructorReqs.add(checkConstructorBodyContains(plClass,"ParkingLot",
                new String[]{"int"},
                "int parameter and properly initializes variables",
                "^.*if.*length( )*\\<( )*2.*(lot( )*\\=( )*null( )*;.*numSpots( )*\\=( )*0|numSpots( )*\\=( )*0.*lot( )*;\\=( )*null( )*).*"
                + "else.*(lot( )*\\=( )*new( )+ArrayList<Vehicle>.*;.*numSpots( )*\\=( )*length|numSpots( )*\\=( )*length.*lot( )*;\\=( )*new( )+ArrayList<Vehicle>.*)",
                5));
        return constructorReqs;
    }
    
    public ArrayList<Requirement> checkMethodSyntax(){
        ArrayList<Requirement> methodReqs=new ArrayList();
        methodReqs.add(checkMethodBodyContains(plClass, "fillLot",
                new String[]{},
                "ArrayList method calls",
                "^.*lot\\.add\\(( )*first( )*\\).*for.*(.*lot\\.add.*){2}lot\\.add\\(( )*last()*\\).*$",
                6));
        methodReqs.add(checkMethodBodyContains(plClass, "removeRedCars",
                new String[]{},
                "ArrayList method calls",
                "^.*for.*lot\\.size.*if.*(get\\(.*\\).*\"RED\".*&&.*get.*\\(.*\\).*instanceof( )+Car|get\\(.*\\).*instanceof( )+Car.*&&.*get\\(.*\\)\"RED\").*lot\\.remove"
                        + ".*$",
                6));
        methodReqs.add(checkMethodBodyContains(plClass, "toString",
                new String[]{},
                "Iterator on ArrayList",
                "^.*Iterator.*iterator.*hasNext.*next.*$",
                6));
        return methodReqs;
    }
    
    public ArrayList<Requirement> checkMethodReturns(){
        ArrayList<Requirement> methodReqs = new ArrayList();
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
                            + "(?!null)(\\|\\-.*)"
                            + "\\| 2WD 2009 RED GMC Canyon with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2013 GREEN Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*"
                            + "\\| 2WD 2014 BLACK Toyota Camry with \\d+\\.\\d\\d+ miles.*"
                            + "\\|\\-.*";
        methodReqs.add(assertSystemOutputRegex(teObject,
                "main", new Class[]{String[].class}, new Object[]{new String[]{}},
                mainOutput, 20));
        return methodReqs;
    }
}
