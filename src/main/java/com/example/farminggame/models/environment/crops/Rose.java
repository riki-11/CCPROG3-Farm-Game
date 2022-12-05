package com.example.farminggame.models.environment.crops;

public class Rose extends Crop {

    private final int MIN_PRODUCE = 1;
    private final int MAX_PRODUCE = 1;

    public Rose() {
        super();
        this.name = "Rose";
        this.type = "Flower";
        this.harvestTime = 1;
        this.waterNeeds = 1;
        this.waterBonusLimit = 2;
        this.fertilizerNeeds = 0;
        this.fertilizerBonusLimit = 1;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 5;
        this.sellingPrice = 5;
        this.xpYield = 2.5;
    }
}
