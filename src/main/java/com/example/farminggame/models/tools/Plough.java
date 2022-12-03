package com.example.farminggame.models.tools;
import com.example.farminggame.models.environment.Tile;
/**
 * Represents a Plough tool
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Plough extends Tool {

    /**
     * Creates a Plough with default usage cost and XP gained upon use
     */
    public Plough() {
        this.usageCost = 0;
        this.experienceGained = 0.5;
    }

    /**
     * Plows a Tile to allow a Crop to be planted
     * @param tile specified Tile to be plowed
     * @return true if plowing was successful, false otherwise
     */
    public boolean plowTile(Tile tile) {
        if (!(tile.isPlowed())) {
            tile.setPlowed(true);
            System.out.println("SUCCESSFULY PLOWED!");
            return true;
        }
        return false;
    }
}
