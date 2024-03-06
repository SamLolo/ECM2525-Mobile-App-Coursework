# ECM2525 - Mobile & Ubiquitous Computing Coursework

- **Author:** Sam Townley
- **Version:** 1.0

## Introduction

For the project, I have designed an app called **TrainTrack UK** which displays information about any train service and railway station in the UK.

### Purpose

The purpose of the application is to provide a simple, easy to understand UI that members of the general public can use to check the next departures from their local railway station. This could be to check on the status of a train they're about to catch, or to track a service that's arriving with a friend or family member when picking them up from the station. There is also in-built functionality to help the users get directions to the station if they're unfamiliar with the area.

### Functionality

When using the application, users have the ability to search for any station currently open in the UK, provided it is part of the National Rail network (so this doesn't include private/heritage railways or seperate transport entities, such as tram/metro networks and the London Underground). A map of all stations and routes available can be found [here](https://assets.nationalrail.co.uk/e8xgegruud3g/41qqJtpU8TScvc8NlClu7f/e7ff46f8c428d112eef4bfc6b32a8a1f/National_Rail_Network_map_v39_Dec_23.pdf).

The user will then be displayed a view of the next 10* services to leave and depart their chosen station. These are seperated into departures and arrivals tabs, allowing the user to get a clear view of the next services regardless of their use case. From this screen, users can access information about the station, which has the station address and a button to quickly get the fastest directions using Google maps. 

Clicking on a specific service will bring up more information about that service, including a handy map of the journey made by the service, as well as it's progress along that journey. This is perfect for tracking where your delayed train is when you're stood at the station waiting, and can also be used by users that want to check that the service they are about to get on is going to stop at their desired station, and get their estimated arrival time at that station.

(*) *Due to API restrictions, arrivals and departures are only availble for up to 2 hours from the current time, so there may be instances where there are less than 10 services availble for quieter stations.*

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

For all activities, I settled on using the ConstraintLayout. This proved to be the most versatile in terms of layout, allowing me flexibility in how data is displayed based on the size of the data. Since my views needed to display a range of data of unknown size at runtime, this was especially important, and I have used AutoText scaling in some TextViews to make sure that the text is scaled down if it's too large for it's box rather than spilling over into other elements.

### Data Storage

For my app, I am storing user data using SharedPreferences, due to how easy it is to use in Java. To store the data, I settled on a JSON format, stored in SharedPreferences as one string. This made it easy to parse through the use of a JSONObject, and since the size of the stored data is never about 8 entries, it won't slow down the device or application by getting too large.

---

My application design consists of 4 Activities. Below is some more information about the design choices made whilst implementing each activity:

### [SelectStation](app/src/main/java/SelectStationActivity.java)

This is the main launcher activity of the application, and provides the "home page". Here, users can search for their desired station or select one of their previously searched stations for quicker access. One of the challenges associated with this activity was implementing the AutoCompleteTextView. This is an editable text field with a drop down menu where users can select their chosen station. I decided to store the stations in JSON, under the assets directory, which is loaded when the application starts. Here, Computer Reservation Codes (or CRS for short) are mapped to their respective full station names. Each CRS code is unqiue and identifies one station through the use of 3 capital letters. These are the same codes you will see on standard railway tickets. For providing the names to the AutoCompleteTextView, I used an inbuilt ArrayAdapter storing an ArrayList of Strings. This worked perfectly for what I needed as I didn't want to extend any of the logic implemented by the ArrayAdapter already.

SelectStation also implements an options menu, with an option to clear the user's search history. Placing this option inside a menu allowed me to keep the UI simple and clean. It also allows me to expand the range of options later down the line, for example, adding an ability to toggle betwene light and dark mode.

### [StationBoard](app/src/main/java/StationBoardActivity.java)

StationBoard uses fragments to display the 2 different RecyclerView's on the same page. This allows me to seperate the functionality, whislt making the transition between the seemless to the user. The TabLayout provides a clean and intuitive method of switching between departures and arrivals. For navigating backwards, and to get more information about the station, I decided to use ImageButtons instead of icons on the Toolbar, since these are easier to implement. However, I would expand this to an action menu in the future.

### [StationInfo](app/src/main/java/StationInfoActivity.java)

StationInfo is the simplest of the activities. It's main feature is a button that opens a maps app on the phone, allowing the user to get directions to the station in one click. I had originally planned to include more content here, however, didn't have the time to design the layout of the data.

### [ServiceInfo](app/src/main/java/ServiceInfoActivity.java)

ServiceInfo is the biggest and also most complex activity within the app. It provides a detailed view of the service, using as much information as possible from the National Rail API. It implements a nice RecyclerView which draws a simple map of the journey of the service, showing the stations it's calling at, the times the service is due at each station, and it's progress along the route. This activity can be used for both arrivals and departures.
