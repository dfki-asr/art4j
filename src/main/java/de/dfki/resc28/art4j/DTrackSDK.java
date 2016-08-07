/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.art4j;

import de.dfki.resc28.art4j.targets.Marker;
import de.dfki.resc28.art4j.targets.Body;
import de.dfki.resc28.art4j.nar.NarSystem;

public class DTrackSDK {

    public static final int SYS_DTRACK_UNKNOWN = 0; // unknown system
    public static final int SYS_DTRACK = 1; // DTrack system
    public static final int SYS_DTRACK_2 = 2; // DTrack2 system

    public static final int ERR_NONE = 0; // no error
    public static final int ERR_TIMEOUT = 1; //!< timeout occured
    public static final int ERR_NET = 2; //!< network error
    public static final int ERR_PARSE = 3; //!< error while parsing command

    public static String errorToString(int error) {
        switch (error) {
            case DTrackSDK.ERR_NONE:
                return "no error";
            case DTrackSDK.ERR_TIMEOUT:
                return "timeout occured";
            case DTrackSDK.ERR_NET:
                return "network error";
            case DTrackSDK.ERR_PARSE:
                return "error while parsing command";
            default:
                return "unknown error code: " + error;
        }
    }

    public static String systemTypeToString(int sysType) {
        switch (sysType) {
            case DTrackSDK.SYS_DTRACK_UNKNOWN:
                return "unknown system";
            case DTrackSDK.SYS_DTRACK:
                return "DTrack system";
            case DTrackSDK.SYS_DTRACK_2:
                return "DTrack2 system";
            default:
                return "unknown system type: " + sysType;
        }
    }

    @Deprecated
    public static synchronized DTrackSDK getInstance() {
        if (inst == null) {
            inst = new DTrackSDK();
        }

        return inst;
    }

    @Deprecated
    public static synchronized DTrackSDK getInstance(String serverHost, int serverPort, int dataPort) {
        if (inst == null) {
            inst = new DTrackSDK(serverHost, serverPort, dataPort);
        }

        return inst;
    }

    public DTrackSDK() {
        nativeHandle = DTrackSDK_create(5000);
    }

    public DTrackSDK(int dataPort) {
        nativeHandle = DTrackSDK_create(dataPort);
    }

    public DTrackSDK(String serverHost, int serverPort, int dataPort) {
        nativeHandle = DTrackSDK_create(serverHost, serverPort, dataPort);
    }

    //================================================================================
    // Public Methods
    //================================================================================

    public synchronized final boolean isDestroyed() {
        return DTrackSDK_isDestroyed(nativeHandle);
    }

    public synchronized final int getLastDataError() {
        return DTrackSDK_getLastDataError(nativeHandle);
    }

    public synchronized final int getLastServerError() {
        return DTrackSDK_getLastServerError(nativeHandle);
    }

    public synchronized final int getLastDTrackError() {
        return DTrackSDK_getLastDTrackError(nativeHandle);
    }

    public synchronized final String getLastDTrackErrorDescription() {
        return DTrackSDK_getLastDTrackErrorDescription(nativeHandle);
    }

    public synchronized final int getDataPort() {
        return DTrackSDK_getDataPort(nativeHandle);
    }

    public synchronized final boolean isLocalDataPortValid() {
        return DTrackSDK_isLocalDataPortValid(nativeHandle);
    }

    public synchronized final boolean isUDPValid() {
        return DTrackSDK_isUDPValid(nativeHandle);
    }

    public synchronized final boolean isCommandInterfaceValid() {
        return DTrackSDK_isCommandInterfaceValid(nativeHandle);
    }

    public synchronized final boolean isTCPValid() {
        return DTrackSDK_isTCPValid(nativeHandle);
    }

    public synchronized final boolean sendCommand(final String command) {
        return DTrackSDK_sendCommand(nativeHandle, command);
    }

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
                case 0:
                    b.append("specific answer");
                    break;
                case 1:
                    b.append("dtrack2 ok");
                    break;
                case 2:
                    b.append("dtrack2 error, use getLastDTrackError");
                    break;
                case -1:
                    b.append("receive timeout");
                    break;
                case -2:
                    b.append("wrong system type");
                    break;
                case -3:
                    b.append("command too long");
                    break;
                case -9:
                    b.append("brocken tcp connection");
                    break;
                case -10:
                    b.append("tcp connection invalid");
                    break;
                case -11:
                    b.append("send command failed");
                    break;
                default:
                    if (this.result < 0) {
                        b.append("unknown error code");
                    } else {
                        b.append("unknown result code");
                    }
            }
            b.append("), answer \"").append(this.answer).append("\" >");
            return b.toString();
        }
    }

    public synchronized final CommandResult sendDTrack2Command(final String command) {
        return DTrackSDK_sendDTrack2Command(nativeHandle, command);
    }

    public synchronized final int getRemoteSystemType() {
        return DTrackSDK_getRemoteSystemType(nativeHandle);
    }

    /**
     * \brief	Set DTrack2 parameter.
     *
     * @param category parameter category
     * @param name parameter name
     * @param value parameter value
     * @return	success? (if not, a DTrack error message is available)
     */
    public synchronized final boolean setParam(final String category, final String name, final String value) {
        return DTrackSDK_setParam(nativeHandle, category, name, value);
    }

    /**
     * \brief	Set DTrack2 parameter.
     *
     * @param parameter complete parameter string without starting "dtrack set "
     * @return success? (if not, a DTrack error message is available)
     */
    public synchronized final boolean setParam(final String parameter) {
        return DTrackSDK_setParam(nativeHandle, parameter);
    }

    /**
     * \brief	Get DTrack2 parameter.
     *
     * @param category parameter category
     * @param name parameter name
     * @return	value? (if null, a DTrack error message is available)
     */
    public synchronized final String getParam(final String category, final String name) {
        return DTrackSDK_getParam(nativeHandle, category, name);
    }

    /**
     * \brief	Get DTrack2 parameter.
     *
     * @param parameter complete parameter string without starting "dtrack get "
     * @return	value? (if null, a DTrack error message is available)
     */
    public synchronized final String getParam(final String parameter) {
        return DTrackSDK_getParam(nativeHandle, parameter);
    }

    public synchronized boolean receive() {
        return DTrackSDK_receive(nativeHandle);
    }

    public synchronized boolean startMeasurement() {
        return DTrackSDK_startMeasurement(nativeHandle);
    }

    public synchronized boolean stopMeasurement() {
        return DTrackSDK_stopMeasurement(nativeHandle);
    }

    public synchronized int getFrameCounter() {
        return DTrackSDK_getFrameCounter(nativeHandle);
    }

    public synchronized double getTimeStamp() {
        return DTrackSDK_getTimeStamp(nativeHandle);
    }

    public synchronized int getNumMarker() {
        return DTrackSDK_getNumMarker(nativeHandle);
    }

    public synchronized Marker getMarker(int id) {
        return DTrackSDK_getMarker(nativeHandle, id);
    }

    public synchronized int getNumBody() {
        return DTrackSDK_getNumBody(nativeHandle);
    }

    public synchronized Body getBody(int id) {
        return DTrackSDK_getBody(nativeHandle, id);
    }

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
     *
     * @return channel number or -1 on error
     */
    public int findInactiveChannel(final int numChannels) {
        return findInactiveChannel(1, numChannels);
    }

    /**
     * Find first inactive channel starting with startChannel.
     *
     * @param startChannel
     * @return positive inactive channel number, 0 if no channel found and -1 on
     * error.
     */
    public int findInactiveChannel(final int startChannel, final int numChannels) {
        int currentChannel = startChannel;
        boolean channelFound = false;
        while (true) {
            if (numChannels > 0 && startChannel >= numChannels) {
                return 0;
            }
            String chName = String.format("ch%02d", currentChannel);
            String value = getParam("output net_active", chName);
            if (value == null) {
                return -1;
            }
            if ("no".equals(value)) {
                return currentChannel;
            }
            currentChannel += 1;
        }
    }

    public boolean configureUDPChannel(int channel, String host, int port) {
        return setParam("output net", String.format("ch%02d", channel), "udp " + host + " " + port);
    }

    public boolean configureMulticastChannel(int channel, String host, int port) {
        return setParam("output net", String.format("ch%02d", channel), "multicast " + host + " " + port);
    }

    public boolean activateChannel(int channel, String outputType) {
        return setParam("output active", String.format("ch%02d", channel), outputType + " yes");
    }

    public boolean deactivateChannel(int channel, String outputType) {
        return setParam("output active", String.format("ch%02d", channel), outputType + " no");
    }

    public synchronized final void destroy() {
        nativeHandle = DTrackSDK_destroy(nativeHandle);
    }

    //================================================================================
    // Private Methods
    //================================================================================

    private static native long DTrackSDK_create(int dataPort);

    private static native long DTrackSDK_create(String serverHost, int serverPort, int dataPort);

    private static native long DTrackSDK_destroy(long handle);

    private static native boolean DTrackSDK_isDestroyed(long handle);

    private static native int DTrackSDK_getLastDataError(long handle);

    private static native int DTrackSDK_getLastServerError(long handle);

    private static native int DTrackSDK_getLastDTrackError(long handle);

    private static native String DTrackSDK_getLastDTrackErrorDescription(long handle);

    private static native int DTrackSDK_getDataPort(long handle);

    private static native boolean DTrackSDK_isLocalDataPortValid(long handle);

    private static native boolean DTrackSDK_isUDPValid(long handle);

    private static native boolean DTrackSDK_isCommandInterfaceValid(long handle);

    private static native boolean DTrackSDK_isTCPValid(long handle);

    private static native boolean DTrackSDK_sendCommand(long handle, final String command);

    private static native CommandResult DTrackSDK_sendDTrack2Command(long handle, final String command);

    private static native int DTrackSDK_getRemoteSystemType(long handle);

    private static native boolean DTrackSDK_setParam(long handle, final String category, final String name, final String value);

    private static native boolean DTrackSDK_setParam(long handle, final String parameter);

    private static native String DTrackSDK_getParam(long handle, final String category, final String name);

    private static native String DTrackSDK_getParam(long handle, final String parameter);

    private static native boolean DTrackSDK_receive(long handle);

    private static native boolean DTrackSDK_startMeasurement(long handle);

    private static native boolean DTrackSDK_stopMeasurement(long handle);

    private static native int DTrackSDK_getFrameCounter(long handle);

    private static native double DTrackSDK_getTimeStamp(long handle);

    private static native int DTrackSDK_getNumMarker(long handle);

    private static native Marker DTrackSDK_getMarker(long handle, int id);

    private static native int DTrackSDK_getNumBody(long handle);

    private static native Body DTrackSDK_getBody(long handle, int id);

    //================================================================================
    // Private Members
    //================================================================================
    private long nativeHandle;

    @Deprecated
    private static DTrackSDK inst;

    static {
        NarSystem.loadLibrary();
    }
//    static { System.loadLibrary("art4j-0.1"); }
}
