package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.SudoPopupWindow;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the code for {@code com/smart/peepingbill/models/impl/SudoPopupWindowImpl.java} class.
 * The SudoPopupWindow utilizing the {@link Popup} object underneath. The SudoPopupWindow is
 * used to create a Popup that requests the user to enter the Linux system sudo password. Sudo
 * is needed for access to many Linux commands. Voiding sudo means to erase the characters used.
 * This is for security reasons, as sudo will never be stored, by this application, on or off of
 * a user's system.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2021/12/26
 *
 * @see Popup
 * @see javafx.stage.PopupWindow
 *
 */
public class SudoPopupWindowImpl implements SudoPopupWindow {
    private static final Logger LOG = LoggerFactory.getLogger(SudoPopupWindowImpl.class);

    private final Popup popupWindow;
    private final Stage currentStage;
    private PasswordField sudoField;
    private Button sudoSubmitButton;

    private StringBuilder sudo;
    private boolean sudoSet;

    /**
     * Constructor for sudo Popup window. Creates a {@link SudoPopupWindowImpl} on the
     * application's current stage environment.
     * @param stage         current stage {@link Stage}
     * @param passwordField sudo key {@link PasswordField}
     * @param submitButton  SudoPopupWindow's submit button {@link Button}
     */
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
            sudoField.clear();
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
