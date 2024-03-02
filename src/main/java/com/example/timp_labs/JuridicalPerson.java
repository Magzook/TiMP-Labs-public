package com.example.timp_labs;

import java.io.FileNotFoundException;

public class JuridicalPerson extends Person implements IBehaviour{
    public static int countJuridicalPerson=0;
    public JuridicalPerson(int _x, int _y) throws FileNotFoundException {
        super( _x, _y,"src/main/resources/juridical_person.png");
        countJuridicalPerson++;
    }
}