package gui;

import model.ButtonAction;

import java.io.File;

import datagame.Data;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PacmanSkin {
    private Scene scene;
    private Stage primaryStage;
    private MainMenu mainMenu;
    int width = Data.getWidth_accueil();
    int height = Data.getHeight_accueil();

    public PacmanSkin() {
        this.mainMenu = Data.getmainMenu();
        this.primaryStage = Data.getprimaryStage();
        initialize();
    }

    private void initialize() {
        Pane root = new Pane();


        //Image de fond
        Image imagebg = new Image(/* ../ressources/*/"accueil.jpeg");
        ImageView imageViewbg = new ImageView(imagebg);
        imageViewbg.setFitHeight(height); // Hauteur ajustée pour l'image de fond
        imageViewbg.setFitWidth(width);  // Largeur ajustée pour l'image de fond

        //Bouton pour retourner au menu d'accueil
        Label home = new Label("Home");
        setupButton(home, () -> {
            primaryStage.setScene(mainMenu.getScene());
            primaryStage.show();
        });
        home.setStyle("-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");//style

        //création des boutons avec leurs images et actions
        Button pacman = createImageButton(/* ../ressources/*/"pacman.png");
        setupButton1(pacman, () -> {Data.setskin(1);});
        Button pacmanblue = createImageButton(/* ../ressources/*/"pacmanblue.png");
        setupButton1(pacmanblue, () -> {Data.setskin(2);});
        Button pacmangreen = createImageButton(/* ../ressources/*/"pacmangreen.png");
        setupButton1(pacmangreen, () -> {Data.setskin(3);});
        
        //positionnements des elements
        home.setLayoutX(width*0.45);
        home.setLayoutY(height*0.80);
        pacman.setLayoutX(width*0.35);
        pacman.setLayoutY(height*0.60);
        pacmanblue.setLayoutX(width*0.45);
        pacmanblue.setLayoutY(height*0.60);
        pacmangreen.setLayoutX(width*0.55 );
        pacmangreen.setLayoutY(height*0.60);

        //ajout des elements au Pane
        root.getChildren().addAll(imageViewbg, home, pacman, pacmanblue, pacmangreen);

        //Création d'une nouvelle scene avec pour argument notre Pane root
        scene = new Scene(root, width, height);
    }

    /**
     * Methode qui sert a creer des image cliquable avec une action associé
     * @param imagePath chemin de l'image
     * @return
     */
    private Button createImageButton(String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setPreserveRatio(true);

        Button button = new Button();
        button.setGraphic(imageView);
        return button;
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

    /**
     * associe a un bouton une action
     * @param button
     * @param action
     */
    private void setupButton(Label button, ButtonAction action) {
        button.setOnMouseClicked(e -> action.performAction());
        gestionButton(button);
    }

    private void setupButton1(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        
    }

    public Scene getScene() {
        return scene;
    }
}

