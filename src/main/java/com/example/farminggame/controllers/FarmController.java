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
import javafx.scene.layout.GridPane;
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

    @FXML
    protected void buyCrop(ActionEvent event) throws IOException {
        // Grab the object that triggered the action (THIS WILL BE USEFUL FOR THE FARM AS WELL)
        Object node = event.getSource();

        // Since it'll be a Button, we can typecast node to a Button type
        Button button = (Button) node;
        System.out.println("BUYING " + button.getText());
        String seedName = button.getText();

        // STILL HAVE TO GRAB HOW MANY SEEDS WE'RE BUYING

        // Pass what crop to buy to the farmer
        switch (seedName) {
            case "Apple" -> farmer.buySeeds(new Apple(), 1);
            case "Carrot" -> farmer.buySeeds(new Carrot(), 1);
            case "Mango" -> farmer.buySeeds(new Mango(), 1);
            case "Potato" -> farmer.buySeeds(new Potato(), 1);
            case "Rose" -> farmer.buySeeds(new Rose(), 1);
            case "Sunflower" -> farmer.buySeeds(new Sunflower(), 1);
            case "Tulip" -> farmer.buySeeds(new Tulip(), 1);
            case "Turnip" -> farmer.buySeeds(new Turnip(), 1);
        }

        seedPouch.showSeedList();
        balanceDisplay.setText("Balance: " + wallet.getObjectCoins());
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
