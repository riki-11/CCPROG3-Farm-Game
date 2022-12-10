package com.example.farminggame.models.environment;

import com.example.farminggame.models.environment.crops.Crop;
import java.util.ArrayList;

/**
 * Represents a farm lot with up to 50 Tiles
 * @author Enrique Lejano and Krizchelle Wong
 */
public class FarmLot {
    private ArrayList<Tile> lot;

    /**
     * Creates an ArrayList of Tiles
     */
    public FarmLot() {
        this.lot = new ArrayList<>();

        // Populate lot with Tiles that have rocks and some that don't
        for (int i = 0; i < 50; i++) {
            lot.add(new Tile());
        }

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
     * Gets the number of active/growing crops
     * @return the number of active/growing crops
     */
    public ArrayList<Crop> getCropsPlanted() {
        ArrayList<Crop> cropList = new ArrayList<>();
        for (Tile tile : lot) {
            if (tile.hasCrop() && !(tile.hasWitheredCrop())) {
                cropList.add(tile.getCrop());
            }
        }

        return cropList;
    }

    /**
     * Gets the entire FarmLot
     * @return An ArrayList of all the Tiles in FarmLot
     */
    public ArrayList<Tile> getLot() {
        return lot;
    }

    /**
     * Puts a rock in a specified tile
     * @param rockPosition Index of the tile to add a rock in
     */
    public void setRockPosition(int rockPosition) {
        getTile(rockPosition - 1).placeRock();
    }
}
