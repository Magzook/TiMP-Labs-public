package com.example.timp_labs;

import java.io.FileNotFoundException;

public class PhysicalPerson extends Person implements IBehaviour {
    public static int count = 0;
    public PhysicalPerson(int x, int y) throws FileNotFoundException {
        super( x, y, "src/main/resources/physical_person.png");
    }
}
