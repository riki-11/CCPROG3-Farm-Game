package com.example.farminggame.models.environment.crops;

public class Tulip extends Crop {

    private final int MIN_PRODUCE = 1;
    private final int MAX_PRODUCE = 1;

    public Tulip() {
        super();
        this.name = "Tulip";
        this.type = "Flower";
        this.harvestTime = 2;
        this.waterNeeds = 2;
        this.waterBonusLimit = 3;
        this.fertilizerNeeds = 0;
        this.fertilizerBonusLimit = 1;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 10;
        this.sellingPrice = 9;
        this.xpYield = 5;
    }
}
