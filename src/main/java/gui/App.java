package gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import config.MazeConfig;
import model.MazeState;


// import javax.sound.sampled.AudioSystem;
// import javax.sound.sampled.Clip;
// import java.io.File;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        var root = new Pane();
     //  root.setPrefSize(900, 600); //agrandir la fenetre 
        var gameScene = new Scene(root);
        var pacmanController = new PacmanController();
        gameScene.setOnKeyPressed(pacmanController::keyPressedHandler);
        gameScene.setOnKeyReleased(pacmanController::keyReleasedHandler);
        var maze = new MazeState(MazeConfig.makeExample1());
        var gameView = new GameView(maze, root, 100.0);
        primaryStage.setScene(gameScene);
        primaryStage.show();
        gameView.animate();

        // try {
        //         File soundFile = new File("../model/waka.wav"); // Remplacez "chemin/vers/le/son.wav" par le chemin du fichier audio
        //         Clip clip = AudioSystem.getClip();
        //         clip.open(AudioSystem.getAudioInputStream(soundFile));
        //         clip.start();
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }


        
    }
}
