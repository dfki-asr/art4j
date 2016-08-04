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

        public static class CommandResult {
            public final int result;
            public final String answer;

            public CommandResult(final int result, final String answer) {
                this.result = result;
                this.answer = answer;
            }

            public boolean isOK() {
                return this.result == 1;
            }

            public boolean isError() {
                return this.result < 0 || this.result == 2;
            }

            public boolean isDTrackError() {
                return this.result == 2;
            }

            public String toString() {
                StringBuilder b = new StringBuilder("<Command Result ");
                b.append("code ").append(this.result).append(" (");
                switch (this.result) {
                    case 0: b.append("specific answer"); break;
                    case 1: b.append("dtrack2 ok"); break;
                    case 2: b.append("dtrack2 error, use getLastDTrackError"); break;
                    case -1: b.append("receive timeout"); break;
                    case -2: b.append("wrong system type"); break;
                    case -3: b.append("command too long"); break;
                    case -9: b.append("brocken tcp connection"); break;
                    case -10: b.append("tcp connection invalid"); break;
                    case -11: b.append("send command failed"); break;
                    default:
                        if (this.result < 0)
                            b.append("unknown error code");
                        else
                            b.append("unknown result code");
                }
                b.append("), answer \"").append(this.answer).append("\" >");
                return b.toString();
            }
        }

        public native CommandResult sendDTrack2Command(final String command);

        public native int getRemoteSystemType();

        /**
	 * 	\brief	Set DTrack2 parameter.
	 *
	 *	@param 	category  parameter category
	 *	@param 	name      parameter name
	 *	@param 	value     parameter value
	 *	@return	success?  (if not, a DTrack error message is available)
	 */
        public native boolean setParam(final String category, final String name, final String value);

        /**
	 * 	\brief	Set DTrack2 parameter.
	 *
	 * 	@param  parameter   complete parameter string without starting "dtrack set "
	 *	@return success?    (if not, a DTrack error message is available)
	 */
        public native boolean setParam(final String parameter);

        /**
	 * 	\brief	Get DTrack2 parameter.
	 *
	 *	@param 	category  parameter category
	 *	@param 	name      parameter name
	 *	@return	value?    (if null, a DTrack error message is available)
	 */
	public native String getParam(final String category, final String name);

        /**
	 * 	\brief	Get DTrack2 parameter.
	 *
	 *	@param  parameter complete parameter string without starting "dtrack get "
	 *	@return	value?    (if null, a DTrack error message is available)
	 */
        public native String getParam(final String parameter);

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

        /**
         * Find first inactive channel
         * @return channel number or -1 on error
         */
        public int findInactiveChannel(final int numChannels) {
            return findInactiveChannel(1, numChannels);
        }

        /**
         * Find first inactive channel starting with startChannel.
         * @param startChannel
         * @return positive inactive channel number, 0 if no channel found and -1 on error.
         */
        public int findInactiveChannel(final int startChannel, final int numChannels) {
            int currentChannel = startChannel;
            boolean channelFound = false;
            while (true) {
                if (numChannels > 0 && startChannel >= numChannels)
                    return 0;
                String chName = String.format("ch%02d", currentChannel);
                String value = getParam("output net_active", chName);
                if (value == null)
                    return -1;
                if ("no".equals(value))
                    return currentChannel;
                currentChannel += 1;
            }
        }

        public boolean configureUDPChannel(int channel, String host, int port) {
            return setParam("output net", String.format("ch%02d", channel), "udp "+host+" "+port);
        }

        public boolean configureMulticastChannel(int channel, String host, int port) {
            return setParam("output net", String.format("ch%02d", channel), "multicast "+host+" "+port);
        }

        public boolean activateChannel(int channel, String outputType) {
            return setParam("output active", String.format("ch%02d", channel), outputType+" yes");
        }

        public boolean deactivateChannel(int channel, String outputType) {
            return setParam("output active", String.format("ch%02d", channel), outputType+" no");
        }

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
