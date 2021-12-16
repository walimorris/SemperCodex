package com.smart.peepingbill.models;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Popup;
import javafx.stage.Stage;

public interface SudoPopupWindow {

    /**
     * Returns sudo popup window.
     * @return {@link Popup}
     */
    Popup getPopupWindow();

    void popup();

    void voidSudo();

    /**
     * Adds sudo popup to current stage window.
     */
    void getSudoFromUser();

    /**
     * Get sudo.
     * @return {@link String}
     */
    String getSudo();

    /**
     * Monitor for sudo. Reports if sudo is set.
     * @return boolean
     */
    boolean isSudoSet();

    /**
     * Get Popups {@link SudoPopupWindow} current stage.
     * @return {@link Stage}
     */
    Stage getCurrentPopUpWindowStage();

    /**
     * Set sudo passwordField
     * @param passwordField : {@link PasswordField}
     */
    void setSudoField(PasswordField passwordField);

    /**
     * Set submit button
     * @param button : {@link Button}
     */
    void setSubmitButton(Button button);
}
