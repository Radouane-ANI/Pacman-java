package model;

import gui.MainMenu;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class FinalScreen {
    private final Group FinalScreenLayout = new Group();
    private final Button playAgainButton = new Button("Rejouer");
    private final Button exitButton = new Button("Exit");
    private final Button homeButton = new Button("Home");
    boolean win;

    public FinalScreen(ButtonAction playAgainAction, Stage primaryStage, boolean win) {
        this.win = win;

        setupButton(playAgainButton, playAgainAction);
        setupButton(exitButton, () -> {System.exit(0);});
        setupButton(homeButton, () -> {
            MainMenu mainMenu = new MainMenu(primaryStage);
            primaryStage.setScene(mainMenu.getScene());
            primaryStage.show();
        });

        String img = win ? "win.png" : "game_over.png";
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
        FinalScreenLayout.getChildren().addAll(playAgainButton, exitButton, homeButton, image);
    }

    private void setupButton(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(button);
    }

    private void applyHoverAnimation(Button button) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    Node getFinalScreenLayout(){return FinalScreenLayout;}
}