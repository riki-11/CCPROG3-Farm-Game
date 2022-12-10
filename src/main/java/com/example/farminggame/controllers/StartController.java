package com.example.farminggame.controllers;

import com.example.farminggame.models.farmer.Farmer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class StartController {

    private Stage stage;
    private SceneController sceneController;
    private final String FXML_URL = "/com/example/farminggame/fxml/";
    private final int SCENE_WIDTH = 1350;
    private final int SCENE_HEIGHT = 850;
    private Farmer farmer = new Farmer();

    // Field where user enters their Farmer Name
    @FXML private TextField nameField;

    @FXML private Button startButton;

    // Creates the stage for the Start Game view
    public void createStage(Stage stage) {
        this.stage = stage;
        try {
            // Designate startView.fxml file as root node
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_URL + "startView.fxml"));

            // Set this class as the controller
            fxmlLoader.setController(this);

            Parent root = fxmlLoader.load();

            // Set the scene and stage title
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
            this.stage.setScene(scene);
            this.stage.setTitle("Ed's Farming Game");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show the stage
    public void showStage() {
        this.stage.show();
    }

    // Sets all the necessary events when view is first initialized
    @FXML
    private void initialize() {
        // Add an action for the "Open Layout2" button
        startButton.setOnAction(event -> {
            try {
                startGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Starts the game by saving the Farmer name and switches to the Farm Game view
    @FXML
    protected void startGame() throws IOException {
        if (!nameField.getText().equals("")) {
            storeFarmerName(getEnteredText());
        }
        sceneController.switchToFarmView(this.farmer);
    }

    // Grabs the Farmer name inputted by the user
    public String getEnteredText() {
        return nameField.getText();
    }

    // Stores the Farmer name inputted by the user into the Farmer model
    public void storeFarmerName(String name) {
        this.farmer.setName(name);
    }

    // Stores an instance of a SceneController to switch between views
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
