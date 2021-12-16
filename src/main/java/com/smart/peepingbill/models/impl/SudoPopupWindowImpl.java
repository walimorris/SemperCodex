package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.SudoPopupWindow;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SudoPopupWindowImpl implements SudoPopupWindow {
    private static final Logger LOG = LoggerFactory.getLogger(SudoPopupWindowImpl.class);

    private final Popup popupWindow;
    private final Stage currentStage;
    private PasswordField sudoField;
    private Button sudoSubmitButton;

    private StringBuilder sudo;
    private boolean sudoSet;

    public SudoPopupWindowImpl(Stage stage, PasswordField passwordField, Button submitButton) {
        currentStage = stage;
        popupWindow = new Popup();
        sudoField = passwordField;
        sudoSubmitButton = submitButton;
        sudoSet = false;

        popupWindow.getContent().addAll(sudoField, sudoSubmitButton);
    }

    @Override
    public Popup getPopupWindow() {
        return popupWindow;
    }

    @Override
    public void popup() {
        if (!popupWindow.isShowing()) {
            popupWindow.show(currentStage);
        }
    }

    @Override
    public void getSudoFromUser() {
        LOG.info("sudo submit button click: {}", sudoField.getText());
        sudo = new StringBuilder(sudoField.getText());

        if (sudo.length() > 4) {
            popupWindow.hide();
            sudoSet = true;
        }
    }

    @Override
    public void voidSudo() {
        this.sudo = null;
        this.sudoSet = false;
    }

    @Override
    public String getSudo() {
        return sudo.toString();
    }

    @Override
    public boolean isSudoSet() {
        return sudoSet;
    }

    @Override
    public Stage getCurrentPopUpWindowStage() {
        return currentStage;
    }

    @Override
    public void setSudoField(PasswordField passwordField) {
        this.sudoField = passwordField;
    }

    @Override
    public void setSubmitButton(Button button) {
        this.sudoSubmitButton = button;
    }
}
