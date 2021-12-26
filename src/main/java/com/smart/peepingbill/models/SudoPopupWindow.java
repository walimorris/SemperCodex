package com.smart.peepingbill.models;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.SudoPopupWindowImpl} class.
 */
public interface SudoPopupWindow {

    /**
     * Returns sudo popup window.
     * @return {@link Popup}
     */
    Popup getPopupWindow();

    /**
     * Show sudo popup window.
     */
    void popup();

    /**
     * Remove and null sudo key.
     */
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
