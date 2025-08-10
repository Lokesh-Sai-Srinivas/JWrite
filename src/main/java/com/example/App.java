package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The main entry point for the JWrite Java editor application.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Use a path relative to the root of the resources folder
        URL fxmlUrl = getClass().getResource("/com/example/MainView.fxml");
        if (fxmlUrl == null) {
            System.err.println("Cannot find FXML file. Make sure it's in the correct resources folder.");
            System.exit(1);
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root, 1600, 900);

        primaryStage.setTitle("JWrite - Java Code Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
