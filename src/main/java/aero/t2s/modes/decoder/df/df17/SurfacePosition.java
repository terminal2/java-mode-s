package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.CprPosition;
import aero.t2s.modes.Track;

public class SurfacePosition extends ExtendedSquitter {
    private final String address;
    private int velocityEncoded = 0;
    private double velocity = 0;

    private boolean trackValid = false;
    private int trackEncoded = 0;

    private double lat;
    private double lon;
    private boolean positionAvailable = false;
    private boolean velocityAvailable = false;

    public SurfacePosition(short[] data, String address) {
        super(data);
        this.address = address;
    }

    @Override
    public SurfacePosition decode() {
        velocityEncoded = ((data[4] & 0x7) << 4) | (data[5] >>> 4);
        velocityAvailable = velocityEncoded != 0;
        velocity = decodeEncodedVelocity();

        trackValid = ((data[5] >>> 3) & 0x1) == 0x1;
        trackEncoded = ((data[5] & 0x7) << 4) | (data[6] >>> 4);

        // Decode position
        int time = (data[6] >>> 3) & 0x1;
        boolean isCprEven = ((data[6] >>> 2) & 0x1) == 0;

        int cprLat = (data[6] & 0x3) << 15;
        cprLat = cprLat | (data[7] << 7);
        cprLat = cprLat | (data[8] >>> 1);

        int cprLon = ((data[8] & 0x1) << 16);
        cprLon = cprLon | (data[9] << 8);
        cprLon = cprLon | data[10];

        CprPosition newPosition = PositionUpdate.calculate(address, isCprEven, new CprPosition(cprLat, cprLon, true));
        if (newPosition != null) {
            this.lat = newPosition.getLat();
            this.lon = newPosition.getLon();
            this.positionAvailable = true;
        } else {
            this.positionAvailable = false;
        }

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setGroundBit(true);

        if (positionAvailable) {
            track.setLatLon(lat, lon);
        }

        if (velocityAvailable) {
            track.setGs(velocity);
        }
    }

    public boolean isPositionAvailable() {
        return positionAvailable;
    }

    public boolean isVelocityAvailable() {
        return this.velocityAvailable;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getAddress() {
        return address;
    }

    public int getVelocityEncoded() {
        return velocityEncoded;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public boolean isTrackValid() {
        return trackValid;
    }

    public int getTrackEncoded() {
        return trackEncoded;
    }

    public double getTrack() {
        return trackEncoded * 360.0 / 128.0;
    }

    private double decodeEncodedVelocity() {
        if (this.velocityEncoded == 1) {
            return 0;
        }
        if (this.velocityEncoded <= 8) {
            return (this.velocityEncoded - 1) * 0.125 + 0;
        }
        if (this.velocityEncoded <= 12) {
            return (this.velocityEncoded - 9) * 0.25 + 1.0;
        }
        if (this.velocityEncoded <= 38) {
            return (this.velocityEncoded - 13) * 0.5 + 2.0;
        }
        if (this.velocityEncoded <= 93) {
            return (this.velocityEncoded - 39) * 1.0 + 15.0;
        }
        if (this.velocityEncoded <= 93) {
            return (this.velocityEncoded - 39) * 1.0 + 15.0;
        }
        if (this.velocityEncoded <= 108) {
            return (this.velocityEncoded - 94) * 2.0 + 70.0;
        }
        if (this.velocityEncoded <= 123) {
            return (this.velocityEncoded - 109) * 5.0 + 100.0;
        }
        if (this.velocityEncoded == 124) {
            return 175.0;
        }

        return 0;   // Could be either "not available" or "reserved"... just assume zero speed for now
    }
}
