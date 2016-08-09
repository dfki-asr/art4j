/*
 * This file is part of art4j. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.art4j;

import de.dfki.resc28.art4j.targets.Body;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class DTrackTest {

    public static boolean sendCommand(DTrackSDK tracker, String command) {
        System.out.format("Send command: %s%n", command);
        DTrackSDK.CommandResult result = tracker.sendDTrack2Command(command);
        System.out.format("Command result: %s%n", result);
        if (result.isDTrackError()) {
            System.out.format("Last DTrack Error : %s%n", DTrackSDK.errorToString(tracker.getLastDTrackError()));
            System.out.format("Last DTrack Error Description: %s%n", tracker.getLastDTrackErrorDescription());
        }
        return result.isOK();
    }

    public static String checkParam(DTrackSDK tracker, final String category, final String name)
    {
        String value = tracker.getParam(category, name);
        if (value != null)
            System.out.format("Get param %s %s -> %s%n", category, name, value);
        else {
            System.out.format("Failed get param %s %s -> %s%n", category, name, tracker.getLastDTrackErrorDescription());
        }
        return value;
    }

    public static void main(String[] args) throws UnknownHostException {
        //DTrackSDK tracker = DTrackSDK.getInstance("192.168.81.110", 50105, 5002);
        //DTrackSDK tracker = DTrackSDK.getInstance(null, 50105, 5002);

        DTrackSDK tracker = new DTrackSDK("192.168.81.110", 50105, 5002);
        //DTrackSDK tracker = new DTrackSDK("localhost", 50105, 5002);

        if (!tracker.isSystemAccessible()) {
            System.err.println("WARNING: Controller is already in use !");
        }

        checkParam(tracker, "system", "version");
        checkParam(tracker, "system", "hostname");
        checkParam(tracker, "system", "controller_serial");
        checkParam(tracker, "system", "protocol_version");
        checkParam(tracker, "system", "access");
        checkParam(tracker, "system", "ethernet_mac");

        int inactiveChannel = 0;
        do {
            inactiveChannel = tracker.findInactiveChannel(inactiveChannel+1, DTrackSDK.NUM_DTRACK2_OUTPUT_CHANNELS);
            if (inactiveChannel > 0)
                System.out.format("Inactive channel: %d%n", inactiveChannel);
        } while (inactiveChannel > 0);

        // Initialize
        //System.out.println(Inet4Address.getLocalHost().getHostAddress());
        tracker.configureUDPChannel(5, "192.168.81.107", 5002);
        tracker.activateChannel(5, "all");

        sendCommand(tracker, "dtrack2 getmsg");

        // End Initialize

        System.out.println("DTrack Info");
        System.out.format("Data port: %d%n", tracker.getDataPort());
        System.out.format("Local data port valid: %b%n", tracker.isLocalDataPortValid());
        System.out.format("UDP valid: %b%n", tracker.isUDPValid());
        System.out.format("TCP valid: %b%n", tracker.isTCPValid());
        System.out.format("Command interface valid: %b%n", tracker.isCommandInterfaceValid());
        System.out.format("Remote system type: %s%n", DTrackSDK.systemTypeToString(tracker.getRemoteSystemType()));

        System.out.format("Frame counter: %d%n", tracker.getFrameCounter());
        System.out.format("Time stamp: %f%n", tracker.getTimeStamp());
        System.out.format("Num marker: %d%n", tracker.getNumMarker());
        System.out.format("Num body: %d%n", tracker.getNumBody());

        System.out.format("Last Data Error : %s%n", DTrackSDK.errorToString(tracker.getLastDataError()));
        System.out.format("Last Server Error : %s%n", DTrackSDK.errorToString(tracker.getLastServerError()));
        System.out.format("Last DTrack Error : %s%n", DTrackSDK.errorToString(tracker.getLastDTrackError()));
        System.out.format("Last DTrack Error Description: %s%n", tracker.getLastDTrackErrorDescription());


        checkParam(tracker, "status", "active");
        checkParam(tracker, "status", "frames_lost");
        checkParam(tracker, "status", "number_of_bodies");
        checkParam(tracker, "status", "number_of_marker");
        checkParam(tracker, "inertial", "available");
        checkParam(tracker, "artwpan", "available");
        String numCamStr = checkParam(tracker, "camera", "number_of_cameras");
        if (numCamStr != null) {
            final int numCam = Integer.parseInt(numCamStr);
            for (int i = 1; i <= numCam; ++i) {
                String camName = String.format("c%02d", i);
                checkParam(tracker, "camera display_upside_down", camName);
                checkParam(tracker, "camera dynamic_reflex_skip", camName);
                checkParam(tracker, "camera has_dynamic_reflex_skip", camName);
                checkParam(tracker, "camera has_static_reflex_skip", camName);
                checkParam(tracker, "camera number_of_areas", camName);
                checkParam(tracker, "camera static_reflex_skip", camName);
                checkParam(tracker, "status camera_aggregated_status", camName);
                checkParam(tracker, "status number_of_rays", camName);
                checkParam(tracker, "status number_of_used_rays", camName);
                checkParam(tracker, "tracking has_room", camName);
            }
        }


        String[] typeNames = null;

        checkParam(tracker, "config", "is_changeable");
        checkParam(tracker, "output", "net_via_router");
        String numTypesStr = checkParam(tracker, "output", "number_of_types");
        if (numTypesStr != null) {
            final int numTypes = Integer.parseInt(numTypesStr);
            typeNames = new String[numTypes];
            for (int i = 1; i <= numTypes; ++i) {
                String typeName = checkParam(tracker, "output type", String.format("t%02d", i));
                typeNames[i-1] = typeName;
                checkParam(tracker, "output type_desc", typeName);
                checkParam(tracker, "output available", typeName);
            }
        }

        checkParam(tracker, "output net", "chmon");
        int numChannels = 0;
        int currentChannel = 1;
        boolean channelFound = false;
        while (true) {
            String chName = String.format("ch%02d", currentChannel);
            String chNet = checkParam(tracker, "output net", chName);
            if (chNet == null)
                break;

            checkParam(tracker, "output net_active", chName);
            checkParam(tracker, "output net_divisor", chName);

            if (typeNames != null) {
                final String category = "output active " + chName;
                for (String typeName : typeNames) {
                    checkParam(tracker, category, typeName);
                }
            }

            currentChannel += 1;
        }

        System.out.format("Available channels: %d%n", currentChannel-1);



//        sendCommand(tracker, "dtrack2 get status active");
//        sendCommand(tracker, "dtrack2 set output net ch04 udp 192.168.81.107 5001");
//        sendCommand(tracker, "dtrack2 set output active ch04 all yes");

        System.out.println("Start Measurement");

        tracker.startMeasurement();
        for (int i = 0; i < 5; i++) {
            System.out.println("Frame counter: " + tracker.getFrameCounter());
            if (!tracker.receive()) {
                System.out.println("No data frames !");
                System.out.format("Last Data Error : %s%n", DTrackSDK.errorToString(tracker.getLastDataError()));
                System.out.format("Last Server Error : %s%n", DTrackSDK.errorToString(tracker.getLastServerError()));
                System.out.format("Last DTrack Error : %s%n", DTrackSDK.errorToString(tracker.getLastDTrackError()));
                System.out.format("Last DTrack Error Description: %s%n", tracker.getLastDTrackErrorDescription());
            } else {
                for (int j = 0; j < tracker.getNumBody(); j++) {
                    Body artBody = tracker.getBody(j);
                    double[] rotation = artBody.getRotation();
                    System.out.format("Body %d: rotation = %s%n", j, Arrays.toString(rotation));
                }
            }
        }
        tracker.stopMeasurement();
        tracker.destroy();
    }
}
