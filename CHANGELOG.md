## Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

### [Unreleased][unreleased]

#### Fixed

#### Changed

#### Added

#### Removed

### [v0.2.0][v0.2.0]

#### Fixed
 
 - BDS21: Is not valid if first byte is 0b00100000
 - BDS45: Is invalid if status = false and static pressure is likely wrong
 - BDS60
    - Calculate CAS and compare with IAS to increase accuracy / validity
    - If status flag = true and value is non-zero it cannot be BDS60 message
    - Perform status bit checks on correct data bits
 - BDS50: Filter groundspeed > 600 as incorrect message to reduce number of false positives
 - BDS40: Check status flags and non-zero values
 - BDS40: MCP Altitude calculations
 - BDS45: check radio height on correct data bits
      
#### Changed
 
 - Migrate Maven POM based release to Gradle based release
 - ModeSDatabase can now be loaded using external CSV files

#### Added

 - DF17: Aircraft Operational Status (Version 2 Airborne/Ground)
 - DF17: Aircraft Operational Status (Version 1 Airborne/Ground)
 - DF17: Airborne Velocity
 - Only work with Downlink Message without track management
 - Disable tracks deleted on automatically

