/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stevedev.cs1grader;

//import static com.stevedev.cs1grader.Assignment.checkInitialConditions;
//import static com.stevedev.cs1grader.Assignment.getInstance;
//import static com.stevedev.cs1grader.Assignment.getUsername;
//import static com.stevedev.cs1grader.Assignment.grade;
import java.util.ArrayList;

/**
 *
 * @author steve
 */
public class Lab2 extends Assignment
{
    private static final String MAIN_CLASS = "BananaStand";
    private static ParsedClass parsedClass;
    private static Object testObj;
    
    public static void main(String[] args)
    {
        parsedClass = JavaFileParser.parse(MAIN_CLASS);
        if(parsedClass==null){
            System.err.println("Class was not parsed correctly");
            System.exit(0);
        }
        System.out.println(parsedClass.getMethods().get(0).getName());
        Lab2 lab = new Lab2();
        ArrayList<Requirement> requirements = new ArrayList<Requirement>();
        requirements = checkInitialConditions(MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 &&requirements.get(1).getReceived()>0)
        {
                //requirements.add(lab.);
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);

    }

    public Lab2()
    {
            super("Lab",2);
            testObj = getInstance(MAIN_CLASS,null,null);
    }
}
