package client.controllers;

import client.model.JuridicalPerson;
import client.model.Person;
import client.model.PhysicalPerson;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

public class ConsoleController {
    @FXML
    private TextArea textArea;
    private int numOfCharsBeforeEnter;
    public void initialize() {
        textArea.setStyle("-fx-control-inner-background: #222222; -fx-text-fill: #41DB00;");
        String welcomeText = "Windows 13: Cursed edition\n(C) Podval Corporation. Все права и лева защищены\n" +
                "Используйте команду help для помощи\n\n";
        textArea.setText(welcomeText);
        textArea.positionCaret(welcomeText.length());
        numOfCharsBeforeEnter = textArea.getText().length();
        // Блокировка кнопок
        textArea.addEventFilter(KeyEvent.ANY, keyEvent -> {
            // Запрет на стирание символов до последнего Enter'а
            if (textArea.getText().length() < numOfCharsBeforeEnter) {
                textArea.appendText("\n");
            }
            // Запрет на комбинации клавиш с Ctrl
            if (keyEvent.isControlDown()) keyEvent.consume();
            else switch (keyEvent.getCode()) {
                // Запрет на клавиши, перемещающие курсор
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
                // При нажатии Enter'а извлечь команду и запомнить количество введённых символов. Их уже нельзя будет стереть
                case ENTER:
                    String text = textArea.getText();
                    numOfCharsBeforeEnter = text.length();
                    String command = text.substring(text.lastIndexOf("\n") + 1);
                    execute(command);
                    break;
            }
        });

        // Запрет на использование мыши
        textArea.addEventFilter(MouseEvent.ANY, Event::consume);
    }
    private void execute(String command) {
        if (command.isEmpty()) return;
        String[] arr = command.split(" ", 2);
        String firstWord = arr[0];
        String parameter = "";
        if (arr.length > 1) parameter = arr[1];
        switch (firstWord) {
            case "help":
                helpCommand();
                break;
            case "alive":
                aliveCommand(parameter);
                break;
            default:
                textArea.appendText("\nНеизвестная команда\n");
        }
    }
    private void helpCommand() {
        textArea.appendText("\nКоманды:\n\talive <параметр> - вывести количество живых объектов одного типа\n" +
                                           "\tjoke - рассказать анекдот (В разработке)\n");
    }
    private void aliveCommand(String parameter) {
        Vector<Person> collection = Habitat.getInstance().getObjCollection();
        int count = 0;
        switch (parameter) {
            case "p":
                synchronized (collection) {for (var obj : collection) if (obj instanceof PhysicalPerson) count++;}
                textArea.appendText("\nФизических лиц: " + count);
                break;
            case "j":
                synchronized (collection) {for (var obj : collection) if (obj instanceof JuridicalPerson) count++;}
                textArea.appendText("\nЮридических лиц: " + count);
                break;
            default:
                textArea.appendText("\nalive <параметр>. Возможные параметры:\np - физические лица, j - юридические лица");
                break;
        }
    }
}
