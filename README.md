# Arduino log conversion to Garmin TCX file

I've made an [Arduino-based speedometer](https://github.com/Radiokot/rollers-speedometer) for my bike rollers, but I don't want to make and Android app for it.
So I use serial terminal to collect logs and then I pass it to this script to get a Garmin TCX file to upload on Strava.
The speedometer reports speed in meters per hour and distance increase in milimeters. 

Sample input:
```
20:52:20.428 USB device detected
20:52:23.305 Connected to PL2303 device
20:52:23.318 0;0
20:52:25.320 0;0
20:52:27.322 0;0
20:52:29.324 0;0
20:52:31.326 0;0
20:52:33.327 0;0
20:52:35.329 11855;3168
20:52:36.321 20784;5808
20:52:37.343 26968;7656
21:24:50.448 26758;7656
21:24:51.478 25836;7392
21:24:52.526 23601;6864
21:24:54.528 8254;1848
21:24:56.528 0;0
21:24:58.531 0;0
21:25:00.533 0;0
21:25:01.343 Disconnected
21:25:01.493 Connected to PL2303 device
```

Sample output:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<TrainingCenterDatabase xmlns="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation
="http://www.garmin.com/xmlschemas/ActivityExtension/v2 http://www.garmin.com/xmlschemas/ActivityExtensionv2.xsd http://www.garmin.com/xmlschemas/TrainingCenterDat
abase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd">
   <Activities>
       <Activity Sport="Biking">
           <Id>42</Id>
           <Lap StartTime="2021-03-27T20:52:33.327Z">
               <TotalTimeSeconds>1943</TotalTimeSeconds>
               <DistanceMeters>18</DistanceMeters>
               <TriggerMethod>Manual</TriggerMethod>
                <Track>
                   <Trackpoint>
                       <Time>2021-03-27T20:52:33.327Z</Time>
                       <DistanceMeters>0.00</DistanceMeters>
                   </Trackpoint>
                   <Extensions>
                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                           <Speed>0.00</Speed>
                       </TPX>
                   </Extensions>
                   <Trackpoint>
                       <Time>2021-03-27T20:52:35.329Z</Time>
                       <DistanceMeters>3.17</DistanceMeters>
                   </Trackpoint>
                   <Extensions>
                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                           <Speed>3.29</Speed>
                       </TPX>
                   </Extensions>
                   <Trackpoint>
                       <Time>2021-03-27T20:52:36.321Z</Time>
                       <DistanceMeters>8.98</DistanceMeters>
                   </Trackpoint>
                   <Extensions>
                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                           <Speed>5.77</Speed>
                       </TPX>
                   </Extensions>
                   <Trackpoint>
                       <Time>2021-03-27T20:52:37.343Z</Time>
                       <DistanceMeters>16.63</DistanceMeters>
                   </Trackpoint>
                   <Extensions>
                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                           <Speed>7.49</Speed>
                       </TPX>
                   </Extensions>
                   <Trackpoint>
                       <Time>2021-03-27T21:24:54.528Z</Time>
                       <DistanceMeters>18.48</DistanceMeters>
                   </Trackpoint>
                   <Extensions>
                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                           <Speed>2.29</Speed>
                       </TPX>
                   </Extensions>
                   <Trackpoint>
                       <Time>2021-03-27T21:24:56.528Z</Time>
                       <DistanceMeters>18.48</DistanceMeters>
                   </Trackpoint>
                   <Extensions>
                       <TPX xmlns="https://www8.garmin.com/xmlschemas/ActivityExtensionv2.xsd">
                           <Speed>0.00</Speed>
                       </TPX>
                   </Extensions>
                </Track>
            </Lap>
        </Activity>
    </Activities>
</TrainingCenterDatabase>
```
