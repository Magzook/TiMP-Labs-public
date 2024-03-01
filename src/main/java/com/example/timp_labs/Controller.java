package com.example.timp_labs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Controller {
    @FXML
    private Label labelTextTIMER;
    @FXML
    private Label labelTimer;
    @FXML
    private GridPane pane;
    @FXML
    private Pane modalPane;
    @FXML
    private Label statistic;

    public Label getLabelTextTIMER() {
        return labelTextTIMER;
    }

    public Label getLabelTimer() {
        return labelTimer;
    }
    public Label getStatistic() {
        return statistic;
    }

    public Pane getPane() {
        return modalPane;
    }
    @FXML
    void keyPressed(KeyEvent keyEvent) {
        keyEvent.consume();
        if (keyEvent.getCode().equals(KeyCode.T)) {
            Habitat.getInstance().showTimer();
        } else if (keyEvent.getCode().equals(KeyCode.B)) {
            if (!Habitat.getInstance().startFlag) {
                Habitat.getInstance().startAction();
            }
        } else if (keyEvent.getCode().equals(KeyCode.E)) {
            if (Habitat.getInstance().startFlag) {
                Habitat.getInstance().stopAction();
            }
        }

    }
}