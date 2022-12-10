package com.example.farminggame.models.environment.crops;

/**
 * Represents a Potato which is one type of Crop
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Potato extends Crop {

    private final int MIN_PRODUCE = 1;
    private final int MAX_PRODUCE = 10;

    /**
     * Creates a Potato with its corresponding attributes
     */
    public Potato() {
        super();
        this.name = "Potato";
        this.type = "Root Crop";
        this.harvestTime = 5;
        this.waterNeeds = 3;
        this.waterBonusLimit = 4;
        this.fertilizerNeeds = 1;
        this.fertilizerBonusLimit = 2;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 20;
        this.sellingPrice = 3;
        this.xpYield = 12.5;
    }
}
