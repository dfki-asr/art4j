/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j.targets;



public class Body extends Target6DOF
{
	public Body(final int id, final double quality, final double[] location, final double[] rotation)
	{
		super(id, quality, location, rotation);
	}
}