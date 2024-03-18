package com.example.timp_labs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    public Label getLabelTextTIMER() {
        return labelTextTIMER;
    }
    public Label getLabelTimer() {
        return labelTimer;
    }
    public Pane getPane() {
        return modalPane;
    }

    @FXML
    public Button btnStart, btnStop;
    @FXML
    public CheckBox btnShowInfo;
    @FXML
    public RadioButton btnShowTime, btnHideTime;
    @FXML
    public TextField fieldN1, fieldN2;
    @FXML
    public ComboBox<String> boxP1, boxP2;
    @FXML
    public MenuItem menuStart, menuStop, menuExit;
    @FXML
    public CheckMenuItem menuShowInfo;
    @FXML
    public RadioMenuItem menuShowTime, menuHideTime;
    @FXML
    void initialize() {
        btnStop.setDisable(true); // Кнопка "Стоп" изначально заблокирована
        menuStop.setDisable(true);
        btnShowTime.setSelected(true); // Переключатель "Показать время" изначально выбран
        menuShowTime.setSelected(true);
        fieldN1.setText("1"); // Значения для текстовых полей по умолчанию
        fieldN2.setText("2");
        // Дальше идёт заполнение комбобоксов
        for (int value = 0; value <= 100; value += 10) {
            boxP1.getItems().add(value + "%");
            boxP2.getItems().add(value + "%");
        }
        boxP1.setValue("60%"); // Значения для комбобоксов по умолчанию
        boxP2.setValue("30%");
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
            hab.p1 = Float.parseFloat(boxP1.getValue().replace("%", "")) / 100;
            hab.p2 = Float.parseFloat(boxP2.getValue().replace("%", "")) / 100;
            fieldN1.setDisable(true);
            fieldN2.setDisable(true);
            boxP1.setDisable(true);
            boxP2.setDisable(true);
            btnStart.setDisable(true);
            btnStop.setDisable(false);
            menuStart.setDisable(true);
            menuStop.setDisable(false);
            Statistics st = Statistics.getInstance();
            st.startAction();

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
        menuStart.setDisable(false);
        menuStop.setDisable(true);
        if (!btnShowInfo.isSelected()) {
            st.restartFlag = true;
            fieldN1.setDisable(false);
            fieldN2.setDisable(false);
            boxP1.setDisable(false);
            boxP2.setDisable(false);
        }
        st.stopAction();
    }
    @FXML
    private void clickInfo() {
        menuShowInfo.setSelected(!menuShowInfo.isSelected());
    }
    @FXML
    private void menuClickInfo() {
        btnShowInfo.setSelected(!btnShowInfo.isSelected());
    }
    @FXML
    private void clickTimeSwitch() {
        Statistics st = Statistics.getInstance();
        if (btnShowTime.isSelected()) {
            st.timeFlag = false;
            menuShowTime.setSelected(true);
            menuHideTime.setSelected(false);
        }
        else if (btnHideTime.isSelected())
        {
            st.timeFlag = true;
            menuShowTime.setSelected(false);
            menuHideTime.setSelected(true);
        }
        st.showTimer();
    }
    @FXML
    private void menuClickTimeSwitch() {
        Statistics st = Statistics.getInstance();
        if (menuShowTime.isSelected()) {
            st.timeFlag = false;
            btnShowTime.setSelected(true);
            btnHideTime.setSelected(false);
        }
        else if (menuHideTime.isSelected())
        {
            st.timeFlag = true;
            btnShowTime.setSelected(false);
            btnHideTime.setSelected(true);
        }
        st.showTimer();
    }
    @FXML
    public void menuClickExit() {
        Platform.exit();
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
                    menuShowTime.setSelected(false);
                    btnHideTime.setSelected(true);
                    menuHideTime.setSelected(true);
                }
                else if (btnHideTime.isSelected()) {
                    btnShowTime.setSelected(true);
                    menuShowTime.setSelected(true);
                    btnHideTime.setSelected(false);
                    menuHideTime.setSelected(false);
                }
                break;
            case KeyCode.B:
                if (!st.startFlag) {
                    clickStart();
                }
                break;
            case KeyCode.E:
                if (st.startFlag) {
                    clickStop();
                }
                break;
        }
    }
}