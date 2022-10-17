package aero.t2s.modes.decoder.df;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.constants.AcasReplyInformation;
import aero.t2s.modes.constants.AcasSensitivity;
import aero.t2s.modes.constants.CrossLinkCapability;
import aero.t2s.modes.constants.VerticalStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DF0Test {
    private DF0 df0;

    @Test
    void it_decodes_vertical_status()
    {
        df0 = new DF0(BinaryHelper.stringToByteArray("02E194979F2C4B")).decode();
        assertEquals(VerticalStatus.AIRBORNE, df0.getVerticalStatus());

        df0 = new DF0(BinaryHelper.stringToByteArray("6E194979F2C4B")).decode();
        assertEquals(VerticalStatus.GROUND, df0.getVerticalStatus());
    }

    @Test
    void it_decodes_cross_link_capability()
    {
        df0 = new DF0(BinaryHelper.stringToByteArray("02E194979F2C4B")).decode();
        assertEquals(CrossLinkCapability.SUPPORTED, df0.getCrossLinkCapability());

        df0 = new DF0(BinaryHelper.stringToByteArray("00E194979F2C4B")).decode();
        assertEquals(CrossLinkCapability.UNSUPPORTED, df0.getCrossLinkCapability());
    }

    @Test
    void it_decodes_sensitivity_level()
    {
        df0 = new DF0(BinaryHelper.stringToByteArray("02E194979F2C4B")).decode();
        assertEquals(AcasSensitivity.LEVEL7, df0.getSensitivity());
    }

    @Test
    void it_decodes_reply_information()
    {
        df0 = new DF0(BinaryHelper.stringToByteArray("02E194979F2C4B")).decode();
        assertEquals(AcasReplyInformation.ACAS_RA_VERTICAL_ONLY, df0.getReplyInformation());
    }

    @Test
    void it_decodes_altitude()
    {
        df0 = new DF0(BinaryHelper.stringToByteArray("02E1951DE7596A")).decode();

        assertEquals(32925, df0.getAltitude().getAltitude(), 0.1);
        assertEquals(25, df0.getAltitude().getStep());
        assertFalse(df0.getAltitude().isMetric());
    }
}
