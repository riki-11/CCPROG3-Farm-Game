package com.example.farminggame.controllers;

import com.example.farminggame.models.farmer.Farmer;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;

    // Instantiates a SceneController with the same stage from StartController
    public SceneController(Stage stage) {
        this.stage = stage;
    }

    // Opens the Start Game view
    public void switchToStartView() {
        StartController startController = new StartController();
        startController.setSceneController(this);
        startController.createStage(this.stage);
        startController.showStage();
    }

    // Opens the Farm Game view
    public void switchToFarmView(Farmer farmer) {
        FarmController farmController = new FarmController();
        farmController.setFarmer(farmer);
        farmController.setSceneController(this);
        farmController.createStage(this.stage);
        farmController.showStage();
    }
}
