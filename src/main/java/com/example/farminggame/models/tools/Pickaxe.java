package com.example.farminggame.models.tools;

import com.example.farminggame.models.environment.Tile;

/**
 * Represents a Pickaxe tool
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Pickaxe extends Tool {

    /**
     * Creates a Pickaxe with default values of usage cost and XP gained upon use
     */
    public Pickaxe() {
        this.usageCost = 50;
        this.experienceGained = 15;
    }

    /**
     * Removes a rock from a Tile
     * @param tile the Tile to have its rock removed
     */
    public void removeRock(Tile tile) {
        tile.removeRock();
    }

}
