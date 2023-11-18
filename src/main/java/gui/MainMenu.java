package gui;

import model.ButtonAction;
import datagame.Data;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenu {
    // Menu d'accueil
    private Scene scene;
    private Stage stage;
    PacmanSkin skin;

    public MainMenu() {
        skin = new PacmanSkin(this);
        this.stage = Data.getprimaryStage();
        initialize();
    }

    private void initialize() {
        // Création du layout principal
        Pane root = new Pane();

         // Création du bouton pour exit
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));
        exitButton.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(exitButton);
        exitButton.setLayoutX(420);
        exitButton.setLayoutY(200);
        
        // Création du bouton pour jouer
        Button playButton = new Button("Jouer");
        playButton.setOnAction(e -> {
            startGame();
        });
        playButton.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(playButton);
        playButton.setLayoutX(40);
        playButton.setLayoutY(200);

        // Création du bouton pour choisir les skin
        Button changeskin = new Button("Skin");
        changeskin.setOnAction(e -> changeSkin());
        changeskin.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(changeskin);
        changeskin.setLayoutX(230);
        changeskin.setLayoutY(200);

        var image = new ImageView(new Image(/* ../ressources/*/"accueil.jpg"));
        image.setFitWidth(500);
        image.setFitHeight(500);
        image.setPreserveRatio(false);

        // Ajout des elements au layout
        root.getChildren().addAll(image, playButton, exitButton,changeskin);
        
        // Création de la scène avec le layout
        scene = new Scene(root, 500, 500);
    }

    /**
     * Création de l'instance de la classe Game qui lance le jeu
     * Définition de la scène de jeu comme scène principale de la fenêtre
     */
    private void startGame() {
        Game game = new Game();
        stage.setScene(game.getScene());
    }

    /**
     * Création de l'instance de la classe Game qui lance le jeu
     * Définition de la scène de jeu comme scène principale de la fenêtre
     */
    private void changeSkin() {
        stage.setScene(skin.getScene());
    }

    /**
     * permet de changer a couleur d'un bouton lorsqu'on clique dessus
     * @param button
     */
    private void applyHoverAnimation(Button button) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    public Scene getScene() {
        return scene;
    }
}