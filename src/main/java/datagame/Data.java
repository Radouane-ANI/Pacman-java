package datagame;

import gui.*;
import model.*;

import javafx.stage.Stage;

public class Data {
    //Base de donnée, chaque donnée est protégé avec un champs private. Avec getter/setter associé
    private static Stage primaryStage;
    public static Stage getprimaryStage(){return primaryStage;}
    public static void setprimaryStage(Stage p){primaryStage=p;}

    public static MainMenu mainMenu;
    public static MainMenu getmainMenu(){return mainMenu;}
    public static void setmainMenu(MainMenu m){mainMenu=m;}

    private static int skin = 0;
    public static int getskin(){return skin;}
    public static void setskin(int s){skin=s;}

    private static Game game;
    public static Game getGame() {
        return game;
    }
    public static void setGame(Game g) {
        game = g;
    }




    /*
    public static  get
    public static void set
    */
}
