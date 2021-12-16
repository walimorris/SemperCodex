package com.smart.peepingbill.models;

import java.util.Map;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.HostLANMapImpl} class.
 */
public interface HostLANMap {

    /**
     * Get Host Ip Address.
     * @return {@link String}
     */
    String getIpAddress();

    /**
     * Get Addresses of all devices in host Local Area Network.
     * @return {@link Map} key: mac address value: ip address
     */
    Map<String, String> getLANMacAddresses();
}
