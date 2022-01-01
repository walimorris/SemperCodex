package com.smart.peepingbill.models;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public interface DeviceNode {

    Circle getNode();

    public int getSystemDeviceIndex();

    Label getDeviceNodeLabel();

    void addLabelToDeviceNode(int column, int row);
}
