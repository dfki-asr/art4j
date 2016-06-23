/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j;

import de.dfki.resc28.art4j.targets.Marker;
import de.dfki.resc28.art4j.targets.Body;
import de.dfki.resc28.art4j.nar.NarSystem;

public class DTrackSDK
{
	public static synchronized DTrackSDK getInstance()
	{
		if (inst == null)
		{
			inst = new DTrackSDK();
		}
		
		return inst;
	}
	
	
	//================================================================================
    // Public Methods
    //================================================================================

	public native int getDataPort();
	public native boolean isLocalDataPortValid();
	public native boolean isUDPValid();
	public native boolean isCommandInterfaceValid();
	public native boolean isTCPValid();
	
	
	public native boolean receive();
	public native boolean startMeasurement();
	public native boolean stopMeasurement();
	
	public native int getFrameCounter();
	public native double getTimeStamp();

	public native int getNumMarker();
	public native Marker getMarker(int id);
	
	public native int getNumBody();
	public native Body getBody(int id); 
	
//	public native int getNumFlyStick();
//	getFlyStick( );
//	
//	getNumMeaTool()
//	getMeaTool( )
//		
//	getNumMeaRef()
//	getMeaRef( )
//	
//	getNumHand()
//	getHand( )
//	
//	getNumHuman()
//	getHuman( )
//	
//	getNumInertial()
//	getInertial( )

	//================================================================================
    // Private Methods
    //================================================================================

	private DTrackSDK() { initialise(); }
	private native void initialise();
	
	//================================================================================
    // Private Members
    //================================================================================
	
	private long nativeHandle;
	private static DTrackSDK inst;
	
	
	static { NarSystem.loadLibrary(); }
//	static { System.loadLibrary("art4j-0.1"); }
}
