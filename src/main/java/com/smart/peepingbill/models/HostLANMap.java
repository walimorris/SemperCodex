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
     * Get Host Name.
     * @return {@link String}
     */
    String getHostname();

    /**
     * Get {@link Map} containing ipaddress and host name.
     * @return {@link Map}
     */
    String[] getIpToHostArray();

    /**
     * Get Addresses of all devices in host Local Area Network.
     * @return {@link Map} key: mac address value: ip address
     */
    Map<String, String> getLANMacAddresses();
}
