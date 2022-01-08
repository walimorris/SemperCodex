package com.smart.peepingbill.models;

import org.json.JSONObject;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.SmartSystemNetworkImpl} class.
 */
public interface SmartSystemNetwork {

    /**
     * Returns the local area network, its devices and data as a {@link JSONObject}.
     * @return {@link JSONObject}
     */
    JSONObject getSmartSystemJSON();

    /**
     * Get SmartSystem Host Node json data.
     * @return {@link JSONObject}
     */
    JSONObject getSmartSystemHostNode();

    /**
     * Get SmartSystem Host Node json details section data.
     * @return {@link JSONObject}
     */
    JSONObject getSmartSystemHostNodeDetails();

    /**
     * Get Host Name.
     * @return {@link String}
     */
    String getSmartSystemHostName();

    /**
     * Get Host Mac Address.
     * @return {@link String}
     */
    String getSmartSystemHostMacAddress();

    /**
     * Get Host External IP Address.
     * @return {@link String}
     */
    String getSmartSystemHostExternalIpaddress();

    /**
     * Get Host Local IP Address.
     * @return {@link String}
     */
    String getSmartSystemHostLocalIpaddress();

    /**
     * Get system device at index from system json data structure.
     * @param device index
     * @return {@link JSONObject}
     */
    JSONObject getSmartSystemDeviceNode(int device);

    /**
     * Get system device details data from system json data structure.
     * @param device index
     * @return {@link JSONObject}
     */
    JSONObject getSmartSystemDeviceNodeDetails(int device);

    /**
     * Get system device mac address.
     * @param device index
     * @return {@link String}
     */
    String getSmartSystemDeviceNodeMacAddress(int device);

    /**
     * Get system device local ip address.
     * @param device index
     * @return {@link String}
     */
    String getSmartSystemDeviceNodeLocalIpAddress(int device);
}
