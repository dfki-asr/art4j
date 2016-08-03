/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.art4j;

import java.net.InetAddress;

import de.dfki.resc28.art4j.targets.Marker;
import de.dfki.resc28.art4j.targets.Body;
import de.dfki.resc28.art4j.nar.NarSystem;

public class DTrackSDK
{

	public static final int SYS_DTRACK_UNKNOWN = 0; // unknown system
        public static final int SYS_DTRACK = 1; // DTrack system
        public static final int SYS_DTRACK_2 = 2; // DTrack2 system

	public static final int ERR_NONE = 0; // no error
	public static final int ERR_TIMEOUT = 1; //!< timeout occured
	public static final int ERR_NET = 2; //!< network error
	public static final int ERR_PARSE = 3; //!< error while parsing command

        public static String errorToString(int error) {
            switch (error) {
                case DTrackSDK.ERR_NONE: return "no error";
                case DTrackSDK.ERR_TIMEOUT: return "timeout occured";
                case DTrackSDK.ERR_NET: return "network error";
                case DTrackSDK.ERR_PARSE: return "error while parsing command";
                default: return "unknown error code: " + error;
            }
        }

        public static String systemTypeToString(int sysType) {
            switch (sysType) {
                case DTrackSDK.SYS_DTRACK_UNKNOWN: return "unknown system";
                case DTrackSDK.SYS_DTRACK: return "DTrack system";
                case DTrackSDK.SYS_DTRACK_2: return "DTrack2 system";
                default: return "unknown system type: " + sysType;
            }
        }

	public static synchronized DTrackSDK getInstance()
	{
		if (inst == null)
		{
			inst = new DTrackSDK();
		}
		
		return inst;
	}

	public static synchronized DTrackSDK getInstance(String serverHost, int serverPort, int dataPort)
	{
		if (inst == null)
		{
			inst = new DTrackSDK(serverHost, serverPort, dataPort);
		}
		
		return inst;
	}
	
	//================================================================================
    // Public Methods
    //================================================================================

        public native int getLastDataError();
        public native int getLastServerError();
        public native int getLastDTrackError();

        public native String getLastDTrackErrorDescription();


	public native int getDataPort();
	public native boolean isLocalDataPortValid();
	public native boolean isUDPValid();
	public native boolean isCommandInterfaceValid();
	public native boolean isTCPValid();

        public native boolean sendCommand(final String command);

        public native int getRemoteSystemType();

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
	
	private DTrackSDK(String serverHost, int serverPort, int dataPort) { initialise(serverHost, serverPort, dataPort); }
	private native void initialise(String serverHost, int serverPort, int dataPort);
	
	//================================================================================
    // Private Members
    //================================================================================
	
	private long nativeHandle;
	private static DTrackSDK inst;
	
	
	static { NarSystem.loadLibrary(); }
//	static { System.loadLibrary("art4j-0.1"); }
}
