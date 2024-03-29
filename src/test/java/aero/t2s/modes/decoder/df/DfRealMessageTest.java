package aero.t2s.modes.decoder.df;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;
import aero.t2s.modes.decoder.UnknownDownlinkFormatException;
import aero.t2s.modes.decoder.df.bds.*;
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

    @Test
    public void test_df21_bds40_407776() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A000169EB2CC0030A80106C25083");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("407776", df.getIcao()); // Military / corrupt transponder
        assertEquals(35350, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds40.class, df20.getBds());

        Bds40 bds = (Bds40) df20.getBds();
        assertTrue(bds.isStatusMcp());
        assertEquals(26000, bds.getSelectedAltitude(), 0.1);
        assertTrue(bds.isStatusFms());
        assertEquals(0, bds.getFmsAltitude(), 0.1);
        assertTrue(bds.isStatusBaro());
        assertEquals(1013.2, bds.getBaro(), 0.1);
        assertTrue(bds.isStatusTargetSource());
        assertEquals(SelectedAltitudeSource.MCP, bds.getSelectedAltitudeSource());
        assertTrue(bds.isStatusMcpMode());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
    }


    @Test
    public void test_df21_bds10_407776() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A000042210000600B0000089B69E");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("44CC63", df.getIcao()); // Military / corrupt transponder
        assertEquals(2000, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds10.class, df20.getBds());

        Bds10 bds = (Bds10) df20.getBds();
    }


    @Test
    public void test_df21_bds40_4CA6F8() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A000069E8BBC2F30A40000528AB8");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("4CA6F8", df.getIcao()); // Military / corrupt transponder
        assertEquals(9750, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds40.class, df20.getBds());

        Bds40 bds = (Bds40) df20.getBds();
        assertFalse(bds.isStatusTargetSource());
        assertNull(bds.getSelectedAltitudeSource());
        assertTrue(bds.isStatusMcp());
        assertEquals(6000, bds.getSelectedAltitude());
        assertTrue(bds.isStatusFms());
        assertEquals(3008, bds.getFmsAltitude());
        assertTrue(bds.isStatusBaro());
        assertEquals(1013.0, bds.getBaro(), 0.1);
        assertFalse(bds.isStatusMcpMode());
        assertFalse(bds.isAutopilotApproach());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
    }

    @Test
    public void test_df21_bds50_485209() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A000093BFFF16B276004997B748F");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("485209", df.getIcao()); // Military / corrupt transponder
        assertEquals(14075, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds50.class, df20.getBds());

        Bds50 bds = (Bds50) df20.getBds();
        assertTrue(bds.isStatusGs());
        assertEquals(314, bds.getGs(), 0.1);
        assertTrue(bds.isStatusTas());
        assertEquals(306, bds.getTas(), 0.1);
        assertTrue(bds.isStatusRollAngle());
        assertEquals(-0.1, bds.getRollAngle(), 0.1);
        assertTrue(bds.isStatusTrueAngleRate());
        assertEquals(0, bds.getTrackAngleRate(), 0.1);
        assertTrue(bds.isStatusTrackAngle());
        assertEquals(31.8, bds.getTrueTrack(), 0.1);
    }

    @Test
    public void test_df21_bds50_484FDF() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A800080080502D2A600CA9DF5877");

        assertInstanceOf(DF21.class, df);
        DF21 df21 = (DF21) df;
        assertEquals("484FDF", df.getIcao()); // Military / corrupt transponder
        assertEquals(1000, df21.getModeA());

        assertTrue(df21.isValid());
        assertInstanceOf(Bds50.class, df21.getBds());

        Bds50 bds = (Bds50) df21.getBds();
        assertTrue(bds.isStatusGs());
        assertEquals(338, bds.getGs(), 0.1);
        assertTrue(bds.isStatusTas());
        assertEquals(338, bds.getTas(), 0.1);
        assertTrue(bds.isStatusRollAngle());
        assertEquals(0.35, bds.getRollAngle(), 0.1);
        assertTrue(bds.isStatusTrueAngleRate());
        assertEquals(0, bds.getTrackAngleRate(), 0.1);
        assertTrue(bds.isStatusTrackAngle());
        assertEquals(3.8, bds.getTrueTrack(), 0.1);
    }

    @Test
    public void test_df20_bds50_48418A() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A0000E978F1FBD316114BDA0FFBF");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("48418A", df.getIcao()); // Military / corrupt transponder
        assertEquals(22375, df20.getAltitude().getAltitude());

        assertTrue(df20.isValid());
        assertInstanceOf(Bds50.class, df20.getBds());

        Bds50 bds = (Bds50) df20.getBds();
        assertTrue(bds.isStatusGs());
        assertEquals(394, bds.getGs(), 0.1);
        assertTrue(bds.isStatusTas());
        assertEquals(378, bds.getTas(), 0.1);
        assertTrue(bds.isStatusRollAngle());
        assertEquals(21, bds.getRollAngle(), 0.1);
        assertTrue(bds.isStatusTrueAngleRate());
        assertEquals(1, bds.getTrackAngleRate(), 0.1);
        assertTrue(bds.isStatusTrackAngle());
        assertEquals(354, bds.getTrueTrack(), 0.1);
    }

    @Test
    public void test_df16_02A198() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("80C18819584195384EF8505941FD");

        assertInstanceOf(DF16.class, df);
        DF16 df16 = (DF16) df;
        assertEquals("02A198", df.getIcao()); // Military / corrupt transponder
        assertEquals(12025, df16.getAltitude().getAltitude());

        assertEquals(VerticalStatus.AIRBORNE, df16.getVerticalStatus());
        assertEquals(AcasSensitivity.LEVEL6, df16.getSensitivity());
        assertEquals(AcasReplyInformation.ACAS_RA_VERTICAL_ONLY, df16.getReplyInformation());
        assertFalse(df16.getResolutionAdvisory().isActive());
        assertFalse(df16.getResolutionAdvisory().isRequiresCorrectionUpwards());
        assertFalse(df16.getResolutionAdvisory().isRequiresCorrectionDownwards());
        assertFalse(df16.getResolutionAdvisory().isRequiresPositiveClimb());
        assertFalse(df16.getResolutionAdvisory().isRequiresPositiveDescend());
        assertFalse(df16.getResolutionAdvisory().isRequiresCrossing());
        assertFalse(df16.getResolutionAdvisory().isSenseReversal());

        assertFalse(df16.isMultipleThreats());
        assertFalse(df16.isRANotPassAbove());
        assertFalse(df16.isRANotPassBelow());
        assertFalse(df16.isRANotTurnLeft());
        assertFalse(df16.isRANotTurnRight());
    }

    @Test
    public void test_df21_bds50_02A185() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A8001AA0F4596112BDFC569A3AEC");

        assertInstanceOf(DF21.class, df);
        DF21 df21 = (DF21) df;
        assertEquals("02A185", df.getIcao()); // Military / corrupt transponder
        assertEquals(7110, df21.getModeA());

        assertFalse(df21.isMultipleMatches());
        assertTrue(df21.isValid());
        assertEquals(Bds50.class, df21.getBds().getClass());

        Bds50 bds = (Bds50) df21.getBds();
        assertTrue(bds.isStatusGs());
        assertEquals(148, bds.getGs(), 0.1);
        assertTrue(bds.isStatusTas());
        assertEquals(172, bds.getTas(), 0.1);
        assertTrue(bds.isStatusRollAngle());
        assertEquals(-16.5, bds.getRollAngle(), 0.1);
        assertTrue(bds.isStatusTrueAngleRate());
        assertEquals(-2.0, bds.getTrackAngleRate(), 0.1);
        assertTrue(bds.isStatusTrackAngle());
        assertEquals(210.9, bds.getTrueTrack(), 0.1);
    }

    @Test
    public void test_df20_bds17_3D2C7C() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("A0280314020100000000004E25E8");

        assertInstanceOf(DF20.class, df);
        DF20 df20 = (DF20) df;
        assertEquals("3D2C7C", df.getIcao()); // Military / corrupt transponder
        assertEquals(3900, df20.getAltitude().getAltitude());

        assertFalse(df20.isMultipleMatches());
        assertTrue(df20.isValid());
        assertEquals(Bds17.class, df20.getBds().getClass());

        Bds17 bds = (Bds17) df20.getBds();
        assertFalse(bds.isBds0A());
        assertFalse(bds.isBds05());
        assertFalse(bds.isBds06());
        assertFalse(bds.isBds07());
        assertFalse(bds.isBds08());
        assertFalse(bds.isBds09());
        assertFalse(bds.isBds0A());
        assertTrue(bds.isBds20());
        assertFalse(bds.isBds21());
        assertFalse(bds.isBds40());
        assertFalse(bds.isBds41());
        assertFalse(bds.isBds42());
        assertFalse(bds.isBds43());
        assertFalse(bds.isBds44());
        assertFalse(bds.isBds45());
        assertFalse(bds.isBds48());
        assertTrue(bds.isBds50());
        assertFalse(bds.isBds51());
        assertFalse(bds.isBds52());
        assertFalse(bds.isBds53());
        assertFalse(bds.isBds54());
        assertFalse(bds.isBds55());
        assertFalse(bds.isBds56());
        assertFalse(bds.isBds5F());
        assertFalse(bds.isBds60());
    }

    private DownlinkFormat testMessage(String message) throws UnknownDownlinkFormatException {
        Decoder decoder = new Decoder(new HashMap<>(), 50, 2, ModeSDatabase.createDatabase());

        return  decoder.decode(BinaryHelper.stringToByteArray(message));
    }
}
