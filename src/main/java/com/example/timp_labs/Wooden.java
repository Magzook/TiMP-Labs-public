package com.example.timp_labs;

import java.io.FileNotFoundException;

public class Wooden extends Building implements IBehaviour{
    public static int countWooden=0;
    public Wooden(int _x, int _y) throws FileNotFoundException {
        super( _x, _y,"src/main/resources/woodenBuilding.jpg");
        countWooden++;
    }
}