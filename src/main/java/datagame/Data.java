package datagame;

import gui.*;
import model.*;

import javafx.stage.Stage;

public class Data {
    private static Stage primaryStage;
    public static Stage getprimaryStage(){return primaryStage;}
    public static void setprimaryStage(Stage p){primaryStage=p;}

    public static MainMenu mainMenu;
    public static MainMenu getmainMenu(){return mainMenu;}
    public static void setmainMenu(MainMenu m){mainMenu=m;}

    private static int skin = 0;
    public static int getskin(){return skin;}
    public static void setskin(int s){skin=s;}



    /*
    public static  get
    public static void set
    */
}
