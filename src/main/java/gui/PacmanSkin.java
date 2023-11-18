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

        Image imagebg = new Image(/* ../ressources/*/"accueil.jpg");
        ImageView imageViewbg = new ImageView(imagebg);
        imageViewbg.setFitHeight(500); // Hauteur ajustée pour l'image de fond
        imageViewbg.setFitWidth(500);  // Largeur ajustée pour l'image de fond

        Button home = new Button("Home");
        setupButton(home, () -> {
            primaryStage.setScene(mainMenu.getScene());
            primaryStage.show();
        });
        home.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(home);

        Button pacman = createImageButton(/* ../ressources/*/"pacman.png");
        setupButton(pacman, () -> {Data.setskin(1);});
        Button pacmanblue = createImageButton(/* ../ressources/*/"pacmanblue.png");
        setupButton(pacmanblue, () -> {Data.setskin(2);});
        Button pacmangreen = createImageButton(/* ../ressources/*/"pacmangreen.png");
        setupButton(pacmangreen, () -> {Data.setskin(3);});

        home.setLayoutX(220);
        home.setLayoutY(350);
        pacman.setLayoutX(100);
        pacman.setLayoutY(200);
        pacmanblue.setLayoutX(200);
        pacmanblue.setLayoutY(200);
        pacmangreen.setLayoutX(300);
        pacmangreen.setLayoutY(200);

        root.getChildren().addAll(imageViewbg, home, pacman, pacmanblue, pacmangreen);

        scene = new Scene(root, 500, 500);
    }

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

    private void applyHoverAnimation(Button button) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    private void setupButton(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        applyHoverAnimation(button);
    }

    public Scene getScene() {
        return scene;
    }
}

