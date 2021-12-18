package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.SmartSystemNetwork;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class SmartSystemNetworkImpl implements SmartSystemNetwork {
    private static final Logger LOG = LoggerFactory.getLogger(SmartSystemNetworkImpl.class);

    private final JSONObject smartSystemJson;

    public SmartSystemNetworkImpl(JSONObject systemJson) {
        smartSystemJson = orderSmartSystemJSON(systemJson);
    }

    @Override
    public JSONObject getSmartSystemJSON() {
        return this.smartSystemJson;
    }

    private JSONObject orderSmartSystemJSON(JSONObject smartJson) {
        JSONObject orderedSmartSystemJson = new JSONObject();
        createOrderedJSONObjectByReflection(orderedSmartSystemJson);

        int size = smartJson.length() - 1;

        // add host device node first
        orderedSmartSystemJson.put(PeepingConstants.HOST_SMART_NODE, smartJson.get(PeepingConstants.HOST_SMART_NODE));

        // add devices by order
        for (int i = 0; i < size; i++) {
            String device = StringUtils.join(PeepingConstants.DEVICE_SMART_NODE, i);
            orderedSmartSystemJson.put(device, smartJson.get(device));
        }
        return orderedSmartSystemJson;
    }

    private void createOrderedJSONObjectByReflection(JSONObject unorderedJSON) {
        try {
            Field changeMap = unorderedJSON.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(unorderedJSON, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOG.info("Error occurred reflecting JSONObject's field 'map' to LinkedHashMap: {}", e.getMessage());
        }
    }
}
