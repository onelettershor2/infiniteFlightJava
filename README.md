# Infinite Flight Connect API - For Java
This repo will allow you to connect to the Infinite Flight Connect API (Connect API) in Java with ease.

## How to use in an Eclipse project
Implementing this into your project is quite simple!

1. Download the .jar file which will be needed to refrence this repo. **If you need help with this, view [this](#getting-a-jar-file)**
2. - Under **_Package Explorer_** right click the project you would like to use this in.
   - Hover ```Build Path -> Configure Build Path```
   - Under **_Libraries_** tab... ```Add External JARs```
   - Find this repo's .jar file
   - Press **_OK_** then your done!
   - Still need more help? Visit [this](https://www.tutorialspoint.com/eclipse/eclipse_java_build_path.htm) page for a little more explanation

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

### Endpoints that may need converting
Here are some of the endpoints I have found are easier to use when converted

| Endpoint        | Short Name           | Conversion  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |



### Getting a .jar file

