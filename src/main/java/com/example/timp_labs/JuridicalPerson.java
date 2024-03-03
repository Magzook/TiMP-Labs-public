package com.example.timp_labs;

import java.io.FileNotFoundException;

public class JuridicalPerson extends Person implements IBehaviour {
    public static int count = 0;
    public JuridicalPerson(int x, int y) throws FileNotFoundException {
        super( x, y,"src/main/resources/juridical_person.png");
    }
}