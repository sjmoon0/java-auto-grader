/*
 * Copyright (C) 2017 steve
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
public class Lab0 extends Assignment{
    private static final String MAIN_CLASS = "HelloWorld";
    private static Object testObj;

    public static void main(String[] args)
    {
        Lab0 lab = new Lab0();
        ArrayList<Requirement> requirements = new ArrayList<Requirement>();
        requirements = checkInitialConditions(MAIN_CLASS);
        if(requirements.get(0).getReceived()>0 &&requirements.get(1).getReceived()>0)
        {
            requirements.add(lab.checkHelloWorld());
        }
        grade(requirements,args.length>0?Boolean.parseBoolean(args[0]):false);
    }

    public Lab0()
    {
        super("Lab",0,false);
        testObj = getInstance(MAIN_CLASS,null,null);
    }

    public Requirement checkHelloWorld(){
        return assertSystemOutputRegex(testObj,"main",new Class[] {String[].class},new Object[] {new String[]{""}},"Hello World by "+getUsername(),10);
    }
}
