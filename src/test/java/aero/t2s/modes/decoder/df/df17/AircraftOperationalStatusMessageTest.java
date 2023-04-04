package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.decoder.df.df17.data.SurfaceOperationalMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AircraftOperationalStatusMessageTest {
    @Test
    public void it_decodes_surface_version1_message() {
        AircraftOperationalStatusMessage message = new AircraftOperationalStatusMessage(BinaryHelper.stringToByteArray("8F40622DF900260283493839FD1F")).decode();

        Assertions.assertInstanceOf(AircraftOperationalStatusVersion2.class, message);
        Assertions.assertInstanceOf(AircraftOperationalStatusVersion2Surface.class, message);

        AircraftOperationalStatusVersion2Surface surface = (AircraftOperationalStatusVersion2Surface) message;
        Assertions.assertEquals(Version.VERSION2, surface.getVersion());
        Assertions.assertEquals(LengthWidthCode.CAT6, surface.getLengthWidthCode());

        Assertions.assertFalse(surface.getSurfaceCapability().isCockpitDisplayOfTraffic());
        Assertions.assertFalse(surface.getSurfaceCapability().isUatReceive());
        Assertions.assertFalse(surface.getSurfaceCapability().isLowB2Power());
        Assertions.assertFalse(surface.getSurfaceCapability().isPositionOffsetApplied());
        Assertions.assertFalse(surface.getSurfaceCapability().isReceive1090ES());
        Assertions.assertEquals(NavigationUncertaintyCategory.NUC1, surface.getSurfaceCapability().getNACv());
        Assertions.assertEquals(0, surface.getSurfaceCapability().getNICsuppC());

        Assertions.assertFalse(surface.getOperationalMode().isAcasIdent());
        Assertions.assertTrue(surface.getOperationalMode().isGpsLateralOffsetAvailable());
        Assertions.assertFalse(surface.getOperationalMode().isSingleAntennaFlag());
        Assertions.assertTrue(surface.getOperationalMode().isGpsLongitudinalOffsetAvailable());
        Assertions.assertFalse(surface.getOperationalMode().isGpsLongitudinalOffsetAppliedBySensor());
        Assertions.assertEquals(AcasState.RA_NOT_ACTIVE, surface.getOperationalMode().getAcasRA());
        Assertions.assertEquals(0, surface.getOperationalMode().getGpsLateralOffset());
        Assertions.assertEquals(0, surface.getOperationalMode().getGpsLateralOffset());
        Assertions.assertEquals(4, surface.getOperationalMode().getGpsLongitudinalOffset());
        Assertions.assertEquals(SourceIntegrityLevel.LESS_THEN_ONE_PER_HUNDRED_THOUSAND, surface.getOperationalMode().getSystemDesignAssurance());

        Assertions.assertEquals(NavigationIntegrityCategory.RC_75_M, surface.getNICp());
        Assertions.assertEquals(Angle.TRUE_TRACK, surface.getHorizontalSource());
    }
}
