package model;

import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;

import static model.Ghost.*;

public final class MazeState {
    private final MazeConfig config;
    private final int height;
    private final int width;
    private boolean isGameRunning = true;
    private boolean boulbirespawn;

    private final boolean[][] gridState;

    private final List<Critter> critters;
    private int score;

    private final Map<Critter, RealCoordinates> initialPos;
    private int lives = 3;

    Pane gameRoot;
    private final GameOverScreen gos;

    public MazeState(MazeConfig config, Pane gameRoot) {
        this.config = config;
        this.gameRoot = gameRoot;

        height = config.getHeight();
        width = config.getWidth();
        critters = List.of(PacMan.INSTANCE, Ghost.CLYDE, BLINKY, INKY, PINKY);
        gridState = new boolean[height][width];
        initialPos = Map.of(
                PacMan.INSTANCE, config.getPacManPos().toRealCoordinates(1.0),
                BLINKY, config.getBlinkyPos().toRealCoordinates(1.0),
                INKY, config.getInkyPos().toRealCoordinates(1.0),
                CLYDE, config.getClydePos().toRealCoordinates(1.0),
                PINKY, config.getPinkyPos().toRealCoordinates(1.0));
        resetCritters();

        ButtonAction playAgainAction = () -> {

            System.out.println("Reset game");
            restartGame();
            //gos.close();
        };
        ButtonAction exitAction = () -> {
            System.exit(0);
        };
        gos = new GameOverScreen(playAgainAction, exitAction);
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
            Ghost.BLINKY.iaBlinky();
            // FIXME: too many things in this method. Maybe some responsibilities can be delegated to other methods or classes?
            for  (var critter: critters) {
                var curPos = critter.getPos();
                var nextPos = critter.nextPos(deltaTns);
                var curNeighbours = curPos.intNeighbours();
                var nextNeighbours = nextPos.intNeighbours();
                if (!curNeighbours.containsAll(nextNeighbours)) { // the critter would overlap new cells. Do we allow it?
                    switch (critter.getDirection()) {
                        case NORTH -> {
                            for (var n: curNeighbours) if (config.getCell(n).northWall()) {
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
            // FIXME Pac-Man rules should somehow be in Pacman class
            var pacPos = PacMan.INSTANCE.getPos().round();
            if (!gridState[pacPos.y()][pacPos.x()]) {
                addScore(1);
                gridState[pacPos.y()][pacPos.x()] = true;
            }
            for (var critter : critters) {
                if (critter instanceof Ghost && critter.getPos().round().equals(pacPos)) {
                    if (PacMan.INSTANCE.isEnergized()) {
                        addScore(10);
                        resetCritter(critter);
                    } else {
                        playerLost();
                        return;
                    }
                }
            }
        }
    }

    private void addScore(int increment) {
        score += increment;
        displayScore();
    }

    private void displayScore() {
        // FIXME: this should be displayed in the JavaFX view, not in the console
        System.out.println("Score: " + score);
    }

    public boolean GetBoulbi() {
        return boulbirespawn;
    }

    private void playerLost() {
        // FIXME: this should be displayed in the JavaFX view, not in the console. A
        // game over screen would be nice too.

        boulbirespawn = true;
        lives--;
        boulbirespawn = false;
        if (lives == 0) {
            isGameRunning=false;
            gameRoot.getChildren().add(gos.getGameOverLayout());
        }else{
            System.out.println("Lives: " + lives);
            resetCritters();
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

    public void resetGame() {
        resetCritters();
        for(boolean[] k:gridState) for(int i=0;i<k.length-1;i++){
            k[i]=false;
        }
        lives=3;
        score=0;

    }

    public void restartGame() {
        resetGame();// Réinitialisez toutes les valeurs du jeu à l'état initial
        isGameRunning = true; // Redémarrez le jeu
        gameRoot.getChildren().remove(gameRoot.getChildren().size() - 1);
        

        /*System.out.println("Vie"+lives+" score"+score);
        for(boolean[] k:gridState){ 
            for(boolean i : k){
                System.out.print(i);
            }
            System.out.println("");
        }*/ //DEBUG

    }

    public MazeConfig getConfig() {
        return config;
    }

    public boolean getGridState(IntCoordinates pos) {
        return gridState[pos.y()][pos.x()];
    }
}
