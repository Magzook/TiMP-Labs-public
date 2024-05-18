package client.model;

public class AIPhysical extends BaseAI {
    private static AIPhysical instance;
    public static AIPhysical getInstance() {
        if (instance == null) {
            instance = new AIPhysical("AI Physical");
        }
        return instance;
    }
    private AIPhysical(String name) {
        super(name);
        // Случайная точка назначения для физических лиц: x:[550; 1020] y:[300; 520] (нижняя правая четверть)
        leftBoundX = 550;
        rightBoundX = 1020;
        lowerBoundY = 300;
        upperBoundY = 520;
        objectType = "PhysicalPerson";
        monitor = "monitor phy";
    }
}
