package model;

import gui.MainMenu;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class GameOverScreen {
    private final Group gameOverLayout = new Group();
    private final Button playAgainButton = new Button("Rejouer");
    private final Button exitButton = new Button("Exit");
    private final Button homeButton = new Button("Home");
    boolean win;

    public GameOverScreen(ButtonAction playAgainAction, ButtonAction exitAction, Stage primaryStage, boolean win) {
        this.win = win;
        playAgainButton.setOnAction(e -> playAgainAction.performAction());
        exitButton.setOnAction(e -> exitAction.performAction());
        homeButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu(primaryStage);
            primaryStage.setScene(mainMenu.getScene());
            primaryStage.show();
        });
        String img;
        if(win){
            img="win.png";
        }
        else{
            img="game_over.png";
        }
        
        var image = new ImageView(new Image(/* ../ressources/*/img, 500, 500, true, true));
        // Positionnez les boutons et l'image à l'emplacement souhaité
        playAgainButton.setLayoutX(200);
        playAgainButton.setLayoutY(500);
        exitButton.setLayoutX(300);
        exitButton.setLayoutY(500);
        homeButton.setLayoutX(400);
        homeButton.setLayoutY(500);
        image.setLayoutX(50);
        image.setLayoutY(50);

        

        // Ajoutez les boutons et l'image au conteneur
        gameOverLayout.getChildren().addAll(playAgainButton, exitButton, homeButton, image);
    }

    Node getGameOverLayout(){return gameOverLayout;}
}