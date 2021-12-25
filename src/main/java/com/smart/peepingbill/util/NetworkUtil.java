package com.smart.peepingbill.util;

import com.smart.peepingbill.util.constants.PeepingConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * Defines the code for {@code com/smart/peepingbill/util/NetworkUtil.java}. The NetworkUtil consists
 * of various operations used for receiving, passing or commanding network data. Contains helper
 * functions that can be used throughout the application code base to ease the use of network
 * functionality. A simple use case is utilizing the java.net {@link Inet4Address} package to obtain
 * network host and ip-address.
 * </p>
 *
 * @author Wali Morris<walimmorris@gmaill.com>
 * created on 2021/12/24
 */
public class NetworkUtil {
    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtil.class);

    // These partial strings will be compared to each buffered line from nmap os-scan response
    private static final String[] nmapResponsePartials = {PeepingConstants.NMAP_AGGRESSIVE_OS_GUESSES, PeepingConstants.NMAP_WARNING,
            PeepingConstants.NMAP_TOO_MANY_FINGERPRINTS_MATCH, PeepingConstants.NMAP_OS_DETAILS, PeepingConstants.NMAP_RUNNING,
            PeepingConstants.MAC_ADDRESS_1};

    // if true, read nmap lines that reports an open port
    private static boolean readOpenPorts = false;

    /**
     * Get host ip-address.
     * @return {@link String}
     * @see    Inet4Address#getHostAddress()
     */
    public static String getIpaddress() {
        String ipaddress = null;
        try {
            ipaddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Error requesting local host ipaddress: {}", e.getMessage());
        }
        return ipaddress;
    }

    /**
     * Get host name.
     * @return {@link String}
     * @see    Inet4Address#getHostName()
     */
    public static String getHost() {
        String host = null;
        try {
            host = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("Error requesting local host name: {}", e.getMessage());
        }
        return host;
    }

    /**
     * <p>
     * Runs nmap command with host ipaddress to scan all devices on local area network. Builds
     * a {@link Map} containing key:mac-address to value:ip-address.
     * @param key sudo secret key
     * @return    {@link Map}
     *
     * @see      #writeDeviceMacAndIpAddresses(BufferedReader)
     */
    @NotNull
    public static Map<String, String> getLanDeviceMacAndIpAddresses(String key) {
        String ipaddress = getIpaddress();
        BufferedReader bufferedReader = spawnProcess(PeepingConstants.NMAP_DEVICE_SCAN_FOR_MAC_ADDRESSES +
                " " + ipaddress.concat("/24"), key);
        return writeDeviceMacAndIpAddresses(bufferedReader);
    }

    /**
     * <p>
     * Runs nmap command with given local area network device ipaddress and returns {@link String}
     * response containing device operating system information.
     * </p>
     * @param address ip-address of device
     * @param key     sudo secret key
     * @return        {@link String}
     * @see           #writeDeviceScanForOsResponse(BufferedReader, String)
     */
    @NotNull
    public static String scanDeviceForOS(String address, String key) {
        BufferedReader bufferedReader = spawnProcess(PeepingConstants.NMAP_DEVICE_SCAN_FOR_OS +
                " " + address, key);
        return writeDeviceScanForOsResponse(bufferedReader, address).toString();
    }

    /**
     * <p>
     * Returns the network segment from the given ip-address. That is, the first two 8-bit parts
     * of an ip-address containing four 8-bit parts.
     * </p>
     * @param ip ip-address
     * @return   {@link String}
     */
    @NotNull
    private static String getIPNetworkSegment(String ip) {
        int count = 0, dotCount = 0;
        StringBuilder partialIp = new StringBuilder();
        char current = ip.charAt(count);
        while (dotCount < 2) {
            dotCount = current == '.' ? dotCount + 1 : dotCount;
            if (dotCount == 2) {
                break;
            }
            partialIp.append(current);
            count++;
            current = ip.charAt(count);
        }
        return partialIp.toString();
    }

    /**
     * <p>
     * Spawns process for given command and creates a {@link BufferedReader} to read in and
     * process the returned response from given command.
     * </p>
     * @param cmd given command
     * @param key sudo pass
     * @return    {@link BufferedReader}
     * @see       #isInvalidKey(byte[], StringBuilder)
     */
    private static BufferedReader spawnProcess(String cmd, String key) {
        BufferedReader bufferedReader = null;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            OutputStream outputStream = process.getOutputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());

            // get pw in byte array and feed to prompt
            StringBuilder secretKey = new StringBuilder(key);
            Charset charset = StandardCharsets.US_ASCII;
            byte[] secretKeyByteArray = charset.encode(secretKey.toString()).array();
            outputStream.write(secretKeyByteArray);
            outputStream.write('\n');
            outputStream.flush();

            //empty password
            secretKeyByteArray = new byte['\0'];
            secretKey.setLength(0);

            if (isInvalidKey(secretKeyByteArray, secretKey)) {
                LOG.info("Secret Key rendered void and useless");
                bufferedReader = new BufferedReader(inputStreamReader);
            } else {
                LOG.info("Password has not been rendered useless, re-trying. Process logged");
            }
        } catch (IOException e) {
            LOG.error("Error establishing BufferedReader for cmd process: {}", e.getMessage());
        }
        return bufferedReader;
    }

    /**
     * Returns if key is valid or not.
     * @param keyByteArray  key byte array
     * @param key           {@link String key}
     * @return              boolean
     */
    private static boolean isInvalidKey(byte[] keyByteArray, StringBuilder key) {
        return keyByteArray.length == 0 && key.length() == 0;
    }

    /**
     * Iterates through the response from nmap os scan and returns key data from each line.
     * Lines that contain key operating system data is appended to the final response
     * {@link StringBuilder} data from the nmap os scan.
     * @param bufferedReader {@link BufferedReader} contains the buffered response from nmap os scan
     * @param address        the ip-address being scanned
     * @return               {@link StringBuilder} response containing key operating system data
     * @see                  #appendNmapOSScanValue(String, StringBuilder)
     */
    @NotNull
    private static StringBuilder writeDeviceScanForOsResponse(@NotNull BufferedReader bufferedReader, String address) {
        StringBuilder response = new StringBuilder(address + "\n");
        try {
            String line = bufferedReader.readLine();
            while((line != null)) {
                if (line.contains(PeepingConstants.NMAP_SCANNING)) {
                    if (line.contains(getHost())) {
                        response.append(PeepingConstants.NMAP_HOST_OS_SCAN_NOT_NECESSARY).append("\n");
                        break;
                    }
                }
                appendNmapOSScanValue(line, response);
                line = bufferedReader.readLine();
            }
            //cleanup
            bufferedReader.close();
            readOpenPorts = false;
            response.append("/Scan terminated/");
        } catch(IOException e) {
            LOG.error("Error building response from nmap scan for device os type: {}", e.getMessage());
        }
        return response;
    }

    /**
     * Writes mac-addresses and ip-addresses to addresses map based on each line of the {@link BufferedReader}
     * containing special keywords depicting the presence of either a device mac-address or ip-address.
     * @param bufferedReader {@link BufferedReader}
     * @return               {@link Map}
     * @see                  #extractMacAddressFromNmapLine(String)
     * @see                  #extractIpAddressFromNmapLine(String)
     */
    @NotNull
    private static Map<String, String> writeDeviceMacAndIpAddresses(@NotNull BufferedReader bufferedReader) {
        Map<String, String> addresses = new HashMap<>();
        try {
            String line = bufferedReader.readLine();
            StringBuilder deviceMacAddress = new StringBuilder();
            StringBuilder deviceIpAddress = new StringBuilder();

            while ((line != null)) {
                if (line.contains(PeepingConstants.MAC_ADDRESS_1)) {
                    deviceMacAddress.append(extractMacAddressFromNmapLine(line));
                }

                if (line.contains(PeepingConstants.NMAP_SCAN_REPORT) && !line.contains(PeepingConstants.GATEWAY)) {
                    deviceIpAddress.append(extractIpAddressFromNmapLine(line));
                }

                if (deviceMacAddress.length() > 1 && deviceIpAddress.length() > 1) {
                    addresses.put(deviceMacAddress.toString(), deviceIpAddress.toString());
                    deviceMacAddress.setLength(0);
                    deviceIpAddress.setLength(0);
                }

                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            LOG.error("Error building mac to ip address map: {}", e.getMessage());
        }
        return addresses;
    }

    /**
     * Extracts mac address from given line from nmap command response.
     * @param line line from nmap response
     * @return     {@link String}
     */
    @Nullable
    private static String extractMacAddressFromNmapLine(@NotNull String line) {
        if (line.length() > 30) {
            return line.substring(13, 30);
        }
        return null;
    }

    /**
     * Extracts ip-address from given line from nmap command response.
     * Returns the host ip-address if the line contains hostname.
     * @param line line from nmap response
     * @return     {@link String}
     */
    @NotNull
    private static String extractIpAddressFromNmapLine(@NotNull String line) {
        return line.contains(getHost()) ? getIpaddress() : line.substring(21);
    }

    /**
     * <p>
     * Builds the response from nmap operating system scan. A single line from the nmap os scan is run
     * through this routine and checked against important strings containing data needed for the nmap
     * os scan. If a line from nmap scan contains the value of a needed string, then that line is
     * appended to the response. Important operating system data is added to the final nmap os scan
     * response.
     * </p>
     * @param line     single line from nmap os scan
     * @param response response containing important os scan data
     * @see            PeepingConstants
     */
    private static void appendNmapOSScanValue(String line, StringBuilder response) {
        String match = null;
        for (String partial : nmapResponsePartials) {
            if (line.contains(partial)) {
                match = partial;
                break;
            } else if (line.contains(PeepingConstants.PORT) && line.contains(PeepingConstants.STATE) && line.contains(PeepingConstants.SERVICE)) {
                match = PeepingConstants.NMAP_CONTAINS_PORT_STATE_SERVICE;
                break;
            } else if (line.contains(PeepingConstants.NMAP_ALL_1000_SCANNED_PORTS) && line.contains(PeepingConstants.NMAP_ARE_CLOSED)) {
                match = PeepingConstants.NMAP_ALL_1000_PORTS_ARE_CLOSED;
                break;
            } else {
                match = PeepingConstants.EMPTY_STRING;
            }
        }

        switch(Objects.requireNonNull(match)) {

            // nmap defaults to scanning 1000 most common used ports. Check port availability
            case PeepingConstants.NMAP_ALL_1000_PORTS_ARE_CLOSED:
                response.append("most commonly used 1000 ports closed").append("\n");
                break;
            case PeepingConstants.NMAP_AGGRESSIVE_OS_GUESSES:
            case PeepingConstants.NMAP_TOO_MANY_FINGERPRINTS_MATCH:
            case PeepingConstants.NMAP_WARNING:
            case PeepingConstants.NMAP_OS_DETAILS:
            case PeepingConstants.NMAP_RUNNING:
                response.append(line).append("\n");
                break;
            case PeepingConstants.MAC_ADDRESS_1:
                // this particular line comes after the final open port line
                if (readOpenPorts) {
                    // do not continue reading open ports
                    readOpenPorts = false;
                }
                break;
            case PeepingConstants.NMAP_CONTAINS_PORT_STATE_SERVICE:
                // begin reading open ports on following line
                readOpenPorts = true;
                break;
            default:
                LOG.info("No object to write for nmap OS scan");
                break;
        }
        if (readOpenPorts) {
            response.append(line).append("\n");
        }
    }
}
