package com.example.farminggame.controllers;

import com.example.farminggame.models.farmer.Farmer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;

import java.io.IOException;

public class SceneController {
    private Stage stage;
    private static Scene scene;
    private static Parent root;
    private static final int SCENE_WIDTH;
    private static final int SCENE_HEIGHT;
    private static String fxmlURL;
    private static String assetsURL;
    private static String gameTitle;
    private static Image gameIcon;

    @FXML
    private FarmController farmController;

    private Model model = new Model();

    static {
        SCENE_WIDTH = 1750;
        SCENE_HEIGHT = 1500;
        // Save location of assets folder
        fxmlURL = "/com/example/farminggame/fxml/";
        assetsURL = SceneController.class.getResource("/com/example/farminggame/assets/").toString();
        gameTitle = "Farming Game";
    }

    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void startGameView() {
        StartController startController = new StartController();
        startController.setModel(model);
        startController.setSceneController(this);
        startController.createStage(this.stage);
        startController.showStage();
    }

    public void switchToStartView() {
        // Grab the .fxml file that represents the view
        StartController startController = new StartController();
        startController.setModel(model);
        startController.setSceneController(this);
        startController.createStage(this.stage);
        startController.showStage();
    }


    public void switchToFarmView(Farmer farmer) {
        FarmController farmController = new FarmController();
        farmController.setFarmer(farmer);
        farmController.setSceneController(this);
        farmController.createStage(this.stage);
        farmController.showStage();
        // CREATE AND SHOW STAGE LAST, BECAUSE INITIALIZE CREATES THE FXML FILE WHEN THE SCENE IS LOADED, THUS U CAN'T UPDATE VARIABLES ANYMORE
    }
}
