# ECM2525 - Mobile & Ubiquitous Computing Coursework

- **Author:** Sam Townley
- **Version:** 1.0

## Introduction

For the project, I have designed an app called **TrainTrack UK** which displays information about any train service and railway station in the UK.

### Purpose

The purpose of the application is to provide a simple, easy to understand UI that members of the public can use to check the next departures from their local railway station. This could be to check on the status of a train they're about to catch, or to track a service that's arriving with a friend or family member when picking them up from the station. There is also in-built functionality to help the users get directions to the station if they're unfamiliar with the area.

### Functionality

When using the application, users can search for any station currently open in the UK, provided it is part of the National Rail network (so this doesn't include private/heritage railways, tram/metro networks or the London Underground). A map of all stations and routes can be found [here](https://assets.nationalrail.co.uk/e8xgegruud3g/41qqJtpU8TScvc8NlClu7f/e7ff46f8c428d112eef4bfc6b32a8a1f/National_Rail_Network_map_v39_Dec_23.pdf).

The user can then see the next 10* services at their chosen station, separated into departures and arrivals. From here, users can also access information about the station, which has the station address and a button to quickly get the fastest directions using Google maps. 

Clicking on a service will bring up more information, including a map of the journey made by the service, and its progress. This is perfect for tracking where your delayed train is or for checking that a service they are about to get on is going to stop at their desired station, and what time it gets there.

(*) *Due to API restrictions, services are only available for up to 2 hours from the current time, so there may be instances where there are less than 10 services available for quieter stations.*

### Using The Application

Everything needed to run the application should be present out of the box. For this submission, the `gradle.properties` file has been included with my personal API tokens.

If installing from the Github, users will need to provide the following API keys inside the `gradle.properties` file:

```properties
# Example gradle.properties file
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.nonTransitiveRClass=true

# Add your own API keys here
ArrDepAPIKey = <Key from https://raildata.org.uk/dataProduct/P-7c866984-8a7b-4272-a6f8-00b4aaf821fa/overview>
StationsAPIKey = <Key from https://raildata.org.uk/dataProduct/P-88ffe920-471c-4fd9-8e0d-95d5b9b7a257/overview>
```

These can be obtained by signing up for an account with the [Rail Data Marketplace](https://raildata.org.uk/), which is a free service run by National Rail.

## Design

### Layouts & Views

I settled on using the ConstraintLayout. This proved to be the most versatile in terms of layout, and since my views needed to display data of unknown size at runtime, the layout needed to be flexible. I have also used AutoText scaling in some TextViews to make sure that the text is scaled down if it's too large rather than overlapping with other elements.

### Data Storage

I am storing user data using SharedPreferences, due to its ease of use in Java. To store the data, I settled on a JSON format, stored as one string. This could then be parsed by a JSONObject, and since the size of the stored data is never about 8 entries, it won't slow down the device or application.

### Activities

#### [SelectStation](app/src/main/java/com/example/railinfo/SelectStationActivity.java)

This is the main launcher activity of the application. One of the challenges associated with this activity was implementing the AutoCompleteTextView. This is an editable text field with a dropdown menu where users can select their station. I decided to store the stations in JSON, under the assets directory, which is read when the application starts. Here, Computer Reservation Codes (or CRS for short) are mapped to the full names. Each CRS code identifies one station uniquely with 3 letters. I used the built-in ArrayAdapter for this, storing an ArrayList of Strings. This worked perfectly as I didn't want to extend any of the logic already implemented.

SelectStation also implements an options menu, with an option to clear the user's search history. Placing this option inside a menu allowed me to keep the UI simple and clean. It also allows me to expand the range of options later down the line, for example, adding an ability to toggle between light and dark mode.

#### [StationBoard](app/src/main/java/com/example/railinfo/StationBoardActivity.java)

StationBoard uses fragments to display the 2 different RecyclerViews on the same page. This allows me to separate the functionality, whilst making the transition between them seamless. The TabLayout provides a clean and intuitive method of switching between departures and arrivals. For navigating backwards, and to get more information about the station, I decided to use ImageButtons instead of icons on the Toolbar, since these are easier to implement. However, I would expand this to an action menu in the future.

#### [StationInfo](app/src/main/java/com/example/railinfo/StationInfoActivity.java)

This is the simplest activity. Its main feature is a button that opens a maps app on the phone, allowing the user to get directions to the station in one click. I had originally planned to include more content here, however, didn't have the time to design the layout of the data.

#### [ServiceInfo](app/src/main/java/com/example/railinfo/ServiceInfoActivity.java)

ServiceInfo is the biggest and most complex activity. It provides a detailed view of the service, using as much information as possible from the National Rail API. It implements a nice RecyclerView which draws a simple map of the journey of the service, showing the stations it's calling at, the times the service is due at each station, and its progress along the route. This activity can be used for both arrivals and departures.
