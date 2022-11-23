package com.example.farminggame.models.environment.crops;

/**
 * Represents a crop that can be planted and harvested and can wither
 * @author Enrique Lejano and Krizchelle Wong
 */
public abstract class Crop {
    protected String seedName;
    protected String cropType;
    protected int harvestTime;
    protected int waterNeeds;
    protected int waterBonusLimit;
    protected int fertilizerNeeds;
    protected int fertilizerBonusLimit;
    protected int produce;
    protected int seedCost;
    protected int sellingPrice;
    protected double xpYield;
    protected int fertilizerCount;
    protected int waterCount;
    protected int daysPlanted;

    /**
     * Creates a Crop with a specified type
     *
     */
    public Crop() {
        this.fertilizerCount = 0;
        this.waterCount = 0;
        this.daysPlanted = 0;
    }

    /**
     * Increases fertilizer count of Crop by 1
     */
    public void addFertilizerCount() {
        this.fertilizerCount++;
    }

    /**
     * Increases water count of Crop by 1
     */
    public void addWaterCount() {
        this.waterCount++;
    }

    /**
     * Determines whether Crop is withered
     * @return true if Crop meets the requirements to be deemed withered and false otherwise
     */
    public boolean isWithered() {
        if (this.daysPlanted > this.harvestTime ||
            (this.daysPlanted == this.harvestTime && (this.fertilizerCount < this.fertilizerNeeds || this.waterCount < this.waterNeeds))) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether Crop is harvestable
     * @return true if Crop meets the requirements to be deemed harvestable and false otherwise.
     */
    public boolean isHarvestable() {
        if (this.fertilizerCount >= this.fertilizerNeeds && this.waterCount >= this.waterNeeds
                && !isWithered() && this.harvestTime - this.daysPlanted == 0 ) {
            return true;
        }
        return false;
    }

    /**
     * Increments number of days a Crop has been planted.
     */
    public void addDayPlanted() {
        this.daysPlanted++;
    }

    /**
     * Gets the name of the Crop
     * @return this Crop's name
     */
    public String getSeedName() {
        return this.seedName;
    }

    /**
     * Gets the number of days this Crop takes to become harvestable
     * @return the number of days this Crop takes to become harvestable
     */
    public int getHarvestTime() {
        return this.harvestTime;
    }

    /**
     * Gets number of times this Crop needs to be watered to become harvestable
     * @return number of times this Crop needs to be watered
     */
    public int getWaterNeeds() {
        return this.waterNeeds;
    }

    /**
     * Gets number of times Crop needs to be fertilized to become harvestable
     * @return number of times this Crop needs to be fertilized
     */
    public int getFertilizerNeeds() {
        return this.fertilizerNeeds;
    }

    /**
     * Gets maximum number of times Crop can be watered to earn bonus ObjectCoins
     * @return maximum number of times Crop can be watered to earn bonus ObjectCoins
     */
    public int getWaterBonusLimit() {
        return this.waterBonusLimit;
    }

    /**
     * Gets maximum number of times Crop can be fertilized to earn bonus ObjectCoins
     * @return maximum number of times Crop can be fertilized to earn bonus ObjectCoins
     */
    public int getFertilizerBonusLimit() {
        return this.fertilizerBonusLimit;
    }

    /**
     * Returns number of crops obtained when harvesting the crop
     * @return number of crops obtained when harvesting the crop
     */
    public int getProduce() {
        return this.produce;
    }

    /**
     * Gets cost of purchasing the Crop
     * @return cost of purchasing the Crop
     */
    public int getSeedCost() {
        return this.seedCost;
    }

    /**
     * Gets selling price of Crop
     * @return selling price of Crop
     */
    public int getSellingPrice() {
        return this.sellingPrice;
    }

    /**
     * Gets experience gained by harvesting Crop
     * @return experience gained by harvesting Crop
     */
    public double getXpYield() {
        return this.xpYield;
    }

    /**
     * Gets current number of times Crop has been watered
     * @return current number of times Crop has been watered
     */
    public int getWaterCount() {
        return this.waterCount;
    }

    /**
     * Gets current number of times Crop has been fertilized
     * @return
     */
    public int getFertilizerCount() {
        return this.fertilizerCount;
    }

    /**
     * Gets number of days Crop has been planted
     * @return number of days Crop has been planted
     */
    public int getDaysPlanted() {
        return this.daysPlanted;
    }

}
