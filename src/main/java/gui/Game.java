package gui;

import config.MazeConfig;
import model.MazeState;
import datagame.Data;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Game {
    //Lançage du jeu
    private Scene gameScene;
    private MazeState maze;

    public Game() {
        Stage primaryStage = Data.getprimaryStage();
        var root = new Pane();
        gameScene = new Scene(root);
        var pacmanController = new PacmanController();
        gameScene.setOnKeyPressed(pacmanController::keyPressedHandler);
        gameScene.setOnKeyReleased(pacmanController::keyReleasedHandler);
        int scale = 30;
        Data.setScale(scale);
        maze = new MazeState(MazeConfig.makeExample1(), root);
        var gameView = new GameView(maze, root, scale);
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameView.animate();
    }

    public Scene getScene() {
        maze.restartGame();
        return gameScene;
    }
}