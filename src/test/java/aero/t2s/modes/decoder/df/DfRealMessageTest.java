package aero.t2s.modes.decoder.df;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.constants.SelectedAltitudeSource;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;
import aero.t2s.modes.decoder.UnknownDownlinkFormatException;
import aero.t2s.modes.decoder.df.bds.Bds40;
import aero.t2s.modes.decoder.df.bds.Bds50;
import aero.t2s.modes.decoder.df.bds.Bds60;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class DfRealMessageTest {
    @Test
    public void test_bds60() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A0001838E71A21357F640110A153");

        assertInstanceOf(DF20.class, df);

        assertTrue(((DF20)df).isValid());
        assertFalse(((DF20)df).isMultipleMatches());
        assertInstanceOf(Bds60.class, ((DF20)df).getBds());
        assertEquals(38000, ((DF20) df).getAltitude().getAltitude());

        Bds60 bds = (Bds60) ((DF20)df).getBds();
        assertTrue(bds.isStatusMagneticHeading());
        assertEquals(289.863, bds.getMagneticHeading(), 0.001);
        assertTrue(bds.isStatusIas());
        assertEquals(272, bds.getIas());
        assertTrue(bds.isStatusMach());
        assertEquals(0.852, bds.getMach());
        assertTrue(bds.isStatusBaroRocd());
        assertEquals(-640, bds.getBaroRocd());
        assertTrue(bds.isStatusIrsRocd());
        assertEquals(32, bds.getIrsRocd());
    }

    @Test
    public void test_df_20_bds_40_a48e35() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A00006A1E3A71D30AA014672C8DF");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("A48E35", df.getIcao());
        assertEquals(51000, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds40.class, df20.getBds());

        Bds40 bds = (Bds40) df20.getBds();
        assertTrue(bds.isStatusTargetSource());
        assertEquals(SelectedAltitudeSource.MCP, bds.getSelectedAltitudeSource());
        assertTrue(bds.isStatusMcp());
        assertEquals(51008, bds.getSelectedAltitude());
        assertTrue(bds.isStatusFms());
        assertEquals(51008, bds.getFmsAltitude());
        assertTrue(bds.isStatusBaro());
        assertEquals(1013.3, bds.getBaro(), 0.1);
        assertTrue(bds.isStatusMcpMode());
        assertFalse(bds.isAutopilotApproach());
        assertFalse(bds.isAutopilotVnav());
        assertTrue(bds.isAutopilotAltitudeHold());
    }


    @Test
    public void test_df20_bds60_800736() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A0001A1FA439F534BF07FFDE1ECC");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("800736", df.getIcao());
        assertEquals(40975, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds60.class, df20.getBds());

        Bds60 bds = (Bds60) df20.getBds();
        assertTrue(bds.isStatusIrsRocd());
        assertEquals(-32, bds.getIrsRocd());
        assertTrue(bds.isStatusBaroRocd());
        assertEquals(-1024.0, bds.getBaroRocd());
        assertTrue(bds.isStatusIas());
        assertEquals(250, bds.getIas());
        assertTrue(bds.isStatusMach());
        assertEquals(0.84, bds.getMach(), 0.1);
        assertTrue(bds.isStatusMagneticHeading());
        assertEquals(101.7, bds.getMagneticHeading(), 0.1);
    }

    @Test
    public void test_df21_bds60_4CA708() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A8800337D439E730BFE600C28696");

        assertInstanceOf(DF21.class, df);
        DF21 df21 = (DF21) df;
        assertEquals("4CA708", df.getIcao());
        assertEquals(2547, df21.getModeA());

        assertTrue(df21.isValid());
        assertInstanceOf(Bds60.class, df21.getBds());

        Bds60 bds = (Bds60) df21.getBds();
        assertTrue(bds.isStatusIrsRocd());
        assertEquals(0, bds.getIrsRocd(), 0.1);
        assertTrue(bds.isStatusBaroRocd());
        assertEquals(-128, bds.getBaroRocd(), 0.1);
        assertTrue(bds.isStatusIas());
        assertEquals(243, bds.getIas());
        assertTrue(bds.isStatusMach());
        assertEquals(0.77, bds.getMach(), 0.1);
        assertTrue(bds.isStatusMagneticHeading());
        assertEquals(236.7, bds.getMagneticHeading(), 0.1);
    }

    @Test
    public void test_df21_bds60_00000() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A800198EEABA2B30F0041257522A");

        assertInstanceOf(DF21.class, df);
        DF21 df21 = (DF21) df;
        assertEquals("000000", df.getIcao()); // Military / corrupt transponder
        assertEquals(5652, df21.getModeA());

        assertTrue(df21.isValid());
        assertInstanceOf(Bds60.class, df21.getBds());

        Bds60 bds = (Bds60) df21.getBds();
        assertTrue(bds.isStatusIrsRocd());
        assertEquals(576, bds.getIrsRocd(), 0.1);
        assertTrue(bds.isStatusBaroRocd());
        assertEquals(0, bds.getBaroRocd(), 0.1);
        assertTrue(bds.isStatusIas());
        assertEquals(277, bds.getIas());
        assertTrue(bds.isStatusMach());
        assertEquals(0.77, bds.getMach(), 0.1);
        assertTrue(bds.isStatusMagneticHeading());
        assertEquals(300.0, bds.getMagneticHeading(), 0.1);
    }

    @Test
    public void test_df21_bds60_406ECD() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A8000C98A549F33461E40552947D");

        assertInstanceOf(DF21.class, df);
        DF21 df21 = (DF21) df;
        assertEquals("406ECD", df.getIcao()); // Military / corrupt transponder
        assertEquals(5221, df21.getModeA());

        assertTrue(df21.isValid());
        assertInstanceOf(Bds60.class, df21.getBds());

        Bds60 bds = (Bds60) df21.getBds();
        assertTrue(bds.isStatusIrsRocd());
        assertEquals(160, bds.getIrsRocd(), 0.1);
        assertTrue(bds.isStatusBaroRocd());
        assertEquals(1920, bds.getBaroRocd(), 0.1);
        assertTrue(bds.isStatusIas());
        assertEquals(249, bds.getIas());
        assertTrue(bds.isStatusMach());
        assertEquals(0.77, bds.getMach(), 0.1);
        assertTrue(bds.isStatusMagneticHeading());
        assertEquals(104.7, bds.getMagneticHeading(), 0.1);
    }


    @Test
    public void test_df21_bds50_44CD73() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A0000333E17987192250004423EC");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("44CD73", df.getIcao()); // Military / corrupt transponder
        assertEquals(4275, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds50.class, df20.getBds());

        Bds50 bds = (Bds50) df20.getBds();
        assertTrue(bds.isStatusGs());
        assertEquals(200, bds.getGs(), 0.1);
        assertFalse(bds.isStatusTas());
        assertEquals(0, bds.getTas(), 0.1);
        assertTrue(bds.isStatusRollAngle());
        assertEquals(-43, bds.getRollAngle(), 0.1);
        assertTrue(bds.isStatusTrueAngleRate());
        assertEquals(2.3, bds.getTrackAngleRate(), 0.1);
        assertTrue(bds.isStatusTrackAngle());
        assertEquals(214.2, bds.getTrueTrack(), 0.1);
    }

    private DownlinkFormat testMessage(String message) throws UnknownDownlinkFormatException {
        Decoder decoder = new Decoder(new HashMap<>(), 50, 2, ModeSDatabase.createDatabase());

        return  decoder.decode(BinaryHelper.stringToByteArray(message));
    }
}
