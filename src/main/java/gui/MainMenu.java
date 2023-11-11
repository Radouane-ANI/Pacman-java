package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ButtonAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainMenu {

    private Scene scene;
    private Stage stage;

    public MainMenu(Stage stage) {
        this.stage = stage;
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
        playButton.setOnAction(e -> startGame());
        playButton.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        applyHoverAnimation(playButton);
        playButton.setLayoutX(40);
        playButton.setLayoutY(200);

        var image = new ImageView(new Image(/* ../ressources/*/"accueil.jpg"));
        image.setFitWidth(500);
        image.setFitHeight(500);
        image.setPreserveRatio(false);

        // Ajout des elements au layout
        root.getChildren().addAll(image, playButton, exitButton);
        
        // Création de la scène avec le layout
        scene = new Scene(root, 500, 500);
    }

    private void startGame() {
        // Création de l'instance de la classe Game qui lance le jeu
        Game game = new Game(stage);
        // Définition de la scène de jeu comme scène principale de la fenêtre
        stage.setScene(game.getScene());
    }

    private void applyHoverAnimation(Button button) {
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    public Scene getScene() {
        return scene;
    }
}