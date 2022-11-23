package com.example.farminggame.models.environment.crops;

public class Apple extends Crop {

    private final int MIN_PRODUCE = 10;
    private final int MAX_PRODUCE = 15;

    public Apple() {
        super();
        this.seedName = "Apple";
        this.cropType = "Fruit Tree";
        this.harvestTime = 10;
        this.waterNeeds = 7;
        this.waterBonusLimit = 7;
        this.fertilizerNeeds = 5;
        this.fertilizerBonusLimit = 5;
        this.produce = (int) ((Math.random() * (MAX_PRODUCE - MIN_PRODUCE + 1) + MIN_PRODUCE));
        this.seedCost = 200;
        this.sellingPrice = 5;
        this.xpYield = 25;
    }

}
