/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j.targets;

import de.dfki.resc28.art4j.targets.Target3DOF;

public class Marker extends Target3DOF
{
	public Marker(final int id, final double quality, final double[] location)
	{
		super(id, quality, location);
	}
}
