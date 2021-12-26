package com.smart.peepingbill.models;

import org.json.JSONObject;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.DeviceNodeImpl} class.
 */
public interface DeviceNode {

    /**
     * Get Smart Host Device data parsed as json-string.
     * @return {@link String}
     */
    String getSmartDeviceJsonString();

    /**
     * Get Smart Host Device data parsed as json.
     * @return {@link JSONObject}
     */
    JSONObject getSmartDeviceJsonObject();

    /**
     * Pretty Print Smart Host Device Json.
     */
    void prettyPrint();

    /**
     * Remove and null sudo key.
     */
    void voidSudo();

    /**
     * Get Device name.
     * @return {@link String}
     */
    String getDeviceName();

    /**
     * Get response from nmap os scan.
     * @return {@link String}
     */
    String getNmapScanResponse();
}
