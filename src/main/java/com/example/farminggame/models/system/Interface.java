package com.example.farminggame.models.system;
import com.example.farminggame.models.farmer.Farmer;
import com.example.farminggame.models.environment.FarmLot;

/**
 * Represents the GUI of the farming game that the player interacts with
 * @author Enrique Lejano and Krizchelle Wong
 */
public class Interface {

    /**
     * Displays the main menu of the game with all possible actions
     * @param day current in-game day number
     */
    public void displayMenu(int day) {
        System.out.printf("\nToday it is Day %d! What would you like to do?\n\n", day);
        System.out.println("""
                            [1] Buy Seeds
                            [2] Plant Seeds
                            [3] Equip a Tool
                            [4] Use Tool
                            [5] Unequip Tool
                            [6] Harvest Crops
                            [7] Farmer Stats
                            [8] Advance to The Next Day
                            [9] Exit Game""");
        System.out.print("----------------------------\n");
        System.out.print("Enter input: ");
    }

    /**
     * Displays the farmer's current stats
     * @param farmer the current farmer/player character
     */
    public void displayFarmerStats(Farmer farmer) {
        System.out.println("\n----------------------------------");
        this.displayFarmer();
        System.out.printf("%s\n", farmer.getName());
        System.out.printf("""
                            Farmer Type - %s
                            Level - %d
                            Experience - %.1f
                            ObjectCoins - %.2f
                            Current Tool: %s
                            """,
                          farmer.getFarmerType(), farmer.getLevel(), farmer.getCurrentXP(),
                          farmer.getWallet().getObjectCoins(), farmer.getCurrentTool());

        // Check if the farmer is eligible to upgrade their Farmer Type
        int upgradeStatus = farmer.getCanUpgrade();

        // Display a special screen if the farmer can upgrade Farmer Type
        if (upgradeStatus >= 1) {
            System.out.println("---------------------------------");

            if (upgradeStatus == 1) {
                System.out.println("You can now upgrade to Registered Farmer.");
            } else if (upgradeStatus == 2) {
                System.out.println("You can now upgrade to Distinguished Farmer.");
            } else if (upgradeStatus == 3) {
                System.out.println("You can now upgrade to Legendary Farmer.");
            }

            System.out.println("[1] Upgrade to next Farmer Type");
            System.out.println("[2] Exit Farmer Stats");
            System.out.println("---------------------------------");
            System.out.print("Enter input: ");
        }
    }

    /**
     * Displays a portrait of the farmer using ASCII art
     */
    public void displayFarmer() {
        System.out.println("""

                _/`\\_
                  O \s
                 /|\\\s
                 / \\\s
                 """);
    }

    /**
     * Displays the seed store where the farmer can purchase seeds
     */
    public void displaySeedStore() {
        System.out.print("\nWELCOME TO THE SEED STORE!\n" +
                         "---------------------------\n");
        System.out.print("What crops would you like to buy?\n" +
                         "[1] Turnips - 5 OC\n" +
                         "[0] Exit Store\n");
        System.out.print("---------------------------\n");
        System.out.print("Enter input: ");
    }

    /**
     * Displays the information of a successful transaction from the seed store
     * @param choice specific seed that was purchased
     * @param count number of seeds purchased
     * @param cost base cost of plant
     * @param farmer current farmer (to obtain the cost reduction)
     */
    public void displayTransaction(int choice, int count, int cost, Farmer farmer) {
        System.out.println("\nSuccessful transaction!");
        // Will add more crops later on to switch statement
        switch (choice) {
            case 1:
                System.out.print("Turnip | ");
                break;
        }
        int discount = farmer.getCostReduction();
        System.out.printf("Cost - %d | Qty - %d\n", cost - discount, count);
        System.out.printf("\nTotal: %d\n", (cost - discount) * count);

    }

    /**
     * Displays the tool shed where the farmer can select a tool to equip
     */
    public void displayToolShed() {
        System.out.print("\nWELCOME TO THE TOOL SHED!\n" +
                         "---------------------------\n");
        System.out.print("Which tool do you want to equip?\n" +
                         "[1] Plough\n" +
                         "[2] Watering Can\n" +
                         "[3] Fertilizer\n" +
                         "[4] Pickaxe\n" +
                         "[5] Shovel\n");
        System.out.print("---------------------------\n");
        System.out.print("Enter input: ");
    }

    /**
     * Displays the game over screen along with the cause for getting a game over.
     * @param code the game over code indicating how the player lost the game
     * @param farmer the current farmer
     * @param farm the farm lot
     */
    public void displayGameOver(int code, Farmer farmer, FarmLot farm) {
        switch (code) {
            case 1:
                this.displayFarmerStats(farmer);
                System.out.println("\nYou can't buy any more seeds. Manage your money better.");
                break;
            case 2:
                System.out.println(farm.getTile(0).getAppearance());
                System.out.println("\nYou don't have any growing crops. Are you really a farmer?");
                break;
        }
        System.out.println("\nGame over!\n");
    }

    /**
     * Displays an error message for invalid inputs
     */
    public void displayErrorMessage() {
        System.out.print("Input is invalid. Enter another: ");
    }

    /**
     * Asks the player how many seeds they wish to purchase
     */
    public void displaySeedNumInput() {
        System.out.print("Enter number of seeds: ");
    }

    /**
     * Notifies the player that they don't have enough ObjectCoins for the transaction
     */
    public void displayNotEnoughCoins() {
        System.out.println("You do not have enough coins to buy this.");
    }

    /**
     * Notifies the player that they are using a tool outside its intended purpose
     */
    public void displayWrongTool() {
        System.out.println("You cannot use this tool on this tile.");
    }
}
