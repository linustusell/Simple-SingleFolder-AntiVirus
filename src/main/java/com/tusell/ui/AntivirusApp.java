package com.tusell.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AntivirusApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main-view.fxml"));
        Scene scene = new Scene(loader.load(), 900, 620);
        scene.getStylesheets().add(getClass().getResource("/ui/styles.css").toExternalForm());

        stage.setTitle("AntiVirus - JavaFX");
        stage.setScene(scene);
        stage.setMinWidth(860);
        stage.setMinHeight(580);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
