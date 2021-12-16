package com.smart.peepingbill.controllers;

import com.smart.peepingbill.PeepingBillApplication;
import com.smart.peepingbill.models.Password;
import com.smart.peepingbill.models.impl.PasswordImpl;
import com.smart.peepingbill.models.impl.User;
import com.smart.peepingbill.util.DBUtil;
import com.smart.peepingbill.util.ReasonUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the code for {@code com/smart/peepingbill/create-user.fxml}.
 */
public class CreateUserController {
    private static final Logger LOG = LoggerFactory.getLogger(CreateUserController.class);

    @FXML
    private TextField createUserName;

    @FXML
    private PasswordField createUserPassword;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Label successLabel = new Label();

    @FXML
    private Button backButton;

    @FXML
    private Button createUserButton;

    @FXML
    private Button helpButton;

    @FXML
    protected void redirectToLogin(ActionEvent event) {
        if (event.getTarget().equals(backButton)) {
            loadLoginPage();
        }
        String success = createUser(createUserName, createUserPassword, confirmPassword);
        if (StringUtils.equals(success, PeepingConstants.SUCCESS)) {
            successLabel.setText(success);
            successLabel.setStyle("-fx-text-fill: green");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                LOG.error("Thread interruption error during pause after new user creation: {}", e.getMessage());
                // restore interrupted state
                Thread.currentThread().interrupt();
            }
            loadLoginPage();
        } else {
            successLabel.setText(success);
            successLabel.setStyle("-fx-text-fill: #ff0000");
        }
    }

    /**
     * Defines the create user process and creates user in database after validation.
     * @param user {@link TextField} user text from create user view
     * @param password {@link PasswordField} password text from create user view
     * @param confirmPassword {@link PasswordField} password confirm text from create user view
     * @return {@link String} user creation validation message
     */
    private String createUser(TextField user, PasswordField password, PasswordField confirmPassword) {
        if (!StringUtils.isAnyEmpty(user.getText(), password.getText(), confirmPassword.getText())) {
            if (StringUtils.equals(password.getText(), confirmPassword.getText())) {
                if (isValidPassword().equals(PeepingConstants.SUCCESS)) {
                    User createdUser = new User(user.getText(), password.getText());
                    DBUtil.getInstance().usersDatabaseConnect();
                    String createdUserResult = DBUtil.getInstance().insertNewUser(createdUser,
                            ReasonUtil.getCreateUserReason());
                    DBUtil.getInstance().close();
                    resetPassword();
                    return createdUserResult.equals(PeepingConstants.SUCCESS) ? PeepingConstants.SUCCESS :
                            PeepingConstants.USER_UNAVAILABLE;
                } else {
                    String message = isValidPassword();
                    resetDestroyPassword();
                    return message;
                }
            } else {
                resetDestroyPassword();
                return PeepingConstants.PASSWORD_MISMATCH;
            }
        }
        resetDestroyPassword();
        return PeepingConstants.MISSING_PROPERTY;
    }

    /**
     * Loads login view.
     */
    private void loadLoginPage() {
        Stage stage = PeepingBillApplication.getPrimaryStage();
        PeepingBillApplication.peepingBillLogin(stage);
    }

    /**
     * Initializes an {@link Password} object to validate password from new user creation attempt.
     * @return {@link String} password validation message.
     */
    private String isValidPassword() {
        Password validatedPassword = new PasswordImpl(createUserPassword.getText());
        String isValid = validatedPassword.isPasswordValid(validatedPassword.getPassword());
        validatedPassword.voidPassword();
        return isValid;
    }

    /**
     *
     */
    private void resetPassword() {
        createUserPassword.setText(null);
    }

    /**
     * Destroys and resets current password.
     */
    private void resetDestroyPassword() {
        createUserPassword.setText(null);
    }
}
