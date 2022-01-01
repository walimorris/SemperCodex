package com.smart.peepingbill.controllers;

import com.smart.peepingbill.models.SudoPopupWindow;
import com.smart.peepingbill.models.impl.*;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Defines the code for {@code com/smart/peepingbill/pad-landingp-view.fxml}. The pad-landing view
 * features the main system network building page, that is, there is a feature to build out a visual
 * representation of your local area network and all reachable devices on the network. The underlying
 * data-structure for device data is held within a {@link JSONObject}.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2021/12/24
 */
public class PadLandingController implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(PadLandingController.class);

    @FXML
    private Button buildNetworkButton;

    @FXML
    private Button jsonSnapShotButton;

    @FXML
    private VBox vboxLeft;

    @FXML
    private VBox vboxBottom;

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
    private boolean isNetworkUiRendered;
    private String[] ipHostArray;
    private Map<String, String> macAddresses;
    private SudoPopupWindow sudoPopupWindow;
    private Stage currentStage;
    private Scene currentScene;
    private SmartSystemNetworkImpl smartSystemNetwork;

    // snapshots
    SnapShotImpl jsonSnapShot;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sudo = new PasswordField();
        isNetworkUiRendered = false;
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
        currentScene = buildNetworkButton.getScene();

        sudoPopupWindow = new SudoPopupWindowImpl(currentStage, sudo, popupSubmit);

        sudoPopupWindow.popup();

        // build smart system network on click
        popupSubmit.setOnMouseClicked(mouseEvent -> {
            sudoPopupWindow.getSudoFromUser();

            if (sudoPopupWindow.isSudoSet()) {
                initBuildSmartSystemJsonData();
            }
        });
    }

    @FXML
    protected void getJsonSnapShot(ActionEvent e) {
        if (isSnapShotAvailable() && jsonSnapShot == null) {
            jsonSnapShot = new SnapShotImpl(PeepingConstants.JSON, smartSystemNetwork);
            jsonSnapShot.writeSnapShot();
        } else {
            if (isSnapShotAvailable() && jsonSnapShot != null) {
                jsonSnapShot.writeSnapShot();
            }
        }
    }

    /**
     * Returns if taking a CodexSnapShot is available by determining if system network build
     * button is enabled and smartSystemNetwork {@link JSONObject} is created and not null.
     * @return boolean is CodexSnapShot available
     */
    private boolean isSnapShotAvailable() {
        return !buildNetworkButton.isDisabled() && smartSystemNetwork != null;
    }

    private void resetDeviceCount() {
        deviceCount = 0;
    }

    /**
     * Get current stage.
     * @return {@link Stage}
     */
    @Nullable
    private Stage getCurrentStage() {
        if (currentStage != null) {
            return currentStage;
        }
        return null;
    }

    @Nullable
    private Scene getCurrentScene() {
        if (currentScene != null) {
            return currentScene;
        }
        return null;
    }

    /**
     * <p>
     * Get Smart System Network (current local area network) system, it's devices and data
     * represented as {@link JSONObject}.
     * </p>
     * @return {@link JSONObject}
     */
    @Nullable
    private JSONObject getSmartSystemNetworkJSON() {
        if (smartSystemNetwork != null) {
            return smartSystemNetwork.getSmartSystemJSON();
        }
        return null;
    }

    /**
     * <p>
     * The underlying data-structure which holds the data for each device on a user's local area
     * network is held within a {@link JSONObject}. As the system network data-structure is
     * constructed, each device is represented and created as a device node {@link DeviceJsonImpl}
     * with its json data appended to the single json object. This json object and its data
     * is then passed to a smart system network object {@link SmartSystemNetworkImpl} that finalizes
     * the system network build.
     * </p>
     *
     * @param sudo secret key from {@link SudoPopupWindowImpl}
     * @see        DeviceJsonImpl
     * @see        SmartSystemNetworkImpl
     */
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

    /**
     * <p>
     * Adds a {@link DeviceJsonImpl} device to the given smart system json object which will then be passed
     * to the final {@link SmartSystemNetworkImpl} that builds a {@link JSONObject} representing the local
     * area network consisting of all devices and its data.
     * </p>
     * @param deviceName            device name
     * @param ipaddress             the device ip-address
     * @param mac                   the device mac-address
     * @param key                   the secret key to run system commands
     * @param isHost                determines if the {@link DeviceJsonImpl} being created is a host or not
     * @param smartSystemJsonObject the smart system json object that'll be passed to the
     *                              finalizing {@link SmartSystemNetworkImpl} object
     *
     * @see DeviceJsonImpl#getNmapScanResponse()
     * @see JSONObject
     * @see SmartSystemNetworkImpl
     */
    private void initDeviceNode(String deviceName, String ipaddress, String mac, String key, boolean isHost,
                                         JSONObject smartSystemJsonObject) {
        DeviceJsonImpl node = new DeviceJsonImpl(deviceName, ipaddress, mac, key, isHost);
        System.out.println(node.getNmapScanResponse());
        if (isHost) {
            smartSystemJsonObject.put(PeepingConstants.HOST_SMART_NODE, node.getSmartDeviceJsonObject());
        } else {
            smartSystemJsonObject.put(PeepingConstants.DEVICE_SMART_NODE + deviceCount, node.getSmartDeviceJsonObject());
        }
        node.voidSudo();
    }

    /**
     * Builds the final representation of the local area network's system that includes all devices
     * and its data. This process is run as a {@link Task} on a separate thread, as a background
     * process, supplied with its own {@link ProgressBar} that informs the user that the build
     * process is underway.
     *
     * @see NetworkUtil#getLanDeviceMacAndIpAddresses(String)
     * @see #parseSmartSystemNetworkJsonData(String)
     */
    public void initBuildSmartSystemJsonData() {
        ProgressBar bar = new ProgressBar();
        Label progressbarLabel = new Label();

        setProgressLabelProperties(progressbarLabel);
        setProgressBarInVBoxBottom(bar, progressbarLabel);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                ipHostArray = new String[]{NetworkUtil.getIpaddress(), NetworkUtil.getHost()};
                macAddresses = NetworkUtil.getLanDeviceMacAndIpAddresses(sudoPopupWindow.getSudo());
                parseSmartSystemNetworkJsonData(sudoPopupWindow.getSudo());
                sudoPopupWindow.voidSudo();
                return null;
            }
        };
        bar.progressProperty().bind(task.progressProperty());
        task.setOnRunning(workerStateEvent -> {
            disableUI();
        });
        task.setOnSucceeded(workerStateEvent -> {
            removeProgressBarFromVBoxBottom(bar, progressbarLabel);
            enableUI();
            // build host smart node visual
            Circle hostNode = new Circle(PeepingConstants.HOST_NODE_V, PeepingConstants.HOST_NODE_V1, PeepingConstants.HOST_NODE_V2);
            HostNodeImpl host = new HostNodeImpl(hostNode, vboxBottom, smartSystemNetwork, 1, 0, gridpaneCenter);
            gridpaneCenter.setGridLinesVisible(true);
            gridpaneCenter.add(host.getNode(), 0, 0);
            gridpaneCenter.add(host.getHostNodeLabel(), 0, 0);
        });
        new Thread(task).start();
    }

    /**
     * Disables the build-network-button on Smart Pad Landing UI.
     * @see Button#setDisable(boolean)
     */
    private void disableBuildNetworkButton() {
        buildNetworkButton.setDisable(true);
    }

    /**
     * Enables the build-network-button on Smart Pad Landing UI.
     * @see Button#setDisable(boolean)
     */
    private void enableBuildNetworkButton() {
        buildNetworkButton.setDisable(false);
    }

    /**
     * Disables the json-snapshot-button on Smart Pad Landing UI.
     * @see Button#setDisable(boolean)
     */
    private void disableJsonSnapShotButton() {
        jsonSnapShotButton.setDisable(true);
    }

    /**
     * Enables the build-network-button on Smart Pad Landing UI.
     * @see Button#setDisable(boolean)
     */
    private void enableJsonSnapShotButton() {
        jsonSnapShotButton.setDisable(false);
    }

    /**
     * Disables the Center Gridpane on Smart Pad Landing UI.
     * @see GridPane#setDisable(boolean)
     */
    private void disableGridPaneCenter() {
        gridpaneCenter.setDisable(true);
    }

    /**
     * Enables the Center Gridpane on Smart Pad Landing UI.
     * @see GridPane#setDisable(boolean)
     */
    private void enableGridPaneCenter() {
        gridpaneCenter.setDisable(false);
    }

    /**
     * Disables the entire Smart Pad Builder UI and its child-nodes.
     */
    private void disableUI() {
        disableGridPaneCenter();
        disableJsonSnapShotButton();
        disableBuildNetworkButton();
    }

    /**
     * Enables the entire Smart Pad Builder UI and its child-nodes.
     */
    private void enableUI() {
        enableGridPaneCenter();
        enableJsonSnapShotButton();
        enableBuildNetworkButton();
    }

    private void setProgressBarInVBoxBottom(ProgressBar bar, Label progressBarLabel) {
        vboxBottom.setAlignment(Pos.BASELINE_CENTER);
        vboxBottom.getChildren().add(bar);
        vboxBottom.getChildren().add(progressBarLabel);
    }

    private void removeProgressBarFromVBoxBottom(ProgressBar bar, Label progressBarLabel) {
        vboxBottom.getChildren().remove(bar);
        vboxBottom.getChildren().remove(progressBarLabel);
        vboxBottom.setAlignment(Pos.TOP_LEFT);
    }

    private void setProgressLabelProperties(Label progressLabel) {
        progressLabel.setText("initializing build...");
        progressLabel.setTextFill(Color.web(PeepingConstants.HACKER_GREEN));
        progressLabel.setFont(new Font(16.0f));
        progressLabel.setPadding(new Insets(5.0f));
    }
}
