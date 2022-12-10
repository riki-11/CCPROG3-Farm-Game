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
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class FarmController {

    // STAGE variables
    private Stage stage;
    private StartController startController;

    private final int SCENE_WIDTH = 1350;
    private final int SCENE_HEIGHT = 850;

    // RESOURCE/PATH variables
    private final String ASSETS_URL = FarmController.class.getResource("/com/example/farminggame" +
            "/assets").toExternalForm();
    private final String FXML_URL = "/com/example/farminggame/fxml/";

    // GAME-WIDE VARIABLES

    // In-game day counter
    private int days = 1;

    // List of possible seeds you can buy
    private ArrayList<Crop> seedStoreList = new ArrayList<>();

    // Stores all the FXML Buttons for the farm tiles
    private ArrayList<Button> tileBtnList = new ArrayList<>();

    // Stores the tile that was last clicked by the user
    private int activeTileIndex = -1;

    // Stores the name of the last selected crop in the market
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
    @FXML private SceneController sceneController;
    @FXML private Button exitBtn;
    @FXML private Button nextDayBtn;
    @FXML private BorderPane gameOverScreen;
    @FXML private Text gameOverText;

    // Seed Market FXML Variables
    @FXML private TextField cropNumber;
    @FXML private Button buyBtnMarket;

    // NOTIFICATION FXML Variables
    @FXML private Text successText;
    @FXML private Text failedText;
    @FXML private Text misinputText;

    // Profile FXML Variables
    @FXML private Text nameDisplay;

    @FXML private Text levelDisplay;
    @FXML private Text xpDisplay;
    @FXML private Text balanceDisplay;
    @FXML private Text dayDisplay;
    @FXML private Button upgradeFarmerBtn;
    @FXML private Text farmerRankPopUp;

    // Farm Lot FXML Variables
    @FXML private GridPane farmLotGrid;
    @FXML private Text farmTitle;

    // Tool Button FXML Variables
    @FXML private FlowPane toolButtons;
    @FXML private Button ploughBtn;
    @FXML private Button shovelBtn;
    @FXML private Button shovelCropBtn; // to be displayed on tiles with crops
    @FXML private Button pickaxeBtn;
    @FXML private Button fertilizerBtn;
    @FXML private Button wateringCanBtn;
    @FXML private Button harvestBtn;
    @FXML private Text shovelNotif;

    // Crop Button FXML Variables
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

    // Tile Information FXML Variables
    @FXML private ImageView tileDescImage;
    @FXML private TextField tileDescLabel;
    @FXML private Text tileDescText;
    @FXML private VBox tileDescriptionBox;

    // Profile Pop Up
    @FXML private StackPane profilePane;
    @FXML private Button profileBtn;
    @FXML private GridPane marketPane;
    @FXML private Button openMarketBtn;
    @FXML private Rectangle marketRectangle;
    @FXML private Rectangle descriptionRectangle;
    @FXML private ImageView cropDescription;
    @FXML private Button exitMarket;
    @FXML private ImageView farmerProfileDesc;

    // Creates the stage for the Farm Game view
    public void createStage(Stage stage) {
        this.stage = stage;
         try {
             // Designate farmView.fxml file as root node
             FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_URL + "farmView.fxml"));

             // Set this class as the controller
             fxmlLoader.setController(this);

             Parent root = fxmlLoader.load();

             // Set the new scene as the grabbed .fxml file
             Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
             this.stage.setScene(scene);

             // Set the screen to full screen
             this.stage.setFullScreen(true);

         } catch(Exception e) {
             e.printStackTrace();
         }
    }

    // Sets all the necessary events when view is first initialized
    @FXML
    private void initialize() throws IOException {
        InputStream is = getClass().getResourceAsStream("/com/example/farminggame/input/rockInput.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String rockInput;
        int tileIdCount = 1;

        // Creates a new farm lot
        this.lot = new FarmLot();

        // Reads input from rockInput.txt to place 10 rocks in specific tiles
        while ((rockInput = br.readLine()) != null) {
            lot.setRockPosition(Integer.parseInt(rockInput));
        }

        // Creates the FXML farm lot (5 rows x 10 columns)
        GridPane newFarm = farmLotGrid;

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 10; column++) {

                // Each farm tile is a button with an id (e.x. tile1, tile2, tile3, etc.)
                Button newTile = new Button();
                newTile.setId("tile" + tileIdCount);

                // Check the FarmLot model if this tile should have a rock or not
                if (lot.getTile(tileIdCount - 1).hasRock()) {
                    newTile.getStyleClass().add("rock-tile");
                } else {
                    newTile.getStyleClass().add("unplowed-tile");
                }

                newTile.getStyleClass().add("farm-tile");
                newTile.getStyleClass().add("imgBtn");

                // Attach the display() event handler to each button
                newTile.setOnAction(event -> {
                    try {
                        display(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                // Add new tile to its corresponding position
                newFarm.add(newTile, column, row);

                // Add the new tile to the arraylist of all tiles (for the controller to change the visual attributes)
                tileBtnList.add(newTile);

                tileIdCount++;
            }
        }

        // Initialize other GUI elements
        nameDisplay.setText(farmer.getName());
        farmTitle.setText(farmer.getName() + "'s Farm");
        updateDay();
        updateStats();

        // Initialize seed store list with each crop
        seedStoreList.add(new Apple());
        seedStoreList.add(new Carrot());
        seedStoreList.add(new Mango());
        seedStoreList.add(new Potato());
        seedStoreList.add(new Rose());
        seedStoreList.add(new Sunflower());
        seedStoreList.add(new Tulip());
        seedStoreList.add(new Turnip());

        // Set an event handler for the exit button
        exitBtn.setOnAction(event -> exitGame());
    }

    // Show the stage
    public void showStage() {
        this.stage.show();
    }

    @FXML
    protected void display(ActionEvent event) throws IOException {
        // Grabs the button that triggered this event
        Object node = event.getSource();
        Button tileBtn = (Button) node;

        // Stores the index of the tile that was last clicked
        setActiveTileIndex(tileBtn);

        // Display the corresponding tool/crop buttons and tile information depending on the tile's status
        displayButtons();
        displayData();
    }

    private void displayData() {
        final String ENVIRONMENT_ASSETS = "/com/example/farminggame/assets/environment/";

        Image rockImage = new Image(getClass().getResourceAsStream(ENVIRONMENT_ASSETS + "rock.png"));
        Image unplowedImage = new Image(getClass().getResourceAsStream(ENVIRONMENT_ASSETS + "default-tile.png"));
        Image plowedImage = new Image(getClass().getResourceAsStream(ENVIRONMENT_ASSETS + "plowed-tile.png"));
        Image witheredImage = new Image(getClass().getResourceAsStream(ENVIRONMENT_ASSETS + "withered-crop.png"));
        Image seedImage = new Image(getClass().getResourceAsStream(ENVIRONMENT_ASSETS + "planted-tile.png"));

        Tile activeTile = lot.getTile(this.activeTileIndex);

        // Display information about the crop based on its status

        if (activeTile.hasRock()) {
            // If tile has a rock on it
            tileDescImage.setImage(rockImage);
            tileDescLabel.setText("ROCKS");
            tileDescText.setText("Use a pickaxe to get\nrid of the rocks");
        } else if (!(activeTile.isPlowed())) {
            // If tile is not plowed
            tileDescImage.setImage(unplowedImage);
            tileDescLabel.setText("UNPLOWED TILE");
            tileDescText.setText("Plow tile to plant crops");
        } else if (!(activeTile.hasCrop())) {
            // If tile is plowed but doesn't have a crop
            tileDescImage.setImage(plowedImage);
            tileDescLabel.setText("PLOWED TILE");
            tileDescText.setText("You can now plant\na crop on this tile");
        } else {
            // If tile has a crop
            if (activeTile.hasHarvestableCrop()) {
                // If crop is harvestable
                setCropData(activeTile);
                tileDescText.setText(activeTile.getCropInfo());
            } else if (activeTile.hasWitheredCrop()) {
                // If crop is withered
                tileDescImage.setImage(witheredImage);
                tileDescLabel.setText("WITHERED CROP");
                tileDescText.setText("Oh, no! Your crop died.");
            } else {
                // If crop is still growing
                tileDescImage.setImage(seedImage);
                tileDescLabel.setText(activeTile.getCrop().getCropName());
                tileDescText.setText(activeTile.getCropInfo());
            }
        }

        // Show the tile information in the view
        tileDescriptionBox.setVisible(true);
    }

    // Displays the corresponding crop data based on what crop is currently planted on the tile
    private void setCropData(Tile activeTile) {
        // Path to assets folder containing all the crop images
        final String CROP_ASSETS = "/com/example/farminggame/assets/environment/";

        // Name of the seed currently planted in the active tile
        String seedName = activeTile.getCrop().getCropName();

        Image appleImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-apple.png"));
        Image carrotImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-carrot.png"));
        Image mangoImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-mango.png"));
        Image potatoImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-potato.png"));
        Image roseImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-rose.png"));
        Image sunflowerImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-sunflower.png"));
        Image tulipImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-tulip.png"));
        Image turnipImage = new Image(getClass().getResourceAsStream(CROP_ASSETS + "planted-turnip.png"));

        switch (seedName) {
            case "Apple" -> {
                tileDescImage.setImage(appleImage);
                tileDescLabel.setText("APPLE");
            }
            case "Carrot" -> {
                tileDescImage.setImage(carrotImage);
                tileDescLabel.setText("CARROT");
            }
            case "Mango" -> {
                tileDescImage.setImage(mangoImage);
                tileDescLabel.setText("MANGO");
            }
            case "Potato" -> {
                tileDescImage.setImage(potatoImage);
                tileDescLabel.setText("POTATO");
            }
            case "Rose" -> {
                tileDescImage.setImage(roseImage);
                tileDescLabel.setText("ROSE");
            }
            case "Sunflower" -> {
                tileDescImage.setImage(sunflowerImage);
                tileDescLabel.setText("SUNFLOWER");
            }
            case "Turnip" -> {
                tileDescImage.setImage(turnipImage);
                tileDescLabel.setText("TURNIP");
            }
            case "Tulip" -> {
                tileDescImage.setImage(tulipImage);
                tileDescLabel.setText("TULIP");
            }
        }
    }

    private void displayButtons(){

        Tile activeTile = lot.getLot().get(this.activeTileIndex);

        // set the tools to visible first
        toolButtons.setVisible(true);
        toolButtons.setDisable(false);
        cropButtons.setDisable(false);
        cropButtons.setVisible(false);

        // Check the status of the last clicked tile
        if (activeTile.hasRock()) {
            // If tile has a rock
            shovelBtn.setDisable(false);
            ploughBtn.setDisable(true);
            pickaxeBtn.setDisable(false);
            fertilizerBtn.setDisable(true);
            wateringCanBtn.setDisable(true);
            harvestBtn.setDisable(true);
        } else if (!(activeTile.isPlowed())) {
            // If tile is unplowed
            shovelBtn.setDisable(false);
            ploughBtn.setDisable(false);
            pickaxeBtn.setDisable(true);
            fertilizerBtn.setDisable(true);
            wateringCanBtn.setDisable(true);
            harvestBtn.setDisable(true);
        } else if (!(activeTile.hasCrop())) {
            // If tile is plowed but doesn't have a crop, show Crops instead of Tools (except the Shovel)
            toolButtons.setVisible(false);
            showCropBtns();

        } else {
            // If tile has a crop

            pickaxeBtn.setDisable(true);
            ploughBtn.setDisable(true);
            shovelBtn.setDisable(false);

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
                // growing crop
                fertilizerBtn.setDisable(false);
                wateringCanBtn.setDisable(false);
                harvestBtn.setDisable(true);
            }
        }

        // Disable any tools that the user cannot afford to use
        if (wallet.getObjectCoins() < shovel.getCost()) {
            shovelBtn.setDisable(true);
            shovelCropBtn.setDisable(true);
        }
        if (wallet.getObjectCoins() < pickaxe.getCost()) {
            pickaxeBtn.setDisable(true);
        }
        if (wallet.getObjectCoins() < fertilizer.getCost()) {
            fertilizerBtn.setDisable(true);
        }
        if (wallet.getObjectCoins() < wateringCan.getCost()) {
            wateringCanBtn.setDisable(true);
        }
    }

    @FXML
    protected void showCropBtns() {
        // show all crop buttons
        cropButtons.setVisible(true);

        // check seed inventory for each seed count
        Hashtable<String, Integer> inventory = seedPouch.getSeedList();

        int apples = inventory.get("Apple");
        int carrots = inventory.get("Carrot");
        int mangoes = inventory.get("Mango");
        int potatoes = inventory.get("Potato");
        int roses = inventory.get("Rose");
        int sunflowers = inventory.get("Sunflower");
        int tulips = inventory.get("Tulip");
        int turnips = inventory.get("Turnip");

        // Show user how many of each seed they own
        appleCount.setText(Integer.toString(apples));
        carrotCount.setText(Integer.toString(carrots));
        mangoCount.setText(Integer.toString(mangoes));
        potatoCount.setText(Integer.toString(potatoes));
        roseCount.setText(Integer.toString(roses));
        sunflowerCount.setText(Integer.toString(sunflowers));
        tulipCount.setText(Integer.toString(tulips));
        turnipCount.setText(Integer.toString(turnips));

        // If the farmer hasn't bought at least one of any seed, disable that particular seed in the view.
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

        // Disable the shovel if the farmer doesn't have enough money to use it
        if (wallet.getObjectCoins() < shovel.getCost()) {
            shovelCropBtn.setDisable(true);
        } else {
            shovelCropBtn.setDisable(false);
        }
    }

    // Use a tool based on the tool button that was clicked
    @FXML
    protected void useTool(ActionEvent event) {
        Tile activeTile = lot.getTile(activeTileIndex);
        Button activeTileBtn = tileBtnList.get(activeTileIndex);

        // Grab the button that triggered the event
        Object node = event.getSource();
        Button tool = (Button) node;

        String toolID = tool.getId();

        // Farmer uses the tool and the tile changes appearance according to the tool used
        switch (toolID) {
            case "ploughBtn" -> {
                farmer.usePlough(activeTile, plough);
                setBtnImage(activeTileBtn, "environment/plowed-tile.png");
            }
            case "pickaxeBtn" -> {
                farmer.usePickaxe(activeTile, pickaxe);
                setBtnImage(activeTileBtn, "environment/default-tile.png");
            }
            case "shovelBtn", "shovelCropBtn" -> {
                if (activeTile.hasCrop()) {
                    // The shovel works only on tiles with crops
                    setBtnImage(activeTileBtn, "environment/default-tile.png");
                } else {
                    // Notify user that they can't use the shovel on anything other than a crop
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    shovelNotif.setText("You're not supposed to use that on that tile");

                    shovelNotif.setVisible(true);
                    pause.setOnFinished(f -> shovelNotif.setVisible(false));
                    pause.play();
                }

                farmer.useShovel(activeTile, shovel);
            }
            case "fertilizerBtn" -> farmer.useFertilizer(activeTile, fertilizer);
            case "wateringCanBtn" -> farmer.useWateringCan(activeTile, wateringCan);
            case "harvestBtn" -> {
                farmer.harvestCrop(activeTile);
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                shovelNotif.setText("Crop was successfully harvested");
                shovelNotif.setVisible(true);
                pause.setOnFinished(
                        f -> shovelNotif.setVisible(false));
                pause.play();
                setBtnImage(activeTileBtn, "environment/default-tile.png");
            }
        }

        updateStats();
        toolButtons.setDisable(true);
        toolButtons.setVisible(false);
        cropButtons.setVisible(false);
        cropButtons.setDisable(true);
        tileDescriptionBox.setVisible(false);
    }

    // Returns true if a tile has any crops or rocks adjacent to it (up, down, left, right, diagonals), false otherwise
    private boolean hasAdjacentElements() {
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

    // Returns true if a tile is not located on one of the outer edges of the farm lot
    private boolean inRange() {
        // Check if tile's index is not one of the outer edge positions
        if ((activeTileIndex >= 11 && activeTileIndex <= 18) ||
                (activeTileIndex >= 21 && activeTileIndex <= 28) ||
                (activeTileIndex >= 31 && activeTileIndex <= 38)) {
            return true;
        } else {
            return false;
        }
    }

    // Buys a seed from the seed market
    @FXML
    protected void buyCrop() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        boolean seedBought = false;

        // Grab input of how many seeds user wants to buy (ensure valid input)
        try {
            int cropNum = Integer.parseInt(cropNumber.getText());

            // If user bought at least one seed
            if (cropNum >= 0) {
                // Pass seed to be bought to the farmer, seedBought represents whether it was a successful transaction or not
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
            } else {
                misinputText.setTranslateX(-150);
                misinputText.setVisible(true);
                pause.setOnFinished(
                        f -> misinputText.setVisible(false));
                pause.play();
            }

        } catch (NumberFormatException n) {
            // If user doesn't enter an integer
            misinputText.setTranslateX(-150);
            misinputText.setVisible(true);
            pause.setOnFinished(
                    f -> misinputText.setVisible(false));
            pause.play();
        }

        cropNumber.setText("");
        updateStats();
    }

    // Plants a crop on a tile
    @FXML
    protected void plantCrop(ActionEvent event) {
        Tile activeTile = lot.getTile(activeTileIndex);
        Button activeTileBtn = tileBtnList.get(activeTileIndex);
        Object node = event.getSource();

        boolean canPlant = true;
        Button seed = (Button) node;

        String seedId = seed.getId();

        switch (seedId) {
            case "carrotBtn":
                farmer.plantSeed(activeTile, "Carrot");
                // change appearance of tile
                setBtnImage(activeTileBtn, "environment/planted-carrot.png");
                break;
            case "roseBtn":
                farmer.plantSeed(activeTile, "Rose");
                setBtnImage(activeTileBtn, "environment/planted-rose.png");
                break;
            case "sunflowerBtn":
                farmer.plantSeed(activeTile, "Sunflower");
                setBtnImage(activeTileBtn, "environment/planted-sunflower.png");
                break;
            case "tulipBtn":
                farmer.plantSeed(activeTile, "Tulip");
                setBtnImage(activeTileBtn, "environment/planted-tulip.png");
                break;
            case "potatoBtn":
                farmer.plantSeed(activeTile, "Potato");
                setBtnImage(activeTileBtn, "environment/planted-potato.png");
                break;
            case "turnipBtn":
                farmer.plantSeed(activeTile, "Turnip");
                setBtnImage(activeTileBtn, "environment/planted-turnip.png");
                break;
            default:
                if (inRange() && !hasAdjacentElements()) {
                    if (seedId.equals("appleBtn")) {
                        farmer.plantSeed(activeTile, "Apple");
                        setBtnImage(activeTileBtn, "environment/planted-apple.png");
                    } else if (seedId.equals("mangoBtn")) {
                        farmer.plantSeed(activeTile, "Mango");
                        setBtnImage(activeTileBtn, "environment/planted-mango.png");
                    }
                } else {
                    // If selected crop can't be planted on the tile
                    canPlant = false;
                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    shovelNotif.setText("Fruit trees cannot be planted on edge tiles or tiles with adjacent crops or rocks");
                    shovelNotif.setVisible(true);
                    pause.setOnFinished(
                            f -> shovelNotif.setVisible(false));
                    pause.play();
                }
                break;

        }

        // Set the image of the tile to show a seed
        if (canPlant) {
            setBtnImage(activeTileBtn, "environment/planted-tile.png");
        }

        updateStats();
        tileDescriptionBox.setVisible(false);
        cropButtons.setDisable(true);
        cropButtons.setVisible(false);
    }

    // Opens the profile pane
    @FXML
    protected void openProfile(){
        farmerUpgradeButton();
        setProfilePane();
        profilePane.setVisible(true);
        disableButtons();
    }

    // Enables/disables the upgrade button based on whether farmer can upgrade or not
    private void farmerUpgradeButton() {
        if (farmer.getCanUpgrade() != -1) {
            upgradeFarmerBtn.setDisable(false);
        } else {
            upgradeFarmerBtn.setDisable(true);
        }
    }

    // Sets the data to be displayed on the profile pane
    private void setProfilePane() {
        final String PROFILE_ASSETS = "/com/example/farminggame/assets/profile/";

        Image farmerImage = new Image(getClass().getResourceAsStream(PROFILE_ASSETS + "farmer.png"));
        Image registeredFarmerImage = new Image(getClass().getResourceAsStream(PROFILE_ASSETS + "registered.png"));
        Image distinguishedFarmerImage = new Image(getClass().getResourceAsStream(PROFILE_ASSETS + "distinguished.png"));
        Image legendaryFarmerImage = new Image(getClass().getResourceAsStream(PROFILE_ASSETS + "legendary.png"));

        String farmerType = farmer.getFarmerType();

        switch (farmerType) {
            case "Farmer" -> farmerProfileDesc.setImage(farmerImage);
            case "Registered Farmer" -> farmerProfileDesc.setImage(registeredFarmerImage);
            case "Distinguished Farmer" -> farmerProfileDesc.setImage(distinguishedFarmerImage);
            case "Legendary Farmer" -> farmerProfileDesc.setImage(legendaryFarmerImage);
        }
    }

    // Exits the profile pane
    @FXML
    protected void exitProfile() {
        profilePane.setVisible(false);
        enableButtons();
    }

    // Upgrades the farmer to the next level
    @FXML
    protected void upgradeFarmer() {
        farmer.upgradeFarmer();
        exitProfile();
    }

    // Updates the stats of the farmer based on their farmer type
    @FXML
    protected void updateStats() {
        levelDisplay.setText("Level: " + farmer.getLevel());
        xpDisplay.setText("XP: " + farmer.getCurrentXP());
        DecimalFormat format = new DecimalFormat("#.##");
        balanceDisplay.setText("Balance: " + format.format(wallet.getObjectCoins()));

        // check game over conditions
        gameOver();
    }

    // Opens the market pane
    @FXML
    protected void openMarket(){
        marketPane.setVisible(true);
        buyBtnMarket.setDisable(true);
        cropNumber.setDisable(true);
        cropNumber.setText("");
        disableButtons();
    }

    // Exits the market pane
    @FXML
    protected void exitMarket() {
        descriptionRectangle.setVisible(false);
        marketPane.setVisible(false);
        marketRectangle.setWidth(500);
        cropDescription.setVisible(false);
        exitMarket.setTranslateX(0);
        enableButtons();

    }

    // Displays information of whichever seed the user selects
    @FXML
    protected void displayMarketInformation(ActionEvent event) throws IOException {

        final String MARKET_DISPLAY_ASSETS = "/com/example/farminggame/assets/market/displays/";

        // Switch image in the market description
        Image carrotImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "carrot-desc.png"));
        Image appleImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "apple-desc.png"));
        Image mangoImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "mango-desc.png"));
        Image potatoImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "potato-desc.png"));
        Image roseImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "rose-desc.png"));
        Image sunflowerImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "sunflower-desc.png"));
        Image tulipImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "tulip-desc.png"));
        Image turnipImage = new Image(getClass().getResourceAsStream(MARKET_DISPLAY_ASSETS + "turnip-desc.png"));

        expandMarket();

        Object node = event.getSource();
        Button button = (Button) node;
        String buttonId = button.getId();

        // Shows seed information corresponding to the button pressed
        switch (buttonId) {
            case "marketCarrotBtn" -> {
                setSelectedButton("Carrot");
                cropDescription.setImage(carrotImage);
            }
            case "marketAppleBtn" -> {
                setSelectedButton("Apple");
                cropDescription.setImage(appleImage);
            }
            case "marketMangoBtn" -> {
                setSelectedButton("Mango");
                cropDescription.setImage(mangoImage);
            }
            case "marketPotatoBtn" -> {
                setSelectedButton("Potato");
                cropDescription.setImage(potatoImage);
            }
            case "marketRoseBtn" -> {
                setSelectedButton("Rose");
                cropDescription.setImage(roseImage);
            }
            case "marketSunflowerBtn" -> {
                setSelectedButton("Sunflower");
                cropDescription.setImage(sunflowerImage);
            }
            case "marketTulipBtn" -> {
                setSelectedButton("Tulip");
                cropDescription.setImage(tulipImage);
            }
            case "marketTurnipBtn" -> {
                setSelectedButton("Turnip");
                cropDescription.setImage(turnipImage);
            }
        }
    }

    // Expands the market pane to show seed information
    private void expandMarket() {
        marketRectangle.setWidth(800);
        descriptionRectangle.setTranslateX(500);

        buyBtnMarket.setDisable(false);
        cropNumber.setDisable(false);

        cropDescription.setTranslateX(250);
        cropDescription.setVisible(true);
        descriptionRectangle.setVisible(true);
        exitMarket.setTranslateX(300);
        exitMarket.toFront();
    }


    // Move to the next in-game day and update any necessary game elements
    @FXML
    protected void nextDay() {
        this.days++;

        // Grab all the crops currently planted
        ArrayList<Crop> cropList = lot.getCropsPlanted();

        // Advance the days planted for each crop
        for (Crop crop : cropList) {
            crop.addDayPlanted();
        }

        updateDay();
    }

    // Updates the Farm Game view and the tiles with harvestable crops or withered crops
    @FXML
    protected void updateDay() {
        dayDisplay.setText("Day: " + days);

        // After each day, check if there are any withered crops
        updateWitheredCrops();
        updateHarvestableCrops();

        // Hide any tile-related buttons or panes
        cropButtons.setVisible(false);
        tileDescriptionBox.setVisible(false);

        // Check game over conditions
        gameOver();
    }

    // Checks all tiles and updates their appearance accordingly if its crop is withered
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

    // Checks all tiles and updates their appearance accordingly if its crop is harvestable
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

    // Sets the background image for an FXML button by passing the button and the imagePath as arguments
    private void setBtnImage(Button button, String imagePath) {
        button.setStyle(String.format("-fx-background-image: url(\"%s\");", ASSETS_URL + imagePath));
    }

    // Enables the different menu buttons
    private void enableButtons() {
        exitBtn.setDisable(false);
        profileBtn.setDisable(false);
        openMarketBtn.setDisable(false);
        nextDayBtn.setDisable(false);
    }

    // Disables the different menu buttons
    private void disableButtons() {
        exitBtn.setDisable(true);
        profileBtn.setDisable(true);
        openMarketBtn.setDisable(true);
        nextDayBtn.setDisable(true);
        toolButtons.setDisable(true);
        cropButtons.setDisable(true);
    }

    // Checks whether the player has lost the game or not and returns the reason why if they did
    private String checkGameOver() {
        boolean foundNotWithered = false;
        boolean hasSeeds = false;
        boolean hasCoins = false;
        boolean foundActiveCrop = false;

        ArrayList<String> seedList = new ArrayList<>(List.of("Apple", "Carrot", "Mango", "Potato", "Rose",
                                                             "Sunflower", "Tulip", "Turnip"));

        // Check if there are any tiles WITHOUT withered crops remaining
        for (int i = 0; i < lot.getLot().size(); i++) {
            // Check if there are any tiles WITHOUT withered crops remaining
            if (!lot.getTile(i).hasWitheredCrop()) {
                foundNotWithered = true;
            }
            // Check if a tile still has active crops
            if (lot.getTile(i).hasCrop() && !(lot.getTile(i).getCrop().isWithered())) {
                foundActiveCrop = true;
            }
        }

        // Check if farmer's seed pouch is empty.
        for (String seed : seedList) {
            if (seedPouch.getSeedCount(seed) > 0) {
                hasSeeds = true;
            }
        }

        // If farmer still has enough money to at least buy some seeds
        if (!hasSeeds) {
            if (farmer.getWallet().getObjectCoins() >= 5) {
                hasCoins = true;
            }
        }

        // If farmer has no seeds, can't buy more, and if there are no more growing crops
        if ((!hasSeeds && !hasCoins) && !foundActiveCrop) {
            return "\n\nUh oh! You don't have enough coins to do anything!";
        } else if (!foundNotWithered) {
            return "\n\nOh no! All your tiles have dead crops!";
        }

        return "";
    }

    // Display the game over screen
    private void gameOver() {
        String gameOverReason = checkGameOver();
        if (!gameOverReason.equals("")) {
            disableButtons();
            gameOverScreen.setVisible(true);
            gameOverText.setText(gameOverReason);
        }
    }

    // Exit to the start menu
    @FXML
    protected void exitGame() {
        sceneController.switchToStartView();
    }

    // Exit the program altogether
    @FXML
    protected void exitProgram() {
        stage.close();
    }

    // Stores the last clicked button (in the market pane)
    private void setSelectedButton(String button) {
        this.selectedButton = button;
    }

    // Stores the last clicked tile button
    private void setActiveTileIndex(Button tileBtn) {

        if (tileBtn.getId().length() == 5) {
            // If tile has a single digit index, grab the fifth character of the id (e.x. '1' in tile1)
            this.activeTileIndex = Integer.parseInt(tileBtn.getId().substring(4)) - 1;
        } else if (tileBtn.getId().length() == 6) {
            // If tile has a double-digit index, grab the fifth and sixth characters of the id (e.x. '12' in tile12)
            this.activeTileIndex = Integer.parseInt(tileBtn.getId().substring(4, 6)) - 1;
        }
    }

    // Set the farmer to be used by FarmController
    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
        this.wallet = farmer.getWallet();
        this.seedPouch = farmer.getSeedPouch();
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
