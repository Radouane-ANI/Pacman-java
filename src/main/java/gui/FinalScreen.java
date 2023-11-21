package gui;

import model.ButtonAction;
import datagame.Data;
import gui.Game;

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

    public FinalScreen(ButtonAction reseAction, boolean win) {
        Stage primaryStage = Data.getprimaryStage();
        this.win = win;

        setupButton(playAgainButton, reseAction);//À chaque nouvelle partie une nouvelle instance de Game est crée ce qui permet un réeinitialisation complete du jeu

        setupButton(exitButton, () -> {System.exit(0);});
        setupButton(homeButton, () -> {
            MainMenu mainMenu = new MainMenu();
            primaryStage.setScene(mainMenu.getScene());
            primaryStage.show();
        });

        String img = win ? "win.png" : "game_over.png";
        var image = new ImageView(new Image(/* ../ressources/*/img, 500, 500, true, true));

        // Positionnez les boutons et l'image à l'emplacement souhaité
        playAgainButton.setLayoutX(250);
        playAgainButton.setLayoutY(620);
        exitButton.setLayoutX(350);
        exitButton.setLayoutY(620);
        homeButton.setLayoutX(450);
        homeButton.setLayoutY(620);
        image.setLayoutX(140);
        image.setLayoutY(160);

        // Ajoutez les boutons et l'image au conteneur
        FinalScreenLayout.getChildren().addAll(playAgainButton, exitButton, homeButton, image);
    }

    /**
     * Permet d'affecter une aciton a un bouton
     * @param button 
     * @param action
     */
    private void setupButton(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(button);
    }

    /**
     * permet de changer a couleur d'un bouton lorsqu'on clique dessus
     * @param button
     */
    private void applyHoverAnimation(Button button) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    public Node getFinalScreenLayout(){return FinalScreenLayout;}
}