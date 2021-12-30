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

    JSONObject getSmartSystemHostNode();

    JSONObject getSmartSystemHostNodeDetails();

    String getSmartSystemHostName();

    String getSmartSystemHostMacAddress();

    String getSmartSystemHostExternalIpaddress();

    String getSmartSystemHostLocalIpaddress();

    JSONObject getSmartSystemDeviceNode(int device);

    JSONObject getSmartSystemDeviceNodeDetails(int device);

    String getSmartSystemDeviceNodeMacAddress(int device);

    String getSmartSystemDeviceNodeLocalIpAddress(int device);
}
