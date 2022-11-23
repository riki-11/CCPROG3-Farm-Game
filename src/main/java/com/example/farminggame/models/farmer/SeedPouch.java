package com.example.farminggame.models.farmer;

/**
 * Represents a Seed Pouch that holds seeds in the Farmer's inventory
 * @author Enrique Lejano and Krizchelle Wong
 */
public class SeedPouch {
    private int[] seeds; // make use of polymorphism again because arraylists (idk abt arrays) can store references to subclass as well

    public SeedPouch() {
        this.seeds = new int[8];
    }

    /**
     * Prints all seeds present in SeedPouch
     */
    public void printSeedList() {
        System.out.printf("Seed List:\n" +
                          "Turnips - %d\n", seeds[0]);
    }

    /**
     * Adds seed/s to the SeedPouch
     * @param index Index of specific seed to be added
     * @param seedCount Number of seeds bought
     */
    public void updateSeedList(int index, int seedCount) {
        seeds[index] += seedCount;
    }

    /**
     * Gets the entire Array of seeds in the SeedPouch
     * @return Array of seeds in SeedPouch
     */
    public int[] getSeedList() {
        return this.seeds;
    }

    /**
     * Gets the total number of seeds
     * @return number of seeds as int
     */
    public int getSeedCount() {
        int totalSeeds = 0;
        for (int i = 0; i < 8; i++) {
            totalSeeds += seeds[i];
        }
        return totalSeeds;
    }
}
