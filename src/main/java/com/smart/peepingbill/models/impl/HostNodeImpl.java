package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.HostNode;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

public class HostNodeImpl implements HostNode, EventHandler<MouseEvent> {

    private final Circle node;
    private Set<Label> hostNodeLabels;
    private final VBox vboxBottom;
    private final GridPane gridPane;
    private int row;
    private int column;
    private boolean isNetworkUiRendered;
    private final SmartSystemNetworkImpl smartSystemNetwork;


    public HostNodeImpl(Circle node, VBox vboxBottom, SmartSystemNetworkImpl systemNetwork, int column,
                        int row, GridPane gridpane) {
        this.node = node;
        this.gridPane = gridpane;
        this.row = row;
        this.column = column;
        this.hostNodeLabels = new HashSet<>();
        this.isNetworkUiRendered = false;
        this.vboxBottom = vboxBottom;
        this.smartSystemNetwork = systemNetwork;
        this.node.setFill(Color.web(PeepingConstants.HACKER_GREEN));
        this.node.setOnMouseClicked(this);
    }

    @Override
    public Circle getNode() {
        return this.node;
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
            for (int i = 0; i < size; i++) {
                Circle deviceNode = new Circle(75.0f, 33.75f, 25.0f);
                if (getColumn() == 4) {
                    setColumn(0);
                    incrementRow();
                }
                DeviceNodeImpl device = new DeviceNodeImpl(deviceNode, i, vboxBottom, smartSystemNetwork);
                this.gridPane.add(device.getNode(), getColumn(), getRow());
                incrementColumn();
            }
            setIsNetworkUiRendered(true);
        }
        if (hostNodeLabels.isEmpty()) {
            hostNodeLabels = buildHostNodeLabels();
        }
        if (vboxBottom.getChildren().size() > 0) {
            vboxBottom.getChildren().clear();
        }
        hostNodeLabels.forEach((label -> vboxBottom.getChildren().add(label)));
    }

    private Set<Label> buildHostNodeLabels() {
        return new HashSet<>(Set.of((new Label(smartSystemNetwork.getSmartSystemHostName())),
                new Label(smartSystemNetwork.getSmartSystemHostMacAddress()),
                new Label(smartSystemNetwork.getSmartSystemHostExternalIpaddress()),
                new Label(smartSystemNetwork.getSmartSystemHostLocalIpaddress())));
    }
}
