/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stevedev.cs1grader;

import java.util.ArrayList;

/**
 *
 * @author steve
 */
public class Lab1 extends Assignment
{
    /*
    public Lab1(){
        super("Lab",1);
    }*/
    private static final String MAIN_CLASS = "PersonalInfoPrinter";
    private static Object testObj;

    public static void main(String[] args)
    {
        Lab1 lab = new Lab1();
        ArrayList<Requirement> requirements = new ArrayList<Requirement>();
        requirements = checkInitialConditions(MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 &&requirements.get(1).getReceived()>0)
        {
                requirements.add(lab.checkPrintUsername());
                requirements.add(lab.checkPrintAge());
                requirements.add(lab.checkPrintFavQuotes());
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);

    }

    public Lab1()
    {
            super("Lab",1);
            testObj = getInstance(MAIN_CLASS,null,null);
    }

    public Requirement checkPrintUsername(){
            return assertSystemOutput(testObj,"printUsername",null,getUsername(),10);
    }

    public Requirement checkPrintAge(){
            return assertSystemOutputRegex(testObj,"printAge",null,"^\\d\\d$",10);
    }

    public Requirement checkPrintFavQuotes(){
            return assertSystemOutputRegex(testObj,"printFavQuotes",null,"^(\".+\"\t:\t.+\\R){2,}$",10);
    }
}
