package com.example.farminggame.controllers;

import com.example.farminggame.models.farmer.Farmer;
import com.example.farminggame.models.farmer.SeedPouch;
import com.example.farminggame.models.farmer.Wallet;
import com.example.farminggame.models.tools.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;


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

    @FXML private Button plantTurnipBtn;

    @FXML private Button resetBtn;
    @FXML private Button exitBtn;


    @FXML
    private SceneController sceneController;

    // CONTROLLER VARIABLES
    private Plough plough = new Plough();
    private Fertilizer fertilizer = new Fertilizer();
    private Pickaxe pickaxe = new Pickaxe();
    private Shovel shovel = new Shovel();
    private WateringCan wateringCan = new WateringCan();

    private Farmer farmer;
    private Wallet wallet;
    private SeedPouch seedPouch;

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

    @FXML
    private void initialize() {
        nameDisplay.setText(getFarmerName());
        levelDisplay.setText(Integer.toString(farmer.getLevel()));
        levelDisplay.setStyle("color: red;");
        balanceDisplay.setText(Double.toString(wallet.getObjectCoins()));
        plantTurnipBtn.setOnAction(event -> plantTurnip());
        resetBtn.setOnAction(event -> resetTile());
        exitBtn.setOnAction(event -> exitGame());
    }

    @FXML
    protected void plantTurnip() {
        // Use the getResource method if grabbing images from resources folder
        String newTurnipURL = ASSETS_URL + "/crops/new-turnip.png";
        plantTurnipBtn.setStyle("-fx-background-image: url("+newTurnipURL+");");
        System.out.println(plantTurnipBtn.getStyle());
    }

    @FXML
    protected void resetTile() {
        String defaultTileURL = ASSETS_URL + "/farm/default-tile.png";
        plantTurnipBtn.setStyle("-fx-background-image: url("+defaultTileURL+");");
    }

    @FXML
    protected void exitGame() {
        sceneController.switchToStartView();
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
