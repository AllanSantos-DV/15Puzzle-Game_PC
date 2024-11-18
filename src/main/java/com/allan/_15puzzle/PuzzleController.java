package com.allan._15puzzle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.List;

public class PuzzleController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btn13, btn14, btn15;

    @FXML
    private Label lblTimer, lblMoveCounter;

    @FXML
    private Button btnStart, btnReset;

    private List<Button> puzzleButtons;
    private final List<String> targetWords = List.of("MATO", "AMOR", "COCA", "UVA");
    private int emptyRow = 3;
    private int emptyCol = 3;
    private int elapsedTime = 0;
    private int moveCount = 0;
    private Timeline gameTimer;

    @FXML
    private void initialize() {
        btnStart.setOnAction(e -> startGame());
        btnReset.setOnAction(e -> resetGame());
        puzzleButtons = List.of(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btn13, btn14, btn15);
        puzzleButtons.forEach(button -> button.setDisable(true));
        puzzleButtons.forEach(button -> button.setOnAction(e -> {
            if (isMoveValid(button)) {
                moveButton(button);
                updateMoveCounter();
            }
        }));
        setupGameTimer();
    }

    private boolean isMoveValid(Button button) {
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);
        return Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1;
    }

    private void moveButton(Button button) {
        int newEmptyRow = GridPane.getRowIndex(button);
        int newEmptyCol = GridPane.getColumnIndex(button);
        gridPane.getChildren().remove(button);
        gridPane.add(button, emptyCol, emptyRow);
        emptyRow = newEmptyRow;
        emptyCol = newEmptyCol;
    }

    private Map<Integer, String> getButtonMap() {
        return gridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .collect(TreeMap::new,
                        (map, node) -> {
                            int row = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
                            int col = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
                            int index = row * 4 + col;
                            map.put(index, ((Button) node).getText());
                        },
                        TreeMap::putAll
                );
    }

    private void checkIfPuzzleIsSolved() {
        String currentText = getButtonMap().values().stream()
                .reduce("", String::concat);

        String targetText = targetWords.stream()
                .reduce("", String::concat);

        if (currentText.equals(targetText) && (emptyRow == 3 && emptyCol == 3)) {
            gameTimer.stop();
            showVictoryDialog();
            resetGame();
        }
    }

    private void setupGameTimer() {
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedTime++;
            lblTimer.setText(formatElapsedTime(elapsedTime));
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
    }

    private void startGame() {
        puzzleButtons.forEach(button -> button.setDisable(false));
        elapsedTime = 0;
        moveCount = 0;
        lblTimer.setText("00:00");
        lblMoveCounter.setText("0");
        gameTimer.play();
        shuffleAndPlaceButtons();
    }

    private void resetGame() {
        puzzleButtons.forEach(button -> button.setDisable(true));
        gameTimer.stop();
        elapsedTime = 0;
        moveCount = 0;
        lblTimer.setText("00:00");
        lblMoveCounter.setText("0");
        resetPuzzleBoard();
    }

    private void resetPuzzleBoard() {
        gridPane.getChildren().removeAll(puzzleButtons);
        placeButtonsOnGrid(puzzleButtons);
    }

    private void shuffleAndPlaceButtons() {
        List<Button> shuffledButtons = new ArrayList<>(puzzleButtons);
        Collections.shuffle(shuffledButtons);
        gridPane.getChildren().removeAll(puzzleButtons);
        placeButtonsOnGrid(shuffledButtons);
    }

    private void placeButtonsOnGrid(List<Button> buttonList) {
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (index < buttonList.size()) {
                    gridPane.add(buttonList.get(index), col, row);
                    index++;
                }
            }
        }
        emptyRow = 3;
        emptyCol = 3;
    }

    private void updateMoveCounter() {
        moveCount++;
        lblMoveCounter.setText(String.valueOf(moveCount));
        checkIfPuzzleIsSolved();
    }

    private String formatElapsedTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void showVictoryDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vitória!");
        alert.setHeaderText(null);
        alert.setContentText("Parabéns! Você venceu!\nTempo: " + lblTimer.getText() + "\nMovimentos: " + lblMoveCounter.getText());

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icons-15puzzle_complete.png"))));

        alert.showAndWait();
    }

}