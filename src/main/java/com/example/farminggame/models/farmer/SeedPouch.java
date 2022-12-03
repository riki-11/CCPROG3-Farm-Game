package com.example.farminggame.models.farmer;

import java.util.Hashtable;
import java.util.Map;

/**
 * Represents a Seed Pouch that holds seeds in the Farmer's inventory
 * @author Enrique Lejano and Krizchelle Wong
 */

// CHANGE THIS TO IMPLEMENT ALL THE TYPES OF SEEDS
public class SeedPouch {

    // Stores seedName and no. of seeds as pairs
    private Hashtable<String, Integer> seedList;

    public SeedPouch() {
        this.seedList = new Hashtable<String, Integer>();
        // initialize hashtable with all crops
        this.seedList.put("Turnip", 0);
        this.seedList.put("Carrot", 0);
        this.seedList.put("Potato", 0);
        this.seedList.put("Rose", 0);
        this.seedList.put("Tulip", 0);
        this.seedList.put("Sunflower", 0);
        this.seedList.put("Mango", 0);
        this.seedList.put("Apple", 0);
    }

    /**
     * Prints all seeds present in SeedPouch
     */
    public void showSeedList() {
        System.out.println("Seed List:");
        for (Map.Entry<String, Integer> entry : seedList.entrySet()) {
            String cropName = entry.getKey();
            int count = entry.getValue();
            System.out.println(cropName + " - " + count);
        }
    }

    /**
     * Adds seed/s to the SeedPouch
     * @param index Index of specific seed to be added
     * @param seedCount Number of seeds bought
     */
    public void updateSeedList(String seedName, int seedCount) {
        int currentCount = this.seedList.get(seedName);
        this.seedList.put(seedName, currentCount + seedCount);
    }

    public int getSeedCount(String seedName) {
        return this.seedList.get(seedName);
    }

    /**
     * Gets the entire Array of seeds in the SeedPouch
     *
     * @return Array of seeds in SeedPouch
     */
    public Hashtable<String, Integer> getSeedList() {
        return this.seedList;
    }
/*
    /**
    /**
     * Gets the total number of seeds
     * @return number of seeds as int

    public int getSeedPouchCount() {
        int totalSeeds = 0;
        for (int i = 0; i < 8; i++) {
            totalSeeds += seeds[i];
        }
        return totalSeeds;
    }
    */
}
