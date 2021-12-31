package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.DeviceNode;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

public class DeviceNodeImpl implements DeviceNode, EventHandler<MouseEvent> {
    private final int systemDeviceIndex;
    private final Circle node;
    private final VBox vboxBottom;
    private final SmartSystemNetworkImpl smartSystemNetwork;
    private final Set<Label> deviceNodeLabels;

    public DeviceNodeImpl(Circle node, int deviceIndex, VBox vboxBottom,
                          SmartSystemNetworkImpl systemNetwork) {
        this.node = node;
        this.systemDeviceIndex = deviceIndex;
        this.vboxBottom = vboxBottom;
        this.smartSystemNetwork = systemNetwork;
        this.deviceNodeLabels = buildDeviceNodeLabels();
        this.node.setFill(Color.web(PeepingConstants.HACKER_GREEN));

        // sick!
        this.node.setOnMouseClicked(this);
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

    private Set<Label> buildDeviceNodeLabels() {
        return new HashSet<>(Set.of((new Label(PeepingConstants.DEVICE_SMART_NODE + systemDeviceIndex)),
                new Label(smartSystemNetwork.getSmartSystemDeviceNodeMacAddress(systemDeviceIndex)),
                new Label(smartSystemNetwork.getSmartSystemDeviceNodeLocalIpAddress(systemDeviceIndex))));
    }
}