package com.example.farminggame.models.farmer;

import com.example.farminggame.models.environment.crops.*;
import com.example.farminggame.models.environment.Tile;

import com.example.farminggame.models.tools.Fertilizer;
import com.example.farminggame.models.tools.Pickaxe;
import com.example.farminggame.models.tools.Plough;
import com.example.farminggame.models.tools.Shovel;
import com.example.farminggame.models.tools.WateringCan;

/**
 * Represents a Farmer which acts as the player character
 * @author Enrique Lejano and Krizchelle Wong
 */

public class Farmer {
    private String name;
    private int level;
    private double experience;
    private int bonusEarnings;
    private int costReduction;
    private int waterBonusLimit;
    private int fertilizerBonusLimit;
    private String farmerType;
    private int canUpgrade;
    private Wallet wallet;
    private SeedPouch seedPouch;

    /**
     * Creates a Farmer with default stats
     */
    public Farmer() {
        this.name = "Ed"; // If user doesn't input a name
        this.level = 0;
        this.experience = 0;
        this.bonusEarnings = 0;
        this.costReduction = 0;
        this.waterBonusLimit = 0;
        this.fertilizerBonusLimit = 0;
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
     * Buys seeds from the seed store
     * @param cropToBuy The class of the crop the player wants to buy
     * @param count The number of seeds for the crop to be bought
     * @return true if transaction was successful, false otherwise
     */
    public boolean buySeeds(Crop cropToBuy, int count) {
        String seedName = cropToBuy.getCropName();
        int basePrice = cropToBuy.getCropCost();

        // If farmer has enough coins and decided to buy at least 1 seed
        if (this.wallet.getObjectCoins() >= (basePrice - this.costReduction) * count && count != 0) {
            double totalPrice = basePrice * count;

            this.seedPouch.updateSeedList(seedName, count);
            this.wallet.setObjectCoins(-1 * totalPrice);

            return true;
        }
        // If there's not enough coins to buy seeds or if farmer cancelled transaction
        return false;

    }

    /**
     * Plants a seed on a selected Tile
     * @param tile Tile where seed will be planted
     * @param seedName name of seed to be planted
     */
    public void plantSeed(Tile tile, String seedName) {

        if (tile.isPlowed() && !tile.hasCrop() && !tile.hasRock())  {
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

            // Deduct seed from seed pouch
            int currentSeedCount = seedPouch.getSeedCount(seedName);
            seedPouch.getSeedList().put(seedName, currentSeedCount - 1);
        }
    }

    /**
     * Harvests a harvestable crop from a selected Tile
     * @param tile Tile containing harvestable crop
     * @return true if crop was successfully  harvested, false otherwise
     */
    public boolean harvestCrop(Tile tile) {

        Crop harvestedCrop = tile.getCrop();

        if (harvestedCrop.isHarvestable()) {
            // Add to the farmer's current xp
            addXP((harvestedCrop.getXpYield()));

            // Get how many times the crop was fertilized and watered
            int fertilizerCount = harvestedCrop.getFertilizerCount();
            int waterCount = harvestedCrop.getWaterCount();

            // Bonuses for watering/fertilizing are capped by the Crop's bonus limit plus the Farmer's bonus increase
            if (waterCount > harvestedCrop.getWaterBonusLimit() + this.waterBonusLimit) {
                waterCount = harvestedCrop.getWaterBonusLimit();
            }
            if (fertilizerCount > harvestedCrop.getFertilizerBonusLimit() + this.fertilizerBonusLimit) {
                fertilizerCount = harvestedCrop.getFertilizerBonusLimit();
            }

            // Compute for the final harvest price
            double harvestTotal = harvestedCrop.getProduce() * (harvestedCrop.getSellingPrice() + this.bonusEarnings);
            double waterBonus = harvestTotal * 0.2 * (waterCount - 1);
            double fertilizerBonus = harvestTotal * 0.5 * fertilizerCount;
            double finalHarvestPrice = harvestTotal + waterBonus + fertilizerBonus;

            if (harvestedCrop.getType().equals("Flower")) {
                finalHarvestPrice *= 1.1;
            }

            this.wallet.setObjectCoins(finalHarvestPrice);
            tile.removeCrop();

            return true;
        } else {
            return false;
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
            addXP(plough.getXP());
        }

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
            addXP(wateringCan.getXP());

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
            addXP(fertilizer.getXP());
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
            addXP(shovel.getXP());

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
            addXP(pickaxe.getXP());
            // Deduct from wallet
            this.wallet.setObjectCoins(-1 * pickaxe.getCost());
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

