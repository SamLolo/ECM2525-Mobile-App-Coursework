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

```text
ArrDepAPIKey = <Key from https://raildata.org.uk/dataProduct/P-7c866984-8a7b-4272-a6f8-00b4aaf821fa/overview>
StationsAPIKey = <Key from https://raildata.org.uk/dataProduct/P-88ffe920-471c-4fd9-8e0d-95d5b9b7a257/overview>
```

These can be obtained by signing up for an account with the [Rail Data Marketplace](https://raildata.org.uk/), which is a free service run by National Rail.
