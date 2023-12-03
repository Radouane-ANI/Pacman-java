package datagame;

import gui.*;
import model.*;

import javafx.stage.Stage;

public class Data {
    //Base de donnée, chaque donnée est protégé avec un champs private. Avec getter/setter associé
    private static Stage primaryStage;
    public static Stage getprimaryStage(){return primaryStage;}
    public static void setprimaryStage(Stage p){primaryStage=p;}

    private static MainMenu mainMenu;
    public static MainMenu getmainMenu(){return mainMenu;}
    public static void setmainMenu(MainMenu m){mainMenu=m;}

    private static int skin = 0;
    public static int getskin(){return skin;}
    public static void setskin(int s){skin=s;}

    private static Game game;
    public static Game getGame() {return game;}
    public static void setGame(Game g) {game = g;}

    private static int live = 3;
    public static void resetLive(){live=3;}
    public static int getLive(){return live;}
    public static void setLive(int i){live=live+i;}

    private static int score = 0;
    public static void resetScore(){score=0;}
    public static int getScore(){return score;}
    public static void setScore(int i){score=score+i;}
    
    private static double scale = 0;
    public static double getScale(){return scale;}
    public static void setScale(double i){scale=scale+i;}
    
    private static double height = 0;
    public static double getHeight(){return height;}
    public static void setHeight(double i){height=height+i;}

    private static double width = 0;
    public static double getWidth(){return width;}
    public static void setWidth(double i){width=width+i;}

    /*
    private static
    public static get
    public static void set
    */
}
