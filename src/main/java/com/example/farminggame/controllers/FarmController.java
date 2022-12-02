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
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;


public class FarmController {

    // STAGE VARIABLES
    private Stage stage;
    private StartController startController;
    private Model model;
    private final int SCENE_WIDTH = 1300;
    private final int SCENE_HEIGHT = 700;

    private final String ASSETS_URL = FarmController.class.getResource("/com/example/farminggame" +
            "/assets").toExternalForm();
    private final String fxmlURL = "/com/example/farminggame/fxml/";

    private int activeTileIndex = 3; // CHANGE THIS

    // To store all the tile buttons
    private ArrayList<Button> tileBtnList = new ArrayList<>();

    // FXML VARIABLES
    @FXML private Text nameDisplay;

    @FXML private Text levelDisplay;
    @FXML private Text balanceDisplay;

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


    // FXML-RELATED METHODS
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

         } catch(Exception e) {
             e.printStackTrace();
         }
    }

    public void showStage() {
        this.stage.show();
    }

    @FXML private FlowPane toolButtons;
    // Adjust certain elements to their positions
    private void translateView() {
        toolButtons.setTranslateX(285);
        toolButtons.setTranslateY(475);
    }

    @FXML
    protected void displayButtons(ActionEvent tile) {
        Object node = tile.getSource();
        Button tileBtn = (Button) node;

    }

    // TOOL METHODS
    @FXML protected void usePlough(ActionEvent e) {
        Tile activeTile = lot.getTile(activeTileIndex);
        plough.plowTile(activeTile);

        // Grab the tile button from the list of tile buttons
        Button activeTileBtn = tileBtnList.get(activeTileIndex);
        activeTileBtn.setStyle(String.format("-fx-background-image: url(\"%s\");", ASSETS_URL + "farm/plowed-tile.png"));
        // change appearance of tile

        // grab index of active tile
        // pass it into plough
        // voila
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
    private void disableButtons() {
        exitBtn.setDisable(true);
        profileBtn.setDisable(true);
        openMarketBtn.setDisable(true);
    }

    private void enableButtons() {
        exitBtn.setDisable(false);
        profileBtn.setDisable(false);
        openMarketBtn.setDisable(false);
    }


    // Expand the market when any of the plant buttons are pressed
    private void expandMarket() {
        marketRectangle.setWidth(800);
        descriptionRectangle.setTranslateX(500);
        cropDescription.setTranslateX(555);
        cropDescription.setVisible(true);
        descriptionRectangle.setVisible(true);
        exitMarket.setTranslateX(300);
        exitMarket.toFront();
    }

    @FXML private TextField cropNameDesc;
    @FXML private Text harvestTimeDesc;
    @FXML private Text waterNeedsDesc;
    @FXML private Text fertilizerNeedsDesc;
    @FXML private Text productsProducedDesc;
    @FXML private Text basePriceDesc;

    @FXML
    private ImageView cropDescImage;

    // Selected crop while in the market
    private String selectedButton;

    private void setSelectedButton(String button) {
        this.selectedButton = button;
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
        translateView();

        InputStream is = getClass().getResourceAsStream("/com/example/farminggame/input/rockInput.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String rockInput;


        nameDisplay.setText(getFarmerName());
        levelDisplay.setText("Level: " + farmer.getLevel());
        balanceDisplay.setText("Balance: " + wallet.getObjectCoins());
        exitBtn.setOnAction(event -> exitGame());


        // Check the rockInput.txt file for positions of rocks
        this.lot = new FarmLot();

        while ((rockInput = br.readLine()) != null) {
            lot.setRockPosition(Integer.parseInt(rockInput));
            // change
        }

        // Create the farm lot of 50 tiles (5 rows, 10 columns)
        GridPane newFarm = farmLotGrid;
        int tileIdCount = 1;

        // For loop is per col
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

                // Add new tile to its corresponding row and column position (column, row)
                newFarm.add(newTile, column, row);

                // Add the new tile to the arraylist of all tiles (for the controller to change the visual attributes)
                tileBtnList.add(newTile);

                tileIdCount++;
            }
        }

        // Initialize seed store list
        seedStoreList.add(new Apple());
        seedStoreList.add(new Carrot());
        seedStoreList.add(new Mango());
        seedStoreList.add(new Potato());
        seedStoreList.add(new Rose());
        seedStoreList.add(new Sunflower());
        seedStoreList.add(new Tulip());
        seedStoreList.add(new Turnip());
    }

    // GETTERS AND SETTERS

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
        this.wallet = farmer.getWallet();
        this.seedPouch = farmer.getSeedPouch();
    }

    public String getFarmerName() {
        return farmer.getName();
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
