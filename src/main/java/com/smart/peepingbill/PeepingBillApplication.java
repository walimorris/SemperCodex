package com.smart.peepingbill;

import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Defines the application logic for PeepingB!ll Application.
 */
public class PeepingBillApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(PeepingBillApplication.class);
    private static Stage primaryStage;

    private static void setPrimaryStage(Stage stage) {
        PeepingBillApplication.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return PeepingBillApplication.primaryStage;
    }

    public static boolean isLinuxFlavor() {
        try {
            LOG.info(PeepingConstants.CHECKING_SYSTEM_PROPERTIES);
            return System.getProperty(PeepingConstants.OPERATING_SYSTEM_PROPERTY).equalsIgnoreCase(PeepingConstants.LINUX);
        } catch (NullPointerException e) {
            LOG.error(PeepingConstants.ERROR_GETTING_SYSTEM_PROPERTIES, PeepingConstants.OPERATING_SYSTEM_PROPERTY, e.getMessage());
            return false;
        }
    }

    @Override
    public void start(Stage stage) {
        peepingBillLogin(stage);
    }

    public static void peepingBillLogin(Stage stage) {
        if (isLinuxFlavor()) {
            LOG.info(PeepingConstants.SYSTEM_CHECK_PASS, System.getProperty(PeepingConstants.OPERATING_SYSTEM_PROPERTY),
                    System.getProperty(PeepingConstants.OPERATING_SYSTEM_ARCHITECTURE_PROPERTY),
                    System.getProperty(PeepingConstants.OPERATING_SYSTEM_VERSION));

            setPrimaryStage(stage);
            FXMLLoader fxmlLoader = new FXMLLoader(PeepingBillApplication.class.getResource(PeepingConstants.LOGIN_VIEW));

            try {
                Scene scene = new Scene(fxmlLoader.load(), 640, 480);
                stage.setTitle(PeepingConstants.APPLICATION_SIGNATURE_NAME);
                stage.setScene(scene);
                stage.show();
                LOG.info(PeepingConstants.PEEPING_BILL_LOGIN_SCENE_APPLIED);
            } catch (IOException e) {
                LOG.error(PeepingConstants.ERROR_CREATING_PRIMARY_STAGE_SCENE, PeepingBillApplication.class, e.getLocalizedMessage());
            }
        } else {
            LOG.info(PeepingConstants.SYSTEM_SUPPORT_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}