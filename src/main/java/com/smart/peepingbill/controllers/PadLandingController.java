package com.smart.peepingbill.controllers;

import com.smart.peepingbill.models.SudoPopupWindow;
import com.smart.peepingbill.models.impl.DeviceNodeImpl;
import com.smart.peepingbill.models.impl.SmartSystemNetworkImpl;
import com.smart.peepingbill.models.impl.SudoPopupWindowImpl;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private VBox vboxLeft;

    @FXML
    private GridPane gridpaneCenter;

    @FXML
    private HBox gridpaneHbox;

    @FXML
    private Label smartNetworkTabLabel;

    @FXML
    private PasswordField sudo;

    @FXML
    private Button popupSubmit;

    private boolean is3DSupported;
    private int deviceCount;
    private String[] ipHostArray;
    private Map<String, String> macAddresses;
    private SudoPopupWindow sudoPopupWindow;
    private Stage currentStage;
    private SmartSystemNetworkImpl smartSystemNetwork;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sudo = new PasswordField();
        deviceCount = 0;
        popupSubmit = new Button(PeepingConstants.SUBMIT);
        is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
    }

    @FXML
    protected void onBuildNetworkButtonClick(ActionEvent e) {
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
                initBuildSmartSystemJsonData();

                // build 2d smart system network ui
            }
        });
    }

    private void resetDeviceCount() {
        deviceCount = 0;
    }

    private void parseSmartSystemNetworkJsonData(String sudo) {
        JSONObject smartSystemJson = new JSONObject();

        for (String key : macAddresses.keySet()) {
            if (StringUtils.equals(macAddresses.get(key), ipHostArray[0])) {
                initDeviceNode(ipHostArray[1], ipHostArray[0], key, sudo, true, smartSystemJson);
            } else {
                initDeviceNode(PeepingConstants.DEVICE_SMART_NODE + deviceCount,
                        macAddresses.get(key), key, sudo, false, smartSystemJson);
                deviceCount = deviceCount + 1;
            }
        }
        // reset device count for follow-on system builds on same pad instance
        resetDeviceCount();
        smartSystemNetwork = new SmartSystemNetworkImpl(smartSystemJson);
        System.out.println(smartSystemNetwork.getSmartSystemJSON());
    }

    private void initDeviceNode(String deviceType, String ipaddress, String mac, String key, boolean isHost,
                                         JSONObject smartSystemJsonObject) {
        DeviceNodeImpl node = new DeviceNodeImpl(deviceType, ipaddress, mac, key, isHost);
        System.out.println(node.getNmapScanResponse());
        if (isHost) {
            smartSystemJsonObject.put(PeepingConstants.HOST_SMART_NODE, node.getSmartDeviceJsonObject());
        } else {
            smartSystemJsonObject.put(PeepingConstants.DEVICE_SMART_NODE + deviceCount, node.getSmartDeviceJsonObject());
        }
        node.voidSudo();
    }

    public void initBuildSmartSystemJsonData() {
        ProgressBar bar = new ProgressBar();
        gridpaneCenter.getChildren().add(bar);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                // disable while task is in progress
                buildNetworkButton.setDisable(true);
                ipHostArray = new String[]{NetworkUtil.getIpaddress(), NetworkUtil.getHost()};
                macAddresses = NetworkUtil.getLanDeviceMacAndIpAddresses(sudoPopupWindow.getSudo());

                parseSmartSystemNetworkJsonData(sudoPopupWindow.getSudo());
                sudoPopupWindow.voidSudo();
                return null;
            }
        };

        //bind progress bar to task
        bar.progressProperty().bind(task.progressProperty());
        task.setOnRunning(workerStateEvent -> buildNetworkButton.setDisable(true));
        task.setOnSucceeded(workerStateEvent -> {
            gridpaneCenter.getChildren().remove(bar);
            buildNetworkButton.setDisable(false);
        });
        new Thread(task).start();
    }
}
