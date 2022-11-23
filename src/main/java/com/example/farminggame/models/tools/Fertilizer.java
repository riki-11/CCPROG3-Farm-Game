package com.example.farminggame.models.tools;

import com.example.farminggame.models.environment.crops.Crop;

/**
 * Represents a Fertilizer tool
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Fertilizer extends Tool {

    /**
     * Creates a Fertilizer with default usage cost and XP gained upon use
     */
    public Fertilizer() {
        this.usageCost = 10;
        this.experienceGained = 4;
    }

    /**
     * Fertilizes a specific Crop
     * @param crop Crop to be fertilized
     */
    public void fertilizeCrop(Crop crop) {
       if (crop != null) {
            crop.addFertilizerCount();
        }
    }
}
