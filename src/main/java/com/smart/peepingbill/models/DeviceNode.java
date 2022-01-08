package com.smart.peepingbill.models;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.DeviceNodeImpl}
 */
public interface DeviceNode {

    /**
     * Get node, visually represented by {@link Circle}.
     * @return {@link Circle}
     */
    Circle getNode();

    /**
     * Get Device's Index in System Network.
     * @return int
     */
    public int getSystemDeviceIndex();

    /**
     * Get Device's Label.
     * @return {@link Label}
     */
    Label getDeviceNodeLabel();

    /**
     * Adds Label to a device node.
     * @param column at column
     * @param row    at row
     */
    void addLabelToDeviceNode(int column, int row);
}
