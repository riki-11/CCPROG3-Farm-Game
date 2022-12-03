package com.example.farminggame.models.farmer;

import com.example.farminggame.models.environment.crops.*;
import com.example.farminggame.models.environment.Tile;

import com.example.farminggame.models.tools.Fertilizer;
import com.example.farminggame.models.tools.Pickaxe;
import com.example.farminggame.models.tools.Plough;
import com.example.farminggame.models.tools.Shovel;
import com.example.farminggame.models.tools.WateringCan;

import com.example.farminggame.models.farmer.*;

import java.util.ArrayList;

/**
 * Represents a Farmer which acts as the player character
 * @author Enrique Lejano and Krizchelle Wong
 */


// MAKE THIS THE FARMER SUPERCLASS
public class Farmer {
    // REFLECT IN UML
    private String name;
    private int level;
    private double experience;
    private boolean equipped; // REMOVE tHIS
    private int bonusEarnings;
    private int costReduction;
    private int waterBonusLimit;
    private int fertilizerBonusLimit;
    private String currentTool; // REMOVE THIS
    private String farmerType;
    private Wallet wallet;
    private SeedPouch seedPouch;
    private int canUpgrade;

    /**
     * Creates a Farmer with default stats
     */
    public Farmer() {
        this.name = "";
        this.level = 0;
        this.experience = 0;
        this.bonusEarnings = 0;
        this.costReduction = 0;
        this.waterBonusLimit = 0;
        this.fertilizerBonusLimit = 0;
        this.currentTool = "None";
        this.equipped = false;
        this.farmerType = "Farmer";
        this.canUpgrade = -1;
        this.wallet = new Wallet();
        this.seedPouch = new SeedPouch();
    }

    /**
     * Sets the farmers name
     * @param name Name of the farmer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Increases farmer's XP
     * @param amount Amount of XP to be added
     */
    public void addXP(double amount) {
        this.experience += amount;
        if (this.experience >= (this.level + 1) * 100) {
            this.level += 1;
        }
    }

    /**
     * Upgrades farmer to corresponding Farmer Type based on return value of getCanUpgrade method
     */
    public void upgradeFarmer() {
        if (this.getCanUpgrade() == 1) {
            this.farmerType = "Registered Farmer";
            this.bonusEarnings = 1;
            this.costReduction = 1;
        } else if (this.getCanUpgrade() == 2) {
            this.farmerType = "Distinguished Farmer";
            this.bonusEarnings = 2;
            this.costReduction = 2;
            this.waterBonusLimit = 1;
        } else if (this.getCanUpgrade() == 3) {
            this.farmerType = "Legendary Farmer";
            this.bonusEarnings = 4;
            this.costReduction = 3;
            this.waterBonusLimit = 2;
            this.fertilizerBonusLimit = 1;
        }
    }

    /**
     * Lets player buy as many seeds as they desire as long they can afford it
     * @param count Number of seeds to be bought
     * @return true for a successful transaction, false if otherwise
    */


    /**
     * Buys seeds from the seed store
     * @param cropToBuy
     * @param count
     * @return
     */
    public boolean buySeeds(Crop cropToBuy, int count) {
        String seedName = cropToBuy.getSeedName();
        int basePrice = cropToBuy.getSeedCost();

        // If farmer has enough coins and decided to buy at least 1 seed
        if (this.wallet.getObjectCoins() >= (basePrice - this.costReduction) * count && count != 0) {
            double totalPrice = basePrice * count;

            this.seedPouch.updateSeedList(seedName, count);
            this.wallet.setObjectCoins(-1 * totalPrice);
            System.out.printf("Successfully bought %d seeds of %s\n", count, seedName);

            return true;
        }
        // If not enough coins to buy seeds or if farmer cancelled transaction
        return false;

    }

    /**
     * Plants a seed on a selected Tile
     * @param tile Tile where seed will be planted
     * @param seedName name of seed to be planted
     */
    public void plantSeed(Tile tile, String seedName) {
        if (tile.isPlowed() && !tile.hasCrop())  {
            if (seedName.equals("Carrot")) {
                tile.setCrop(new Carrot());
            } else if (seedName.equals("Apple")) {
                tile.setCrop(new Apple());
            } else if (seedName.equals("Mango")) {
                tile.setCrop(new Mango());
            } else if (seedName.equals("Potato")) {
                tile.setCrop(new Potato());
            } else if (seedName.equals("Rose")) {
                tile.setCrop(new Rose());
            } else if (seedName.equals("Sunflower")) {
                tile.setCrop(new Sunflower());
            } else if (seedName.equals("Tulip")) {
                tile.setCrop(new Tulip());
            } else if (seedName.equals("Turnip")) {
                tile.setCrop(new Turnip());
            }

            System.out.printf("\nA %s has been successfully planted.\n", seedName);
        } else {
            System.out.println("\nA seed can't be planted here.\n");
        }
    }

    /**
     * Harvests a harvestable crop from a selected Tile
     * @param tile Tile containing harvestable crop
     * @return true if harvesting was successful, false if otherwise
     */
    public boolean harvestCrop(Tile tile) {
        if (tile != null) {
            Crop harvestedCrop = tile.getCrop();
            addXP(harvestedCrop.getXpYield());

            int waterCount = harvestedCrop.getWaterCount();
            int fertilizerCount = harvestedCrop.getFertilizerCount();

            // Bonuses for watering/fertilizing are capped by the Crop's bonus limit plus the Farmer's bonus increase
            if (waterCount > harvestedCrop.getWaterBonusLimit() + this.waterBonusLimit) {
                waterCount = harvestedCrop.getWaterBonusLimit();
            }
            if (fertilizerCount > harvestedCrop.getFertilizerBonusLimit() + this.waterBonusLimit) {
                fertilizerCount = harvestedCrop.getFertilizerBonusLimit();
            }

            double harvestTotal = harvestedCrop.getProduce() * (harvestedCrop.getSellingPrice() + this.bonusEarnings);
            double waterBonus = harvestTotal * 0.2 * (waterCount - 1);
            double fertilizerBonus = harvestTotal * 0.5 * fertilizerCount;
            double finalHarvestPrice = harvestTotal + waterBonus + fertilizerBonus;

            this.wallet.setObjectCoins(finalHarvestPrice);

            tile.removeCrop();

            return true;
        }

        return false;
    }

    // Get rid of all the tool-related equip and use methods

    /**
     * Equips Plough tool
     */
    public void equipPlough() {
        this.currentTool = "Plough";
        this.equipped = true;
    }

    /**
     * Equips Watering Can tool
     */
    public void equipWateringCan() {
        this.currentTool = "Watering Can";
        this.equipped = true;
    }

    /**
     * Equips Fertilizer tool
     */
    public void equipFertilizer() {
        this.currentTool = "Fertilizer";
        this.equipped = true;
    }

    /**
     * Equips Pickaxe tool
     */
    public void equipPickaxe() {
        this.currentTool = "Pickaxe";
        this.equipped = true;
    }

    /**
     * Equips Shovel tool
     */
    public void equipShovel() {
        this.currentTool = "Shovel";
        this.equipped = true;
    }

    /**
     * Unequips tool if Farmer currently has one equipped
     */
    public void unequipTool() {
        if (!this.currentTool.equals("None")) {
            System.out.printf("\n%s has been unequipped\n", this.currentTool);
            this.currentTool = "None";
            this.equipped = false;
        }
    }

    /**
     * Uses Plough tool
     * @param tile Tile where Plough will be used
     * @param plough Instance of Plough tool
     */
    public void usePlough(Tile tile, Plough plough) {

        // Check if tile was successfully plowed
        boolean success = plough.plowTile(tile);

        // Charge farmer for using plough
        this.wallet.setObjectCoins(plough.getCost());

        // Gain xp for successful plow
        if (success) {
            this.experience += plough.getXP();
        }

        // Unequip tool after using
        this.unequipTool();
    }

    /**
     * Uses Watering Can tool
     * @param tile Tile where Watering Can will be used
     * @param wateringCan Instance of Watering Can tool
     */
    public void useWateringCan(Tile tile, WateringCan wateringCan) {
        if (tile.hasCrop()) {
            wateringCan.waterCrop(tile.getCrop());

            this.wallet.setObjectCoins(-1 * wateringCan.getCost());
            this.experience += wateringCan.getXP();

            System.out.println("Successfully watered plant");

            this.unequipTool();
        }
    }

    /**
     * Uses Fertilizer tool
     * @param tile Tile where Fertilizer will be used
     * @param fertilizer Instance of Fertilizer tool
     */
    public void useFertilizer(Tile tile, Fertilizer fertilizer) {
        if (tile.hasCrop() && !tile.hasWitheredCrop()) {
            fertilizer.fertilizeCrop(tile.getCrop());

            this.wallet.setObjectCoins(-1 * fertilizer.getCost());
            this.experience += fertilizer.getXP();

            System.out.println("Successfully fertilized plant"); // remove this?
            this.unequipTool();
        }
    }

    /**
     * Uses Shovel tool
     * @param tile - Tile where Shovel will be used
     * @param shovel - Instance of Shovel tool
     */
    public void useShovel(Tile tile, Shovel shovel) {
        if (tile.hasCrop()) {
            // Remove plant
            shovel.removePlant(tile);
            this.experience += shovel.getXP();

            this.unequipTool();
        } else {
            System.out.println("Nothing happened. We can't use the shovel here.");
        }

        // Deduct from wallet
        this.wallet.setObjectCoins(-1 * shovel.getCost());
    }

    /**
     * Uses Pickaxe tool
     * @param tile Tile where Pickaxe will be used
     * @param pickaxe Instance of Pickaxe tool
     */
    public void usePickaxe(Tile tile, Pickaxe pickaxe) {
        if (tile.hasRock()) {
            pickaxe.removeRock(tile);
            this.experience += pickaxe.getXP();

            // Uneqip tool after using
            this.unequipTool();

            // Deduct from wallet
            this.wallet.setObjectCoins(-1 * pickaxe.getCost());
        } else {
            System.out.println("Nothing happened. We can't use the pickaxe here.");
        }
    }

    /**
     * Gets farmer's name
     * @return name of Farmer as a String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets farmer's level
     * @return level of farmer as int
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Gets farmer's accumulated experience
     * @return amount of experience as double
    */
    public double getCurrentXP() {
        return this.experience;
    }

    /**
     * Gets seed cost reduction given by Farmer Level
     * @return amount reduced as int
     */
    public int getCostReduction() {
        return this.costReduction;
    }

    /**
     * Gets farmer's current tool
     * @return name of tool as String
    */
    public String getCurrentTool() {
        return this.currentTool;
    }

    /**
     * Gets farmer's current Farmer Type/Level
     * @return farmerType as String
    */
    public String getFarmerType() {
        return this.farmerType;
    }

    /**
     * Determines the appropriate FarmerType available for upgrade and returns a corresponding integer
     * @return 1 if eligible for Registered Farmer,
     *         2 if eligible for Distinguished Farmer,
     *         3 if eligible for upgrade to Legendary Farmer,
     *         -1 if the farmer is ineligible for upgrading.
     */
    public int getCanUpgrade() {
        if (this.farmerType.equals("Farmer") && this.level >= 5 && this.wallet.getObjectCoins() >= 200)  {
            // If you can upgrade to Registered Farmer, charge the farmer and return 1
            this.wallet.setObjectCoins(-200);
            return 1;
        } else if (this.farmerType.equals("Registered Farmer") && this.level >= 10 && wallet.getObjectCoins() >= 300) {
            // If you can upgrade to Distinguished Farmer, charge the farmer and return 2
            this.wallet.setObjectCoins(-300);
            return 2;
        } else if (this.farmerType.equals("Distinguished Farmer") && this.level >= 15 && wallet.getObjectCoins() >= 400) {
            // If you can upgrade to Legendary Farmer, charge the farmer and return 3
            this.wallet.setObjectCoins(-400);
            return 3;
        } else {
            // Otherwise, return -1
            return -1;
        }
    }

    /**
     * Returns farmer's ObjectCoin wallet
     * @return Wallet of farmer
     */
    public Wallet getWallet() {
        return this.wallet;
    }

    /**
     * Returns farmer's SeedPouch where seeds are stored
     * @return SeedPouch of farmer
     */
    public SeedPouch getSeedPouch() {
        return this.seedPouch;
    }
}

