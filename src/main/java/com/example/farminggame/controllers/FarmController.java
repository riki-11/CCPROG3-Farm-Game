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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;


public class FarmController {

    // STAGE VARIABLES
    private Stage stage;
    private StartController startController;

    private final int SCENE_WIDTH = 1350;
    private final int SCENE_HEIGHT = 850;

    // ASSET VARIABLES
    private final String ASSETS_URL = FarmController.class.getResource("/com/example/farminggame" +
            "/new-assets").toExternalForm();
    private final String fxmlURL = "/com/example/farminggame/fxml/";

    // GAME-WIDE VARIABLES
    private int days = 1;

    // List of possible seeds you can buy
    private ArrayList<Crop> seedStoreList = new ArrayList<>();

    // To store all the tile buttons
    private ArrayList<Button> tileBtnList = new ArrayList<>();

    // Keeps track of the last pressed tile
    private int activeTileIndex = -1;

    // Selected crop while in the market
    private String selectedButton;

    // MODEL VARIABLES
    private FarmLot lot;
    private Farmer farmer;
    private Wallet wallet;
    private SeedPouch seedPouch;
    private Plough plough = new Plough();
    private Fertilizer fertilizer = new Fertilizer();
    private Pickaxe pickaxe = new Pickaxe();
    private Shovel shovel = new Shovel();
    private WateringCan wateringCan = new WateringCan();

    // Game-Wide FXML Variables
    @FXML private Button exitBtn;

    @FXML
    private SceneController sceneController;

    // Profile FXML Variables
    @FXML private Text nameDisplay;

    @FXML private Text levelDisplay;
    @FXML private Text xpDisplay;
    @FXML private Text balanceDisplay;
    @FXML private Text dayDisplay;

    // Farm Lot FXML Variables
    @FXML private GridPane farmLotGrid;
    @FXML private Text farmTitle;

    // Tool Buttons FXML Variables
    @FXML private FlowPane toolButtons;
    @FXML private Button ploughBtn;
    @FXML private Button shovelBtn;
    @FXML private Button pickaxeBtn;
    @FXML private Button fertilizerBtn;
    @FXML private Button wateringCanBtn;
    @FXML private Button harvestBtn;

    // Tile Information FXML Variables
    @FXML private ImageView tileDescImage;
    @FXML private TextField tileDescLabel;
    @FXML private Text tileDescText;
    @FXML private VBox tileDescriptionBox;

    // Crop Buttons FXML Variables
    @FXML private FlowPane cropButtons;
    @FXML private Button appleBtn;
    @FXML private Button carrotBtn;
    @FXML private Button mangoBtn;
    @FXML private Button potatoBtn;
    @FXML private Button roseBtn;
    @FXML private Button sunflowerBtn;
    @FXML private Button tulipBtn;
    @FXML private Button turnipBtn;
    @FXML private Text appleCount;
    @FXML private Text carrotCount;
    @FXML private Text mangoCount;
    @FXML private Text potatoCount;
    @FXML private Text roseCount;
    @FXML private Text sunflowerCount;
    @FXML private Text tulipCount;
    @FXML private Text turnipCount;

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

    // Adjust certain elements to their positions
    private void setActiveTileIndex(Button tileBtn) {
        // get the index of the tile in the farmlot arrayList
        if (tileBtn.getId().length() == 5) {
            this.activeTileIndex = Integer.parseInt(tileBtn.getId().substring(4)) - 1;
        } else if (tileBtn.getId().length() == 6) {
            this.activeTileIndex = Integer.parseInt(tileBtn.getId().substring(4, 6)) - 1;
        }
    }

    private void setCropData(Tile activeTile) {
        Image appleImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-apple.png"));
        Image carrotImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-carrot.png"));
        Image mangoImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-mango.png"));
        Image potatoImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-potato.png"));
        Image roseImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-rose.png"));
        Image sunflowerImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-sunflower.png"));
        Image tulipImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-tulip.png"));
        Image turnipImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-turnip.png"));

        String seedName = activeTile.getCrop().getCropName();

        if (seedName.equals("Apple")) {
            tileDescImage.setImage(appleImage);
            tileDescLabel.setText("APPLE");
        } else if (seedName.equals("Carrot")) {
            tileDescImage.setImage(carrotImage);
            tileDescLabel.setText("CARROT");
        } else if (seedName.equals("Mango")) {
            tileDescImage.setImage(mangoImage);
            tileDescLabel.setText("MANGO");
        } else if (seedName.equals("POtato")) {
            tileDescImage.setImage(potatoImage);
            tileDescLabel.setText("POTATO");
        } else if (seedName.equals("Rose")) {
            tileDescImage.setImage(roseImage);
            tileDescLabel.setText("ROSE");
        } else if (seedName.equals("Sunflower")) {
            tileDescImage.setImage(sunflowerImage);
            tileDescLabel.setText("SUNFLOWER");
        } else if (seedName.equals("Turnip")) {
            tileDescImage.setImage(turnipImage);
            tileDescLabel.setText("TURNIP");
        } else if (seedName.equals("Tulip")) {
            tileDescImage.setImage(tulipImage);
            tileDescLabel.setText("TULIP");
        }
    }

    @FXML
    protected void display(ActionEvent tile) throws IOException {
        Object node = tile.getSource();
        Button tileBtn = (Button) node;

        setActiveTileIndex(tileBtn);
        displayButtons();
        displayData();
    }

    private void displayData() {
        Image rockImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/rock.png"));
        Image unplowedImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/default-tile.png"));
        Image plowedImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/plowed-tile.png"));
        Image witheredImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/withered-crop.png"));
        Image seedImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/environment/planted-tile.png"));

        Tile activeTile = lot.getTile(this.activeTileIndex);

        System.out.println("Crop: " + activeTile.hasCrop());
        if (activeTile.hasRock()) {
            tileDescImage.setImage(rockImage);
            tileDescLabel.setText("ROCKS");
            tileDescText.setText("Use a pickaxe to get\nrid of the rocks");
        } else if (!(activeTile.isPlowed())) {
            tileDescImage.setImage(unplowedImage);
            tileDescLabel.setText("UNPLOWED TILE");
            tileDescText.setText("Plow tile to plant crops");

        } else if (!(activeTile.hasCrop())) {
            tileDescImage.setImage(plowedImage);
            tileDescLabel.setText("PLOWED TILE");
            tileDescText.setText("You can now plant\na crop on this tile");
        } else {
            System.out.println("With crop");
            if (activeTile.hasHarvestableCrop()) {
                setCropData(activeTile);
                tileDescText.setText(activeTile.getCropInfo());
            } else if (activeTile.hasWitheredCrop()) {
                tileDescImage.setImage(witheredImage);
                tileDescLabel.setText("WITHERED CROP");
                tileDescText.setText("Oh, no! Your crop died.");
            } else {
                System.out.println("Seed");
                tileDescImage.setImage(seedImage);
                tileDescLabel.setText(activeTile.getCrop().getCropName());
                tileDescText.setText(activeTile.getCropInfo());
            }
        }
        tileDescriptionBox.setVisible(true);
    }

    private void displayButtons(){

        Tile activeTile = lot.getLot().get(this.activeTileIndex);

        // buttons that appear will depend on the tile status
        toolButtons.setVisible(true);
        ploughBtn.setVisible(true);
        pickaxeBtn.setVisible(true);
        fertilizerBtn.setVisible(true);
        wateringCanBtn.setVisible(true);
        shovelBtn.setTranslateX(0);
        cropButtons.setVisible(false);

        // Check the status of the last clicked tile (aka the active Tile)
        if (activeTile.hasRock()) {
            // If tile has a rock
            ploughBtn.setDisable(true);
            pickaxeBtn.setDisable(false);
            fertilizerBtn.setDisable(true);
            wateringCanBtn.setDisable(true);
            harvestBtn.setDisable(true);
        } else if (!(activeTile.isPlowed())) {
            // If tile is unplowed
            ploughBtn.setDisable(false);
            pickaxeBtn.setDisable(true);
            fertilizerBtn.setDisable(true);
            wateringCanBtn.setDisable(true);
            harvestBtn.setDisable(true);
        } else if (!(activeTile.hasCrop())) {
            // If tile is plowed
            // remove all tools except shovel
            ploughBtn.setVisible(false);
            pickaxeBtn.setVisible(false);
            fertilizerBtn.setVisible(false);
            wateringCanBtn.setVisible(false);
            harvestBtn.setVisible(false);
            shovelBtn.setTranslateX(510);
            shovelBtn.setTranslateY(15);
            
            showCropBtns(); // method to show crops depending on seed inventory
            
        } else {
            pickaxeBtn.setDisable(true);
            ploughBtn.setDisable(true);

            Crop tileCrop = activeTile.getCrop();

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

    @FXML protected void useTool(ActionEvent event) {
        Tile activeTile = lot.getTile(activeTileIndex);
        Button activeTileBtn = tileBtnList.get(activeTileIndex);
        Object node = event.getSource();

        Button tool = (Button) node;
        String toolID = tool.getId();

        if (toolID.equals("ploughBtn")) {
            farmer.usePlough(activeTile, plough);
            setBtnImage(activeTileBtn, "environment/plowed-tile.png");
        } else if (toolID.equals("pickaxeBtn")) {
            farmer.usePickaxe(activeTile, pickaxe);
            setBtnImage(activeTileBtn, "environment/default-tile.png");
        } else if (toolID.equals("shovelBtn")) {
            if (activeTile.hasCrop()) {
                setBtnImage(activeTileBtn, "environment/default-tile.png");
            }
            farmer.useShovel(activeTile, shovel);
        } else if (toolID.equals("fertilizerBtn")) {
            farmer.useFertilizer(activeTile, fertilizer);
        } else if (toolID.equals("wateringCanBtn")) {
            farmer.useWateringCan(activeTile, wateringCan);
        } else if (toolID.equals("harvestBtn")) {
            // Harvest crop returns boolean. Use this to notify the user.
            farmer.harvestCrop(activeTile);
            // Change the tile image back to the default state
            setBtnImage(activeTileBtn, "environment/default-tile.png");
        }

        updateStats();
        toolButtons.setVisible(false);
        tileDescriptionBox.setVisible(false);
    }

    private boolean hasAdjacentCrops() {
        // check if the tile has any adjacent crops or rocks
        if (!(lot.getTile(activeTileIndex - 10).hasCrop() || lot.getTile(activeTileIndex - 10).hasRock()) &&
            !(lot.getTile(activeTileIndex - 9).hasCrop() || lot.getTile(activeTileIndex - 9).hasRock()) &&
            !(lot.getTile(activeTileIndex - 11).hasCrop() || lot.getTile(activeTileIndex - 11).hasRock()) &&
            !(lot.getTile(activeTileIndex + 10).hasCrop() || lot.getTile(activeTileIndex + 10).hasRock()) &&
            !(lot.getTile(activeTileIndex + 9).hasCrop() || lot.getTile(activeTileIndex + 9).hasRock()) &&
            !(lot.getTile(activeTileIndex + 11).hasCrop() || lot.getTile(activeTileIndex + 11).hasRock()) &&
            !(lot.getTile(activeTileIndex + 1).hasCrop() || lot.getTile(activeTileIndex + 1).hasRock()) &&
            !(lot.getTile(activeTileIndex - 1).hasCrop() || lot.getTile(activeTileIndex - 1).hasRock())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean inRange() {
        // check if the tile is not on the edges
        if ((activeTileIndex >= 11 && activeTileIndex <= 18) ||
                (activeTileIndex >= 21 && activeTileIndex <= 28) ||
                (activeTileIndex >= 31 && activeTileIndex <= 38)) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    protected void showCropBtns() {
        // show all crop buttons
        cropButtons.setVisible(true);

        // check seed inventory for each crop count
        Hashtable<String, Integer> inventory = seedPouch.getSeedList();

        int apples = inventory.get("Apple");
        int carrots = inventory.get("Carrot");
        int mangoes = inventory.get("Mango");
        int potatoes = inventory.get("Potato");
        int roses = inventory.get("Rose");
        int sunflowers = inventory.get("Sunflower");
        int tulips = inventory.get("Tulip");
        int turnips = inventory.get("Turnip");

        appleCount.setText(Integer.toString(apples));
        carrotCount.setText(Integer.toString(carrots));
        mangoCount.setText(Integer.toString(mangoes));
        potatoCount.setText(Integer.toString(potatoes));
        roseCount.setText(Integer.toString(roses));
        sunflowerCount.setText(Integer.toString(sunflowers));
        tulipCount.setText(Integer.toString(tulips));
        turnipCount.setText(Integer.toString(turnips));

        // If the farmer hasn't bought at least one of each crop, disable it in the view.
        if (apples == 0) {
            appleBtn.setDisable(true);
        } else {
            appleBtn.setDisable(false);
        }
        if (carrots == 0) {
            carrotBtn.setDisable(true);
        } else {
            carrotBtn.setDisable(false);
        }
        if (mangoes == 0) {
            mangoBtn.setDisable(true);
        } else {
            mangoBtn.setDisable(false);
        }
        if (potatoes == 0) {
            potatoBtn.setDisable(true);
        } else {
            potatoBtn.setDisable(false);
        }
        if (roses == 0) {
            roseBtn.setDisable(true);
        } else {
            roseBtn.setDisable(false);
        }
        if (sunflowers == 0) {
            sunflowerBtn.setDisable(true);
        } else {
            sunflowerBtn.setDisable(false);
        }
        if (tulips == 0) {
            tulipBtn.setDisable(true);
        } else {
            tulipBtn.setDisable(false);
        }
        if (turnips == 0) {
            turnipBtn.setDisable(true);
        } else {
            turnipBtn.setDisable(false);
        }
    }

    @FXML
    protected void plantCrop(ActionEvent event) {
        Tile activeTile = lot.getTile(activeTileIndex);
        Button activeTileBtn = tileBtnList.get(activeTileIndex);
        Object node = event.getSource();

        boolean canPlant = true;
        Button seed = (Button) node;

        if (seed.getId().equals("carrotBtn")) {
            farmer.plantSeed(activeTile, "Carrot");
            // change appearance of tile
            setBtnImage(activeTileBtn, "environment/planted-carrot.png");
        } else if (seed.getId().equals("roseBtn")) {
            farmer.plantSeed(activeTile, "Rose");
            setBtnImage(activeTileBtn, "environment/planted-rose.png");
        } else if (seed.getId().equals("sunflowerBtn")) {
            farmer.plantSeed(activeTile, "Sunflower");
            setBtnImage(activeTileBtn, "environment/planted-sunflower.png");
        } else if (seed.getId().equals("tulipBtn")) {
            farmer.plantSeed(activeTile, "Tulip");
            setBtnImage(activeTileBtn,"environment/planted-tulip.png" );
        } else if (seed.getId().equals("potatoBtn")) {
            farmer.plantSeed(activeTile, "Potato");
            setBtnImage(activeTileBtn, "environment/planted-potato.png");
        } else if (seed.getId().equals("turnipBtn")) {
            farmer.plantSeed(activeTile, "Turnip");
            setBtnImage(activeTileBtn, "environment/planted-turnip.png");

        } else {
            if (inRange() && !hasAdjacentCrops()) {
                if (seed.getId().equals("appleBtn")) {
                    farmer.plantSeed(activeTile, "Apple");
                    setBtnImage(activeTileBtn, "environment/planted-apple.png");
                } else if (seed.getId().equals("mangoBtn")) {
                    farmer.plantSeed(activeTile, "Mango");
                    setBtnImage(activeTileBtn, "environment/planted-mango.png");
                }
            } else {
                canPlant = false;
                System.out.println("Fruit trees cannot be planted on tiles with adjacent crops");
                // TODO: add a pop-up for that
            }
        }

        if (canPlant) {
            setBtnImage(activeTileBtn, "environment/planted-tile.png");
        }

        updateStats();
        cropButtons.setVisible(false);
    }


    @FXML TextField cropNumber;

    @FXML Text successText;
    @FXML Text failedText;

    @FXML
    protected void buyCrop() {

        boolean seedBought = false;
        int cropNum = Integer.parseInt(cropNumber.getText());

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

        if (seedBought) {
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
        updateStats();
    }


    // POP UP CONTROLLERS
    @FXML private StackPane profilePane;
    @FXML private GridPane marketPane;
    @FXML private Button profileBtn;
    @FXML private Button openMarketBtn;
    @FXML private Rectangle marketRectangle;
    @FXML private Rectangle descriptionRectangle;
    @FXML private ImageView cropDescription;
    @FXML private Button exitMarket;
    @FXML private ImageView farmerProfileDesc;

    private void setProfilePane() {
        Image farmerImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/profile/farmer.png"));
        Image registeredFarmerImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/profile/registered.png"));
        Image distinguishedFarmerImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/profile/distinguished.png"));
        Image legendaryFarmerImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/profile/legendary.png"));

        if (farmer.getFarmerType().equals("Farmer")) {
            farmerProfileDesc.setImage(farmerImage);
        } else if (farmer.getFarmerType().equals("Registered Farmer")) {
            farmerProfileDesc.setImage(registeredFarmerImage);
        } else if (farmer.getFarmerType().equals("Distinguished Farmer")) {
            farmerProfileDesc.setImage(distinguishedFarmerImage);
        } else if (farmer.getFarmerType().equals("Legendary Farmer")) {
            farmerProfileDesc.setImage(legendaryFarmerImage);
        }
    }

    @FXML
    protected void openProfile(){
        System.out.println("Called profile");
        farmerUpgradeButton();
        setProfilePane();
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
        toolButtons.setVisible(false);
        cropButtons.setVisible(false);
    }

    private void enableButtons() {
        exitBtn.setDisable(false);
        profileBtn.setDisable(false);
        openMarketBtn.setDisable(false);
    }


    private void expandMarket() {
        marketRectangle.setWidth(800);
        descriptionRectangle.setTranslateX(500);
        cropDescription.setTranslateX(250);
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

        // After each day, check if there are any withered crops
        updateWitheredCrops();
        updateHarvestableCrops();

        // Hide any tile-related buttons or panes
        toolButtons.setVisible(false);
        cropButtons.setVisible(false);
        tileDescriptionBox.setVisible(false);
    }

    private void updateHarvestableCrops() {
        // Check each tile if it has a harvestable crop
        for (int i = 0; i < 50; i++) {
            Tile tile = lot.getTile(i);
            if (tile.hasHarvestableCrop()) {
                // change the image of the tile to the corresponding fully grown crop
                Button tileBtn = tileBtnList.get(i);
                switch (tile.getCrop().getCropName()) {
                    case "Apple" -> setBtnImage(tileBtn, "environment/planted-apple.png");
                    case "Carrot" -> setBtnImage(tileBtn, "environment/planted-carrot.png");
                    case "Mango" -> setBtnImage(tileBtn, "environment/planted-mango.png");
                    case "Potato" -> setBtnImage(tileBtn, "environment/planted-potato.png");
                    case "Rose" -> setBtnImage(tileBtn, "environment/planted-rose.png");
                    case "Sunflower" -> setBtnImage(tileBtn, "environment/planted-sunflower.png");
                    case "Tulip" -> setBtnImage(tileBtn, "environment/planted-tulip.png");
                    case "Turnip" -> setBtnImage(tileBtn, "environment/planted-turnip.png");
                }
            }
        }
    }

    private void updateWitheredCrops() {
        // Check each tile if it has a withered crop (MAKE THIS INTO ITS OWN METHOD)
        for (int i = 0; i < 50; i++) {
            Tile tile = lot.getTile(i);
            if (tile.hasWitheredCrop()) {
                // Grab the corresponding tile button from the list of tile buttons.
                Button tileBtn = tileBtnList.get(i);
                setBtnImage(tileBtn, "environment/withered-crop.png");
            }
        }
    }

    @FXML
    protected void displayMarketInformation(ActionEvent event) throws IOException {
        //Switch image in the market description
        Image carrotImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/carrot-desc.png"));
        Image appleImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/apple-desc.png"));
        Image mangoImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/mango-desc.png"));
        Image potatoImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/potato-desc.png"));
        Image roseImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/rose-desc.png"));
        Image sunflowerImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/sunflower-desc.png"));
        Image tulipImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/tulip-desc.png"));
        Image turnipImage = new Image(getClass().getResourceAsStream("/com/example/farminggame/new-assets/market/displays/turnip-desc.png"));

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
            cropDescription.setImage(carrotImage);
        }
        else if (button.getId().equals("marketAppleBtn")) {
            setSelectedButton("Apple");
            cropDescription.setImage(appleImage);
        }
        else if (button.getId().equals("marketMangoBtn")) {
            setSelectedButton("Mango");
            cropDescription.setImage(mangoImage);
        }
        else if (button.getId().equals("marketPotatoBtn")) {
            setSelectedButton("Potato");
            cropDescription.setImage(potatoImage);
        }
        else if (button.getId().equals("marketRoseBtn")) {
            setSelectedButton("Rose");
            cropDescription.setImage(roseImage);
        }
        else if (button.getId().equals("marketSunflowerBtn")) {
            setSelectedButton("Sunflower");
            cropDescription.setImage(sunflowerImage);
        }
        else if (button.getId().equals("marketTulipBtn")) {
            setSelectedButton("Tulip");
            cropDescription.setImage(tulipImage);
        }
        else if (button.getId().equals("marketTurnipBtn")) {
            setSelectedButton("Turnip");
            cropDescription.setImage(turnipImage);
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
                        display(event);
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
        farmTitle.setText(farmer.getName() + "'s Farm");
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
    @FXML
    protected void exitGame() {
        sceneController.switchToStartView();
    }

    private void setBtnImage(Button button, String imagePath) {
        button.setStyle(String.format("-fx-background-image: url(\"%s\");", ASSETS_URL + imagePath));
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
