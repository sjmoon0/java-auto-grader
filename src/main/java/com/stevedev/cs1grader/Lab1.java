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
            super("Lab",1,false);
            testObj = getInstance(MAIN_CLASS,null,null);
    }

    public Requirement checkPrintUsername(){
            return assertSystemOutput(testObj,"printUsername",null,getUsername(),10);
    }

    public Requirement checkPrintAge(){
            return assertSystemOutputRegex(testObj,"printAge",null, null,"^\\d\\d$",10);
    }

    public Requirement checkPrintFavQuotes(){
            return assertSystemOutputRegex(testObj,"printFavQuotes",null,null,"^(\".+\"\t:\t.+\\R){2,}$",10);
    }
}
