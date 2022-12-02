package com.example.farminggame.models.environment;

import com.example.farminggame.models.environment.crops.Crop;

/**
 * Represents an individual tile within FarmLot
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Tile {
    private boolean plowed;
    private boolean rock;
    private Crop crop; // USE POLYMORPHISM
    private String appearance; // REMOVE THIS

    /**
     *  Creates a basic tile in its default state
     */
    public Tile() {
        this.plowed = false;
        this.rock = false;
        this.crop = null;
        this.appearance = String.format("-------\n" +
                                        "|     |\n" +
                                        "|     |\n" +
                                        "|     |\n" +
                                        "-------\n");
    }

    /**
     * Resets tile to its default state
     */
    public void resetTile() {
        this.appearance = String.format("-------\n" +
                "|     |\n" +
                "|     |\n" +
                "|     |\n" +
                "-------\n");
    }


    /**
     * Checks if a tile currently has a crop planted on it
     * @return true if a Crop exists on the tile, false otherwise
     */
    public boolean hasCrop() {
        if (this.crop != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the crop on the tile is harvestable
     * @return true if the planted crop is harvestable, false otherwise
     */
    public boolean hasHarvestableCrop() {
        if (hasCrop()) {
            return this.crop.isHarvestable();
        }
        return false;
    }

    /**
     *  Checks if the planted crop is withered
     *  @return true if the planted crop is withered, false otherwise
     */
    public boolean hasWitheredCrop() {
        if (hasCrop()) {
            return this.crop.isWithered();
        }
        return false;
    }

    /**
     *  Checks if the tile is currently plowed
     *  @return true if the tile is plowed, false otherwise
     */
    public boolean isPlowed() {
        return this.plowed;
    }

    /**
     *  Checks if the tile has a rock on it
     *  @return true if there is a rock on the tile, false otherwise
     */
    public boolean hasRock() {
        return this.rock;
    }

    /**
     * Gets the crop planted on the tile, if there is one
     * @return a reference to the Crop planted on the tile, null otherwise
     */
    public Crop getCrop() {
        return this.crop;
    }

    /**
     * Gets the current appearance of the tile
     * @return appearance of Tile as a string
     */
    public String getAppearance() {
        return this.appearance;
    }

    /**
     * Gets the currently planted crop's information
     * @return information of Crop as a String
     */
    public String getCropInfo() {
        if (hasCrop()) {
            return String.format("""

                                Crop Name: %s        
                                Water Count : %d
                                Fertilizer Count : %d
                                Days until harvestable: %d
                                """,
                                this.crop.getSeedName(), this.crop.getWaterCount(),
                                this.crop.getFertilizerCount(), this.crop.getHarvestTime() - this.crop.getDaysPlanted());
        }
        return "";
    }

    /**
     * Sets the crop to be planted on the tile
     * @param crop the crop to be planted
     */
    public void setCrop(Crop crop) {
        this.crop = crop;
        // A planted crop is represented by its first character in this prototype
        setAppearance(String.format(" %c ", crop.getSeedName().charAt(0)));
    }

    /**
     * Modifies the plowed status of the tile
     * @param value boolean value signifying whether the tile is plowed or not
     */
    public void setPlowed(boolean value) {
        this.plowed = value;
    }

    /**
     * Modifies the existence of a rock on the tile
     *
     */
    public void placeRock() {
        this.rock = true;
    }

    /**
     * Modifies the withered status of the tile
     * @param value boolean value signifying whether the tile has a withered crop or not
    */
    public void setWithered(boolean value) {
        this.plowed = value;
    }

    /**
     * Modifies the appearance of the tile
     * @param status current state of the crop as a String
     */
    public void setAppearance(String status) {
        this.appearance = String.format("-------\n" +
                "|     |\n" +
                "| %s |\n" +
                "|     |\n" +
                "-------\n", status);
    }

    /**
     * Removes the currently planted crop from the Tile
     */
    public void removeCrop() {
        this.crop = null;
        this.setWithered(false);
    }

    /**
     * Removes the rock from the tile
     */
    public void removeRock() {
        this.rock = false;
    }
}
