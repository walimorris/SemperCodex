package com.smart.peepingbill.models;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public interface HostNode {

    Circle getNode();

    Label getHostNodeLabel();

    void setIsNetworkUiRendered(boolean isUiRendered);

    boolean getIsNetworkUiRendered();

    void incrementRow();

    void setRow(int row);

    int getRow();

    void incrementColumn();

    void setColumn(int column);

    int getColumn();
}
