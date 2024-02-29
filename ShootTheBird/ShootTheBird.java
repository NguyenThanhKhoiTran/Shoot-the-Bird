import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Modality;

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
 * @date Feb 29, 2024
 * @version proj_v04 
 ************************************************************************/
public class ShootTheBird extends Application {
    // Instance data
    public static String name = "";
    public static int vBucks = 0;
    public static int number = 0;
    public static int temp = 0;
    public static int click = 0;
    
    // Instantiate ArrayList to remove the number that appeared before
    ArrayList <Integer> removeSameNum = new ArrayList <> ();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*********************************************************************
         * FIRST PANE: OPENING
         *********************************************************************/
        // Instantiate a StackPane
        StackPane root = new StackPane ();
         
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
                try
                {
                    name = playerName.getText();
                    if (name.isEmpty())
                    {
                        throw new NoNameInputException ("Wait !!!! Do you have any name? If does, please type it into this beautiful field, PLEASE!");
                    }
                    else
                    {
                        primaryStage.setScene (playingGame(primaryStage));
                    }
                }
                catch (NoNameInputException nnie)
                {
                    showNameAlert(" >>> ERROR <<<", nnie.getMessage());
                }
            }
        });

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
    private Scene playingGame(Stage primaryStage) 
    {
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
        roll.setOnAction(new EventHandler <ActionEvent> ()
        {
            @Override
            public void handle (ActionEvent event)
            {
                try
                {
                    generateRandomNumber (square);
                }
                catch (OverEightElementsArrayList oeeal)
                {
                    showNoBirdsAlert(primaryStage, ">>> ERROR <<<", oeeal.getMessage());
                }
            }
        });
        
        // Add function for LEAVE button
        leave.setOnAction(new EventHandler <ActionEvent> ()
        {
            @Override
            public void handle (ActionEvent event)
            {
                primaryStage.setScene (rewardScene(primaryStage));
            }
        });
        
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

        // Show the stage
        pane2 = new Scene (bp, 1000, 800);
        primaryStage.show();
        primaryStage.setScene(pane2);
        
        return pane2;
    }
    
    // Method to create playing game scene
    private Scene rewardScene(Stage primaryStage) 
    {
        /*************************************************************************
         * THIRD PANE: REWARDING 
         *************************************************************************/
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
        ending.setBackground (bground);
        
        // Create labels and specify Black color for player name
        Label playerNameLabel = createLabel("Player's Name: " + name, Color.BLACK);  
        
        // Red color for the blinking reward text
        Label rewardLabel = createBlinkingLabel("Reward in the Game ==> " + vBucks, Color.RED);  

        // Create reset button
        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        resetButton.setFont(Font.font("Arial", 30));  // Increase the font size
        
        resetButton.setOnAction(new EventHandler <ActionEvent> () 
        {
            @Override
            public void handle (ActionEvent event)
            {
                name = "";
                vBucks = 0;
                number = 0;
                temp = 0;
                removeSameNum.clear();
                primaryStage.close();
                
                // Create a new Stage
                Stage newPrimaryStage = new Stage ();
                try 
                {
                    start (newPrimaryStage);
                }
                catch (Exception e)
                {
                    // Handle exceptions approriately
                    e.printStackTrace();
                }
            }
        });

        // Add labels and button to VBox
        ending.getChildren().addAll(playerNameLabel, rewardLabel, resetButton);
        
        // Show the stage
        primaryStage.show();
        
        return new Scene(ending, 1000, 800);
    }
    
    /**********************************************************************************
     * Method for generate random number from 0 to 8
     **********************************************************************************/
    private void generateRandomNumber (TextArea ta) throws OverEightElementsArrayList
    {   
        Random rd = new Random ();
        
        if (removeSameNum.size() >= 8)
        {
            throw new OverEightElementsArrayList ("Ohhhh.... There are no birds now, PLEASE click \" LEAVE \" button below to receive reward");
        }
        else
        {
            // If any number in ArrayList is the same as number generated recently, do again
            do
            {
                number = rd.nextInt (8) + 1;
            } while (removeSameNum.contains(number));
            
            removeSameNum.add(number);
            ta.setText ("NUMBER" + "\n>> " + number + " <<");
        }
    }
    
    /************************************************************************************************************
     * Make a method for exception if player does not input anything, display the message to force user inputting
     ************************************************************************************************************/
    private void showNameAlert(String title, String message) 
    {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setTitle(title);
        alertStage.setResizable (false);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("OK, I understand");
        closeButton.setOnAction(event -> alertStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment (Pos.CENTER);

        Scene scene = new Scene(layout, 550, 100);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }
    
    /**********************************************************************************************************
     * Make a method for exception if the same num is over 8 times, output the message that MUST LEAVE THE GAME
     **********************************************************************************************************/
    private void showNoBirdsAlert(Stage primaryStage, String title, String message) 
    {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setTitle(title);
        alertStage.setResizable (false);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("LEAVE");
        closeButton.setOnAction(new EventHandler <ActionEvent> ()
        {
            @Override
            public void handle (ActionEvent event)
            {
                alertStage.close();
                primaryStage.setScene (rewardScene(primaryStage));
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment (Pos.CENTER);

        Scene scene = new Scene(layout, 550, 100);
        alertStage.setScene(scene);
        alertStage.showAndWait();
    }
    
    /**********************************************************
     * Make a method to create a label with some default fonts
     **********************************************************/
    private Label createLabel(String text, Color textColor) 
    {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 30));
        label.setTextFill(textColor);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    /******************************************************
     * Make a method to create a blinking effect for text
     ******************************************************/
    private Label createBlinkingLabel(String text, Color textColor) 
    {
        Label label = createLabel(text, textColor);

        // Create a blinking effect using Timeline
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> 
            {
                if (label.getTextFill() == textColor) 
                    {
                        label.setTextFill(Color.TRANSPARENT);
                    } 
                    else 
                    {
                        label.setTextFill(textColor);
                    }
            }));
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();

        return label;
    }
}
