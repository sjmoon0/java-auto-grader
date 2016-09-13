/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stevedev.cs1grader;

/**
* Basic structure to hold data regarding the grading of
* a single requirement. Requirements include: compilibility,
* directory naming, class naming, zip compression, method
* returning, System input/output, file input/output
* This holds the points received, points possible, and
* an explanation of the grade received
*
* @author Mr. Steven Moon
* @version 2016.08.09
*
*/
class Requirement
{
	private String name;
	private int possible;
	private int received;
	private String description;

	public Requirement(String n, int p, int r, String d)
	{
		name=n;
		possible=p;
		received=r;
		description=d;
	}
	
	public String getName(){
		return name;
	}
	
	public int getReceived(){
		return received;
	}
	
	public int getPossible(){
		return possible;
	}
	
	public String getDescription(){
		return description;
	}
}
