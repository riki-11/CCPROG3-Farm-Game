package com.example.farminggame.controllers;

import com.example.farminggame.models.environment.crops.*;
import com.example.farminggame.models.tools.*;
import com.example.farminggame.models.farmer.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


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

    // FXML VARIABLES
    @FXML private Text nameDisplay;

    @FXML private Text levelDisplay;
    @FXML private Text balanceDisplay;

    @FXML private GridPane farmLot;

    @FXML private Button plantTurnipBtn;

    @FXML private Button resetBtn;
    @FXML private Button exitBtn;



    @FXML
    private SceneController sceneController;

    // MODEL VARIABLES
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

    // Initializes the view upon switching to it
    @FXML
    private void initialize() {
        nameDisplay.setText(getFarmerName());
        levelDisplay.setText("Level: " + farmer.getLevel());
        balanceDisplay.setText("Balance: " + wallet.getObjectCoins());
        //plantTurnipBtn.setOnAction(event -> plantTurnip());
        resetBtn.setOnAction(event -> resetTile());
        exitBtn.setOnAction(event -> exitGame());

        // Create the farm lot of 50 tiles (5 rows, 10 columns)
        GridPane newFarm = farmLot;
        int tileIdCount = 1;

        // For loop is per col
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 10; column++) {
                Button newTile = new Button();
                // Set the respective id of a tile (tile 1, tile 2, etc.)
                newTile.setId("tile" + tileIdCount);
                newTile.getStyleClass().add("default-tile");
                newTile.getStyleClass().add("img-button");

                // Set the corresponding actions for a button
                newTile.setOnAction(event -> {
                    try {
                        plantTurnip(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                // Add new tile to its corresponding row and column position (column, row)
                newFarm.add(newTile, column, row);

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

    @FXML protected void plantTurnip(ActionEvent event) throws IOException {
        String turnipURL = ASSETS_URL + "/crops/new-turnip.png";
        Button tileBtn = (Button) event.getSource();
        tileBtn.setStyle("-fx-background-image: url("+turnipURL+")"); // probably best to use setStyleClass instead
    }

    /*
    @FXML
    protected void plantTurnip() {
        // Use the getResource method if grabbing images from resources folder
        String newTurnipURL = ASSETS_URL + "/crops/new-turnip.png";
        plantTurnipBtn.setStyle("-fx-background-image: url("+newTurnipURL+");");
    }
     */

    @FXML
    protected void resetTile() {
        String defaultTileURL = ASSETS_URL + "/farm/default-tile.png";
        plantTurnipBtn.setStyle("-fx-background-image: url("+defaultTileURL+");");
    }

    @FXML
    protected void exitGame() {
        sceneController.switchToStartView();
    }

    @FXML TextField cropNumber;

    @FXML
    protected void buyCrop(ActionEvent event) throws IOException {
        // Grab the object that triggered the action (THIS WILL BE USEFUL FOR THE FARM AS WELL)
        Object node = event.getSource();

        int cropNum = Integer.parseInt(cropNumber.getText());
        System.out.println(this.selectedButton);

        // STILL HAVE TO GRAB HOW MANY SEEDS WE'RE BUYING

        // Pass what crop to buy to the farmer
        switch (this.selectedButton) {
            case "Apple" -> farmer.buySeeds(new Apple(), cropNum);
            case "Carrot" -> farmer.buySeeds(new Carrot(), cropNum);
            case "Mango" -> farmer.buySeeds(new Mango(), cropNum);
            case "Potato" -> farmer.buySeeds(new Potato(), cropNum);
            case "Rose" -> farmer.buySeeds(new Rose(), cropNum);
            case "Sunflower" -> farmer.buySeeds(new Sunflower(), cropNum);
            case "Tulip" -> farmer.buySeeds(new Tulip(), cropNum);
            case "Turnip" -> farmer.buySeeds(new Turnip(), cropNum);
        }

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
        /*Image carrotImage = new Image(getClass().getResourceAsStream("com/example/farminggame/assets/crops/carrot.jpg"));
        Image appleImage = new Image(getClass().getResourceAsStream("com/example/farminggame/assets/crops/apple.jpg"));
        Image mangoImage = new Image(getClass().getResourceAsStream("com/example/farminggame/assets/crops/mango.jpg"));
        Image potatoImage = new Image(getClass().getResourceAsStream("com/example/farminggame/assets/crops/potato.jpg"));

        Image turnipImage = new Image(getClass().getResourceAsStream("com/example/farminggame/assets/crops/turnip.jpg"));
*/
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
            //cropDescImage.setImage(carrotImage);
            cropNameDesc.setText(carrot.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + carrot.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + carrot.getWaterNeeds() + "(" + carrot.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + carrot.getFertilizerNeeds() + "(" + carrot.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + carrot.getProduce());
            basePriceDesc.setText("Base Selling Price: " + carrot.getSellingPrice());
        }
        else if (button.getId().equals("marketAppleBtn")) {
            setSelectedButton("Apple");
            //cropDescImage.setImage(appleImage);
            cropNameDesc.setText(apple.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + apple.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + apple.getWaterNeeds() + "(" + apple.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + apple.getFertilizerNeeds() + "(" + apple.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + apple.getProduce());
            basePriceDesc.setText("Base Selling Price: " + apple.getSellingPrice());
        }
        else if (button.getId().equals("marketMangoBtn")) {
            setSelectedButton("Mango");
            //cropDescImage.setImage(mangoImage);
            cropNameDesc.setText(mango.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + mango.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + mango.getWaterNeeds() + "(" + mango.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + mango.getFertilizerNeeds() + "(" + mango.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + mango.getProduce());
            basePriceDesc.setText("Base Selling Price: " + mango.getSellingPrice());
        }
        else if (button.getId().equals("marketPotatoBtn")) {
            setSelectedButton("Potato");
            //cropDescImage.setImage(potatoImage);
            cropNameDesc.setText(potato.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + potato.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + potato.getWaterNeeds() + "(" + potato.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + potato.getFertilizerNeeds() + "(" + potato.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + potato.getProduce());
            basePriceDesc.setText("Base Selling Price: " + potato.getSellingPrice());
        }
        else if (button.getId().equals("marketRoseBtn")) {
            setSelectedButton("Rose");
            //harvestTimeDesc.setText("Harvest Time: " + rose.getHarvestTime());
            cropNameDesc.setText(rose.getSeedName());
            waterNeedsDesc.setText("Water Needs (Bonus): " + rose.getWaterNeeds() + "(" + rose.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + rose.getFertilizerNeeds() + "(" + rose.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + rose.getProduce());
            basePriceDesc.setText("Base Selling Price: " + rose.getSellingPrice());
        }
        else if (button.getId().equals("marketSunflowerBtn")) {
            setSelectedButton("Sunflower");

            cropNameDesc.setText(sunflower.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + sunflower.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + sunflower.getWaterNeeds() + "(" + sunflower.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + sunflower.getFertilizerNeeds() + "(" + sunflower.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + sunflower.getProduce());
            basePriceDesc.setText("Base Selling Price: " + sunflower.getSellingPrice());
        }
        else if (button.getId().equals("marketTulipBtn")) {
            setSelectedButton("Tulip");

            cropNameDesc.setText(tulip.getSeedName());
            harvestTimeDesc.setText("Harvest Time: " + tulip.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + tulip.getWaterNeeds() + "(" + tulip.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + tulip.getFertilizerNeeds() + "(" + tulip.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + tulip.getProduce());
            basePriceDesc.setText("Base Selling Price: " + tulip.getSellingPrice());
        }
        else if (button.getId().equals("marketTurnipBtn")) {
            setSelectedButton("Turnip");

            cropNameDesc.setText(turnip.getSeedName());
            //cropDescImage.setImage(turnipImage);
            harvestTimeDesc.setText("Harvest Time: " + turnip.getHarvestTime());
            waterNeedsDesc.setText("Water Needs (Bonus): " + turnip.getWaterNeeds() + "(" + turnip.getWaterBonusLimit() + ")");
            fertilizerNeedsDesc.setText("Fertilizer Needs (Bonus): " + turnip.getFertilizerNeeds() + "(" + turnip.getFertilizerBonusLimit() + ")");
            productsProducedDesc.setText("Products Produced: " + turnip.getProduce());
            basePriceDesc.setText("Base Selling Price: " + turnip.getSellingPrice());
        }

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
