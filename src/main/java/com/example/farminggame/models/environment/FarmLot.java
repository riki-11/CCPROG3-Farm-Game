package com.example.farminggame.models.environment;
import java.util.ArrayList;


/**
 * Represents a farm lot with up to 50 Tiles
 * @author Enrique Lejano and Krizchelle Wong
 */
public class FarmLot {
    private ArrayList<Tile> lot;

    /**
     * Creates an ArrayList of Tiles (contains 1 Tile for now)
     */
    public FarmLot() {
        this.lot = new ArrayList<>();

        // Populate lot with Tiles that have rocks and some that don't
        for (int i = 0; i < 50; i++) {
            lot.add(new Tile());
        }

    }

    /**
     * Displays each Tile in lot and its Crop's info (if available)
     */
    public void displayFarmLot() {
        for (Tile tile : lot) {
            System.out.println("\n" + tile.getAppearance());
            System.out.printf("Tile No. %d\n", lot.indexOf(tile));
            System.out.println(tile.getCropInfo());
        }
        // Display how many crops are ready for harvest
        System.out.printf("There are %d crops ready for harvest.\n", getHarvestableTiles().size());
    }

    /**
     * Gets an ArrayList of every tile that has a crop ready for harvest
     * @return all Tiles with harvestable Crops
     */
    public ArrayList<Tile> getHarvestableTiles() {
        ArrayList<Tile> harvestableTiles = new ArrayList<>();

        for (Tile tile : lot) {

            if (tile.hasHarvestableCrop()) {
                harvestableTiles.add(tile);
            }
        }

        return harvestableTiles;
    }

    /**
     * Gets the number of active/growing crops
     * @return the number of active/growing crops
     */
    public int getCropsPlanted() {
        int total = 0;
        for (Tile tile : lot) {
            // If tile has a crop and it isn't withered
            if (tile.hasCrop() && !tile.hasWitheredCrop()) {
                total += 1;
            }
        }
        return total;
    }

    /**
     * Gets an individual tile from the lot via index
     * @param index Index of desired Tile
     * @return one Tile from the FarmLot
     */
    public Tile getTile(int index) {
        return lot.get(index);
    }

    /**
     * Gets the entire FarmLot
     * @return An ArrayList of all the Tiles in FarmLot
     */
    public ArrayList<Tile> getLot() {
        return lot;
    }

    public void setRockPosition(int rockPosition) {
        getTile(rockPosition - 1).placeRock();
        System.out.println("Tile " + (rockPosition - 1)  + " has rock: " + getTile(rockPosition - 1).hasRock());
    }
}
