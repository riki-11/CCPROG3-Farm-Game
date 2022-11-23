package com.example.farminggame.models.system;

import com.example.farminggame.models.environment.FarmLot;
import com.example.farminggame.models.environment.Tile;
import com.example.farminggame.models.environment.crops.Crop;
import com.example.farminggame.models.environment.crops.Turnip;
import com.example.farminggame.models.farmer.*;
import com.example.farminggame.models.tools.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the system that controls the flow and logic of the farming game
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Game {
    private int daysCount;
    private boolean gameOver;
    private Scanner sc;

    private Interface i = new Interface();

    private FarmLot farm = new FarmLot();

    // Only available crop is turnip for now
    private Crop crop = new Turnip();

    private Plough plough = new Plough();
    private Fertilizer fertilizer = new Fertilizer();
    private Pickaxe pickaxe = new Pickaxe();
    private Shovel shovel = new Shovel();
    private WateringCan wateringCan = new WateringCan();
    Farmer farmer = new Farmer();
    Wallet wallet = farmer.getWallet();
    SeedPouch seedPouch = farmer.getSeedPouch();

    /**
     * Creates a new instance of the Farming Game
     */
    public Game() {
        this.daysCount = 1;
        this.gameOver = false;
        this.sc = new Scanner(System.in);
    }

    /**
     * Starts the Farming Game
     */
    public void start() {
        i.displayFarmer();
        System.out.print("Enter your farmer name: ");
        String name = sc.nextLine();
        farmer.setName(name);

        // While game hasn't reached game over
        while (!this.gameOver) {
            int input;

            farm.displayFarmLot();
            i.displayMenu(daysCount);

            do {
                input = tryCatch();
                if (input < 1 || input > 9) {
                    i.displayErrorMessage();
                }
            } while (input < 1 || input > 9);

            // Purchase seeds from the store
            if (input == 1) {
                buySeeds();

            // Plant a seed on a tile
            } else if (input == 2) {
                plantSeed();

            // Equip one of 5 tools
            } else if (input == 3) {
                equipTool();

            // Use the currently equipped tool
            } else if (input == 4) {
                useTool();

            // Unequip the currently equipped tool
            } else if (input == 5) {
                farmer.unequipTool();

            // Harvest a crop on a specific tile
            } else if (input == 6) {
                harvestCrops();

            // Check the farmer's stats
            } else if (input == 7) {
                farmerStats();

            // Advance to the next in-game day
            } else if (input == 8) {
                nextDay();
                // At the end of every day, check if the game can keep going
                int gameOverCode = checkGameOver(wallet);
                if (gameOverCode >= 1) {
                    i.displayGameOver(gameOverCode, farmer, farm);
                }

            } else {
                this.setGameOver();
            }

        }
    }

    /**
     * Advances the in-game day counter
     */
    public void updateDaysCount() {
        this.daysCount++;
    }

    /**
     * Ends the game by setting gameOver to true
     */
    public void setGameOver() {
        this.gameOver = true;
    }

    /**
     * Ensures the program does not exit despite misinputs from the user
     * @return the user's input if it's an int, 0 otherwise
     */
    public int tryCatch() {
        try {
            int input = sc.nextInt();
            return input;
        } catch (Exception error) {
            System.out.println("Error: " + error);
            sc.next();
        }
        return 0;

    }

    /**
     * Lets farmer purchase seeds from the store
     */
    public void buySeeds() {
        int seedChoice;
        int seedCount;

        i.displaySeedStore();

        // Ensure player only buys turnips or exits the store
        do {
            seedChoice = tryCatch();
            if (seedChoice != 1 && seedChoice != 0) {
                i.displayErrorMessage();
            }
        } while (seedChoice != 1 && seedChoice != 0);

        if (seedChoice > 0) {
            i.displaySeedNumInput();

            // Ensure player either buys seeds or exits the store
            do {
                seedCount = tryCatch();
            } while (seedCount < 0);

            int seedCost = crop.getSeedCost();

            // If transaction was unsuccessful (can't afford or 0 seeds was inputted)
            if (!farmer.buySeeds(seedChoice, seedCount, seedCost)) {
                if (seedCount != 0) {
                    i.displayNotEnoughCoins();
                }
            // Display transaction information if purchase was successful
            } else {
                i.displayTransaction(seedChoice, seedCount, seedCost, farmer);
            }
        }
    }

    /**
     * Lets farmer plant a seed on a specific Tile
     */
    public void plantSeed() {
        int tileIndex;

        System.out.print("Which tile do you want to plant on? ");
        // Ensure user selects a valid tile
        do {
            tileIndex = tryCatch();
            if (tileIndex != 0) {
                i.displayErrorMessage();
            }
        } while (tileIndex != 0);

        Tile tile = farm.getTile(tileIndex);

        // Check if farmer has at least one turnip in their pouch
        if (seedPouch.getSeedList()[0] > 0) {
            farmer.plantSeed(tile, "Turnip");
        }
    }

    /**
     * Lets farmer equip one of 5 tools
     */
    public void equipTool() {
        int input;

        i.displayToolShed();
        do {
            input = tryCatch();
            if (input < 1 || input > 5) {
                i.displayErrorMessage();
            }
        } while (input < 1 || input > 5);

        if (input == 1) {
            farmer.equipPlough();
        } else if (input == 2) {
            farmer.equipWateringCan();
        } else if (input == 3) {
            farmer.equipFertilizer();
        } else if (input == 4) {
            farmer.equipPickaxe();
        } else {
            farmer.equipShovel();
        }

    }

    /**
     * Lets farmer use the currently equipped tool
     */
    public void useTool() {
        int tileIndex;

        System.out.print("Which tile do you want to use the tool on? ");
        // Ensure user selects a valid tile
        do {
            tileIndex = tryCatch();
            if (tileIndex != 0) {
                i.displayErrorMessage();
            }
        } while (tileIndex != 0);
        Tile tile = farm.getTile(tileIndex);

        // Check if tool can be used on the selected tile. Use the tool if valid, display a message otherwise
        switch (farmer.getCurrentTool()) {
            case "Plough":
                if (!(tile.isPlowed())) {
                    farmer.usePlough(tile, plough);
                } else {
                    i.displayWrongTool();
                }

                break;
            case "Fertilizer":
                if (tile.hasCrop()) {
                    farmer.useFertilizer(tile, fertilizer);
                } else {
                    i.displayWrongTool();
                }

                break;
            case "Pickaxe":
                if (tile.hasRock()) {
                    farmer.usePickaxe(tile, pickaxe);
                } else {
                    i.displayWrongTool();
                }

                break;
            // Shovels can be used outside their intended purpose
            case "Shovel":
                farmer.useShovel(tile, shovel);

                break;
            case "Watering Can":
                if (tile.hasCrop()) {
                    farmer.useWateringCan(tile, wateringCan);
                } else {
                    i.displayWrongTool();
                }

                break;
            default:
                System.out.println("No tool equipped!");
                break;
        }
    }

    /**
     * Lets farmer harvest a crop on a specified tile
     */
    public void harvestCrops() {
        int tileIndex;

        // Get all tiles with harvestable crops
        ArrayList<Tile> harvestableTiles = farm.getHarvestableTiles();

        // If there are any tiles with harvestable crops
        if (harvestableTiles.size() > 0) {
            System.out.print("Select a tile to harvest its crop. ");
            do {
                tileIndex = tryCatch();
                if (tileIndex != 0) {
                    i.displayErrorMessage();
                }
            } while (tileIndex != 0);

            Tile selectedTile = harvestableTiles.get(tileIndex);

            if (farmer.harvestCrop(selectedTile)) {
                System.out.println("Successfully harvested crop!");
                System.out.printf("Obtained %d turnip/s!\n", selectedTile.getCrop().getProduce());
            } else {
                System.out.println("Crop harvest failed.");
            }
        }
    }

    /**
     * Checks farmer stats
     */
    public void farmerStats() {
        int input;
        // Display farmer stats
        i.displayFarmerStats(farmer);
        if (farmer.getCanUpgrade() >= 1) {
            do {
                input = tryCatch();
                if (input < 1 || input > 2) {
                    i.displayErrorMessage();
                }
            } while (input < 1 || input > 2);
            if (input == 1) {
                farmer.upgradeFarmer();
                System.out.println("\nYou've successfully upgraded your farmer level!");
            }
        }
    }

    /**
     * Advances one in-game day and updates all tiles and growing crops
     */
    public void nextDay() {
        for (Tile tile : farm.getLot()) {
            // For every tile with a crop, increment days planted
            if (tile.hasCrop()) {
                Crop tileCrop = tile.getCrop();
                tileCrop.addDayPlanted();

                // For every tile that becomes harvestable, update its appearance
                if (tileCrop.isHarvestable()) {
                    tile.setAppearance("*T*");
                } else if (tileCrop.isWithered()) {
                    tile.setAppearance(" W ");
                }
            }
        }
        updateDaysCount();
    }

    /**
     * Checks if the player reaches either of the two game over scenarios
     * @param wallet the farmer's wallet
     * @return 1 if farmer has insufficient ObjectCoins to buy a seed, 2 if there aren't any growing crops
     *          0, otherwise
     */
    public int checkGameOver(Wallet wallet) {
        /*
            Game over codes:
            Return 1 - Farmer cannot buy any more seeds (min. price is 5)
            Return 2 - Farmer doesn't have growing crops by the end of any day
         */
        if (wallet.getObjectCoins() < 5) {
            this.setGameOver();
            return 1;
        } else if (farm.getCropsPlanted() == 0) {
            this.setGameOver();
            return 2;
        } else {
            return 0;
        }
    }
}

