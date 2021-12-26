package com.smart.peepingbill.models.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.smart.peepingbill.models.DeviceNode;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Defines the code for {@code com/smart/peepingbill/models/impl/DeviceNodeImpl.java} class. A device
 * node is derived from scanning the local area network and pulling the ip and mac-addresses from each
 * device. Supplied with this information, you can obtain much more information about each device from
 * the local system as well as various external sources. A device node data is structured as a
 * {@link JSONObject} depicting various details of a device.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2021/12/26
 */
public class DeviceNodeImpl implements DeviceNode {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceNodeImpl.class);

    private boolean vpnActive;
    private String isp;
    private final String name;
    private final String externalIpAddress;
    private final String localIpaddress;
    private final String macAddress;
    private final Boolean isHost;
    private String sudo;
    private String nmapScan;
    private final Map<String, String> vendorKeys;
    private final List<String> blockKeys;
    private JSONObject smartHostJsonObject;
    private String smartHostJson;
    private String smartHostJsonPrettyPrint;

    /**
     * DeviceNodeImpl constructor.
     * @param deviceName device name
     * @param ipaddress  device ip-address
     * @param macAddress device mac-address
     * @param sudo       sudo key
     * @param isHost     determines if device is host device or not
     */
    public DeviceNodeImpl(String deviceName, String ipaddress, String macAddress, String sudo, Boolean isHost) {
        this.name = deviceName;
        this.sudo = sudo;
        this.localIpaddress = ipaddress;
        this.macAddress = macAddress;
        this.isHost = isHost;
        this.nmapScan = NetworkUtil.scanDeviceForOS(localIpaddress, sudo);

        // use AWS service to receive device external ip address
        this.externalIpAddress = Objects.requireNonNull(StringUtils
                .replace(unirestRequest(PeepingConstants.AWS_IP_CHECK_ENDPOINT), "\n", "")).trim();

        // build map object for device vendor data
        vendorKeys = Map.of(PeepingConstants.OUI, PeepingConstants.VENDOR_OUI,
                PeepingConstants.IS_PRIVATE, PeepingConstants.VENDOR_IS_PRIVATE,
                PeepingConstants.COMPANY_NAME, PeepingConstants.VENDOR_COMPANY_NAME,
                PeepingConstants.COMPANY_ADDRESS, PeepingConstants.VENDOR_COMPANY_ADDRESS,
                PeepingConstants.COUNTRY_CODE, PeepingConstants.VENDOR_COUNTRY_CODE);

        // build list object for device block data
        blockKeys = List.of(PeepingConstants.BLOCK_FOUND, PeepingConstants.BORDER_LEFT, PeepingConstants.BORDER_RIGHT,
                PeepingConstants.BLOCK_SIZE, PeepingConstants.ASSIGNMENT_BLOCK_SIZE, PeepingConstants.DATE_CREATED,
                PeepingConstants.DATE_UPDATED);

        initDeviceNode();
    }

    @Override
    public String getSmartDeviceJsonString() {
        return smartHostJson;
    }

    @Override
    public JSONObject getSmartDeviceJsonObject() {
        return smartHostJsonObject;
    }

    @Override
    public void prettyPrint() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        smartHostJsonPrettyPrint = gson.toJson(smartHostJson);
    }

    @Override
    public void voidSudo() {
        this.sudo = null;
    }

    @Override
    public String getDeviceName() {
        return this.name;
    }

    @Override
    public String getNmapScanResponse() {
        return this.nmapScan;
    }

    /**
     * Builds Device Node JSON data as {@link JSONObject} based on device type(host, client).
     *
     * @see #initDeviceNodeJsonForDevice()
     * @see #initDeviceNodeJsonForHostDevice()
     */
    private void initDeviceNode() {
        if (isHost) {
            smartHostJsonObject = initDeviceNodeJsonForHostDevice();
        } else {
            smartHostJsonObject = initDeviceNodeJsonForDevice();
        }
        smartHostJson = smartHostJsonObject.toString();
    }

    /**
     * Creates and builds each segment of the final host device node's json data.
     * @return {@link JSONObject}.
     */
    private JSONObject initDeviceNodeJsonForHostDevice() {
        JSONObject jsonObj = new JSONObject();

        putHostDeviceNodeSegmentJson(jsonObj);

        String macSearchResponse = searchMacAddressAndGetResponse();
        String deepNetworkResponse = unirestRequest(StringUtils.join(PeepingConstants.IP_API_JSON_ENDPOINT,
                externalIpAddress, PeepingConstants.DEEP_IP_SUFFIX));

        putMacSearchSegmentJson(macSearchResponse, jsonObj);
        putOperatingSystemSegmentJson(jsonObj);
        putDeepNetworkSegmentJson(deepNetworkResponse, jsonObj);

        return jsonObj;
    }

    /**
     * Creates and builds each segment of the final client device node's json data.
     * @return {@link JSONObject}.
     *
     * @see    #putGenericDeviceNodeSegmentJson(JSONObject)
     * @see    #searchMacAddressAndGetResponse()
     * @see    #putMacSearchSegmentJson(String, JSONObject)
     */
    private JSONObject initDeviceNodeJsonForDevice() {
        JSONObject jsonObj = new JSONObject();
        putGenericDeviceNodeSegmentJson(jsonObj);

        String macSearchResponse = searchMacAddressAndGetResponse();
        putMacSearchSegmentJson(macSearchResponse, jsonObj);

        return jsonObj;
    }

    /**
     * Builds the request {@link String} query used against macaddress.io api.
     * Collects Application properties to build request string.
     * @return {@link String} request.
     *
     * @see    Properties#load(Reader)
     */
    private String loadMKQueryStr() {
        Properties props = new Properties();
        StringBuilder propFileStr = new StringBuilder();
        StringBuilder mkQueryStr = new StringBuilder();

        try (Reader propFile = new FileReader(String
                .valueOf(Path.of(String.valueOf(Paths.get(PeepingConstants.APPLICATION_PROPERTIES)))));

            BufferedReader applicationPropReader = new BufferedReader(propFile)) {
                propFileStr.append(propFile);
                props.load(applicationPropReader);
                mkQueryStr.append(props.getProperty(PeepingConstants.MK_QUERY_STR));
        } catch (InvalidPathException | IOException e) {
            LOG.error("Error loading given path ' {} '", propFileStr);
        }

        return mkQueryStr.toString();
    }

    /**
     * Builds request string for mac address search. Specifically injects the request
     * suffix for json response.
     * @param apiKey  API key for 'macaddress.io' collected from application properties.
     * @param address Full mac address consisting of six octets.
     * @return        {@link String} request string.
     *
     * @see           <a href="https://macaddress.io/api/documentation/making-requests">macaddress.io api docs</a>
     */
    private String buildMkQueryJsonRequest(String apiKey, String address) {
        // only append pull first 3 octets of device mac address
        return StringUtils.join(PeepingConstants.MAC_LOOKUP_API_URL, apiKey, PeepingConstants.MAC_LOOKUP_API_JSON_SUFFIX,
                StringUtils.substring(address, 0, 8));
    }

    /**
     * Request utilizing {@link Unirest} HTTP client.
     * @param request String request
     * @return       {@link String} response
     */
    private String unirestRequest(String request) {
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(request).asString();
        } catch (UnirestException e) {
            LOG.error("Error from request to ' {} ': {}", request, e.getMessage());
        }
        return response != null ? response.getBody() : null;
    }

    /**
     * Builds macaddress.io api endpoint request, sends unirest request {@link Unirest}
     * and returns response data consisting of various data collected about device
     * mac-address. Only the first 3-octets of device macaddress is appended to the request.
     * @return {@link String}
     * @see    #loadMKQueryStr()
     * @see    #buildMkQueryJsonRequest(String, String)
     * @see    #unirestRequest(String)
     * @see    <a href="https://macaddress.io/api/documentation/making-requests">macaddress.io api docs</a>
     */
    private String searchMacAddressAndGetResponse() {
        String key = loadMKQueryStr();
        String macAddressDataRequestStr = buildMkQueryJsonRequest(key, this.macAddress);
        return unirestRequest(macAddressDataRequestStr);
    }

    /**
     * Input host node device's data in json object.
     * @param jsonNodeObject {@link JSONObject} overall json data object
     */
    private void putHostDeviceNodeSegmentJson(JSONObject jsonNodeObject) {
        JSONObject smartDeviceInitialDataObject = new JSONObject()
                .put(PeepingConstants.SMART_DEVICE_NODE_TYPE, PeepingConstants.HOST)
                .put(PeepingConstants.HOST_NAME, NetworkUtil.getHost())
                .put(PeepingConstants.LOCAL_IP_ADDRESS, localIpaddress)
                .put(PeepingConstants.EXTERNAL_IP_ADDRESS, externalIpAddress)
                .put(PeepingConstants.MAC_ADDRESS_2, macAddress);

        jsonNodeObject.put(PeepingConstants.SMART_DEVICE_NODE_DETAILS, smartDeviceInitialDataObject);
    }

    /**
     * Input client node device's data in json object.
     * @param jsonNodeObject {@link JSONObject} overall json data object
     */
    private void putGenericDeviceNodeSegmentJson(JSONObject jsonNodeObject) {
        JSONObject genericDeviceInitialDataObject = new JSONObject()
                .put(PeepingConstants.SMART_DEVICE_NODE_TYPE, PeepingConstants.CLIENT)
                .put(PeepingConstants.LOCAL_IP_ADDRESS, localIpaddress)

                // external ip address for clients?
                .put(PeepingConstants.MAC_ADDRESS_2, macAddress);

        jsonNodeObject.put(PeepingConstants.SMART_DEVICE_NODE_DETAILS, genericDeviceInitialDataObject);
    }

    /**
     * Inputs data from mac address search response.
     * @param macSearchResponseBody {@link String} search response.
     * @param jsonNodeObject        {@link JSONObject} overall json object data.
     */
    private void putMacSearchSegmentJson(String macSearchResponseBody, JSONObject jsonNodeObject) {
        JSONObject macSearchResponseJson = new JSONObject(macSearchResponseBody);

        try {
            JSONObject vendorDetails = macSearchResponseJson.getJSONObject(PeepingConstants.VENDOR_DETAILS);
            JSONObject blockDetails = macSearchResponseJson.getJSONObject(PeepingConstants.BLOCK_DETAILS);
            JSONObject dataFromMacSearchObject = new JSONObject();
            putMACDetailsSegmentJson(dataFromMacSearchObject, vendorDetails, blockDetails);
            jsonNodeObject.put(PeepingConstants.SMART_DEVICE_MAC_ADDRESS_DETAILS, dataFromMacSearchObject);
        } catch (JSONException e) {
            LOG.error("Error querying mac-address api endpoint. Please check api: {}", e.getMessage());
        }
    }

    /**
     * Puts vendor and block segment data from mac address search response to overall
     * json data object.
     * @param dataJson   {@link JSONObject} overall json object.
     * @param vendorJson {@link JSONObject} vender data in json format from mac address response.
     * @param blockJson  {@link JSONObject} block data in json format from mac address response.
     */
    private void putMACDetailsSegmentJson(JSONObject dataJson, JSONObject vendorJson, JSONObject blockJson) {
        putSegmentDetailsJson(dataJson, vendorJson, vendorKeys);
        putSegmentDetailsJson(dataJson, blockJson, blockKeys);
    }

    /**
     * Puts segment detail to overall json data object.
     * @param obj        {@link JSONObject} overall json data object.
     * @param detailJson {@link JSONObject} segment json data.
     * @param keys       {@link Map} the keys for json detail object.
     */
    private void putSegmentDetailsJson(JSONObject obj, JSONObject detailJson, Map<String, String> keys) {
        keys.forEach((key, value) -> obj.put(value, detailJson.get(key).toString()));
    }

    /**
     * Puts segment detail to overall json data object.
     * @param obj        {@link JSONObject} overall json data object.
     * @param detailJson {@link JSONObject} segment json data.
     * @param keys       {@link List} the keys for json detail object.
     */
    private void putSegmentDetailsJson(JSONObject obj, JSONObject detailJson, List<String> keys) {
        keys.forEach((key) -> obj.put(key, detailJson.get(key).toString()));
    }

    /**
     * Input operating system data to overal json object data.
     * @param obj {@link JSONObject} overall json object.
     */
    private void putOperatingSystemSegmentJson(JSONObject obj) {
        JSONObject systemJsonObj = new JSONObject()
                .put(PeepingConstants.SMART_DEVICE_OS, System.getProperty(PeepingConstants.OPERATING_SYSTEM_PROPERTY))
                .put(PeepingConstants.SMART_DEVICE_OS_ARCHITECTURE, System.getProperty(PeepingConstants.OPERATING_SYSTEM_ARCHITECTURE_PROPERTY))
                .put(PeepingConstants.SMART_DEVICE_OS_VERSION, System.getProperty(PeepingConstants.OPERATING_SYSTEM_VERSION))
                .put(PeepingConstants.SMART_DEVICE_USER_COUNTRY, System.getProperty(PeepingConstants.SYSTEM_USER_COUNTRY))
                .put(PeepingConstants.SMART_DEVICE_SYSTEM_LANGUAGE, System.getProperty(PeepingConstants.SYSTEM_LANGUAGE));

        obj.put(PeepingConstants.SMART_DEVICE_SYSTEM_PROPERTIES, systemJsonObj);
    }

    /**
     * Assigns and appends deeper network data segment to overall smart device json data.
     * @param deepNetworkResponse response from ip-check request.
     * @param obj                 overall device json data.
     */
    private void putDeepNetworkSegmentJson(String deepNetworkResponse, JSONObject obj) {
        JSONObject deepNetworkResponseJson = new JSONObject(deepNetworkResponse);

        if (deepNetworkResponseJson.optString(PeepingConstants.STATUS).equals(PeepingConstants.SUCCESS)) {
            JSONObject deepNetworkDataObj = new JSONObject()
                    .put(PeepingConstants.SMART_DEVICE_REGION, deepNetworkResponseJson.get(PeepingConstants.REGION))
                    .put(PeepingConstants.SMART_DEVICE_CITY, deepNetworkResponseJson.get(PeepingConstants.CITY))
                    .put(PeepingConstants.SMART_DEVICE_ZIP, deepNetworkResponseJson.get(PeepingConstants.ZIP))
                    .put(PeepingConstants.SMART_DEVICE_ISP, deepNetworkResponseJson.get(PeepingConstants.ISP))
                    .put(PeepingConstants.SMART_DEVICE_IS_ON_MOBILE, deepNetworkResponseJson.get(PeepingConstants.MOBILE))
                    .put(PeepingConstants.SMART_DEVICE_IS_ON_PROXY, deepNetworkResponseJson.get(PeepingConstants.PROXY))
                    .put(PeepingConstants.SMART_DEVICE_IS_ON_HOST, deepNetworkResponseJson.get(PeepingConstants.HOSTING));

            obj.put(PeepingConstants.SMART_DEVICE_DEEP_NETWORK_PROPERTIES, deepNetworkDataObj);

            this.isp = deepNetworkResponseJson.optString(PeepingConstants.ISP);
            this.vpnActive = Boolean.getBoolean(deepNetworkResponseJson.optString(PeepingConstants.PROXY));
        } else {
            obj.put(PeepingConstants.SMART_DEVICE_DEEP_NETWORK_PROPERTIES, deepNetworkResponseJson.get(PeepingConstants.MESSAGE));
        }
    }
}
