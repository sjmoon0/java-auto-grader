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
