package com.example.timp_labs.model;

import com.example.timp_labs.Habitat;

import java.util.Vector;

public class AIPhysical extends BaseAI {
    public AIPhysical(String name) {
        super(name);
        // Случайная точка назначения для физических лиц: x:[550; 1020] y:[300; 520] (нижняя правая четверть)
        leftBoundX = 550;
        rightBoundX = 1020;
        lowerBoundY = 300;
        upperBoundY = 520;
        objectType = "PhysicalPerson";
    }
}
