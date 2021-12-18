package com.smart.peepingbill.controllers;

import com.smart.peepingbill.models.DeviceNode;
import com.smart.peepingbill.models.HostLANMap;
import com.smart.peepingbill.models.SudoPopupWindow;
import com.smart.peepingbill.models.impl.DeviceNodeImpl;
import com.smart.peepingbill.models.impl.HostLANMapImpl;
import com.smart.peepingbill.models.impl.SmartSystemNetworkImpl;
import com.smart.peepingbill.models.impl.SudoPopupWindowImpl;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Defines the code for {@code com/smart/peepingbill/pad-landingp-view.fxml}
 */
public class PadLandingController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(PadLandingController.class);

    @FXML
    private Button buildNetworkButton;

    @FXML
    private VBox padLandingVBox;

    @FXML
    private TextField addresses;

    @FXML
    private Circle hostBoxNode;

    @FXML
    private PasswordField sudo;

    @FXML
    private Button popupSubmit;

    private String[] ipHostArray;
    private Map<String, String> macAddresses;
    private SudoPopupWindow sudoPopupWindow;
    private Stage currentStage;
    private SmartSystemNetworkImpl smartSystemNetwork;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sudo = new PasswordField();
        popupSubmit = new Button(PeepingConstants.SUBMIT);
    }

    @FXML
    protected void onBuildNetworkButtonClick(ActionEvent e) {
        boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
        if (!is3DSupported) {
            LOG.info("JavaFx 3D Platform Engine is not supported on this host system.");
        }

        currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        sudoPopupWindow = new SudoPopupWindowImpl(currentStage, sudo, popupSubmit);

        sudoPopupWindow.popup();

        // build smart system network on click
        popupSubmit.setOnMouseClicked(mouseEvent -> {
            sudoPopupWindow.getSudoFromUser();

            if (sudoPopupWindow.isSudoSet()) {
                HostLANMap hostLANMap = new HostLANMapImpl(sudoPopupWindow.getSudo());
                sudoPopupWindow.voidSudo();

                ipHostArray = hostLANMap.getIpToHostArray();
                macAddresses = hostLANMap.getLANMacAddresses();

                parseSmartSystemNetworkJsonData();
            }
        });

    }

    private void parseSmartSystemNetworkJsonData() {
        String ip;
        String mac;
        JSONObject smartSystemJson = new JSONObject();

        int deviceCount = 0;

        for (String key : macAddresses.keySet()) {

            if (StringUtils.equals(macAddresses.get(key), ipHostArray[0])) {
                ip = ipHostArray[0];
                mac = key;
                DeviceNode hostNode = new DeviceNodeImpl(ipHostArray[1], ip, mac, true);
                smartSystemJson.put(PeepingConstants.HOST_SMART_NODE, hostNode.getSmartDeviceJsonObject());
            } else {
                ip = macAddresses.get(key);
                mac = key;
                DeviceNode deviceNode = new DeviceNodeImpl(PeepingConstants.DEVICE_SMART_NODE + deviceCount,
                        ip, mac, false);
                smartSystemJson.put(PeepingConstants.DEVICE_SMART_NODE + deviceCount, deviceNode.getSmartDeviceJsonObject());
                deviceCount = deviceCount + 1;
            }
        }
        smartSystemNetwork = new SmartSystemNetworkImpl(smartSystemJson);
        System.out.println(smartSystemNetwork.getSmartSystemJSON());
    }
}
