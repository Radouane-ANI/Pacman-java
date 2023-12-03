package gui;

import model.ButtonAction;
import datagame.Data;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MainMenu {
    // Menu d'accueil
    private Scene scene;
    private Stage stage;
    int width;
    int height;
    PacmanSkin skin;

    public MainMenu(int w,int h) {
        width = w;
        height = h;
        skin = new PacmanSkin(this);
        this.stage = Data.getprimaryStage();
        initialize();
    }

    private void initialize() {
        // Création du layout principal
        Pane root = new Pane();

        // Son clic sur bouton
        Media clicMedia = new Media(new File("src/main/resources/clic.mp3").toURI().toString());
        MediaPlayer clicPlayer = new MediaPlayer(clicMedia);

         // Création du bouton pour exit
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            clicPlayer.play();
            System.exit(0);
        });
        exitButton.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        gestionButton(exitButton);
        exitButton.setLayoutX(width*0.8);
        exitButton.setLayoutY(height*0.4);
        
        // Création du bouton pour jouer
        Button playButton = new Button("Jouer");
        playButton.setOnAction(e -> {
            clicPlayer.play();
            startGame();
        });
        playButton.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        gestionButton(playButton);
        playButton.setLayoutX(width*0.45);
        playButton.setLayoutY(height*0.4);

        // Création du bouton pour choisir les skin
        Button changeskin = new Button("Skin");
        changeskin.setOnAction(e -> {
            clicPlayer.play();
            changeSkin();
        });
        changeskin.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        gestionButton(changeskin);
        changeskin.setLayoutX(width*0.15);
        changeskin.setLayoutY(height*0.4);

        var image = new ImageView(new Image(/* ../ressources/*/"accueil.jpg"));
        image.setFitWidth(width);
        image.setFitHeight(height);
        image.setPreserveRatio(false);

        // Ajout des elements au layout
        root.getChildren().addAll(image, playButton, exitButton,changeskin);
        
        // Création de la scène avec le layout
        scene = new Scene(root, width, height);
    }

    /**
     * Création de l'instance de la classe Game qui lance le jeu
     * Définition de la scène de jeu comme scène principale de la fenêtre
     */
    private void startGame() {
        if (Data.getGame() == null) {
            Game game = new Game();
            Data.setGame(game);
        }
        stage.setScene(Data.getGame().getScene());
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

    public Scene getScene() {
        return scene;
    }
}