package client.model;

public class AIJuridical extends BaseAI {
    private static AIJuridical instance;
    public static AIJuridical getInstance() {
        if (instance == null) {
            instance = new AIJuridical("AI Juridical");
        }
        return instance;
    }
    private AIJuridical(String name) {
        super(name);
        // Случайная точка назначения для юридических лиц: x:[0; 470] y:[0; 220] (левая верхняя четверть)
        leftBoundX = 0;
        rightBoundX = 470;
        lowerBoundY = 0;
        upperBoundY = 220;
        objectType = "JuridicalPerson";
        monitor = "monitor jur";
    }
}
