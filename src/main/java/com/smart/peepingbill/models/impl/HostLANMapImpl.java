package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.HostLANMap;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HostLANMapImpl implements HostLANMap {
    private static final Logger LOG = LoggerFactory.getLogger(HostLANMapImpl.class);

    private final String ipaddress;
    private final String hostname;
    private final String secretKey;
    private final String[] ipToHostArray;

    public HostLANMapImpl(String key) {
        ipaddress = NetworkUtil.getIpaddress();
        hostname = NetworkUtil.getHost();
        ipToHostArray = new String[]{ipaddress, hostname};
        secretKey = key;
    }

    @Override
    public String getIpAddress() {
        return this.ipaddress;
    }

    @Override
    public String getHostname() { return this.hostname; }

    @Override
    public String[] getIpToHostArray() { return ipToHostArray; }

    @Override
    public Map<String, String> getLANMacAddresses() {
        Map<String, String> macAddresses = new HashMap<>();

        try {
            Process p = Runtime.getRuntime().exec(new String[]{"sudo", "-S", "nmap", "-sn", ipaddress.concat("/24")});
            OutputStream outputStream = p.getOutputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(p.getInputStream());

            // get password in byte array and feed to prompt
            StringBuilder password = new StringBuilder(secretKey);
            Charset charset = StandardCharsets.US_ASCII;
            byte[] passwordByteArray = charset.encode(password.toString()).array();
            outputStream.write(passwordByteArray);
            outputStream.write('\n');
            outputStream.flush();

            // remove password for security reasons and continue to process.
            passwordByteArray = new byte['\0'];
            password.setLength(0);

            if (isInvalidPassword(passwordByteArray, password)) {
                System.out.println("password invalidated.");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                StringBuilder deviceMacAddress = new StringBuilder();
                StringBuilder deviceIpAddress = new StringBuilder();
                while ((line != null)) {
                    if (containsMacAddress(line)) {
                        deviceMacAddress.append(extractMacAddress(line));
                    }

                    if (containsIpAddress(line)) {
                        deviceIpAddress.append(extractIpAddress(line));
                    }

                    if (deviceMacAddress.length() > 1 && deviceIpAddress.length() > 1) {
                        macAddresses.put(deviceMacAddress.toString(), deviceIpAddress.toString());
                        deviceMacAddress.setLength(0);
                        deviceIpAddress.setLength(0);
                    }

                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
            } else {
                // password has not been invalidated. Security risk - log.
                System.out.println("Password has not been invalidated. Process logged, system process terminated.");
            }
        } catch (IOException e) {
            System.out.println("Could not process NMAP '-sn' command, " + e.getLocalizedMessage());
        }
        return pristine(macAddresses, getIPNetworkSegment(ipaddress));
    }

    private String extractMacAddress(String line) {
        if (line.length() > 30) {
            return line.substring(13, 30);
        }
        return null;
    }

    private String extractIpAddress(String line) {
        if (line.length() > 22) {
            return line.substring(21);
        }
        return null;
    }

    private String getIPNetworkSegment(String ip) {
        int count = 0, dotCount = 0;
        StringBuilder partialIp = new StringBuilder();
        char current = ip.charAt(count);
        while (dotCount < 3) {
            dotCount = current == '.' ? dotCount + 1 : dotCount;
            if (dotCount == 3) {
                break;
            }
            partialIp.append(current);
            count++;
            current = ip.charAt(count);
        }
        return partialIp.toString();
    }

    public String extractIpFromNetworkSegment(String segment, String potentialIp) {
        Pattern segmentPattern = Pattern.compile("(" + segment + "\\.\\d{1,3}\\b)");
        Matcher ipMatcher = segmentPattern.matcher(potentialIp);
        if (ipMatcher.find()) {
            return ipMatcher.group(1);
        }
        return null;
    }

    private Map<String, String> pristine(Map<String, String> addresses, String segment) {
        addresses.entrySet().forEach(mac -> {
            String ip = mac.getValue();
            mac.setValue(extractIpFromNetworkSegment(segment, ip));
        });
        return addresses;
    }

    private boolean containsMacAddress(String str) {
        return str.contains(PeepingConstants.MAC_ADDRESS_1);
    }

    private boolean containsIpAddress(String str) {
        return str.contains(PeepingConstants.NMAP_SCAN_REPORT) && !str.contains(PeepingConstants.GATEWAY);
    }

    private boolean isInvalidPassword(byte[] passwordByteArray, StringBuilder password) {
        return passwordByteArray.length == 0 && password.length() == 0;
    }
}