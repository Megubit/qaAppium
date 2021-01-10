## QA Task
This repository contains a simple test for RoboKiller Anroid version. iOS version was not made due to not owning an Mac, but since [Appium](http://appium.io/) is a multi platform framework, it is easy to extend the current test to support iOS devices by adding an iOS WebDriver (e.g. [UIAutomation](http://appium.io/docs/en/drivers/ios-uiautomation/index.html))

### Test description
* Installs RoboKiller Anroid version from this repository
* Enables call filtering for numbers not on contact list
* Simulates phone call
* Checks if previous call was blocked
* Adds previous number to contacts
* Retries call and check if call was blocked
* Removes number from contacts

## Tools/Frameworks used
As mentioned previously, this test uses [Appium](http://appium.io/) as the main testing framework. Appium is used because it's one of the top open-source frameworks for mobile test automation, it supports both Android and iOS devices, has a well written documentation and multiple programming languages supported (e.g. Java, Python).

For this test specifically, Appium desktop was used because of it's ease of use and setup, but for a more advanced setup for CI integration, `npm` version should be used instead.

###  UiAutomator2 and Junit5
There is no specific reason for using UiAutomator2 and Junit5 besides that the tutorial I watched was using the same. In addition UiAutomator2 is supported by Firebase.

### IntelliJ IDEA (Community Edition)
Easy to use IDE with out of the box support for all tools needed to run Appium tests.
 
## Requirements for the project
* Android SDK 28+
* Java 8+ 
* Android emulator (real device does not support appium GSM calls)
  * Android version 11 

## What was not done and why
The given time limit was enough to complete the task even with the "extra credit", but with all the events that were happening, available time I had at my hands was limited.

* Multiple functional tests
  * Only one test was written because any others would be done on the same manner, but just selecting a different filter option 
* Unit test
  * I did lookup how to write one, and I would have used JUnit5 in same manner as the functional one
* Integration tests/Screenshot tests
  * I did not lookup  
* Firebase Test Lab usage
  * Made only minor check, of possible options on how to setup project with firebase
