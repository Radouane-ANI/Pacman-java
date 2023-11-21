package gui;

import model.ButtonAction;
import datagame.Data;

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

public class PacmanSkin {
    private Scene scene;
    private Stage primaryStage;
    MainMenu mainMenu;

    public PacmanSkin(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.primaryStage = Data.getprimaryStage();
        initialize();
    }

    private void initialize() {
        Pane root = new Pane();

        //Image de fond
        Image imagebg = new Image(/* ../ressources/*/"accueil.jpg");
        ImageView imageViewbg = new ImageView(imagebg);
        imageViewbg.setFitHeight(500); // Hauteur ajustée pour l'image de fond
        imageViewbg.setFitWidth(500);  // Largeur ajustée pour l'image de fond

        //Bouton pour retourner au menu d'accueil
        Button home = new Button("Home");
        setupButton(home, () -> {
            primaryStage.setScene(mainMenu.getScene());
            primaryStage.show();
        });
        home.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");//style
        applyHoverAnimation(home);//animation

        //création des boutons avec leurs images et actions
        Button pacman = createImageButton(/* ../ressources/*/"pacman.png");
        setupButton1(pacman, () -> {Data.setskin(1);});
        Button pacmanblue = createImageButton(/* ../ressources/*/"pacmanblue.png");
        setupButton1(pacmanblue, () -> {Data.setskin(2);});
        Button pacmangreen = createImageButton(/* ../ressources/*/"pacmangreen.png");
        setupButton1(pacmangreen, () -> {Data.setskin(3);});

        //positionnements des elements
        home.setLayoutX(220);
        home.setLayoutY(350);
        pacman.setLayoutX(100);
        pacman.setLayoutY(200);
        pacmanblue.setLayoutX(200);
        pacmanblue.setLayoutY(200);
        pacmangreen.setLayoutX(300);
        pacmangreen.setLayoutY(200);

        //ajout des elements au Pane
        root.getChildren().addAll(imageViewbg, home, pacman, pacmanblue, pacmangreen);

        //Création d'une nouvelle scene avec pour argument notre Pane root
        scene = new Scene(root, 500, 500);
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
     * Animation d'un boutton
     * @param button
     */
    private void applyHoverAnimation(Button button) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    /**
     * associe a un bouton une action
     * @param button
     * @param action
     */
    private void setupButton(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        applyHoverAnimation(button);
    }

    private void setupButton1(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        
    }

    public Scene getScene() {
        return scene;
    }
}

