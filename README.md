Mode-S/ADS-B (Dump 1090) Java Library
======================================

This library decodes ADS-B Messages and creates an easy to work with track object.

Message support status

| Downlink Format | Human Readable                           | Supported | Note |
|-----------------|------------------------------------------|-----------|------|
| DF0             | Short Air-Air Surveillance               | ✅        |      |
| DF4             | Surveillance, Altitude request           | ✅        | Updates Altitude information + SPI (Ident) |
| DF5             | Surveillance, Identity request           | ✅        | Updates Callsign + SPI (Ident)     |
| DF11            | MODE S only all-call                     | ✅        | Broadcasts ICAO Mode-S address     |
| DF16            | Long Air-Air Surveillance                | ✅        | Logs warning when ACAS RA is active  |
| DF17            | Extended Squitter (Most ADS-B Data)      | ⚠️        | Partial supported see DF17/18 status below     |
| DF18            | Extended Squitter/Supplementary (Ground) | ⚠️        | Partial supported see DF17/18 status below     |
| DF19            | Extended Squitter Military               | ❌        | Not supported at this stage |
| DF20            | Comm-B Altitude                          | ⚠️        | Partial supported see DF20/21 status below     |
| DF21            | Comm-B Identity                          | ⚠️        | Partial supported see DF20/21 status below     |
| DF24            | Comm-B ELM                               | ❌        | Not supported at this stage |


## DF17/DF18 - Extended Squitter

DF17 is used for aircraft, while DF18 is used for other vessels (ground vehicles, static objects, ...)

Most features of the DF17/18 protocol have been implemented, some message lack support for specific fields.


| Type Code | Human Readable               | Supported | Note |
|-----------|------------------------------|-----------|------|
| 0         | Airborne/Surface No altitude | ✅        |  
| 1         | Aircraft Identification      | ✅        |
| 2         | Aircraft Identification      | ✅        |
| 3         | Aircraft Identification      | ✅        |
| 4         | Aircraft Identification      | ✅        |
| 5         | Surface Position             | ❌        | Not implemented yet
| 6         | Surface Position             | ❌        | Not implemented yet
| 7         | Surface Position             | ❌        | Not implemented yet
| 8         | Surface Position             | ❌        | Not implemented yet
| 9         | Airborne Position            | ✅        |
| 10        | Airborne Position            | ✅        |
| 11        | Airborne Position            | ✅        |
| 12        | Airborne Position            | ✅        |
| 13        | Airborne Position            | ✅        |
| 14        | Airborne Position            | ✅        |
| 15        | Airborne Position            | ✅        |
| 16        | Airborne Position            | ✅        |
| 17        | Airborne Position            | ✅        |
| 18        | Airborne Position            | ✅        |
| 19        | Airborne Velocity            | ✅        |
| 20        | Airborne Position            | ✅        |
| 21        | Airborne Position            | ✅        |
| 22        | Airborne Position            | ✅        |
| 23        | Test Message                 | ✅        |
| 24        | Surface System Status        | ❌        | Not implemented yet
| 25        | Reserved Message             | ✅        |
| 26        | Reserved Message             | ✅        |
| 27        | Reserved (Trajectory Change) | ✅        |
| 28        | Aircraft Status Message      | ✅        | Priority mode A code (emergency) + TCAS/ACAS RA Broadcast
| 29        | Target Status Message        | ⚠️        | Partial support
| 30        | Reserved Message             | ✅        |
| 31        | Aircraft Operational Status  | ⚠️        | Partial support


## DF20/21 Comm-B

DF20/21 messages are replies to data requests from a radar station, you'll only receive these messages if Mode-S radar
is actively requesting this information. You will only receive messages requested by the radar.

Message is structure as follows

```
LSB |1----|6--|9----|14----|20-----------|33------------------------------------------------------|89-----------------------|
    | DF  |FS | DR  |  UM  |     AC      |                         MB                             |            AP           |
MSB |----5|--8|---13|----19|-----------32|------------------------------------------------------88|----------------------112|

LSB: Least Significant Bit (First bit) (inclusive)
MSB: Most Significant bit (Last bit) (Inclusive)

DF: Downlink Format
FS: Flight Status
DR: Downlink Request
UM: Utitlity Message
AC: Altitude Code (DF20: Altitude | DF21: Mode-A Code)
MB: Comm-B Message
AP: Address / Parity (Mode-S Address + Parity check)
```


The system uses BDS (comm-B Data Selected) code to determine the requested information. The radar station will
send a request (UF20 / UF21) containing a reply ID. The reply ID is a used by the aircraft in the response. 
This means the reply by the aircraft does not contain the original BDS, without this info you have to guess which BDS is used.

You run through each BDS and pass the message to the decoder, if the message does not fit you assume it's not correct. 
Repeat this process until you have a match. 

At this moment the coded logic has too many incorrect matches and thus decided to disable BDS guessing.
We are actively looking for a fix, or at least ability to enable this with a experimental flag.
We hope with more BDS implemented the guessing accuracy will improve.

| BDS | Human Readable                             | Supported | Note |
|-----|--------------------------------------------|-----------|------|
| 1,0 | Data link capability report                | ❌        | Detection implemented, decoding missing
| 1,7 | Common usage GICB capability report        | ✅        |
| 1,8 | Mode S services GICB capability report     | ❌        |
| 1,9 | Mode S services GICB capability report     | ❌        |
| 1,A | Mode S services GICB capability report     | ❌        |
| 1,B | Mode S services GICB capability report     | ❌        |
| 1,C | Mode S services GICB capability report     | ❌        |
| 1,D | Mode S services GICB capability report     | ❌        |
| 1,E | Mode S services GICB capability report     | ❌        |
| 1,F | Mode S services GICB capability report     | ❌        |
| 2,0 | Aircraft Identification                    | ✅        |
| 2,1 | Aircraft and Airline registration marking  | ⚠️        | Experimental
| 2,2 | Antenna positions                          | ❌        |
| 2,5 | Antenna type                               | ❌        |
| 3,0 | ACAS Active resolution advisory            | ❌        | Detection implemented, decoding missing
| 4,0 | Selected vertical intention                | ✅️        |
| 4,1 | Next waypoint details                      | ❌        | 9 Characters
| 4,2 | Next waypoint details                      | ❌        | Waypoint lat/lon + crossing altitude
| 4,3 | Next waypoint details                      | ❌        | Bearing, time and distance to waypoint
| 4,4 | Meteorological routine air report          | ✅        |
| 4,5 | Meteorological hazard report               | ✅        |
| 4,8 | VHF Channel report                         | ❌        | Info on VHF 1/2/3 (frequency + status) & Guard status
| 5,0 | Track and turn report                      | ✅        |
| 5,1 | Position report coarse                     | ❌        |
| 5,2 | Position report fine                       | ❌        |
| 5,3 | Air-reference state vector                 | ✅        |
| 5,4 | Waypoint 1                                 | ❌        | 5 Chars, ETA, Estimated level, time to go
| 5,5 | Waypoint 2                                 | ❌        | 5 Chars, ETA, Estimated level, time to go
| 5,5 | Waypoint 3                                 | ❌        | 5 Chars, ETA, Estimated level, time to go
| 5,F | Quasi-static parameter monitoring          | ❌        |
| 6,0 | Heading and speed report                   | ✅        |
| 6,1 | Priority/emergency status                  | ❌        |
| 6,5 | Aircraft operational status                | ❌        |
| E,3 | Transponder type/part number               | ❌        |
| E,4 | Transponder software revision number       | ❌        |
| E,5 | ACAS type/part number                      | ❌        |
| E,6 | ACAS software revision number              | ❌        |
| E,7 | Transponder status and diagnostics         | ❌        |
| E,A | Vendor specific status and diagnostics     | ❌        |
| F,1 | Military application                       | ❌        |
| F,2 | Military application                       | ❌        |


# Installation 

This package we are waiting on sonatype verification process once available we will publish this package on maven-central 

```xml
<dependency>
  <groupId>aero.t2s</groupId>
  <artifactId>mode-s</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

# Usage

```java
class Main
{
    public static void main(String[] args){
        ModeS modes = new ModeS("127.0.0.1", 30002, 52.0, 0.0);

        modes.onTrackCreated(track -> System.out.println("CREATED " + track.toString()));
        modes.onTrackUpdated(track -> System.out.println("UPDATED " + track.toString()));
        modes.onTrackDeleted(track -> System.out.println("DELETED " + track.toString()));
        
        // Starts listening thread
        modes.start();
   
        // Get all tracks Map<String, Track>
        modes.getTracks();
    
        // Stop listening
        modes.stop();
    }
}
```

# Contributing

You can contribute to this project by reporting/fixing bugs or implemented a new packet.  
We are always looking for help on this project.

# License

This library is Apache 2.0 licensed read the full license [here](LICENSE).
