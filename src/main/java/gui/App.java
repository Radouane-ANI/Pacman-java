package gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import config.MazeConfig;
import model.MazeState;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        //System.out.println("1");
        var root = new Pane();
        //System.out.println("2");
        var gameScene = new Scene(root);
        //System.out.println("3");
        var pacmanController = new PacmanController();
        //System.out.println("4");
        gameScene.setOnKeyPressed(pacmanController::keyPressedHandler);
        //System.out.println("5");
        gameScene.setOnKeyReleased(pacmanController::keyReleasedHandler);
        //System.out.println("6");
        var maze = new MazeState(MazeConfig.makeExample1());
        //System.out.println("7");
        var gameView = new GameView(maze, root, 30.0);
        //System.out.println("8");
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameView.animate();
    }
}
