package com.smart.peepingbill.models;

import org.json.JSONObject;

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
     * Void sudo.
     */
    void voidSudo();

    String getNmapScanResponse();
}
