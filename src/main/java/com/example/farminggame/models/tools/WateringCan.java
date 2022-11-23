package com.example.farminggame.models.tools;
import com.example.farminggame.models.environment.crops.Crop;

/**
 * Represents a Watering Can tool
 * @author Enrique Lejano and Krizchelle Wong
 */
public class WateringCan extends Tool {

    /**
     * Creates a Watering Can with default usage cost and XP gained upon use
     */
    public WateringCan() {
        this.usageCost = 0;
        this.experienceGained = 0.5;
    }

    /**
     * Waters a crop
     * @param crop Crop to be watered
     */
    public void waterCrop(Crop crop) {
        crop.addWaterCount();
    }
}
