package model;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class GameOverScreen {
    private final Group gameOverLayout = new Group();
    private final Button playAgainButton = new Button("Rejouer");
    private final Button exitButton = new Button("Exit");

    public GameOverScreen(ButtonAction playAgainAction, ButtonAction exitAction) {
        playAgainButton.setOnAction(e -> playAgainAction.performAction());
        exitButton.setOnAction(e -> exitAction.performAction());
        var image = new ImageView(new Image(/* ../ressources/*/"game_over.png", 500, 500, true, true));

        // Positionnez les boutons et l'image à l'emplacement souhaité
        playAgainButton.setLayoutX(200);
        playAgainButton.setLayoutY(500);
        exitButton.setLayoutX(300);
        exitButton.setLayoutY(500);
        image.setLayoutX(50);
        image.setLayoutY(50);

        

        // Ajoutez les boutons et l'image au conteneur
        gameOverLayout.getChildren().addAll(playAgainButton, exitButton, image);
    }

    Node getGameOverLayout(){return gameOverLayout;}
}