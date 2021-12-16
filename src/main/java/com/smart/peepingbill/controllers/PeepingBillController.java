package com.smart.peepingbill.controllers;

import com.smart.peepingbill.PeepingBillApplication;
import com.smart.peepingbill.models.impl.User;
import com.smart.peepingbill.util.DBUtil;
import com.smart.peepingbill.util.ReasonUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Defines the code for {@code com/smart/peepingbill/login-view.fxml}.
 */
public class PeepingBillController {
    private static final Logger LOG = LoggerFactory.getLogger(PeepingBillController.class);

    private static final String FALSE = "false";
    private static final String TRUE = "true";

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    private Label headerText;

    @FXML
    private Button loginButton;

    @FXML
    private Button newUserButton;

    @FXML
    private Label loginMessage;

    @FXML
    private Button helpButton;

    @FXML
    private Image peepingBillImage;

    @FXML
    protected void onLoginButtonSubmit() {
        LOG.info(PeepingConstants.LOG_LOGIN_PROCESS_LOGIN_ACTIVATED_MESSAGE + "' {} '", user.getText());
        password.setText(password.getText());
        user.setText(user.getText());
        if (StringUtils.isNotEmpty(password.getText()) && StringUtils.isNotEmpty(user.getText())) {
            User currentUser = new User(user.getText(), password.getText());
            DBUtil.getInstance().usersDatabaseConnect();
            String success = DBUtil.getInstance().findUser(currentUser, ReasonUtil.getLoginUserReason());
            LOG.info(PeepingConstants.LOG_LOGIN_PROCESSED_MESSAGE + "' {} '", success);

            DBUtil.getInstance().close();
            actOnLoginSuccessMessage(success);
        } else {
            renderEmptyUserCredentialMessage();
        }
        resetLoginCredentials();
    }

    /**
     * Resets {@link TextField} user and {@link PasswordField} password.
     */
    private void resetLoginCredentials() {
        password.setText(PeepingConstants.EMPTY_STRING);
        user.setText(PeepingConstants.EMPTY_STRING);
    }

    /**
     * Renders login message for invalid user login credentials such as empty user name,
     * empty password or both.
     */
    private void renderEmptyUserCredentialMessage() {
        if (StringUtils.isEmpty(user.getText()) && StringUtils.isEmpty(password.getText())) {
            loginMessage.setText(PeepingConstants.EMPTY_USER_CREDENTIALS_TEXT);
            LOG.info(PeepingConstants.LOG_LOGIN_CREDENTIAL_ERROR_PREFIX + "{}", loginMessage.getText());
        } else if (StringUtils.isEmpty(user.getText())) {
            loginMessage.setText(PeepingConstants.EMPTY_USER_CREDENTIAL_TEXT);
            LOG.info(PeepingConstants.LOG_LOGIN_CREDENTIAL_ERROR_PREFIX + "{}", loginMessage.getText());
        } else {
            loginMessage.setText(PeepingConstants.EMPTY_PASSWORD_CREDENTIAL_TEXT);
            LOG.info(PeepingConstants.LOG_LOGIN_CREDENTIAL_ERROR_PREFIX + "{}", loginMessage.getText());
        }
    }

    private void actOnLoginSuccessMessage(String success) {
        switch(success) {
            case FALSE:
                loginMessage.setText(PeepingConstants.INVALID_CREDENTIALS_MESSAGE_1);
                break;
            case TRUE:
                loadPadLandingPage();
                break;
            default:
                loginMessage.setText(success);
                break;
        }
    }

    @FXML
    protected void onUserCreateSubmit() {
        LOG.info("{}", PeepingConstants.LOG_CREATE_USER_PROCESS_MESSAGE);
        try {
            Stage stage = PeepingBillApplication.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(PeepingBillApplication.class.getResource(
                    PeepingConstants.CREATE_USER_VIEW));

            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            stage.setTitle(PeepingConstants.CREATE_USER_VIEW_TITLE);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOG.error(PeepingConstants.LOG_ERROR_LOADING_CREATE_USER_SCENE + "{}", e.getMessage());
        }
    }

    private void loadPadLandingPage() {
        try {
            Stage stage = PeepingBillApplication.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(PeepingBillApplication.class.getResource(
                    PeepingConstants.PAD_LANDING_VIEW));

            Scene scene = new Scene(fxmlLoader.load());
            stage.setMaximized(true);
            stage.setTitle(PeepingConstants.PAD_LANDING_VIEW_TITLE);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOG.error(PeepingConstants.LOG_ERROR_LOADING_PAD_BUILDER_SCENE + "{}", e.getMessage());
        }
    }
}