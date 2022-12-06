package com.example.farminggame.controllers;

import com.example.farminggame.models.farmer.Farmer;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {

    private Stage stage;

    private Model model;

    private SceneController sceneController;

    private final String fxmlURL = "/com/example/farminggame/fxml/";

    @FXML
    private TextField nameField; // this links to the fx:id attribute of a .fxml element

    @FXML
    private Button startButton;

    private final int SCENE_WIDTH = 1350;
    private final int SCENE_HEIGHT = 850;

    // Model Variables
    private Farmer farmer = new Farmer();


    public void createStage(Stage stage) {
        this.stage = stage;
        try {

            // Designate .fxml file as root node

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlURL + "startView.fxml"));
            fxmlLoader.setController(this);

            Parent root = fxmlLoader.load();

            // Set the scene
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

            this.stage.setTitle("Farming Game");
            this.stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        this.stage.show();
    }

    @FXML
    protected void startGame() throws IOException {
        System.out.println("starting game...");
        if (!nameField.getText().equals("")) {
            storeFarmerName(getEnteredText());
        }
        sceneController.switchToFarmView(this.farmer);
    }

    public String getEnteredText() {
        return nameField.getText();
    }

    public void storeFarmerName(String name) {
        this.farmer.setName(name);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

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
}