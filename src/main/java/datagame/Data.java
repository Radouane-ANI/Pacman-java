package datagame;

import java.util.ArrayList;

import config.MazeConfig;
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

    public static boolean getDifficulty(){return MazeConfig.makeExample1().IsHardMode();}

    public static ArrayList<Ghost> ghostFuite = new ArrayList<>();
    public static boolean ghostFuiteSet = false;

    private static int score = 0;
    public static void resetScore(){score=0;}
    public static int getScore(){return score;}
    public static void setScore(int i){score=score+i;}
    
    private static double scale = 0;
    public static double getScale(){return scale;}
    public static void setScale(double i){scale=scale+i;}
    
    private static int height_accueil = 0;
    public static int getHeight_accueil(){return height_accueil;}
    public static void setHeight_accueil(int i){height_accueil=i;}

    private static int width_accueil = 0;
    public static int getWidth_accueil(){return width_accueil;}
    public static void setWidth_accueil(int i){width_accueil=i;}

    private static double height_game = 0;
    public static double getHeight(){return height_game;}
    public static void setHeight(double i){height_game=i;}

    private static double width_game = 0;
    public static double getWidth(){return width_game;}
    public static void setWidth(double i){width_game=i;}

    /*
    private static
    public static get
    public static void set
    */
}
