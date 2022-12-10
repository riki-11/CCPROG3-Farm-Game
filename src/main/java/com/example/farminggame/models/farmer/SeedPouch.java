package com.example.farminggame.models.farmer;

import java.util.Hashtable;
import java.util.Map;

/**
 * Represents a Seed Pouch that holds seeds in the Farmer's inventory
 * @author Enrique Lejano and Krizchelle Wong
 */

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
        for (Map.Entry<String, Integer> entry : seedList.entrySet()) {
            String cropName = entry.getKey();
            int count = entry.getValue();
            System.out.println(cropName + " - " + count);
        }
    }

    /**
     * Adds seed/s to the SeedPouch
     * @param seedName Name of the seed
     * @param seedCount Number of seeds bought
     */
    public void updateSeedList(String seedName, int seedCount) {
        int currentCount = this.seedList.get(seedName);
        this.seedList.put(seedName, currentCount + seedCount);
    }

    /**
     * Gets the seed count for a specific seed
     * @param seedName Name of the seed
     * @return the number of seeds a specific seed has
     */
    public int getSeedCount(String seedName) {
        return this.seedList.get(seedName);
    }

    /**
     * Gets the entire list of the seeds and their respective counts
     * @return a dictionary of all the seeds and their counts
     */
    public Hashtable<String, Integer> getSeedList() {
        return this.seedList;
    }

}
