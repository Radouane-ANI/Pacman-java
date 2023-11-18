package model;

import config.Cell;
import config.MazeConfig;
import config.Cell.Content;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import gui.FinalScreen;
import datagame.Data;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static model.Ghost.*;

public final class MazeState {
    private MazeConfig config;
    private final int height;
    private final int width;
    private boolean isGameRunning = true;
    private boolean boulbirespawn;

    private final boolean[][] gridState;
    private final Cell[][] grid;

    private final List<Critter> critters;
    private static int score;

    private final Map<Critter, RealCoordinates> initialPos;
    private static int lives = 3;


    Stage primaryStage;
    Pane gameRoot;

    public MazeState(MazeConfig config, Pane gameRoot) {
        this.primaryStage = Data.getprimaryStage();
        this.config = config;
        this.gameRoot = gameRoot;

        height = config.getHeight();
        width = config.getWidth();
        critters = List.of(PacMan.INSTANCE, Ghost.CLYDE, BLINKY, INKY, PINKY);
        gridState = new boolean[height][width];
        grid = config.getGrid();
        initialPos = Map.of(
                PacMan.INSTANCE, config.getPacManPos().toRealCoordinates(1.0),
                BLINKY, config.getBlinkyPos().toRealCoordinates(1.0),
                INKY, config.getInkyPos().toRealCoordinates(1.0),
                CLYDE, config.getClydePos().toRealCoordinates(1.0),
                PINKY, config.getPinkyPos().toRealCoordinates(1.0));
        resetCritters();
        gridState_init(gridState, grid);
    }

    public List<Critter> getCritters() {
        return critters;
    }

    public double getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update(long deltaTns) {
        if (!isGameRunning) {
            return; // Arrêtez d'exécuter la mise à jour du jeu si le jeu n'est pas en cours d'exécution
        }
        else{
            if (!PacMan.INSTANCE.isEnergized()) {
            Ghost.BLINKY.iaBlinky();
            } else {
                Ghost.fuite();
            }
            // FIXME: too many things in this method. Maybe some responsibilities can be
            // delegated to other methods or classes?
            for (var critter : critters) {
                var curPos = critter.getPos();
                var nextPos = critter.nextPos(deltaTns);
                var curNeighbours = curPos.intNeighbours();
                var nextNeighbours = nextPos.intNeighbours();
                if (!curNeighbours.containsAll(nextNeighbours)) { // the critter would overlap new cells. Do we allow it?
                    switch (critter.getDirection()) {
                        case NORTH -> {
                            for (var n : curNeighbours)
                                if (config.getCell(n).northWall()) {
                                nextPos = curPos.floorY();
                                critter.setDirection(Direction.NONE);
                                break;
                            }
                        }
                        case EAST -> {
                            for (var n: curNeighbours) if (config.getCell(n).eastWall()) {
                                nextPos = curPos.ceilX();
                                critter.setDirection(Direction.NONE);
                                break;
                            }
                        }
                        case SOUTH -> {
                            for (var n: curNeighbours) if (config.getCell(n).southWall()) {
                                nextPos = curPos.ceilY();
                                critter.setDirection(Direction.NONE);
                                break;
                            }
                        }
                        case WEST -> {
                            for (var n: curNeighbours) if (config.getCell(n).westWall()) {
                                nextPos = curPos.floorX();
                                critter.setDirection(Direction.NONE);
                                break;
                            }
                        }
                    }

                }

                critter.setPos(nextPos.warp(width, height));
            }
            var pacPos = PacMan.INSTANCE.getPos().round();
            if(PacMan.INSTANCE.PacManDot(gridState))addScore(1);
            
            // Vérifie si la cellule où se trouve Pac-Man contient un Energizer
            if (config.getCell(pacPos).initialContent() == Content.ENERGIZER) {
                // Change le contenu de la cellule en NOTHING.
                config.setCell(pacPos, config.getCell(pacPos).updateNextItemType(Content.NOTHING));
    
                // Activez energized sur Pac-Man
                PacMan.INSTANCE.setEnergized(true);
            }
            
            for (var critter : critters) {
                if (critter instanceof Ghost && critter.getPos().round().equals(pacPos)) {
                    if (PacMan.INSTANCE.isEnergized()) {
                        addScore(10);
                        resetCritter(critter);
                    }else {
                        playerLost();
                        return;
                    }
                }
            }
            Bonus.spawnBonus(); // a une probabilite de faire spawn un bonus
            for (var bonus : Bonus.values()) {
                if (bonus.isActif() && bonus.getPos().equals(pacPos)) { // si pacman mange le bonus appelle la fonction
                    bonus.manger();
                }
            }
            playerWin(); // si tous les points sont recuperé le win screen sera affiché
        }
    }


    /**
     * Methode permettant le bon fonction de la methode playerWin()(methode qui dectecte si le pacman a recuperer tous les points).
     * GridState est une representation booléene de grid.
     * En suivant ce principe on comprendre bien que les cellules sans dot seront noté true et les Cellules avec dot seront noté false, il faut
     * donc initialisé toutes les cases sans dot(en verifiant a l'aide de grid) a la valeur true (dans gridState), les cases avec dot seront
     * à la valeur false.
     * Cela permet de bien verifier si toutes les points ont été mangé en verifiant si toutes la valeur de gridState sont true.
     * @param gridState 
     * @param grid
     */
    private void gridState_init(boolean[][] gridState, Cell[][] grid){
        int height = grid[0].length;
        int length = grid.length;
        for(int k=0 ; k<length ; k++){
            for(int i=0 ; i<height ; i++){
                gridState[k][i] = !(grid[k][i].isDot());
            }
        }
    }

    public static void addLives(int increment) {
        lives += increment;
        System.out.println("Lives: " + lives);
    }

    public static void addScore(int increment) {
        score += increment;
        displayScore();
    }

    private static void displayScore() {
        // FIXME: this should be displayed in the JavaFX view, not in the console
        System.out.println("Score: " + score);
    }

    public boolean GetBoulbi() {
        return boulbirespawn;
    }


    /**
     * Si le Pacman s'est fait mangé par un ghost et que l'utilisateur n'avait plus de vie on affiche l'ecran GAME OVER.
     */
    private void playerLost() {boulbirespawn = true;
        lives--;
        boulbirespawn = false;
        if (lives == 0) {
            isGameRunning=false;

            FinalScreen fs = new FinalScreen(false);
            gameRoot.getChildren().add(fs.getFinalScreenLayout());
        }else{
            System.out.println("Lives: " + lives);
            resetCritters();
        }
    }


    /**
     * Methode permettant de verifier si toutes les valeur de tab sont vrai ce qui permettra dans une autre methode d'en deduire si Pacman
     * a mangé tous les dot ou non.
     * @param tab
     * @return si tous les elemnts de tab sont true
     */
    private static boolean areAllTrue(boolean[][] tab) {
        for (boolean[] t : tab) {
            for (boolean value : t) {
                if (!value) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Si Pacman a mangé tous les points alors on affiche l'ecran YOU WIN.
     */
    private void playerWin(){
        if(areAllTrue(gridState)){
            isGameRunning=false;

            FinalScreen fs = new FinalScreen(true);

            gameRoot.getChildren().add(fs.getFinalScreenLayout());
        }
    }

    private void resetCritter(Critter critter) {
        critter.setDirection(Direction.NONE);
        critter.setPos(initialPos.get(critter));
    }

    private void resetCritters() {
        for (var critter : critters)
            resetCritter(critter);
    }

    public MazeConfig getConfig() {
        return config;
    }

    public boolean getGridState(IntCoordinates pos) {
        return gridState[pos.y()][pos.x()];
    }

    public static int getScore() {
        return score;
    }
    
    public static int getLives() {
        return lives;
    }
}
