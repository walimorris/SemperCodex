package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.HostNode;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the code for {@code com/smart/peepingbill/models/impl/HostNodeImpl.java}.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2022/01/7
 */
public class HostNodeImpl implements HostNode, EventHandler<MouseEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(HostNodeImpl.class);

    private final Circle node;
    private final String key;
    private ArrayList<Label> hostNodeLabels;
    private final VBox vboxDeviceInfo;
    private final VBox vboxDeviceTraffic;
    private final GridPane gridPane;
    private final Label hostNodeLabel;
    private int row;
    private int column;
    private boolean isNetworkUiRendered;
    private final SmartSystemNetworkImpl smartSystemNetwork;


    public HostNodeImpl(Circle node, String key, VBox vboxDeviceInfo, VBox vboxDeviceTraffic, SmartSystemNetworkImpl systemNetwork, int column,
                        int row, GridPane gridpane) {
        this.node = node;
        this.key = key;
        this.gridPane = gridpane;
        this.hostNodeLabel = createHostNodeLabel();
        this.row = row;
        this.column = column;
        this.hostNodeLabels = new ArrayList<>();
        this.isNetworkUiRendered = false;
        this.vboxDeviceInfo = vboxDeviceInfo;
        this.vboxDeviceTraffic = vboxDeviceTraffic;
        this.smartSystemNetwork = systemNetwork;
        this.node.setFill(Color.web(PeepingConstants.HACKER_GREEN));
        this.node.setOnMouseClicked(this);
        this.hostNodeLabel.setOnMouseClicked(this);
    }

    @Override
    public Circle getNode() {
        return this.node;
    }

    @Override
    public Label getHostNodeLabel() {
        return hostNodeLabel;
    }

    @Override
    public void setIsNetworkUiRendered(boolean isUiRendered) {
        this.isNetworkUiRendered = isUiRendered;
    }

    @Override
    public boolean getIsNetworkUiRendered() {
        return isNetworkUiRendered;
    }

    @Override
    public void incrementRow() {
        this.row = this.row + 1;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public void incrementColumn() {
        this.column = this.column + 1;
    }

    @Override
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public int getColumn() {
        return this.column;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!getIsNetworkUiRendered()) {
            int size = smartSystemNetwork.getSmartSystemJSON().length() - 1;

            // build device node from system network device count
            for (int i = 0; i < size; i++) {
                Circle deviceNode = new Circle(PeepingConstants.DEVICE_NODE_V, PeepingConstants.DEVICE_NODE_V1,
                        PeepingConstants.DEVICE_NODE_V2);

                if (getColumn() == 4) {
                    setColumn(0);
                    incrementRow();
                }
                DeviceNodeImpl device = new DeviceNodeImpl(deviceNode, key, i, vboxDeviceInfo, vboxDeviceTraffic, gridPane, smartSystemNetwork);
                this.gridPane.add(device.getNode(), getColumn(), getRow());
                device.addLabelToDeviceNode(getColumn(), getRow());
                incrementColumn();
            }
            setIsNetworkUiRendered(true);
        }
        if (hostNodeLabels.isEmpty()) {
            hostNodeLabels = buildHostNodeLabels();
        }
        if (vboxDeviceInfo.getChildren().size() > 0) {
            vboxDeviceInfo.getChildren().clear();
        }
        hostNodeLabels.forEach((label -> vboxDeviceInfo.getChildren().add(label)));
    }

    /**
     * Create Host Labels for the Info section in System Network user Interface.
     * @return {@link ArrayList}
     */
    private ArrayList<Label> buildHostNodeLabels() {
        return new ArrayList<>(List.of((new Label(smartSystemNetwork.getSmartSystemHostName())),
                new Label(smartSystemNetwork.getSmartSystemHostMacAddress()),
                new Label(smartSystemNetwork.getSmartSystemHostExternalIpaddress()),
                new Label(smartSystemNetwork.getSmartSystemHostLocalIpaddress())));
    }

    /**
     * Create single label used for the visual host node on user interface.
     * @return {@link Label}
     */
    private Label createHostNodeLabel() {
        Label label = new Label(NetworkUtil.getHost());
        label.setFont(new Font(16.0f));
        return label;
    }
}
