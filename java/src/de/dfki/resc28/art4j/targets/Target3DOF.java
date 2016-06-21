/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j.targets;

public class Target3DOF 
{
	public Target3DOF(final int id, final double quality, final double[] location)
	{
		this.id = id;
		this.quality = quality;
		this.location = location;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public double getQuality()
	{
		return this.quality;
	}

	public double[] getLocation()
	{
		return this.location;
	}
	
	private int id;
	private double quality;
	private double[] location;
}
