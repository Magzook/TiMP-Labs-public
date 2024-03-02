package com.example.timp_labs;

import java.io.FileNotFoundException;

public class PhysicalPerson extends Person implements IBehaviour {
    public static int countPhysicalPerson = 0;
    public PhysicalPerson(int _x, int _y) throws FileNotFoundException {
        super( _x, _y, "src/main/resources/physical_person.png");//явно вызываем конструктор базового класса
        countPhysicalPerson++;
    }
}
