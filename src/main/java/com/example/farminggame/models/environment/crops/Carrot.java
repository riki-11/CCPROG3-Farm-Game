package com.example.farminggame.models.environment.crops;

public class Carrot extends Crop{
    private final int MIN_PRODUCE = 1;
    private final int MAX_PRODUCE = 2;

    public Carrot() {
        super();
        this.name = "Carrot";
        this.type = "Root Crop";
        this.harvestTime = 3;
        this.waterNeeds = 1;
        this.waterBonusLimit = 2;
        this.fertilizerNeeds = 0;
        this.fertilizerBonusLimit = 1;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.cost = 10;
        this.sellingPrice = 9;
        this.xpYield = 7.5;
    }

}
