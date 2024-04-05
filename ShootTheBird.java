import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.Random;
import java.util.ArrayList;
import java.io.*;

/************************************************************************
 * Use JavaFX to create a streaming game that meets some particular
 * requirements
 * 
 * @author Nguyen Thanh Khoi Tran
 * @author Sagar Kaithavayalil Jaison
 * @date March 21st, 2024
 * @version proj_v10
 ************************************************************************/
public class ShootTheBird extends Application {
    // Instance data
    private MediaPlayer rollSound;
    private MediaPlayer rewardSound;
    public static String name = "";
    public static int vBucks = 0;
    public static int number = 0;
    public static int temp = 0;
    public static int VBtemp = 0;
    public static int ranNum;
    public static int shieldUsing = 0;
    public static int addVB = 0;

    // For bird
    private String[] fileNames;
    private ArrayList<Image> images;

    // For VBucks
    private String[] fileNames2;
    private ArrayList<Image> images2;

    // Instantiate ArrayList to remove the number that appeared before (in bird)
    ArrayList<Integer> removeSameNum = new ArrayList<>();

    // Instantiate ArrayList to remove the number that appeared before (in VBucks)
    ArrayList<Integer> sameNum = new ArrayList<>();

    // Instantiate ArrayList for ImageView
    ArrayList<ImageView> iv = new ArrayList<>();

    // Instantiate ArrayList for ImageView (VBucks)
    ArrayList<ImageView> iv2 = new ArrayList<>();

    @Override
    public void init() throws Exception {
        // Specify the path to the folder containing images
        String folderPath = "Bird+Num";
        String folderPath2 = "VBucks";

        // Create a File object representing the folder
        File folder = new File(folderPath);
        File folder2 = new File(folderPath2);

        // Instatiate an ArrayList to store Image objects
        images = new ArrayList<>();
        images2 = new ArrayList<>();

        // Check if the specified path exists and is a directory
        // Get the list of files in the folder
        File[] files = folder.listFiles();
        fileNames = new String[files.length];

        File[] files2 = folder2.listFiles();
        fileNames2 = new String[files2.length];

        // Load the opening sound file
        String openingSoundFilePath = "openingsound.MP3";
        Media openingMedia = new Media(new File(openingSoundFilePath).toURI().toString());
        MediaPlayer openingSound = new MediaPlayer(openingMedia);

        // Play the opening sound
        openingSound.play();

        // Iterate over the files and store their names in the array
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }

        // Iterate over the files and store their names in the array
        for (int i = 0; i < files2.length; i++) {
            fileNames2[i] = files2[i].getName();
        }

        // Load the images found in the folder
        for (File file : files) {
            if (file.isFile() && isImageFile(file.getName())) {
                Image image = new Image(file.toURI().toString());
                images.add(image);
            }
        }

        // Load the images found in the folder
        for (File file2 : files2) {
            if (file2.isFile() && isImageFile(file2.getName())) {
                Image image2 = new Image(file2.toURI().toString());
                images2.add(image2);
            }
        }

        // Load sound file
        String soundFilePath = "diceroll.WAV";
        Media rollMedia = new Media(new File(soundFilePath).toURI().toString());
        rollSound = new MediaPlayer(rollMedia);

        String rewardSoundFilePath = "rewardsound.MP3";
        Media rewardMedia = new Media(new File(rewardSoundFilePath).toURI().toString());
        rewardSound = new MediaPlayer(rewardMedia);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*********************************************************************
         * FIRST PANE: OPENING
         *********************************************************************/
        // Instantiate a StackPane
        StackPane root = new StackPane();

        Label title = new Label("SHOOT THE BIRD");
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-text-fill: red; -fx-font-size: 50px; -fx-font-weight: bold;");

        // Create a timeline for blinking effect
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> title.setVisible(true)),
                new KeyFrame(Duration.seconds(1), e -> title.setVisible(false)));
        tl.setCycleCount(tl.INDEFINITE);
        tl.play();

        // Create VBox with background image
        Image bg = new Image("BACKGROUND.jpg");
        BackgroundSize bgSize = new BackgroundSize(1000, 800, true, true, true, true);
        BackgroundImage background = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, bgSize);
        Background bgForVBox = new Background(background);

        VBox opening = new VBox();
        opening.setSpacing(10);
        opening.setPadding(new Insets(10));
        opening.setAlignment(Pos.CENTER);
        opening.setBackground(bgForVBox);

        // Create label for instruction
        Label instructionLabel = new Label("Enter Player's Name");

        // Set text color to black and font size to 20px
        instructionLabel.setStyle("-fx-text-fill: black; -fx-font-size: 40px; -fx-font-weight: bold;");

        // Create TextField for player names
        TextField playerName = new TextField();
        playerName.setPrefWidth(200);
        playerName.setPrefHeight(50);

        // Clear the prompt text
        playerName.setPromptText("");
        playerName.setStyle("-fx-text-fill: black; -fx-font-size: 30px; -fx-font-weight: bold;");

        // Create Start button and place it in an HBox
        Button startButton = new Button("Start");

        // Set preferred size
        startButton.setPrefSize(200, 40);

        // Set button color to green and text to bold
        startButton.setStyle(
                "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 30px;");

        // Add components to VBox
        opening.getChildren().addAll(title, instructionLabel, playerName, startButton);

        // Put the name into provided variable when the button is clicked
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    name = playerName.getText();
                    if (name.isEmpty()) {
                        throw new NoNameInputException(
                                "Wait !!!! Do you have any name? If does, please type it into this beautiful field, PLEASE!");
                    } else {
                        primaryStage.setScene(playingGame(primaryStage));
                    }
                } catch (NoNameInputException nnie) {
                    playErrorSound();
                    showNameAlert(" >>> ERROR <<<", nnie.getMessage());
                }
            }
        });

        root.getChildren().add(opening);

        // Avoid resizing the window
        primaryStage.setResizable(false);

        // Create and set scene
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("SHOOT THE BIRD");
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    // Method to create playing game scene
    private Scene playingGame(Stage primaryStage) {
        /*************************************************************************
         * SECOND PANE: PLAYING GAME
         *************************************************************************/
        // Instantiate BorderPane and Scene
        BorderPane bp = new BorderPane();
        Scene pane2;

        // Set the background
        Image playingBG = new Image("playingScene.png");
        BackgroundSize bgSize2 = new BackgroundSize(1000, 800, true, true, true, true);
        BackgroundImage background2 = new BackgroundImage(playingBG, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, bgSize2);
        Background bg2 = new Background(background2);
        bp.setBackground(bg2);

        // Put a player's name at the top of BorderPane
        Label topName = new Label("Player's name: " + name);
        topName.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");
        bp.setAlignment(topName, Pos.CENTER);
        bp.setTop(topName);

        // Using GridPane to make a grid for 8 birds position
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10));
        gp.setHgap(90);
        gp.setVgap(90);

        // VBucks picture after bird disappears
        for (int i = 0; i < 16; i++) {
            Image vbucks = images2.get(i);
            ImageView tempVB2 = new ImageView(vbucks);
            tempVB2.setFitWidth(115);
            tempVB2.setFitHeight(115);
            iv2.add(tempVB2);
        }

        /****************************************
         * Put 8 birds in 8 position
         ****************************************/

        for (int c = 2; c < 5; c++) {
            for (int r = 0; r < 3; r++) {
                Image bird = images.get(temp);
                ImageView temp2 = new ImageView(bird);
                iv.add(temp2);

                temp2.setFitWidth(115);
                temp2.setFitHeight(115);

                // Add it into GridPane
                gp.add(temp2, c, r);
                temp++;
            }
        }

        // Create a "LEAVE" button and "ROLL" button
        Button leave = new Button("LEAVE");
        Button roll = new Button("ROLL");
        leave.setStyle(
                "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");
        roll.setStyle(
                "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");

        // Create a rectangle and label to contain a random number
        TextArea square = new TextArea();
        square.setPrefWidth(120);
        square.setPrefHeight(115);
        square.setText("NUMBER" + "\n>> " + number + " <<");
        square.setStyle(
                "-fx-control-inner-background: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-alignment: center;");
        square.setEditable(false);

        gp.add(square, 3, 1);

        // Instantiate HBox
        HBox hb = new HBox(100);

        // Create TextField for showing VBucks
        TextField showVBucks = new TextField();
        showVBucks.setPrefWidth(200);
        showVBucks.setPrefHeight(50);

        // Set text and avoid editing for textfield
        showVBucks.setText(vBucks + "VBucks");
        showVBucks.setStyle(
                "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");
        showVBucks.setEditable(false);

        /***********************************************************
         * Create a button to activate special ability (avoid shield)
         * 
         * This ability just use ONCE only in the game, help player
         * avoid being deducted from VB when deducting picture is
         * reveal; and there is nothing happen if getting picture
         * is shown
         ***********************************************************/
        Button shield = new Button("SHIELD");
        shield.setStyle(
                "-fx-background-color: purple; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");
        // Add functionality for "SHIELD" button
        shield.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                shield.setVisible(false);
                shieldUsing++;
            }
        });

        // Add all components into declared HBox
        hb.getChildren().addAll(leave, roll, shield, showVBucks);
        hb.setAlignment(Pos.CENTER);
        bp.setBottom(hb);

        // Add function for ROLL button
        roll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    generateRandomNumber(square);

                    // Play the roll sound
                    rollSound.stop();
                    rollSound.play();

                    // Using switch-case to remove a bird that has the same number of random
                    // generator given
                    switch (number) {
                        case 1:
                            gp.getChildren().remove(iv.get(0));
                            gp.add(calcVBuck(iv2), 2, 0);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 2:
                            gp.getChildren().remove(iv.get(1));
                            gp.add(calcVBuck(iv2), 2, 1);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 3:
                            gp.getChildren().remove(iv.get(2));
                            gp.add(calcVBuck(iv2), 2, 2);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 4:
                            gp.getChildren().remove(iv.get(3));
                            gp.add(calcVBuck(iv2), 3, 0);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 5:
                            gp.getChildren().remove(iv.get(5));
                            gp.add(calcVBuck(iv2), 3, 2);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 6:
                            gp.getChildren().remove(iv.get(6));
                            gp.add(calcVBuck(iv2), 4, 0);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 7:
                            gp.getChildren().remove(iv.get(7));
                            gp.add(calcVBuck(iv2), 4, 1);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        case 8:
                            gp.getChildren().remove(iv.get(8));
                            gp.add(calcVBuck(iv2), 4, 2);
                            showVBucks.setText(vBucks + "VBucks");
                            break;
                        default:
                            throw new OverEightElementsArrayList();
                    }
                } catch (OverEightElementsArrayList oeeal) {
                    showNoBirdsAlert(primaryStage, ">>> ERROR <<<", oeeal.getMessage());
                }
            }
        });

        // Add function for LEAVE button
        leave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Stop previous plays
                rewardSound.stop();
                rewardSound.play();
                primaryStage.setScene(rewardScene(primaryStage));
            }
        });

        // Set GridPane at the center of BorderPane
        bp.setCenter(gp);

        // Show the stage
        pane2 = new Scene(bp, 1000, 800);
        primaryStage.show();
        primaryStage.setScene(pane2);

        return pane2;
    }

    // Method to create playing game scene
    private Scene rewardScene(Stage primaryStage) {
        /*************************************************************************
         * THIRD PANE: REWARDING
         *************************************************************************/
        // Create HBox to set 2 button next to each other
        HBox hb = new HBox(30);
        hb.setAlignment(Pos.CENTER);

        // Create VBox and align at the center
        VBox ending = new VBox();
        ending.setPadding(new Insets(20));
        ending.setSpacing(20);
        ending.setAlignment(Pos.CENTER);

        // Set background image
        Image bg = new Image("BACKGROUND.jpg");
        BackgroundSize bgSize = new BackgroundSize(1000, 800, true, true, true, true);
        BackgroundImage background = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, bgSize);
        Background bground = new Background(background);
        ending.setBackground(bground);

        // Create labels and specify Black color for player name
        Label playerNameLabel = createLabel("Player's Name: " + name, Color.BLACK);

        // Red color for the blinking reward text
        if (vBucks < 0) {
            vBucks = 0;
        }
        Label rewardLabel = createBlinkingLabel("Reward in the Game ==> " + vBucks, Color.RED);

        // Create reset button
        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        resetButton.setFont(Font.font("Arial", 30));

        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                name = "";
                vBucks = 0;
                number = 0;
                temp = 0;
                VBtemp = 0;
                shieldUsing = 0;
                addVB = 0;
                removeSameNum.clear();
                primaryStage.close();

                // Create a new Stage
                Stage newPrimaryStage = new Stage();
                try {
                    start(newPrimaryStage);
                } catch (Exception e) {
                    // Handle exceptions approriately
                    e.printStackTrace();
                }
            }
        });

        // Create close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        closeButton.setFont(Font.font("Arial", 30)); // Increase the font size
        closeButton.setOnAction(e -> primaryStage.close());

        // Add button to HBox
        hb.getChildren().addAll(closeButton, resetButton);

        // Add labels and button to VBox
        ending.getChildren().addAll(playerNameLabel, rewardLabel, hb);

        // Show the stage
        primaryStage.show();

        return new Scene(ending, 1000, 800);
    }

    /**********************************************************************************
     * Method for generate random number from 0 to 8
     **********************************************************************************/
    private void generateRandomNumber(TextArea ta) throws OverEightElementsArrayList {
        Random rd = new Random();

        if (removeSameNum.size() >= 8) {
            throw new OverEightElementsArrayList();
        } else {
            // If any number in ArrayList is the same as number generated recently, do again
            do {
                number = rd.nextInt(8) + 1;
            } while (removeSameNum.contains(number));

            removeSameNum.add(number);
            ta.setText("NUMBER" + "\n>> " + number + " <<");
        }
    }

    /************************************************************************************************************
     * Make a method for exception if player does not input anything, display the
     * message to force user inputting
     ************************************************************************************************************/
    private void showNameAlert(String title, String message) {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setTitle(title);
        alertStage.setResizable(false);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("OK, I understand");
        closeButton.setOnAction(event -> alertStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 550, 100);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }

    /**********************************************************************************************************
     * Make a method for exception if the same num is over 8 times, output the
     * message that MUST LEAVE THE GAME
     **********************************************************************************************************/
    private void showNoBirdsAlert(Stage primaryStage, String title, String message) {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setTitle(title);
        alertStage.setResizable(false);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("LEAVE");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Stop previous plays
                rewardSound.stop();
                rewardSound.play();

                alertStage.close();
                primaryStage.setScene(rewardScene(primaryStage));
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 550, 100);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }

    /**********************************************************
     * Make a method to create a label with some default fonts
     **********************************************************/
    private Label createLabel(String text, Color textColor) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 30));
        label.setTextFill(textColor);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    /******************************************************
     * Make a method to create a blinking effect for text
     ******************************************************/
    private Label createBlinkingLabel(String text, Color textColor) {
        Label label = createLabel(text, textColor);

        // Create a blinking effect using Timeline
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            if (label.getTextFill() == textColor) {
                label.setTextFill(Color.TRANSPARENT);
            } else {
                label.setTextFill(textColor);
            }
        }));
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();

        return label;
    }

    /******************************************************
     * Make a method to identify the ending of file name
     ******************************************************/
    private boolean isImageFile(String fileName) {
        return fileName.toLowerCase().endsWith(".png") ||
                fileName.toLowerCase().endsWith(".jpg") ||
                fileName.toLowerCase().endsWith(".jpeg") ||
                fileName.toLowerCase().endsWith(".gif");
    }

    /******************************************************
     * Method to play the error sound
     *******************************************************/

    private void playErrorSound() {
        // Load error sound file
        String errorSoundFilePath = "errorsound.MP3";
        Media errorMedia = new Media(new File(errorSoundFilePath).toURI().toString());
        MediaPlayer errorSound = new MediaPlayer(errorMedia);
        errorSound.play();
    }

    // Method to play the level up sound after the roll sound finishes
    private void playLevelUpSound() {
        // Load level up sound file
        String levelUpSoundFilePath = "levelup.WAV";
        Media levelUpMedia = new Media(new File(levelUpSoundFilePath).toURI().toString());
        MediaPlayer levelUpSound = new MediaPlayer(levelUpMedia);

        // Add event handler to play level up sound after roll sound finishes
        rollSound.setOnEndOfMedia(() -> {
            levelUpSound.play();
        });

        // Play the roll sound
        rollSound.play();
    }

    // Method to play the level down sound after the roll sound finishes
    private void playLevelDownSound() {
        // Load level down sound file
        String levelDownSoundFilePath = "leveldown.WAV";
        Media levelDownMedia = new Media(new File(levelDownSoundFilePath).toURI().toString());
        MediaPlayer levelDownSound = new MediaPlayer(levelDownMedia);

        // Add event handler to play level down sound after roll sound finishes
        rollSound.setOnEndOfMedia(() -> {
            levelDownSound.play();
        });

        // Play the roll sound
        rollSound.play();
    }

    /******************************************************
     * Method to random add a VBucks picture
     ******************************************************/
    private ImageView calcVBuck(ArrayList<ImageView> vb) {
        Random rand = new Random();

        // Ensure that every number is distinct
        do {
            ranNum = rand.nextInt(16);
        } while (sameNum.contains(ranNum));
        sameNum.add(ranNum);

        final int generateNum = ranNum;
        // Add into VBuck prize
        switch (generateNum) {
            case 0:
                addVB = 50;
                vBucks += addVB;
                break;
            case 1:
                addVB = 50;
                vBucks += addVB;
                break;
            case 2:
                addVB = 50;
                vBucks += addVB;
                break;
            case 3:
                addVB = 100;
                vBucks += addVB;
                break;
            case 4:
                addVB = 100;
                vBucks += addVB;
                break;
            case 5:
                addVB = 100;
                vBucks += addVB;
                break;
            case 6:
                addVB = 200;
                vBucks += addVB;
                break;
            case 7:
                addVB = 500;
                vBucks += addVB;
                break;
            case 8:
                addVB = 500;
                vBucks += addVB;
                break;
            case 9:
                addVB = 800;
                vBucks += addVB;
                break;
            case 10:
                addVB = -100;
                vBucks += addVB;
                break;
            case 11:
                addVB = -100;
                vBucks += addVB;
                break;
            case 12:
                addVB = -500;
                vBucks += addVB;
                break;
            case 13:
                addVB = -500;
                vBucks += addVB;
                break;
            case 14:
                addVB = -500;
                vBucks += addVB;
                break;
            case 15:
                addVB = -800;
                vBucks += addVB;
                break;
            default:
                break;
        }

        // Play sound when get or deduct from VBuck
        if (addVB > 0) {
            playLevelUpSound();
        } else {
            playLevelDownSound();
        }

        // If shield ability is activated, not deduct money
        if (shieldUsing == 1 && addVB < 0) {
            shieldUsing = 0;
            vBucks += Math.abs(addVB);
        } else if (shieldUsing == 1 && addVB > 0) {
            shieldUsing = 0;
        }

        return vb.get(generateNum);
    }
}
