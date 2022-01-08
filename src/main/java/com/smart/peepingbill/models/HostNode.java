package com.smart.peepingbill.models;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.HostNodeImpl}
 */
public interface HostNode {

    /**
     * Get node, visually represented by {@link Circle}.
     * @return {@link Circle}
     */
    Circle getNode();

    /**
     * Get HostNode's label.
     * @return {@link Label}
     */
    Label getHostNodeLabel();

    /**
     * Sets if network user interface is rendered.
     * @param isUiRendered boolean
     */
    void setIsNetworkUiRendered(boolean isUiRendered);

    /**
     * Get if network user interface is rendered.
     * @return boolean
     */
    boolean getIsNetworkUiRendered();

    /**
     * Increment gridpane row.
     */
    void incrementRow();

    /**
     * Sets current row on gridpane.
     * @param row int
     */
    void setRow(int row);

    /**
     * Get current row.
     * @return int
     */
    int getRow();

    /**
     * Increment gridpane column.
     */
    void incrementColumn();

    /**
     * Sets current gridpane column.
     * @param column int
     */
    void setColumn(int column);

    /**
     * Get current column.
     * @return int
     */
    int getColumn();
}
