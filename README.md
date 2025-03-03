[![GitHub version](https://badge.fury.io/gh/kyosho-%2Fcordova-plugin-usb-event.svg)](https://badge.fury.io/gh/kyosho-%2Fcordova-plugin-usb-event)
[![npm version](https://badge.fury.io/js/cordova-plugin-usb-event.svg)](https://badge.fury.io/js/cordova-plugin-usb-event)
![GitHub](https://img.shields.io/github/license/kyosho-/cordova-plugin-usb-event)
[![GitHub issues](https://img.shields.io/github/issues/kyosho-/cordova-plugin-usb-event)](https://github.com/kyosho-/cordova-plugin-usb-event/issues)
[![GitHub forks](https://img.shields.io/github/forks/kyosho-/cordova-plugin-usb-event)](https://github.com/kyosho-/cordova-plugin-usb-event/network)
[![GitHub stars](https://img.shields.io/github/stars/kyosho-/cordova-plugin-usb-event)](https://github.com/kyosho-/cordova-plugin-usb-event/stargazers)

# cordova-plugin-usb-event
This plugin can handle USB attached and detached events on cordova-android.
No special permission settings are required to handle the events.

This plugin can search at [Corova Plugin Search](https://cordova.apache.org/plugins/?q=cordova-plugin-usb-event).
And it is registered at [npmjs](https://www.npmjs.com/package/cordova-plugin-usb-event).

## Install

```
$ cordova plugin add https://github.com/amanuppalhp/cordova-plugin-usb-event2
```

## Usage

* listDevices
* registerEventCallback
* unregisterEventCallback
* existsRegisteredCallback

### List USB devices

`listDevices` method get current connected USB device list.

```js
cordova.plugins.usbevent.listDevices(
      function(list) {
        console.log(list);
      },
      function(error) {
        console.log(error);
      });
```

The following result is output.
USB device information has vendor ID and product ID.

```json
{
  "id": "list",
  "devices": [
    {
      "vendorId": 1234,
      "productId": 4321
    }
  ]
}
```

You can set a filter if you need a specific USB device.

```js
var filter = {
  id: 'include',
  devices: [
    {
      vendorId: 1234,
      productId: 4321
    }
  ]
};
cordova.plugins.usbevent.listDevices(
      function(list) {
        console.log(list);
      },
      function(error) {
        console.log(error);
      },
      filter);
```

### Handle USB attached and detached event

`registerEventCallback` method can get USB device information on attached and detached.

```js
cordova.plugins.usbevent.registerEventCallback(
      function(result) {
        console.log(result);
      },
      function(error) {
        console.log(error);
      });
```

The following result is output.
First, the following result is returned after method call success.

```json
{
  "id": "callbackRegistered"
}
```

Next, each result is returned on attaching an detaching USB.

```json
{
  "id": "attached",
  "devices": [
    {
      "vendorId": 1234,
      "productId": 4321
    }
  ]
}
```

```json
{
  "id": "detached",
  "devices": [
    {
      "vendorId": 1234,
      "productId": 4321
    }
  ]
}
```

You can set a filter if you need a specific USB device.

```js
var filter = {
  id: 'include',
  devices: [
    {
      vendorId: 1234,
      productId: 4321
    }
  ]
};
cordova.plugins.usbevent.registerEventCallback(
      function(result) {
        console.log(result);
      },
      function(error) {
        console.log(error);
      },
      filter);
```

Notice: This plugin can register single callback. The method overrite previous registered callback.

### Unandle USB attached and detached event

`unregisterEventCallback` method unregister event callback.

```js
cordova.plugins.usbevent.unregisterEventCallback(
      function(result) {
        console.log(result);
      },
      function(error) {
        console.log(error);
      });
```

The following result is returned.

```json
{
  "id": "callbackUnregistered"
}
```

### Check callback registered

`existsRegisteredCallback` check exits callback.
The method return true if exits callback.

```js
cordova.plugins.usbevent.existsRegisteredCallback(
      function(result) {
        console.log(result); // true if exists.
      },
      function(error) {
        console.log(error);
      });
```

### Common output object definition

Returned data definition is that.

```ts
// Typescript data definitions.

/**
 * Event ID
 */
enum UsbEventId {
    List = 'list',
    Registered = 'callbackRegistered',
    Unregistered = 'callbackUnregistered',
    Attached = 'attached',
    Detached = 'detached'
}

/**
 * USB device information
 * 
 * Information may be added more.
 */
interface UsbDevice {
    vendorId: number;
    productId: number;
}

/**
 * Output by method call.
 */
interface UsbResult {
    id: UsbEventId;
    devices?: UsbDevice[];
}
```

## Example

### listDevices

```ts
// Typescript
cordova.plugins.usbevent.listDevices(
  (result: UsbResult) => {
    console.log(result.id);
    for(const device in result.devices) {
      console.log(device.vendorId);
      console.log(device.productId);
    }
  },
  (error: string) => {
    console.log(error);
  });
```

### registerEventCallback

```ts
// Typescript
cordova.plugins.usbevent.registerEventCallback(
  (result: UsbResult) => {
    switch (result.id) {
      case 'callbackRegistered':
        console.log(`register is success.`);
        break;
      case 'attached':
        console.log(`USB device is attached.`);
        for(const device in result.devices) {
          console.log(device.vendorId);
          console.log(device.productId);
        }
        break;
      case 'detached':
        console.log(`USB device is detached.`);
        for(const device in result.devices) {
          console.log(device.vendorId);
          console.log(device.productId);
        }
        break;
      default:
        console.log(`Unsupported event. (event=${JSON.stringify(result)})`);
    }
  },
  (error: string) => {
    console.log(error);
  });
```

### unregisterEventCallback

```ts
// Typescript
cordova.plugins.usbevent.unregisterEventCallback(
  (result: UsbResult) => {
    switch (result.id) {
      case 'callbackUnregistered':
        console.log(result);
        break;
      default:
        console.log(`Unsupported event. (event=${JSON.stringify(result)})`);
    }
  },
  (error: string) => {
    console.log(error);
  });
```

### existsRegisteredCallback

```ts
// Typescript
cordova.plugins.usbevent.existsRegisteredCallback(
  (exists: boolean) => {
    console.log(exists); // true if exists.
  },
  (error: any) => {
    console.log(error);
  });
```

## UML - Sequence Diagram

![sequence diagram](./doc/sequence(cordova-plugin-usb-event).png)

# License - MIT

```
MIT License

Copyright (c) 2019 Akira Kurosawa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
