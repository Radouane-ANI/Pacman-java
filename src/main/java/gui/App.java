package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage);
        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}