package com.example.timp_labs;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

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
    public Button btnStart, btnStop;
    @FXML
    public RadioButton btnShowInfo, btnShowTime, btnHideTime;
    @FXML
    public TextField fieldN1, fieldN2;
    @FXML
    void initialize() {
        btnStop.setDisable(true); // Кнопка "Стоп" изначально заблокирована
        btnShowTime.setSelected(true); // Переключатель "Показать время" изначально выбран
    }
    @FXML
    private void clickStart() {
        try {
            Habitat hab = Habitat.getInstance();
            hab.n1 = Integer.parseInt(fieldN1.getText());
            hab.n2 = Integer.parseInt(fieldN2.getText());
            if (hab.n1 < 1 || hab.n2 < 1) {
                throw new NumberFormatException("Слишком маленькое число");
            }
            // Если данные некорректны, работает блок catch, иначе идём дальше
            fieldN1.setDisable(true);
            fieldN2.setDisable(true);
            Statistics st = Statistics.getInstance();
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            if (!st.startFlag) {
                st.startAction();
            }
        }
        catch (NumberFormatException ex) {
            fieldN1.setText("1");
            fieldN2.setText("2");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Некорректный период рождения");
            alert.setContentText("Требуется целое положительное число, не превышающее 2^31-1. Выставлены значения по умолчанию.");
            alert.showAndWait();
        }
    }
    @FXML
    private void clickStop() {
        Statistics st = Statistics.getInstance();
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        if (!btnShowInfo.isSelected()) {
            st.restartFlag = true;
            fieldN1.setDisable(false);
            fieldN2.setDisable(false);
        }
        if (st.startFlag) {
            st.stopAction();
        }
    }
    @FXML
    private void clickTimeSwitch() {
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
                clickStart();
//                if (!st.startFlag) {
//                    st.startAction();
//                    btnStart.setDisable(true);
//                    btnStop.setDisable(false);
//                }
                break;
            case KeyCode.E:
                clickStop();
//                if (!btnShowInfo.isSelected()) {
//                    st.restartFlag = true;
//                }
//                if (st.startFlag) {
//                    btnStart.setDisable(false);
//                    btnStop.setDisable(true);
//                    st.stopAction();
//                }
                break;
        }
    }
}