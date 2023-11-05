package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenu {

    private Scene scene;
    private Stage stage;

    public MainMenu(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        // Création du layout principal
        StackPane root = new StackPane();
        
        // Création du bouton pour jouer
        Button playButton = new Button("Jouer");
        playButton.setOnAction(e -> startGame());

        // Ajout du bouton au layout
        root.getChildren().add(playButton);
        
        // Création de la scène avec le layout
        scene = new Scene(root, 300, 250);
    }

    private void startGame() {
        // Création de l'instance de la classe Game qui lance le jeu
        Game game = new Game(stage);
        // Définition de la scène de jeu comme scène principale de la fenêtre
        stage.setScene(game.getScene());
    }

    public Scene getScene() {
        return scene;
    }
}