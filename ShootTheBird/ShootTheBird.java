import javafx.application.Application;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*; // * as I need to use some StackPane, BorderPane, etc.
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.Random;
import java.util.ArrayList;

/************************************************************************
 * Use JavaFX to create a streaming game that meets some particular 
 * requirements 
 * 
 * @author Nguyen Thanh Khoi Tran
 * @author Sagar Kaithavayalil Jaison
 * @date Feb 27, 2024
 * @version proj_v02 
 ************************************************************************/
public class ShootTheBird extends Application {
    // Instance data
    public static String name = "";
    public static int vBucks = 0;
    public static int number = 0;
    public static int temp = 0;
    
    // Instantiate ArrayList to remove the number that appeared before
    ArrayList <Integer> removeSameNum = new ArrayList <> ();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Instantiate a StackPane
        StackPane root = new StackPane ();
        
        /*********************************************************************
         * FIRST PANE: OPENING
         *********************************************************************/
        Label title = new Label("SHOOT THE BIRD");
        title.setAlignment (Pos.CENTER);
        title.setStyle("-fx-text-fill: red; -fx-font-size: 50px; -fx-font-weight: bold;"); 
        
        // Create a timeline for blinking effect
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> title.setVisible(true)),
                new KeyFrame(Duration.seconds(1), e -> title.setVisible(false))
        );
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
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 30px;"); 
        
        // Add components to VBox
        opening.getChildren().addAll(title, instructionLabel, playerName, startButton);

        // Put the name into provided variable when the button is clicked
        startButton.setOnAction(new EventHandler <ActionEvent> ()
        {
            @Override
            public void handle (ActionEvent event)
            {
                name = playerName.getText();
                primaryStage.setScene (playingGame());
            }
        });

        /*************************************************************************
         * THIRD PANE: REWARDING 
         *************************************************************************/
        
        root.getChildren().add (opening);
        
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
    private Scene playingGame() {
        /*************************************************************************
         * SECOND PANE: PLAYING GAME
         *************************************************************************/
        // Instantiate BorderPane
        BorderPane bp = new BorderPane();
        
        // Set the background
        Image playingBG = new Image("playingScene.png");
        BackgroundSize bgSize2 = new BackgroundSize(1000, 800, true, true, true, true);
        BackgroundImage background2 = new BackgroundImage(playingBG, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, bgSize2);
        Background bg2 = new Background(background2);
        bp.setBackground(bg2);
        
        // Put a player's name at the top of BorderPane
        Label topName = new Label ("Player's name: " + name);
        topName.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");
        bp.setAlignment (topName, Pos.CENTER);
        bp.setTop (topName);
        
        // Using GridPane to make a grid for 8 birds position
        GridPane gp = new GridPane ();
        gp.setPadding(new Insets(10));
        gp.setHgap(90); 
        gp.setVgap(90); 
        
        // Put 8 birds in 8 position
        for (int c = 2; c < 5; c++)
        {
            for (int r = 0; r < 3; r++)
            {
                Image bird = new Image ("Bird.png");
                ImageView iv = new ImageView (bird);
                iv.setFitWidth(115); 
                iv.setFitHeight(115);
                
                gp.add(iv, c, r);
                temp++;
            }
        }
        
        // Create a "LEAVE" button and "ROLL" button
        Button leave = new Button ("LEAVE");
        Button roll = new Button ("ROLL");
        leave.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");
        roll.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");
        
        // Create a rectangle and label to contain a random number
        TextArea square = new TextArea();
        square.setPrefWidth(120);
        square.setPrefHeight(115);
        square.setText("NUMBER" + "\n>> " + number + " <<");
        square.setStyle("-fx-control-inner-background: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px; -fx-alignment: center;"); 
        square.setEditable (false);
         
        gp.add(square, 3, 1);
    
        // Add function for ROLL button
        roll.setOnAction (e -> generateRandomNumber (square));
        
        // Instantiate HBox
        HBox hb = new HBox (100);
        
        // Create TextField for showing VBucks
        TextField showVBucks = new TextField();
        showVBucks.setPrefWidth(200);
        showVBucks.setPrefHeight(50);
        
        // Set text and avoid editing for textfield
        showVBucks.setText(vBucks + "VBucks"); 
        showVBucks.setStyle("-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 20px; -fx-border-color: white; -fx-border-width: 5px;");        
        showVBucks.setEditable (false);
        
        hb.getChildren().addAll (leave, roll, showVBucks);
        hb.setAlignment (Pos.CENTER);
        bp.setBottom (hb);
                
        // Set GridPane at the center of BorderPane
        bp.setCenter (gp);
        
        return new Scene(bp, 1000, 800);
    }
    
    // Method for generate random number from 0 to 8
    private void generateRandomNumber (TextArea ta)
    {   
        Random rd = new Random ();
    
        // If any number in ArrayList is the same as number generated recently, do again
        do
        {
            number = rd.nextInt (8) + 1;
        } while (removeSameNum.contains(number));
        
        removeSameNum.add(number);
        ta.setText ("NUMBER" + "\n>> " + number + " <<");
    }
    
    /******************************************************************************************************
     * Make an exception if the same num is over 8 times, output the message that MUST LEAVE THE GAME
     * Make an exception if player does not input anything, display the message to force user inputting 
     ******************************************************************************************************/
}
