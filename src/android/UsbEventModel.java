package net.kyosho.usb.event;

import android.hardware.usb.UsbDevice;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class UsbEventModel {

  /**
   * Output event value property keys.
   */
  static final String PROPERTY_EVENT_KEY_DEVICE_LIST = "devices";
  static final String PROPERTY_EVENT_KEY_ID = "id";
  static final String PROPERTY_EVENT_KEY_VID = "vendorId";
  static final String PROPERTY_EVENT_KEY_PID = "productId";
  public static final String PROPERTY_EVENT_KEY_DEVICE_ID = "deviceId";
  public static final String PROPERTY_EVENT_KEY_DEVICE_NAME = "deviceName";
  public static final String PROPERTY_EVENT_KEY_PROTOCOL = "protocol";
  public static final String PROPERTY_EVENT_KEY_MANUFACTURER = "manufacturer";
  public static final String PROPERTY_EVENT_KEY_PRODUCT = "product";
  public static final String PROPERTY_EVENT_KEY_SERIAL = "serial";
  public static final String PROPERTY_EVENT_KEY_VERSION = "version";

  private UsbEventId id;

  private Map<String, UsbDevice> deviceMap;

  UsbEventModel(final UsbEventId id, final Map<String, UsbDevice> deviceMap) {
    if (null == id) {
      throw new NullPointerException("id is null.");
    }

    if (null == deviceMap) {
      throw new NullPointerException("deviceMap is null.");
    }

    this.id = id;
    this.deviceMap = deviceMap;
  }

  UsbEventModel(final UsbEventId id, final UsbDevice device) {
    if (null == id) {
      throw new NullPointerException("id is null.");
    }

    if (null == device) {
      throw new NullPointerException("device is null.");
    }

    this.id = id;

    HashMap<String, UsbDevice> deviceMap = new HashMap<>();
    deviceMap.put(device.getDeviceName(), device);
    this.deviceMap = deviceMap;
  }

  UsbEventModel(final UsbEventId id) {
    if (null == id) {
      throw new NullPointerException("id is null.");
    }

    this.id = id;
    this.deviceMap = new HashMap<>();
  }

  JSONObject toJSONObject() throws JSONException {
    return this.toJSONObject(null);
  }

  JSONObject toJSONObject(final IncludeFilter filter) throws JSONException {
    List<UsbDevice> deviceList = new ArrayList<>(this.deviceMap.values());
    List<UsbDevice> filteredList = new ArrayList<>();

    if (null != filter) {
      // filter
      for (UsbDevice device : deviceList) {
        UsbDevice filtered = filter.filter(device);
        if (null == filtered) {
          continue;
        }
        filteredList.add(filtered);
      }
    } else {
      // no filter
      filteredList = deviceList;
    }

    // create JSON object
    JSONObject jsonObject = new JSONObject();
    jsonObject.put(PROPERTY_EVENT_KEY_ID, this.id.toString());

    if (0 < filteredList.size()) {
      JSONArray jsonArrayObject = new JSONArray();

      for (UsbDevice device : filteredList) {
        JSONObject jsonDevice = new JSONObject();
        jsonDevice.put(PROPERTY_EVENT_KEY_VID, device.getVendorId());
        jsonDevice.put(PROPERTY_EVENT_KEY_PID, device.getProductId());
        jsonDevice.put(PROPERTY_EVENT_KEY_DEVICE_ID, device.getDeviceId());
        jsonDevice.put(PROPERTY_EVENT_KEY_DEVICE_NAME, device.getDeviceName());
        jsonDevice.put(PROPERTY_EVENT_KEY_PROTOCOL, device.getDeviceProtocol());
        jsonDevice.put(PROPERTY_EVENT_KEY_PRODUCT, device.getProductName());
        jsonDevice.put(PROPERTY_EVENT_KEY_SERIAL, device.getSerialNumber());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          jsonDevice.put(PROPERTY_EVENT_KEY_VERSION, device.getVersion());
        }
        else{
          jsonDevice.put(PROPERTY_EVENT_KEY_VERSION, "Unknown");
        }
        jsonDevice.put(PROPERTY_EVENT_KEY_MANUFACTURER, device.getManufacturerName());
        jsonArrayObject.put(jsonDevice);
      }
      jsonObject.put(PROPERTY_EVENT_KEY_DEVICE_LIST, jsonArrayObject);
    }

    return jsonObject;
  }
}
