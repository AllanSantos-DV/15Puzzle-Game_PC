package com.allan._15puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PuzzleApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PuzzleApp.class.getResource("15Puzzle-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.getIcons().add(new Image(Objects.requireNonNull(PuzzleApp.class.getResourceAsStream("/icons/icons-15puzzle.png"))));
        stage.setTitle("15Puzzle!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}