package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.SmartSystemNetwork;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * Defines the code for {@code com/smart/peepingbill/models/impl/SmartSystemNetworkImpl.java}. Smart SystemNetwork
 * can be defined as the representation of a local area network, it's devices and its data. This network data is
 * held with a {@link JSONObject} structure for further processing. The processing of this data can be extended in
 * multiple ways. Some examples include, security analysis of individual devices and overall network, examination
 * of device connectivity and so on.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2021/12/25
 *
 * @see <a href="https://www.javadoc.io/doc/org.json/json/latest/org/json/class-use/JSONObject.html">JSONObject Java docs</a>
 */
public class SmartSystemNetworkImpl implements SmartSystemNetwork {
    private static final Logger LOG = LoggerFactory.getLogger(SmartSystemNetworkImpl.class);

    private final JSONObject smartSystemJson;

    /**
     * <p>
     * Constructor for {@link SmartSystemNetworkImpl} class which constructs a {@link JSONObject}
     * data-structure that contains the current local area network, it's devices and data.
     * </p>
     * @param systemJson {@link JSONObject} system data
     * @see              #orderSmartSystemJSON(JSONObject)
     */
    public SmartSystemNetworkImpl(JSONObject systemJson) {
        smartSystemJson = orderSmartSystemJSON(systemJson);
    }

    @Override
    public JSONObject getSmartSystemJSON() {
        return this.smartSystemJson;
    }

    /**
     * <p>
     * Orders System json based on device, host will be the first object in the json structure and then
     * proceeds to order device by order it was first processed during network system scan.
     * </p>
     * @param smartJson {@link JSONObject} un-order system json object
     * @return          ordered system {@link JSONObject}
     * @see             #createOrderedJSONObjectByReflection(JSONObject)
     */
    @NotNull
    private JSONObject orderSmartSystemJSON(@NotNull JSONObject smartJson) {
        JSONObject orderedSmartSystemJson = new JSONObject();

        /*
         * A JSONObject does not preserve the order of each object when added to a JSONObject.
         * Order can change, so you should expect that it will. In this case, we want to
         * change the underlying functionality of how objects are stored, as we want the
         * order in which objects are injected into the JSONObject to persist when collecting
         * its data.
         *
         * @see #createOrderedJSONObjectByReflection(JSONObject) for further details
         */
        createOrderedJSONObjectByReflection(orderedSmartSystemJson);

        // discount host node in length
        int size = smartJson.length() - 1;

        // add host device node first
        orderedSmartSystemJson.put(PeepingConstants.HOST_SMART_NODE, smartJson.get(PeepingConstants.HOST_SMART_NODE));

        // add devices by order (each device node will look such as device0, device1, ...)
        for (int i = 0; i < size; i++) {
            String device = StringUtils.join(PeepingConstants.DEVICE_SMART_NODE, i);
            orderedSmartSystemJson.put(device, smartJson.get(device));
        }
        return orderedSmartSystemJson;
    }

    /**
     * <p>
     * The order in which each object is added to a {@link JSONObject} is not preserved throughout the life
     * of that object. Order in which objects are added can change, and we should expect that they will.
     * This method utilizes Java's Reflection Api to change the underlying structure of the {@link JSONObject}
     * to a {@link LinkedHashMap} which allows the order in which objects are added to a JSONObject to
     * persist throughout its lifetime.
     * </p>
     * @param unorderedJSON {@link JSONObject}
     *
     * @see java.lang.reflect.AccessibleObject
     * @see <a href="https://www.javadoc.io/doc/org.json/json/latest/org/json/class-use/JSONObject.html">JSONObject Java docs</a>
     */
    private void createOrderedJSONObjectByReflection(@NotNull JSONObject unorderedJSON) {
        try {
            // change map field in JSONObject to a structure which will preserve order
            Field changeMap = unorderedJSON.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(unorderedJSON, new LinkedHashMap<>());

            // disable accessibility for security reasons
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOG.info("Error occurred reflecting JSONObject's field 'map' to LinkedHashMap: {}", e.getMessage());
        }
    }
}
