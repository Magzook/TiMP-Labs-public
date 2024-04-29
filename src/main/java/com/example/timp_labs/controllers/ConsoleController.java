package com.example.timp_labs.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ConsoleController {
    @FXML
    private TextArea textArea;
    private int numOfCharsBeforeEnter;
    public void initialize() {
        textArea.setStyle("-fx-control-inner-background: #222222; -fx-text-fill: #41DB00;");
        String welcomeText = "Windows 9: MacOS JavaScript edition\n(C) SanyaBeeline Corporation. Все права и защищены. А твоё ргз - нет. Лох!\n\n";
        textArea.setText(welcomeText);
        textArea.positionCaret(welcomeText.length());
        numOfCharsBeforeEnter = textArea.getText().length();
        // Блокировка кнопок, перемещающих курсор
        textArea.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (textArea.getText().length() < numOfCharsBeforeEnter) {
                textArea.appendText("\n");
            }
            switch (keyEvent.getCode()) {
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                case PAGE_UP:
                case PAGE_DOWN:
                case HOME:
                case END:
                    keyEvent.consume();
                    break;
                case KeyCode.ENTER:
                    numOfCharsBeforeEnter = textArea.getText().length();
                    break;
            }
        });
        // Блокировка мыши
        textArea.addEventFilter(MouseEvent.ANY, mouseEvent -> {
            mouseEvent.consume();
        });
    }
}
