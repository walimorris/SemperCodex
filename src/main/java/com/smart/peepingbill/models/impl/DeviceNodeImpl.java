package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.DeviceNode;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class DeviceNodeImpl implements DeviceNode, EventHandler<MouseEvent> {
    private final int systemDeviceIndex;
    private final Circle node;
    private final VBox vboxBottom;
    private final GridPane gridPane;
    private final Label deviceNodeLabel;
    private final SmartSystemNetworkImpl smartSystemNetwork;
    private final ArrayList<Label> deviceNodeLabels;

    public DeviceNodeImpl(Circle node, int deviceIndex, VBox vboxBottom, GridPane gridPane,
                          SmartSystemNetworkImpl systemNetwork) {
        this.node = node;
        this.gridPane = gridPane;
        this.systemDeviceIndex = deviceIndex;
        this.deviceNodeLabel = createDeviceNodeLabel();
        this.vboxBottom = vboxBottom;
        this.smartSystemNetwork = systemNetwork;
        this.deviceNodeLabels = buildDeviceNodeLabels();
        this.node.setFill(Color.web(PeepingConstants.HACKER_GREEN));

        // sick!
        this.node.setOnMouseClicked(this);
        this.deviceNodeLabel.setOnMouseClicked(this);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (vboxBottom.getChildren().size() > 0) {
            vboxBottom.getChildren().clear();
        }
        deviceNodeLabels.forEach((label -> vboxBottom.getChildren().add(label)));
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

    private ArrayList<Label> buildDeviceNodeLabels() {
        return new ArrayList<>(List.of((new Label(PeepingConstants.DEVICE_SMART_NODE + systemDeviceIndex)),
                new Label(smartSystemNetwork.getSmartSystemDeviceNodeMacAddress(systemDeviceIndex)),
                new Label(smartSystemNetwork.getSmartSystemDeviceNodeLocalIpAddress(systemDeviceIndex))));
    }

    private Label createDeviceNodeLabel() {
        Label label = new Label(PeepingConstants.DEVICE + getSystemDeviceIndex());
        label.setFont(new Font(12.0f));
        return label;
    }
}