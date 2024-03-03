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
        Habitat hab = Habitat.getInstance();
        if (keyEvent.getCode().equals(KeyCode.T)) {
            showTimer();
        } else if (keyEvent.getCode().equals(KeyCode.B)) {
            if (!hab.startFlag) {
                hab.startAction();
            }
        } else if (keyEvent.getCode().equals(KeyCode.E)) {
            if (hab.startFlag) {
                hab.stopAction();
            }
        }
    }
    public static void showStatisticLabel() {
        Habitat hab = Habitat.getInstance();
        if (hab.getStatisticFlag()) {
            String statistic = "Физические лица: " + PhysicalPerson.count+ "\nЮридические лица: " + JuridicalPerson.count;
            statistic += "\nВремя: " + (System.currentTimeMillis() - hab.getStartTime())/1000 + " сек";
            hab.mainController.getStatistic().setText(statistic);
            hab.mainController.getStatistic().setVisible(true);
            hab.mainController.getLabelTimer().setVisible(false);
            hab.mainController.getLabelTextTIMER().setVisible(false);
        }
        else {
            hab.mainController.getStatistic().setVisible(false);
            hab.mainController.getStatistic().setText("");
            hab.mainController.getLabelTimer().setVisible(true);
            hab.mainController.getLabelTextTIMER().setVisible(true);
        }
    }
    public static void showTimer(){
        Habitat hab = Habitat.getInstance();
        hab.timeFlag = !hab.timeFlag;
        if (hab.timeFlag) {
            hab.mainController.getLabelTextTIMER().setVisible(true);
            hab.mainController.getLabelTimer().setVisible(true);
        }
        else {
            hab.mainController.getLabelTextTIMER().setVisible(false);
            hab.mainController.getLabelTimer().setVisible(false);
        }
    }
    public static void updateTimer() {
        Habitat hab = Habitat.getInstance();
        String min = hab.getMinutes() + "";
        String sec = hab.getSeconds() + "";
        if (min.length() < 2)
            min = "0"+ min;
        if (sec.length() < 2)
            sec = ("0" + sec);
        String time = min + ":" + sec;
        hab.mainController.getLabelTimer().setText(time);
    }
}