package model;

import config.Cell;
import config.MazeConfig;
import config.Cell.Content;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import gui.FinalScreen;
import gui.Game;
import datagame.Data;
import gui.ScoreLive;
import javafx.scene.Node;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import gui.PacmanController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static model.Ghost.*;

public final class MazeState {
    private MazeConfig config;
    private final int height;
    private final int width;

    private boolean isGameRunning = true;
    private boolean AudioPlayed;
    private boolean PacmanMort;
    int index_sc;

    private final boolean[][] gridState;
    private final Cell[][] grid;

    private final List<Critter> critters;

    private final Map<Critter, RealCoordinates> initialPos;

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
        Data.setWidth(grid.length * Data.getScale());
        Data.setHeight(grid[0].length * Data.getScale());

        ScoreLive sc = new ScoreLive();
        gameRoot.getChildren().add(sc.getLayout());
        index_sc = gameRoot.getChildren().size() - 1;
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

    public boolean SetAudioPlayed(boolean AudioPlayed) {
        return this.AudioPlayed = AudioPlayed;
    }

    public boolean GetAudioPlayed() {
        return AudioPlayed;
    }

    public void update(long deltaTns) {
        if (!isGameRunning) {
            return; // Arrêtez d'exécuter la mise à jour du jeu si le jeu n'est pas en cours
                    // d'exécution
        } else {
            if (!PacMan.INSTANCE.isEnergized() && Data.ghostFuiteSet) {
                Data.ghostFuite.clear();
                Data.ghostFuiteSet = false;
            }  
            if(PacMan.INSTANCE.isEnergized() && !Data.ghostFuiteSet){
                Data.ghostFuite.add(Ghost.PINKY);
                Data.ghostFuite.add(Ghost.BLINKY);
                Data.ghostFuite.add(Ghost.INKY);
                Data.ghostFuite.add(Ghost.CLYDE);
                Data.ghostFuiteSet = true;
            }
            Ghost.BLINKY.iaBlinky();
            Ghost.PINKY.iaPinky();
            Ghost.INKY.iaInky();
            Ghost.CLYDE.iaClyde();
            // FIXME: too many things in this method. Maybe some responsibilities can be
            // delegated to other methods or classes?
            for (var critter : critters) {
                var curPos = critter.getPos();
                var nextPos = critter.nextPos(deltaTns);
                var curNeighbours = curPos.intNeighbours();
                var nextNeighbours = nextPos.intNeighbours();
                if (!curNeighbours.containsAll(nextNeighbours)) { // the critter would overlap new cells. Do we allow
                                                                  // it?
                    switch (critter.getDirection()) {
                        case NORTH -> {
                            for (var n : curNeighbours) {
                                if (critter instanceof PacMan && config.getCell(n).isnorthWhite()) { // si pacman est en
                                                                                                     // face d'une case
                                                                                                     // blanche il ne
                                                                                                     // peut passer
                                    nextPos = curPos.floorY();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                                if (critter instanceof Ghost && !((Ghost) critter).estPossible(Direction.NORTH)) { //
                                    nextPos = curPos.floorY();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }

                                if (config.getCell(n).isnorthWall()) { //
                                    nextPos = curPos.floorY();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                            }
                        }
                        case EAST -> {
                            for (var n : curNeighbours) {
                                if (critter instanceof PacMan && config.getCell(n).iseastWhite()) {
                                    nextPos = curPos.ceilX();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }

                                if (config.getCell(n).iseastWall()) {
                                    nextPos = curPos.ceilX();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                            }
                        }
                        case SOUTH -> {
                            for (var n : curNeighbours) {
                                if (critter instanceof PacMan && config.getCell(n).issouthWhite()) {
                                    nextPos = curPos.ceilY();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                                if (critter instanceof Ghost && !((Ghost) critter).estPossible(Direction.SOUTH)) {
                                    nextPos = curPos.ceilY();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                                if (config.getCell(n).issouthWall()) {
                                    nextPos = curPos.ceilY();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                            }
                        }
                        case WEST -> {
                            for (var n : curNeighbours) {
                                if (critter instanceof PacMan && config.getCell(n).iswestWhite()) {
                                    nextPos = curPos.floorX();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                                if (config.getCell(n).iswestWall()) {
                                    nextPos = curPos.floorX();
                                    critter.setDirection(Direction.NONE);
                                    break;
                                }
                            }
                        }
                    }

                }

                critter.setPos(nextPos.warp(width, height));

            }
            var pacPos = PacMan.INSTANCE.getPos().round();
            if (PacMan.INSTANCE.PacManDot(gridState)) {
                addScore(1);
                String soundFile = "src/main/resources/waka.wav";
                Media sound = new Media(new File(soundFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }

            // Vérifie si la cellule où se trouve Pac-Man contient un Energizer
            if (config.getCell(pacPos).initialContent() == Content.ENERGIZER) {
                // Change le contenu de la cellule en NOTHING.
                config.setCell(pacPos, config.getCell(pacPos).updateNextItemType(Content.NOTHING));

                // Activez energized sur Pac-Man
                PacMan.INSTANCE.setEnergized(true);
                addScore(5);

<<<<<<< HEAD
                //Remet tout les fantomes dans l'état de fuite
                for (Ghost ghost : Ghost.values()){
                    if (!Data.ghostFuite.contains(ghost)){
                        Data.ghostFuite.add(ghost);
                    }
                    Data.ghostFuiteSet = true;
                }
                try { // Lorsque le pacman mange un dot il emet un son.
                    File soundFile = new File("src/main/resources/power_dot.wav");
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
=======
                String soundFile = "src/main/resources/power_dot.wav";
                Media sound = new Media(new File(soundFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
>>>>>>> develop

            }

            for (var critter : critters) {
                if (critter instanceof Ghost && critter.getPos().round().equals(pacPos)) {
                    if (PacMan.INSTANCE.isEnergized()) {
                        if (!((Ghost) critter).getManger()) {
                            if (!Data.ghostFuite.contains((Ghost)critter)){
                                makeGhostsInvisible();
                                SetPacmanMort(true);
                                return;
                            }else{
                                addScore(10);
                            }
                        }
                        ((Ghost) critter).setManger(true);
                        Data.ghostFuite.remove((Ghost) critter);
                    } else {

                        makeGhostsInvisible();
                        SetPacmanMort(true);

                        // playerLost();
                        return;
                    }
                }
            }
            Bonus.spawnBonus(); // a une probabilite de faire spawn un bonus
            for (var bonus : Bonus.values()) {
                if (bonus.isActif() && bonus.getPos().equals(pacPos)) { // si pacman mange le bonus appelle la fonction
                    bonus.manger();
                    displayScore();
                }
            }

            playerWin();
            // si tous les points sont recuperé le win screen sera affiché
        }
    }

    /**
     * Methode permettant le bon fonction de la methode playerWin()(methode qui
     * dectecte si le pacman a recuperer tous les points).
     * GridState est une representation booléene de grid.
     * En suivant ce principe on comprendre bien que les cellules sans dot seront
     * noté true et les Cellules avec dot seront noté false, il faut
     * donc initialisé toutes les cases sans dot(en verifiant a l'aide de grid) a la
     * valeur true (dans gridState), les cases avec dot seront
     * à la valeur false.
     * Cela permet de bien verifier si toutes les points ont été mangé en verifiant
     * si toutes la valeur de gridState sont true.
     * 
     * @param gridState
     * @param grid
     */
    private void gridState_init(boolean[][] gridState, Cell[][] grid) {
        int height = grid[0].length;
        int length = grid.length;
        for (int k = 0; k < length; k++) {
            for (int i = 0; i < height; i++) {
                gridState[k][i] = !(grid[k][i].isDot());
            }
        }
    }

    public void addLives(int increment) {
        Data.setLive(increment);
        displayScore();
    }

    public void addScore(int increment) {
        Data.setScore(increment);
        displayScore();
    }

    private void displayScore() {
        gameRoot.getChildren().remove(index_sc);
        ScoreLive sc = new ScoreLive();
        gameRoot.getChildren().add(sc.getLayout());
        index_sc = gameRoot.getChildren().size() - 1;
    }

    public boolean GetPacmanMort() {
        return PacmanMort;
    }

    public void SetPacmanMort(boolean PacmanMort) {
        this.PacmanMort = PacmanMort;

    }

    public void playerLost() {
        // FIXME: this should be displayed in the JavaFX view, not in the console. A
        // game over screen would be nice too.
        Data.setLive(-1);
        displayScore();

        if (Data.getLive() == 0) {
            isGameRunning = false;

            ButtonAction resetAction = () -> {
                System.out.println("Reset game");
                restartGame();
            };
            FinalScreen fs = new FinalScreen(resetAction, false);
            gameRoot.getChildren().add(fs.getFinalScreenLayout());
        } else {
            makeGhostVisibleAgain();
            resetCritters();

        }
    }

    /**
     * Methode permettant de verifier si toutes les valeur de tab sont vrai ce qui
     * permettra dans une autre methode d'en deduire si Pacman
     * a mangé tous les dot ou non.
     * 
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
    private void playerWin() {
        if (areAllTrue(gridState)) {
            isGameRunning = false;

            ButtonAction resetAction = () -> {
                System.out.println("Reset game");
                restartGame();
            };
            FinalScreen fs = new FinalScreen(resetAction, true);

            gameRoot.getChildren().add(fs.getFinalScreenLayout());
        }
    }

    private void resetCritter(Critter critter) {
        if (critter instanceof PacMan) {
            critter.setDirection(Direction.NONE);
            critter.setPos(initialPos.get(critter));
            ((PacMan) critter).setEnergized(false);
        }
        if (critter instanceof Ghost) {
            resetGhost((Ghost) critter);
        }
    }

    private void resetGhost(Ghost ghost) {
        ghost.setDirection(Direction.NONE);
        ghost.setPos(initialPos.get((Critter) ghost));
        ghost.reset();

    }

    private void resetCritters() {
        SetAudioPlayed(false);
        for (var critter : critters)
            resetCritter(critter);
    }

    /**
     * Permet de réeinitialiser Gridstate le nombre de vies et le nombre de points.
     */
    private void resetGame() {
        makeGhostVisibleAgain();
        resetCritters();
        for (boolean[] k : gridState)
            for (int i = 0; i < k.length - 1; i++) {
                k[i] = false;
            }
        gridState_init(gridState, grid);
        Data.resetLive();
        Data.resetScore();
        this.config = MazeConfig.makeExample1();
        for (Bonus bonus : Bonus.values()) {
            bonus.setApparut(false);
            bonus.setActif(false);
        }

    }

    /**
     * Permet de réeinitialiser toutes les valeurs du jeu afin de commencer une
     * nouvelle partie.
     */
    public void restartGame() {
        resetGame();
        gameRoot.getChildren().remove(gameRoot.getChildren().size() - 1);
        displayScore();
        isGameRunning = true;
    }

    public MazeConfig getConfig() {
        return config;
    }

    public boolean getGridState(IntCoordinates pos) {
        return gridState[pos.y()][pos.x()];
    }

}
