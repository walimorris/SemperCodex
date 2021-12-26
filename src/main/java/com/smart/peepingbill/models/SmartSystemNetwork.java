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
}
