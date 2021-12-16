package com.smart.peepingbill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class NetworkUtil {
    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtil.class);


    public static String getIpaddress() {
        String ipaddress = null;
        try {
            ipaddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Error requesting local host ipaddress: {}", e.getMessage());
        }
        return ipaddress;
    }

    public static String getHost() {
        String host = null;
        try {
            host = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("Error requesting local host name: {}", e.getMessage());
        }
        return host;
    }
}
