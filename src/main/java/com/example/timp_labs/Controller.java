package com.example.timp_labs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.event.ActionEvent;


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
    public Button btnStart;
    @FXML
    public Button btnStop;
    @FXML
    public RadioButton btnShowInfo;
    @FXML
    public RadioButton btnShowTime;
    @FXML
    public RadioButton btnHideTime;
    @FXML
    void initialize() {
        btnStop.setDisable(true); // Кнопка "Стоп" изначально заблокирована
        btnShowTime.setSelected(true); // Переключатель "Показать время" изначально выбран
    }
    @FXML
    private void clickStart(ActionEvent event) {
        Statistics st = Statistics.getInstance();
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        if (!st.startFlag) {
            st.startAction();
        }
    }
    @FXML
    private void clickStop(ActionEvent event) {
        Statistics st = Statistics.getInstance();
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        if (!btnShowInfo.isSelected()) {
            st.restartFlag = true;
        }
        if (st.startFlag) {
            st.stopAction();
        }
    }
    @FXML
    private void clickTimeSwitch(ActionEvent event) {
        Statistics st = Statistics.getInstance();
        if (btnShowTime.isSelected()) {
            st.timeFlag = false;
        }
        else if (btnHideTime.isSelected())
        {
            st.timeFlag = true;
        }
        st.showTimer();
    }
    @FXML
    void keyPressed(KeyEvent keyEvent) {
        keyEvent.consume();
        Statistics st = Statistics.getInstance();
        switch (keyEvent.getCode()) {
            case KeyCode.T:
                st.showTimer();
                if (btnShowTime.isSelected()) {
                    btnShowTime.setSelected(false);
                    btnHideTime.setSelected(true);
                }
                else if (btnHideTime.isSelected()) {
                    btnShowTime.setSelected(true);
                    btnHideTime.setSelected(false);
                }
                break;
            case KeyCode.B:
                if (!st.startFlag) {
                    st.startAction();
                    btnStart.setDisable(true);
                    btnStop.setDisable(false);
                }
                break;
            case KeyCode.E:
                if (!btnShowInfo.isSelected()) {
                    st.restartFlag = true;
                }
                if (st.startFlag) {
                    btnStart.setDisable(false);
                    btnStop.setDisable(true);
                    st.stopAction();
                }
                break;
        }
    }
}