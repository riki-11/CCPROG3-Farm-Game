package com.example.farminggame.models.tools;

public abstract class Tool {
    protected int usageCost;
    protected double experienceGained;

    /**
     * Gets usage cost of tool
     * @return usage cost of tool
     */
    public int getCost() {
        return this.usageCost;
    }

    /**
     * Gets amount of experience gained from using tool
     * @return experience gained from using tool
     */
    public double getXP() {
        return this.experienceGained;
    }
}
