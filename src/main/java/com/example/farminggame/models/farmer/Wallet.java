package com.example.farminggame.models.farmer;

/**
 * Represents ObjectCoin wallet of Farmer
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Wallet{
    private double objectCoins;

    /**
     * Creates Wallet with 100 ObjectCoins by default
     */
    public Wallet() {
        this.objectCoins = 100;
    }

    /**
     * Gets ObjectCoin balance of Wallet
     * @return number of ObjectCoins as double
     */
    public double getObjectCoins() {
        return this.objectCoins;
    }

    /**
     * Adds or subtracts from ObjectCoin balance
     * @param amount Amount of ObjectCoins to be added
     */
    public void setObjectCoins(double amount) {
        this.objectCoins += amount;
    }
}