package com.example.farminggame.models.environment.crops;

/**
 * Represents a Mango which is one type of Crop
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Mango extends Crop {

    private final int MIN_PRODUCE = 5;
    private final int MAX_PRODUCE = 15;

    /**
     * Creates a Mango with its corresponding attributes
     */
    public Mango() {
        super();
        this.name = "Mango";
        this.type = "Fruit Tree";
        this.harvestTime = 10;
        this.waterNeeds = 7;
        this.waterBonusLimit = 7;
        this.fertilizerNeeds = 5;
        this.fertilizerBonusLimit = 5;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 100;
        this.sellingPrice = 8;
        this.xpYield = 25;
    }

}
