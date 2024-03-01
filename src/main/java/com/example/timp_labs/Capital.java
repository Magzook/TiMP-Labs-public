package com.example.timp_labs;

import java.io.FileNotFoundException;

public class Capital extends Building implements IBehaviour {
    public static int countCapital = 0;
    public Capital(int _x, int _y) throws FileNotFoundException {
        super( _x, _y, "src/main/resources/capitalBuilding.jpg");//явно вызываем конструктор базового класса
        countCapital++;
    }
}
