/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j.targets;

import de.dfki.resc28.art4j.targets.Target3DOF;


public class Target6DOF extends Target3DOF 
{
	public Target6DOF(final int id, final double quality, final double[] location, final double[] rotation)
	{
		super(id, quality, location);
		this.rotation = rotation;
	}
	
	public Target6DOF() {
		
	}
	
	public double[] getRotation()
	{
		return this.rotation;
	}
	
	protected double[] rotation;
}
