# Semper Codex
***
## Introduction
***
Information, data, and systems are connected more today than ever, but the data itself is still very much the star
of the show. Data tells stories and gives answers. It can also be used in malicious ways. Computer users, both 
beginners and advanced, should be aware of their data that flows through even the smallest of networks. Semper 
Codex helps with this by analyzing, securing and making your data available in ways us mere mortals can and will
understand. Common representations of your Local Area Network are broken down by device, collecting important 
information for each device, and reported to you through visual and digital representations. This alone tells 
you many things about your network such as, who's on it, what's on it, are there vulnerabilities in the devices
software, is the device manufacturer still in business, and so on. 

JSON has become one of the most used structures for computer information interchange. It's easily transferable, 
extensible, and understandable. That's why we use it. Luckily, Java gives many superior options for 
JSON use and storage. Semper Codex mainly uses this [JSONObject](https://www.javadoc.io/doc/org.json/json/latest/org/json/class-use/JSONObject.html).
Why? It's a well documented, supported, and known Java class. There may be some features unavailable in this class, 
and in those cases Semper Codex tries to add such convenience operations. 

What you do with your data is your own choice. This application will never store, give away or share your data. 
What we do with your data is this - we make it easily accessible and understandable for you. We provide you 
digital representation committing to the heavy lifting, so to speak, of your own network data and its devices. 
How? We provide features such as saving the JSON representation of your data locally in .json, .txt, .pdf formats, 
a user interface which paints a visual representation of your network using the famous [JavaFx Application Platform](https://openjfx.io/), 
sharing your network and its devices' data in real time - only accessible to you locally. 

### System Network JSON Example
***
The following is an example of your LAN and its devices' data represented as JSON: 
```json
{
  "hostSmartNode": {
    "smartDeviceNodeDetails": {
      "hostName": "hostname",
      "macAddress": "00:00:00:00:00:00",
      "externalIpAddress": "11.123.111.123",
      "smartDeviceNodeType": "host",
      "localIpAddress": "00.0.0.49"
    },
    "smartDeviceMacAddressDetails": {
      "borderLeft": "D8EB46000000",
      "dateCreated": "2021-01-12",
      "vendorCountryCode": "US",
      "vendorIsPrivate": "false",
      "borderRight": "D8EB46FFFFFF",
      "vendorCompanyName": "Google, Inc",
      "vendorCompanyAddress": "1600 Amphitheatre Parkway Mountain View CA 94043 US",
      "blockFound": "true",
      "assignmentBlockSize": "MA-L",
      "vendorOui": "D8EB46",
      "blockSize": "16777216",
      "dateUpdated": "2021-01-12"
    },
    "smartDeviceDeepNetProperties": {
      "smartDeviceCity": "San Francisco",
      "isOnProxy": true,
      "isOnHost": false,
      "smartDeviceIsp": "Comcast Cable Communications, LLC",
      "isOnMobile": false,
      "smartDeviceRegion": "CA",
      "smartDeviceZip": "94016"
    },
    "smartDeviceSystemProperties": {
      "smartDeviceArchitecture": "amd64",
      "smartDeviceLanguage": "en",
      "smartDeviceUserCountry": "US",
      "smartDeviceVersion": "5.11.0-44-generic",
      "smartDeviceOperatingSystem": "Linux"
    }
  },
  "deviceSmartNode0": {
    "smartDeviceNodeDetails": {
      "macAddress": "00:00:00:00:00:01",
      "smartDeviceNodeType": "client",
      "localIpAddress": "00.0.0.124"
    },
    "smartDeviceMacAddressDetails": {
      "borderLeft": "24A160000000",
      "dateCreated": "2020-05-14",
      "vendorCountryCode": "CN",
      "vendorIsPrivate": "false",
      "borderRight": "24A160FFFFFF",
      "vendorCompanyName": "Espressif Inc",
      "vendorCompanyAddress": "Room 204, Building 2, 690 Bibo Rd, Pudong New Area Shanghai Shanghai 201203 CN",
      "blockFound": "true",
      "assignmentBlockSize": "MA-L",
      "vendorOui": "24A160",
      "blockSize": "16777216",
      "dateUpdated": "2020-05-14"
    }
  },
  "deviceSmartNode1": {
    "smartDeviceNodeDetails": {
      "macAddress": "00:00:00:00:00:02",
      "smartDeviceNodeType": "client",
      "localIpAddress": "00.0.0.218"
    },
    "smartDeviceMacAddressDetails": {
      "borderLeft": "102B41000000",
      "dateCreated": "2020-12-22",
      "vendorCountryCode": "KR",
      "vendorIsPrivate": "false",
      "borderRight": "102B41FFFFFF",
      "vendorCompanyName": "Samsung Electronics Co, Ltd",
      "vendorCompanyAddress": "129, Samsung-ro, Youngtongl-Gu Suwon Gyeonggi-Do 16677 KR",
      "blockFound": "true",
      "assignmentBlockSize": "MA-L",
      "vendorOui": "102B41",
      "blockSize": "16777216",
      "dateUpdated": "2020-12-22"
    }
  }
}
```
