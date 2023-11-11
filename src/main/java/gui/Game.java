package gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import config.MazeConfig;
import model.MazeState;

public class Game {
    //Lançage du jeu
    private Scene gameScene;

    public Game(Stage primaryStage) {
        var root = new Pane();
        gameScene = new Scene(root);
        var pacmanController = new PacmanController();
        gameScene.setOnKeyPressed(pacmanController::keyPressedHandler);
        gameScene.setOnKeyReleased(pacmanController::keyReleasedHandler);
        var maze = new MazeState(MazeConfig.makeExample1(), root, primaryStage);
        var gameView = new GameView(maze, root, 100.0);
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameView.animate();
    }

    public Scene getScene() {
        return gameScene;
    }
}