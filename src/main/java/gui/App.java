package gui;

import datagame.Data;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Data.setprimaryStage(primaryStage);
        MainMenu mainMenu = new MainMenu(500,500);
        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}