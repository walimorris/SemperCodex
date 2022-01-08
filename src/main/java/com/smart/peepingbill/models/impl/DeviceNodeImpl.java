package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.DeviceNode;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the code for {@code com/smart/peepingbill/models/impl/DeviceNodeImpl.java}
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2022/01/7
 */
public class DeviceNodeImpl implements DeviceNode, EventHandler<MouseEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceNodeImpl.class);

    private final int systemDeviceIndex;
    private final Circle node;
    private final String key;
    private final VBox vboxDeviceInfo;
    private final VBox vboxDeviceTraffic;
    private final GridPane gridPane;
    private final Label deviceNodeLabel;
    private final SmartSystemNetworkImpl smartSystemNetwork;
    private final ArrayList<Label> deviceNodeLabels;

    public DeviceNodeImpl(Circle node, String key, int deviceIndex, VBox vboxDeviceInfo, VBox vboxDeviceTraffic, GridPane gridPane,
                          SmartSystemNetworkImpl systemNetwork) {
        this.node = node;
        this.key = key;
        this.gridPane = gridPane;
        this.systemDeviceIndex = deviceIndex;
        this.deviceNodeLabel = createDeviceNodeLabel();
        this.vboxDeviceInfo = vboxDeviceInfo;
        this.vboxDeviceTraffic = vboxDeviceTraffic;
        this.smartSystemNetwork = systemNetwork;
        this.deviceNodeLabels = buildDeviceNodeLabels();
        this.node.setFill(Color.web(PeepingConstants.HACKER_GREEN));

        // sick!
        this.node.setOnMouseClicked(this);
        this.deviceNodeLabel.setOnMouseClicked(this);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (vboxDeviceInfo.getChildren().size() > 0) {
            vboxDeviceInfo.getChildren().clear();
        }
        deviceNodeLabels.forEach((label -> vboxDeviceInfo.getChildren().add(label)));

        if (vboxDeviceTraffic.getChildren().size() > 0) {
            vboxDeviceTraffic.getChildren().clear();
        }
        getRealTimeDeviceTraffic();
    }

    @Override
    public Circle getNode() {
        return node;
    }

    public Label getDeviceNodeLabel() {
        return deviceNodeLabel;
    }

    @Override
    public void addLabelToDeviceNode(int column, int row) {
        gridPane.add(getDeviceNodeLabel(), column, row);
    }

    @Override
    public int getSystemDeviceIndex() {
        return systemDeviceIndex;
    }

    /**
     * Create Device Labels for the Info section in System Network user Interface.
     * @return {@link ArrayList}
     */
    private ArrayList<Label> buildDeviceNodeLabels() {
        return new ArrayList<>(List.of((new Label(PeepingConstants.DEVICE_SMART_NODE + systemDeviceIndex)),
                new Label(smartSystemNetwork.getSmartSystemDeviceNodeMacAddress(systemDeviceIndex)),
                new Label(smartSystemNetwork.getSmartSystemDeviceNodeLocalIpAddress(systemDeviceIndex))));
    }

    /**
     * Create a single label for the device node in user interface.
     * @return {@link Label}
     */
    private Label createDeviceNodeLabel() {
        Label label = new Label(PeepingConstants.DEVICE + getSystemDeviceIndex());
        label.setFont(new Font(12.0f));
        return label;
    }

    /**
     * Creates a text area in UI and a task that conducts an OS scan on individual devices
     * once clicked. This scan also reports open/closed ports outside just os data.
     */
    private void getRealTimeDeviceTraffic() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        vboxDeviceTraffic.getChildren().add(textArea);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String cmd = StringUtils.join(PeepingConstants.NMAP_DEVICE_SCAN_FOR_OS, " ",
                        smartSystemNetwork.getSmartSystemDeviceNodeLocalIpAddress(systemDeviceIndex));

                BufferedReader traffic = NetworkUtil.spawnProcess(cmd, key);
                try {
                    String line = traffic.readLine();
                    while((line != null)) {
                        textArea.appendText(line + "\n");
                        line = traffic.readLine();
                    }
                    // cleanup
                    traffic.close();
                } catch (IOException e) {
                    LOG.error("Error reading device traffic from TCPDUMP buffer");
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}