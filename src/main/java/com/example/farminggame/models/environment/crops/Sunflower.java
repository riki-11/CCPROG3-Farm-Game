package com.example.farminggame.models.environment.crops;

/**
 * Represents a Sunflower which is one type of Crop
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Sunflower extends Crop {

    private final int MIN_PRODUCE = 1;
    private final int MAX_PRODUCE = 1;

    /**
     * Creates a Sunflower with its corresponding attributes
     */
    public Sunflower() {
        super();
        this.name = "Sunflower";
        this.type = "Flower";
        this.harvestTime = 3;
        this.waterNeeds = 2;
        this.waterBonusLimit = 3;
        this.fertilizerNeeds = 1;
        this.fertilizerBonusLimit = 2;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 20;
        this.sellingPrice = 19;
        this.xpYield = 8.5;
    }
}
