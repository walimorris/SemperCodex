package com.smart.peepingbill;

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

    @Override
    public void start(Stage stage) {
        peepingBillLogin(stage);
    }

    public static void peepingBillLogin(Stage stage) {
        setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(PeepingBillApplication.class.getResource("login-view.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            stage.setTitle("Peep!ng Bill");
            stage.setScene(scene);
            stage.show();
            LOG.info("PeepingBillApplication Login scene applied");
        } catch (IOException e) {
            LOG.error("Error creating Primary Stage Scene from {} Login Page {}: ", PeepingBillApplication.class, e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}