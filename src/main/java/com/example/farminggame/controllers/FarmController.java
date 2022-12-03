package com.example.farminggame.controllers;

import com.example.farminggame.models.environment.FarmLot;
import com.example.farminggame.models.environment.Tile;
import com.example.farminggame.models.environment.crops.*;
import com.example.farminggame.models.tools.*;
import com.example.farminggame.models.farmer.*;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;


public class FarmController {

    // STAGE VARIABLES
    private Stage stage;
    private StartController startController;
    private Model model;
    private final int SCENE_WIDTH = 1300;
    private final int SCENE_HEIGHT = 700;

    // ASSET VARIABLES
    private final String ASSETS_URL = FarmController.class.getResource("/com/example/farminggame" +
            "/assets").toExternalForm();
    private final String fxmlURL = "/com/example/farminggame/fxml/";

    private final String defaultTileImg = ASSETS_URL + "farm/default-tile.png";

    // GAME-WIDE VARIABLES
    private int days = 1;

    // To store all the tile buttons
    private ArrayList<Button> tileBtnList = new ArrayList<>();

    // Keeps track of the last pressed tile
    private int activeTileIndex = -1;

    // Selected crop while in the market
    private String selectedButton;

    // FXML VARIABLES
    @FXML private Text nameDisplay;

    @FXML private Text levelDisplay;
    @FXML private Text xpDisplay;
    @FXML private Text balanceDisplay;
    @FXML private Text dayDisplay;

    @FXML private GridPane farmLotGrid;
    @FXML private Button ploughBtn;
    @FXML private Button shovelBtn;
    @FXML private Button pickaxeBtn;
    @FXML private Button fertilizerBtn;
    @FXML private Button wateringCanBtn;
    @FXML private Button harvestBtn;
    @FXML private Button exitBtn;

    @FXML
    private SceneController sceneController;

    @FXML private TextField cropNameDesc;
    @FXML private Text harvestTimeDesc;
    @FXML private Text waterNeedsDesc;
    @FXML private Text fertilizerNeedsDesc;
    @FXML private Text productsProducedDesc;
    @FXML private Text basePriceDesc;

    @FXML
    private ImageView cropDescImage;

    // MODEL VARIABLES
    private FarmLot lot;
    private Farmer farmer;
    private Wallet wallet;
    private SeedPouch seedPouch;


    // List of possible seeds you can buy
    private ArrayList<Crop> seedStoreList = new ArrayList<>();

    private Plough plough = new Plough();
    private Fertilizer fertilizer = new Fertilizer();
    private Pickaxe pickaxe = new Pickaxe();
    private Shovel shovel = new Shovel();
    private WateringCan wateringCan = new WateringCan();

    public void createStage(Stage stage) {
        this.stage = stage;

         try {

             // Grab the .fxml file that represents the view
             FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlURL + "farmView.fxml"));
             fxmlLoader.setController(this);

             Parent root = fxmlLoader.load();

             // Set the new scene as the grabbed .fxml file
             Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
             stage.setScene(scene);

             // set the screen to full screen
             // stage.setFullScreen(true);

         } catch(Exception e) {
             e.printStackTrace();
         }
    }

    public void showStage() {
        this.stage.show();
    }
    @FXML private FlowPane toolButtons;
    // Adjust certain elements to their positions



    private void setActiveTileIndex(Button tileBtn) {
        // get the index of the tile in the farmlot arrayList
        if (tileBtn.getId().length() == 5) {
            this.activeTileIndex = Integer.parseInt(tileBtn.getId().substring(4)) - 1;
        } else if (tileBtn.getId().length() == 6) {
            this.activeTileIndex = Integer.parseInt(tileBtn.getId().substring(4, 6)) - 1;
        }
    }

    private void translateButton(Button button) {
        button.setTranslateX(-85);
    }
    @FXML private Button carrotBtn;
    @FXML private Button appleBtn;

    @FXML
    protected void displayButtons(ActionEvent tile) throws IOException{
        Object node = tile.getSource();
        Button tileBtn = (Button) node;

        setActiveTileIndex(tileBtn);

        System.out.println(this.activeTileIndex);

        // access the tile and check what its status is
        Tile activeTile = lot.getLot().get(this.activeTileIndex);

        // buttons that appear will depend on the tile status
        carrotBtn.setDisable(true);
        appleBtn.setDisable(true);

        // WE HAVE TO CLICK ON A TILE AGAIN THO

        if (activeTile.hasRock()) {
            //rock
            ploughBtn.setDisable(true);
            pickaxeBtn.setDisable(false);
            fertilizerBtn.setDisable(true);
            wateringCanBtn.setDisable(true);
            harvestBtn.setDisable(true);
        } else if (!(activeTile.isPlowed())) {
            // unplowed
            ploughBtn.setDisable(false);
            pickaxeBtn.setDisable(true);
            fertilizerBtn.setDisable(true);
            wateringCanBtn.setDisable(true);
            harvestBtn.setDisable(true);
        } else if (!(activeTile.hasCrop())) {
            // plowed
            pickaxeBtn.setDisable(true);
            carrotBtn.setDisable(false);
            appleBtn.setDisable(false);
        } else {
            pickaxeBtn.setDisable(true);
            ploughBtn.setDisable(true);

            Crop tileCrop = activeTile.getCrop();
            // check inventory for how many of this plant there are (JUST FOR TESTING)
            System.out.println(tileCrop.getSeedName() + " " + seedPouch.getSeedCount(tileCrop.getSeedName()));

            if (activeTile.hasHarvestableCrop()) {
                // harvestable
                fertilizerBtn.setDisable(false);
                wateringCanBtn.setDisable(false);
                harvestBtn.setDisable(false);
            } else if (activeTile.hasWitheredCrop()) {
                // withered
                fertilizerBtn.setDisable(true);
                wateringCanBtn.setDisable(true);
                harvestBtn.setDisable(true);
            } else {
                // crop
                fertilizerBtn.setDisable(false);
                wateringCanBtn.setDisable(false);
                harvestBtn.setDisable(true);
            }
        }

    }
    // TOOL METHODS

    // THERE SHOULD BE AN ADDITIONAL CONDITION TO CHECK IF FARMER HAS ENOUGH MONEY

    @FXML protected void usePlough(ActionEvent e) {
        Tile activeTile = lot.getTile(activeTileIndex);
        plough.plowTile(activeTile);

        // Grab the tile button from the list of tile buttons
        Button activeTileBtn = tileBtnList.get(activeTileIndex);

        // change appearance of tile
        activeTileBtn.setStyle(String.format("-fx-background-image: url(\"%s\");", ASSETS_URL + "farm/plowed-tile.png"));

        // Add farmer xp and deduct from wallet
        farmer.addXP(plough.getXP());
        wallet.setObjectCoins(-1 * plough.getCost());

        updateStats();
    }

    @FXML protected void useShovel(ActionEvent e) {
        // It can only do something if the tile has a crop
        Tile activeTile = lot.getTile(activeTileIndex);
        Button activeTileBtn = tileBtnList.get(activeTileIndex);

        if (activeTile.hasCrop()) {
            System.out.println("Plant successfully removed!");
            shovel.removePlant(activeTile);
            activeTileBtn.setStyle("-fx-background-image: url(\"" + defaultTileImg + "\");");
            farmer.addXP(pickaxe.getXP());
        } else {
            System.out.println("nothing happened lol");
        }

        wallet.setObjectCoins(-1 * shovel.getCost());

        updateStats();
    }

    @FXML protected void usePickaxe(ActionEvent e) {
        // Update model
        Tile activeTile = lot.getTile(activeTileIndex);
        pickaxe.removeRock(activeTile);

        // Update view
        Button activeTileBtn = tileBtnList.get(activeTileIndex);
        activeTileBtn.setStyle("-fx-background-image: url(\"" + defaultTileImg + "\");");

        // Add farmer xp and deduct from wallet
        farmer.addXP(pickaxe.getXP());
        wallet.setObjectCoins(-1 * pickaxe.getCost());

        updateStats();
    }

    @FXML
    protected void useFertilizer(ActionEvent e) {
        Tile activeTile = lot.getTile(activeTileIndex);

        // If tile has a crop and it's not withered.
        if (activeTile.hasCrop() && !(activeTile.hasWitheredCrop())) {
            fertilizer.fertilizeCrop(activeTile.getCrop());

            farmer.addXP(fertilizer.getXP());
            wallet.setObjectCoins(-1 * fertilizer.getCost());
            System.out.println("successfully fertilized plant");

            // SHOW CROP INFO IN-GAME
            System.out.println(activeTile.getCropInfo());

            updateStats();
        }
    }

    @FXML
    protected void useWateringCan(ActionEvent e) {
        Tile activeTile = lot.getTile(activeTileIndex);

        // If tile has a crop and it's not withered
        if (activeTile.hasCrop() && !(activeTile.hasWitheredCrop())) {
            wateringCan.waterCrop(activeTile.getCrop());

            farmer.addXP(wateringCan.getXP());
            wallet.setObjectCoins(-1 * fertilizer.getXP());

            System.out.println(activeTile.getCropInfo());

            updateStats();
        }
    }

    // PLANTING METHODS
    @FXML protected void plantCarrot(ActionEvent e) {
        Tile activeTile = lot.getTile(activeTileIndex);
        activeTile.setCrop(new Carrot());

        // Grab the tile button from the list of tile buttons
        Button activeTileBtn = tileBtnList.get(activeTileIndex);

        // change appearance of tile
        activeTileBtn.setStyle(String.format("-fx-background-image: url(\"%s\");", ASSETS_URL + "crops/carrot.jpg"));

    }

    @FXML protected void plantApple(ActionEvent e) {
        Tile activeTile = lot.getTile(activeTileIndex);
        activeTile.setCrop(new Apple());

        // Grab the tile button from the list of tile buttons
        Button activeTileBtn = tileBtnList.get(activeTileIndex);

        // change appearance of tile
        activeTileBtn.setStyle(String.format("-fx-background-image: url(\"%s\");", ASSETS_URL + "crops/apple.jpg"));

    }

    @FXML
    protected void exitGame() {
        sceneController.switchToStartView();
    }
    @FXML TextField cropNumber;
    @FXML Text successText;
    @FXML Text failedText;


    @FXML
    protected void buyCrop() {

        boolean seedBought = false;
        int cropNum = Integer.parseInt(cropNumber.getText());
        System.out.println(this.selectedButton);

        // Pass what crop to buy to the farmer
        switch (this.selectedButton) {
            case "Apple" -> seedBought = farmer.buySeeds(new Apple(), cropNum);
            case "Carrot" -> seedBought = farmer.buySeeds(new Carrot(), cropNum);
            case "Mango" -> seedBought = farmer.buySeeds(new Mango(), cropNum);
            case "Potato" -> seedBought = farmer.buySeeds(new Potato(), cropNum);
            case "Rose" -> seedBought = farmer.buySeeds(new Rose(), cropNum);
            case "Sunflower" -> seedBought = farmer.buySeeds(new Sunflower(), cropNum);
            case "Tulip" -> seedBought = farmer.buySeeds(new Tulip(), cropNum);
            case "Turnip" -> seedBought = farmer.buySeeds(new Turnip(), cropNum);
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        if(seedBought) {
            successText.setTranslateX(-150);
            successText.setVisible(true);
            pause.setOnFinished(
                    f -> successText.setVisible(false));
            pause.play();
        } else {
            failedText.setTranslateX(-150);
            failedText.setVisible(true);
            pause.setOnFinished(
                    f -> failedText.setVisible(false));
            pause.play();
        }

        cropNumber.setText("");
        seedPouch.showSeedList();
        balanceDisplay.setText("Balance: " + wallet.getObjectCoins());
    }


    // POP UP CONTROLLERS

    // Variables
    @FXML private GridPane profilePane;
    @FXML private GridPane marketPane;
    @FXML private Button profileBtn;
    @FXML private Button openMarketBtn;
    @FXML private Rectangle marketRectangle;
    @FXML private Rectangle descriptionRectangle;
    @FXML private VBox cropDescription;
    @FXML private Button exitMarket;


    @FXML
    protected void openProfile(){
        System.out.println("Called profile");
        farmerUpgradeButton();
        profilePane.setVisible(true);
        disableButtons();
    }

    @FXML
    protected void exitProfile() {
        System.out.println("Exit profile");
        profilePane.setVisible(false);
        enableButtons();
    }

    @FXML
    protected void openMarket(){
        System.out.println("Called market");

        marketPane.setVisible(true);

        cropNumber.setText("");
        disableButtons();
    }

    @FXML
    protected void exitMarket() {
        System.out.println("Exit market");
        descriptionRectangle.setVisible(false);
        marketPane.setVisible(false);
        marketRectangle.setWidth(500);
        cropDescription.setVisible(false);
        exitMarket.setTranslateX(0);
        enableButtons();

    }
    // Expand the market when any of the plant buttons are pressed
    // NEXT DAY
    @FXML
    protected void nextDay() {
        this.days++;

        // Grab all the crops currently planted
        ArrayList<Crop> cropList = lot.getCropsPlanted();

        // advance the days planted for each crop
        for (Crop crop : cropList) {
            crop.addDayPlanted();
        }

        // Update the view
        updateDay();
    }

    private void disableButtons() {
        exitBtn.setDisable(true);
        profileBtn.setDisable(true);
        openMarketBtn.setDisable(true);
        ploughBtn.setDisable(true);
        shovelBtn.setDisable(true);
        pickaxeBtn.setDisable(true);
        fertilizerBtn.setDisable(true);
        wateringCanBtn.setDisable(true);
        harvestBtn.setDisable(true);
    }

    private void enableButtons() {
        exitBtn.setDisable(false);
        profileBtn.setDisable(false);
        openMarketBtn.setDisable(false);
    }


    private void expandMarket() {
        marketRectangle.setWidth(800);
        descriptionRectangle.setTranslateX(500);
        cropDescription.setTranslateX(555);
        cropDescription.setVisible(true);
        descriptionRectangle.setVisible(true);
        exitMarket.setTranslateX(300);
        exitMarket.toFront();
    }

    // Variables
    @FXML private Button upgradeFarmerBtn;
    @FXML private Text farmerNamePopUp;
    @FXML private Text farmerRankPopUp;

    private void farmerUpgradeButton() {
        // check if a farmer can be upgraded
        if (farmer.getCanUpgrade() != -1) {
            upgradeFarmerBtn.setDisable(false);
        } else {
            upgradeFarmerBtn.setDisable(true);
        }
    }

    // FARMER PROFILE CODE
    @FXML
    protected void upgradeFarmer() {
        farmer.upgradeFarmer();
        farmerRankPopUp.setText(farmer.getFarmerType());
    }

    @FXML
    protected void updateStats() {
        levelDisplay.setText("Level: " + farmer.getLevel());
        xpDisplay.setText("XP: " + farmer.getCurrentXP());
        balanceDisplay.setText("Balance: " + wallet.getObjectCoins());
    }

    @FXML
    protected void updateDay() {
        dayDisplay.setText("Day: " + days);

        // Check each tile if it has a withered crop
        for (Tile tile : lot.getLot()) {
            if (tile.hasWitheredCrop()) {
                System.out.println("There's a withered crop lol");
                // change background of tile to a withered tile
                // HAVE TO FIND A WAY TO CHANGE ALL TILES WITHOUT ACTUALLY PRESSING THEM
            }
        }
    }

    @FXML
    protected void displayMarketInformation(ActionEvent event) throws IOException {
        //Switch image in the market description
        Image carrotImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/assets/crops/carrot.jpg"));
        Image appleImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/assets/crops/apple.jpg"));
        Image mangoImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/assets/crops/mango.jpg"));
        Image potatoImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/assets/crops/potato.jpg"));
        Image turnipImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/assets/crops/turnip.jpg"));

        Carrot carrot = new Carrot();
        Apple apple = new Apple();
        Mango mango = new Mango();
        Potato potato = new Potato();
        Rose rose = new Rose();
        Sunflower sunflower = new Sunflower();
        Tulip tulip = new Tulip();
        Turnip turnip = new Turnip();

        expandMarket();
        Object node = event.getSource();

        Button button = (Button) node;
        System.out.println("DISPLAYING " + button.getId());

        if (button.getId().equals("marketCarrotBtn")) {
            setSelectedButton("Carrot");
            System.out.println(getClass().getResourceAsStream("/com/example/farminggame/assets/crops/carrot.jpg"));
            cropDescImage.setImage(carrotImage);
            cropNameDesc.setText(carrot.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + carrot.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + carrot.getWaterNeeds() + "(" + carrot.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + carrot.getFertilizerNeeds() + "(" + carrot.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + carrot.getSellingPrice());
        }
        else if (button.getId().equals("marketAppleBtn")) {
            setSelectedButton("Apple");
            cropDescImage.setImage(appleImage);
            cropNameDesc.setText(apple.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + apple.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + apple.getWaterNeeds() + "(" + apple.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + apple.getFertilizerNeeds() + "(" + apple.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + apple.getSellingPrice());
        }
        else if (button.getId().equals("marketMangoBtn")) {
            setSelectedButton("Mango");
            cropDescImage.setImage(mangoImage);
            cropNameDesc.setText(mango.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + mango.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + mango.getWaterNeeds() + "(" + mango.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + mango.getFertilizerNeeds() + "(" + mango.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + mango.getSellingPrice());
        }
        else if (button.getId().equals("marketPotatoBtn")) {
            setSelectedButton("Potato");
            cropDescImage.setImage(potatoImage);
            cropNameDesc.setText(potato.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + potato.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + potato.getWaterNeeds() + "(" + potato.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + potato.getFertilizerNeeds() + "(" + potato.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + potato.getSellingPrice());
        }
        else if (button.getId().equals("marketRoseBtn")) {
            setSelectedButton("Rose");
            harvestTimeDesc.setText("Harvest Time: " + rose.getHarvestTime());
            cropNameDesc.setText(rose.getSeedName());
            waterNeedsDesc.setText("Water Needs (Bonus): " + rose.getWaterNeeds() + "(" + rose.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + rose.getFertilizerNeeds() + "(" + rose.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + rose.getSellingPrice());
        }
        else if (button.getId().equals("marketSunflowerBtn")) {
            setSelectedButton("Sunflower");

            cropNameDesc.setText(sunflower.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + sunflower.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + sunflower.getWaterNeeds() + "(" + sunflower.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + sunflower.getFertilizerNeeds() + "(" + sunflower.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + sunflower.getSellingPrice());
        }
        else if (button.getId().equals("marketTulipBtn")) {
            setSelectedButton("Tulip");

            cropNameDesc.setText(tulip.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + tulip.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + tulip.getWaterNeeds() + "(" + tulip.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + tulip.getFertilizerNeeds() + "(" + tulip.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + tulip.getSellingPrice());
        }
        else if (button.getId().equals("marketTurnipBtn")) {
            setSelectedButton("Turnip");

            cropDescImage.setImage(turnipImage);
            cropNameDesc.setText(turnip.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + turnip.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + turnip.getWaterNeeds() + "(" + turnip.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + turnip.getFertilizerNeeds() + "(" + turnip.getFertilizerBonusLimit() + ")");
            basePriceDesc.setText("Base Selling Price: " + turnip.getSellingPrice());
        }

    }

    // Initializes the view upon switching to it
    @FXML
    private void initialize() throws IOException {
        InputStream is = getClass().getResourceAsStream("/com/example/farminggame/input/rockInput.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String rockInput;
        int tileIdCount = 1;

        // Check the rockInput.txt file for positions of rocks
        this.lot = new FarmLot();

        while ((rockInput = br.readLine()) != null) {
            lot.setRockPosition(Integer.parseInt(rockInput));
        }

        // Create the farm lot of 50 tiles (5 rows, 10 columns)
        GridPane newFarm = farmLotGrid;

        // Initialize the farm lot
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 10; column++) {
                Button newTile = new Button();
                // Set the respective id of a tile (tile 1, tile 2, etc.)
                newTile.setId("tile" + tileIdCount);

                // Check the FarmLot model if this rock should have a tile or not
                if (lot.getTile(tileIdCount - 1).hasRock()) {
                    newTile.getStyleClass().add("rock-tile");
                } else {
                    newTile.getStyleClass().add("unplowed-tile");
                }

                newTile.getStyleClass().add("farm-tile");
                newTile.getStyleClass().add("imgBtn");

                newTile.setOnAction(event -> {
                    try {
                        displayButtons(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                // Add new tile to its corresponding row and column position (column, row)
                newFarm.add(newTile, column, row);

                // Add the new tile to the arraylist of all tiles (for the controller to change the visual attributes)
                tileBtnList.add(newTile);

                tileIdCount++;
            }
        }

        // Initializing UI elements
        nameDisplay.setText(farmer.getName());
        updateDay();
        updateStats();

        // Initialize seed store list
        seedStoreList.add(new Apple());
        seedStoreList.add(new Carrot());
        seedStoreList.add(new Mango());
        seedStoreList.add(new Potato());
        seedStoreList.add(new Rose());
        seedStoreList.add(new Sunflower());
        seedStoreList.add(new Tulip());
        seedStoreList.add(new Turnip());

        // Setting event handlers
        exitBtn.setOnAction(event -> exitGame());
    }


    private void setSelectedButton(String button) {
        this.selectedButton = button;
    }

    // GETTERS AND SETTERS

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
        this.wallet = farmer.getWallet();
        this.seedPouch = farmer.getSeedPouch();
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
