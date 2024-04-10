package com.example.timp_labs.model;

import com.example.timp_labs.Habitat;

import java.util.Vector;

public class AIJuridical extends BaseAI {
    public AIJuridical(String name) {
        super(name);
        // Случайная точка назначения для юридических лиц: x:[0; 470] y:[0; 220] (левая верхняя четверть)
        leftBoundX = 0;
        rightBoundX = 470;
        lowerBoundY = 0;
        upperBoundY = 220;
        objectType = "JuridicalPerson";
    }
}
