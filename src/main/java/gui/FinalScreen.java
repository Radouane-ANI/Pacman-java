package gui;

import model.ButtonAction;

import java.io.File;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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

        setupButton(exitButton, () -> {
            System.exit(0);});
        setupButton(homeButton, () -> {
            MainMenu mainMenu = new MainMenu(500,500);
            primaryStage.setScene(mainMenu.getScene());
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
        gestionButton(button);
    }

    /**
     * Methode qui permet de donner un style au bouton et de jouer un son lorsque l'on passe ou clique dessus
     * @param b bouton sur lequel les changemetns seront appliqués
     */
    private void gestionButton(Button b){
        Media survolMedia = new Media(new File("src/main/resources/onclic.mp3").toURI().toString());
        MediaPlayer survolPlayer = new MediaPlayer(survolMedia);

        // Gestion du survol
        b.setOnMouseEntered(e -> {
            b.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;");
            survolPlayer.stop(); // Arrêter la lecture en cours, s'il y en a une
            survolPlayer.play(); // Lire le son de survol
        });

        // Gérer la sortie du survol du bouton
        b.setOnMouseExited(e -> {
            b.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
            survolPlayer.stop();
        });

        // Gérer clic souris
        b.setOnMousePressed(e -> b.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
        b.setOnMouseReleased(e -> b.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    public Node getFinalScreenLayout(){return FinalScreenLayout;}
}