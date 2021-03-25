# Infinite Flight Connect API - For Java
This repo will allow you to connect to the Infinite Flight Connect API (Connect API) in Java with ease.

First, and most important, here is some documentation that is somewhat essential to understanding all of this:
- [Infinite Flight Connect API V2 Docs](https://infiniteflight.com/guide/developer-reference/connect-api/version-2)
- [Paths and their types](https://github.com/flyingdevelopmentstudio/infiniteflight-api)
- Seriously, if you have any question(s) at all, don't hesitate to [message](https://community.infiniteflight.com/u/onelettershor2/) me on the [Infinite Flight Community](https://community.infiniteflight.com/)

<table>
<thead>
<tr>
<th>Integer</th>
<th>Type</th>
</tr>
</thead>
<tbody>
<tr>
<td>0</td>
<td>Boolean</td>
</tr>
<tr>
<td>1</td>
<td>Integer (32-bit)</td>
</tr>
<tr>
<td>2</td>
<td>Float</td>
</tr>
<tr>
<td>3</td>
<td>Double</td>
</tr>
<tr>
<td>4</td>
<td>String</td>
</tr>
<tr>
<td>5</td>
<td>Long</td>
</tr>
</tbody>
</table>

## How to use in an Eclipse project
Implementing this into your project is quite simple!

### Dependencies 
This project is dependent on [this](https://search.maven.org/remotecontent?filepath=org/json/json/20210307/json-20210307.jar) (JSON for Java)
Add this using the steps below:

1. Download the .jar file which will be needed to refrence this repo. [download](https://github.com/onelettershor2/infiniteFlightJava/raw/master/infiniteFlightJavav1.0.0.jar) (Make sure and download the one above as well!)
2. - Under **_Package Explorer_** right click the project you would like to use this in.
   - Hover ```Build Path -> Configure Build Path```
   - Under **_Libraries_** tab... ```Add External JARs```
   - Find this repo's .jar file
   - Press **_OK_** then your done!
   - Still need more help? Visit [this](https://www.tutorialspoint.com/eclipse/eclipse_java_build_path.htm) page for a little more explanation, or [message me](#getting-a-jar-file)

3. You are almost done! View the example code under the **example** branch to see the basic setup.

## More Documentation
So ontop of the basic getting of data, I included some more methods to ease the conversion proccess of data. (The API returns some not so normal numbers ex: m/s, radians, etc.)

To help with this, I included some static methods to convert. <br>
**_Note: conversions are based on formulas Google uses_** <br>
**_Another Note: sorry if you were hoping for metric conversions, but I did these all based off Imperial_**

```
- convertMetersPerSecondToFeetPerMinute(float ms)
- convertMetersPerSecondToKnots(float ms)
- convertRadianToDegree(float r)
- convertKGSToLBS(float kgs)
- convertDegreeToRadian(float d)
```
## Objects
Due to the nature of not being able to have multiple return types from the exact same method, I figured I would try something new and create objects for data and also for manifest stuff. There are currently two types of objects: **DataObject** and **ManifestObject**

```
DataObject - Used to store data retrived from the Connect API
   - Stores all data types, but only one will be accurate based on the API result
   - It can store a boolean, int, float, double, String, and long
   - to get the type of data it is storing, use: DataObject.getDataType() which will return a corresponding integer based off the Offical Infinite Flight Docs
   - to get the data stored within the object, use: DataObject.getData{INTEGER}() INTEGER being the corresponding data type stored
```
```
ManifestObject - Stores a single manifest entry in it
   - Stores Path(path), ID(id), and Data Type (type)
   - to get the path, use: ManifestObject.getPath()
   - to get the id, use: ManifestObject.getID()
   - to get the type, use: ManifestObject.getType()
```

## Some methods not in the example

```
getManifestObject(int index) 
    - This will return a ManifestObject from the ArrayList<ManifestObject> that was created on init
```
``` 
getManifestObjectFromPath(String path) 
    - If it exist, will return a ManifestObject with the corresponding path
```
```
getManifestObjectFromID(int id)
    - If it exist, will return a ManifestObject with the corresponding id
```
```
(Deprecated) getManifestObjectFromID(int type) 
    - If it exist, will return the first ManifestObject found with the corresponding type
```

## Endpoints that may need converting
Here are some of the endpoints I have found are easier to use when converted

| Endpoint        | Short Name           | Conversion  |
| ------------- |:-------------:| -----:|
| aircraft/0/indicated_airspeed      | IAS | m/s to knots |
| aircraft/0/vertical_speed      | VS      |  m/s to FPM |
| aircraft/0/heading_magnetic | Heading      |    radian to degree | 
| aircraft/0/oat | OAT | celcius to farenheight |

**Of course these are not all of them, just some examples**

## Extra info
So... Java does not, by default, send format in the [Little Endian](https://en.wikipedia.org/wiki/Endianness) format, which is what the Connect API **_requires_** to send and get data. To get around this, you have to do some low level stuff. To save some time, and keep me half-sain, I used a file from [here](https://gist.github.com/MichaelBeeu/6545110). Credit to the creator for the useful converter. The JSON for Java library was also not created by me. (The only need for it is to get the IP from the JSON returned in the UDP broadcast) <br>
- If you would like to contribute to this project, feel free to! 
- I will try my best to continue and update this when I feel needed 
- If you have any issues please don't hesitate to ask me on the [Infinite Flight Community](https://community.infiniteflight.com/u/onelettershor2/) or open an issue here on GitHub.

## Help
Message me on the [Infinite Flight Community](https://community.infiniteflight.com/u/onelettershor2/) or open an issue on GitHub

