package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.SelectedAltitudeSource;

/**
 * 56-bit MB Field is structured in the following format
 * <pre>
 *
 * LSB |1-------|2----------|14------|15---------|27------|28---------|40--------|48------|49----|50---|51---|52--------|54------|55------|
 *     | STATUS |  MCP ALT  | STATUS |  FMS ALT  | STATUS |    BARO   | RESERVED | STATUS | VNAV | ALT | APP | RESERVED | STATUS | SOURCE |
 * MSB |-------1|---------13|------14|---------26|------27|---------39|--------47|------48|----49|---50|---51|--------53|------54|------56|
 * </pre>
 *
 * <b>Status flags</b>
 * <ul>
 *     <li>MCP/FCU Status - (bit 1) indicates if MCP/FCU Selected altitude is available</li>
 *     <li>FMS Status - (bit 14) indicates if FMS Selected altitude is available</li>
 *     <li>Baro Status - (bit 27) indicates if barometric pressure setting is valid</li>
 *     <li>MCP/FCU Mode Status - (bit 48) indicates if MCP/FCU mode bits are available</li>
 *     <li>
 *         Target source altitude - (bit 54) indicates if the selected altitude source is available.
 *         This is not the same as the information stored in bit 1 see information below on MCP/FCU Altitude
 *     </li>
 * </ul>
 *
 * <h2>MCP/FCU Selected altitude</h2>
 *
 * <p>
 *     This information which represents the real “aircraft intent,” when available,
 *     represented by the altitude control panel selected altitude,
 *     the flight management system selected altitude,
 *     or the current aircraft altitude according to the aircraft’s mode of flight
 *     (the intent may not be available at all when the pilot is flying the aircraft).
 * </p>
 *
 * <p>
 *     Source is determined using Target Altitude Source (bit 55-56) and is only available if status bit (bit 54) is set to 1.
 *     See {@link SelectedAltitudeSource} for more details
 * </p>
 *
 * <i>Note: LSB (1 bit) = 16feet with a range 0 - 65520 feet, this class considers altitudes above 50000ft as invalid / error.</i>
 *
 * <h2>FMS Selected altitude</h2>
 *
 * <p>
 *     This field when available indicates the FMS selected altitude.
 *     This field could differ when selects a different altitude on the MCP/FCU panel
 * </p>
 *
 * <p>
 *     Example: FMS has VNAV altitude set at 32000ft, the aircraft is at 26000ft and instructed to climb to 30000ft.
 *     At this moment the pilot selects 30000ft on the MCP/FCU which is transmitted in the MCP/FCU field.
 *     The FMS selected altitude field is transmitting 32000ft, the Target Altitude Source flag is set to MCP.
 * </p>
 *
 * <i>Note: LSB (1 bit) = 16feet with a range 0 - 65520 feet, this class considers altitudes above 50000ft as invalid / error.</i>
 *
 * <h2>Barometric Pressure Setting</h2>
 *
 * <p>When status flag (bit 27) is set to false indicates the information is valid and van be used</p>
 *
 * <i>Note: LSB (1 bit) = 0.1mb with a range 0 - 410mb. You need to add 800mb to receive the real baro steting</i>
 *
 * <h2>MCP/FCU Mode bits</h2>
 * <p>
 * Only available if Status (bit 48) is set.
 *
 * <ul>
 *     <li>VNAV Mode (bit 49) - Indicates if VNAV navigation is engaged</li>
 *     <li>ALT Hold Mode (bit 50) - Indicates if altitude hold is enabled</li>
 *     <li>APP Mode (bit 51) - Indicates if Apprach mode is selected</li>
 * </ul>
 */
public class Bds40 extends Bds {

    private boolean statusMcp;
    private boolean statusFms;
    private boolean statusBaro;
    private boolean statusMcpMode;
    private boolean statusTargetSource;

    private SelectedAltitudeSource selectedAltitudeSource;
    private int selectedAltitude;
    private int fmsAltitude;

    private double baro;

    private boolean autopilotVnav;
    private boolean autopilotAltitudeHold;
    private boolean autopilotApproach;

    public Bds40(short[] data) {
        super(data);

        statusMcp = (data[4] & 0b10000000) != 0;
        statusFms = (data[5] & 0b00000100) != 0;
        statusBaro = (data[7] & 0b00100000) != 0;
        statusMcpMode = (data[9] & 0b00000001) != 0;
        statusTargetSource = (data[10] & 0b00000100) != 0;
        boolean reservedZeroA = ((data[8] & 0x1) | (data[9] >>> 1)) == 0;
        boolean reservedZeroB = ((data[10] >>> 3) & 0x3) == 0;

        if (!reservedZeroA) {
            invalidate();
            return;
        }

        if (!reservedZeroB) {
            invalidate();
            return;
        }

        selectedAltitude = (((data[4] & 0b01111111) << 5) | (data[5] & 0b11111000) >>> 3) * 16;
        if (statusMcp) {
            if (selectedAltitude > 50000) {
                invalidate();
                return;
            }
        } else {
            if (selectedAltitude != 0) {
                invalidate();
                return;
            }
        }

        fmsAltitude = (((data[5] & 0x3) << 10) | (data[6] << 2) | ((data[7] >>> 6) & 0x3)) * 16;
        if (statusFms) {
            if (fmsAltitude <= 0 || fmsAltitude > 50000) {
                invalidate();
                return;
            }
        } else {
            if (fmsAltitude != 0) {
                invalidate();
                return;
            }
        }

        baro = ((((data[7] & 0x1F) << 7) | (data[8] >>> 1)) * 0.1) + 800.0;
        if (statusBaro) {
            if (baro < 850 || baro > 1100) {
                invalidate();
                return;
            }
        } else {
            if (baro != 800) {
                invalidate();
                return;
            }
        }

        autopilotVnav = (data[10] & 0b10000000) != 0;
        autopilotAltitudeHold = (data[10] & 0b01000000) != 0;
        autopilotApproach = (data[10] & 0b00100000) != 0;
        if (!statusMcpMode) {
            if (autopilotAltitudeHold || autopilotVnav || autopilotApproach) {
                invalidate();
                return;
            }
        }

        int selectedSource = data[10] & 0b00000011;
        if (statusTargetSource) {
            selectedAltitudeSource = SelectedAltitudeSource.find(selectedSource);
        } else {
            if (selectedSource != 0) {
                invalidate();
                return;
            }
        }
    }

    @Override
    public void apply(Track track) {
        if (statusMcp)
            track.setSelectedAltitude(selectedAltitude);

        if (statusFms)
            track.setFmsSelectedAltitude(fmsAltitude);

        if (statusBaro)
            track.setBaroSetting(baro);

        if (statusMcpMode) {
            track.setVnav(autopilotVnav);
            track.setAltitudeHold(autopilotAltitudeHold);
            track.setApproachMode(autopilotApproach);
        }

        if (statusTargetSource) {
            track.setSelectedAltitudeSource(selectedAltitudeSource);
        }
    }

    @Override
    protected void reset() {
        statusMcp = false;
        statusFms = false;
        statusBaro = false;
        statusMcpMode = false;
        statusTargetSource = false;

        selectedAltitudeSource = null;
        selectedAltitude = 0;
        fmsAltitude = 0;

        baro = 0;

        autopilotVnav = false;
        autopilotAltitudeHold = false;
        autopilotApproach = false;
    }

    public boolean isStatusMcp() {
        return statusMcp;
    }

    public boolean isStatusFms() {
        return statusFms;
    }

    public boolean isStatusBaro() {
        return statusBaro;
    }

    public boolean isStatusMcpMode() {
        return statusMcpMode;
    }

    public boolean isStatusTargetSource() {
        return statusTargetSource;
    }

    public SelectedAltitudeSource getSelectedAltitudeSource() {
        return selectedAltitudeSource;
    }

    public int getSelectedAltitude() {
        return selectedAltitude;
    }

    public int getFmsAltitude() {
        return fmsAltitude;
    }

    public double getBaro() {
        return baro;
    }

    public boolean isAutopilotVnav() {
        return autopilotVnav;
    }

    public boolean isAutopilotAltitudeHold() {
        return autopilotAltitudeHold;
    }

    public boolean isAutopilotApproach() {
        return autopilotApproach;
    }
}
