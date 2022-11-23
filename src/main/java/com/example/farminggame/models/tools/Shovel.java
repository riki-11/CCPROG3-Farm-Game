package com.example.farminggame.models.tools;
import com.example.farminggame.models.environment.Tile;

/**
 * Represents a Shovel tool
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Shovel extends Tool {

    /**
     * Creates a Shovel with default usage cost and XP gained upon use
     */
    public Shovel() {
        this.usageCost = 7;
        this.experienceGained = 2;
    }

    /**
     * Removes a Crop from a Tile
     * @param tile the Tile to have its Crop removed
     */
    public void removePlant(Tile tile){
        tile.removeCrop();
        tile.resetTile();
    }
}
