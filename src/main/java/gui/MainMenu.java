package gui;

import datagame.Data;

import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private int width;
    private int height;
    private PacmanSkin skin;

    public MainMenu() {
        width = Data.getWidth_accueil();
        height = Data.getHeight_accueil();
        System.out.println(width+"  "+height);
        Data.setmainMenu(this);
        skin = new PacmanSkin();
        this.stage = Data.getprimaryStage();
        initialize();
    }

    private void initialize() {
        // Création du layout principal
        Pane root = new Pane();

         // Création du bouton pour exit
        Label exitButton = new Label("Exit");
        exitButton.setOnMouseClicked(e -> System.exit(0));
        exitButton.setStyle("-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
        gestionButton(exitButton);
        exitButton.setLayoutX(width*0.45);
        exitButton.setLayoutY(height*0.80);
        
        // Création du bouton pour jouer
        Label playButton = new Label("Jouer");
        playButton.setOnMouseClicked(e -> startGame());
        playButton.setStyle("-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
        gestionButton(playButton);
        playButton.setLayoutX(width*0.45);
        playButton.setLayoutY(height*0.60);

        // Création du bouton pour choisir les skin
        Label changeskin = new Label("Skin");
        changeskin.setOnMouseClicked(e -> changeSkin());
        changeskin.setStyle("-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
        gestionButton(changeskin);
        changeskin.setLayoutX(width*0.45);
        changeskin.setLayoutY(height*0.70);

        // GIF pacman
        Image pacgif = new Image("pacmang.gif",350,0,true,true);
        ImageView imageView = new ImageView(pacgif);
        imageView.setLayoutX(width*0.02);
        imageView.setLayoutY(height*0.80);
        
    
        // Image de fond
        var image = new ImageView(new Image("accueil.jpeg"));
        image.setFitWidth(width);
        image.setFitHeight(height+1);
        image.setPreserveRatio(false);

        // Ajout des elements au layout
        root.getChildren().addAll(image, playButton, exitButton,changeskin,imageView);
        stage.setResizable(false);
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
     * Methode qui permet de donner un style au bouton et de jouer un son lorsque l'on passe ou clique dessus
     * @param b bouton sur lequel les changemetns seront appliqués
     */
    private void gestionButton(Label b){
        Media survolMedia = new Media(new File("src/main/resources/onclic.mp3").toURI().toString());
        MediaPlayer survolPlayer = new MediaPlayer(survolMedia);

        // Gestion du survol
        b.setOnMouseEntered(e -> {
            b.setStyle("-fx-text-fill: yellow;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
            survolPlayer.stop(); // Arrêter la lecture en cours, s'il y en a une
            survolPlayer.play(); // Lire le son de survol
        });

        // Gérer la sortie du survol du bouton
        b.setOnMouseExited(e -> {
            b.setStyle("-fx-text-fill: white;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
            survolPlayer.stop();
        });

        // Gérer clic souris
        b.setOnMousePressed(e -> b.setStyle("-fx-text-fill: yellow;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;"));
        b.setOnMouseReleased(e -> b.setStyle("-fx-text-fill: yellow;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;"));
    }

    public Scene getScene() {
        return scene;
    }

    public int getWidth(){return width;}
    public int getHeight(){return height;}
}