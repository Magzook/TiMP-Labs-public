package com.example.timp_labs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Controller {
    @FXML
    private Label labelTextTIMER;
    @FXML
    private Label labelTimer;
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
        Statistics st = Statistics.getInstance();
        if (keyEvent.getCode().equals(KeyCode.T)) {
            st.showTimer();
        } else if (keyEvent.getCode().equals(KeyCode.B)) {
            if (!st.startFlag) {
                st.startAction();
            }
        } else if (keyEvent.getCode().equals(KeyCode.E)) {
            if (st.startFlag) {
                st.stopAction();
            }
        }
    }
}