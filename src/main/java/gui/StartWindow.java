import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import gui.App;

public class StartWindow extends Application {
    public static void main(String[] args) {
        Application.launch(StartWindow.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fenêtre de démarrage");

        Button startButton = new Button("Commencer le jeu");
        startButton.setOnAction(e -> {
            // Code à exécuter lorsque le bouton est cliqué
            System.out.println("Le jeu commence !");
            primaryStage.close(); // Fermeture de la fenêtre de démarrage
            App app = new App();
            app.start(new Stage());
        });

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(startButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}