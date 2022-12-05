package com.example.farminggame.models.environment.crops;

public class Turnip extends Crop {

    private final int MIN_PRODUCE = 1;
    private final int MAX_PRODUCE = 2;

    public Turnip() {
        super();
        this.name = "Turnip";
        this.type = "Root Crop";
        this.harvestTime = 2;
        this.waterNeeds = 1;
        this.waterBonusLimit = 2;
        this.fertilizerNeeds = 0;
        this.fertilizerBonusLimit = 1;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 5;
        this.sellingPrice = 6;
        this.xpYield = 5;
    }
}


