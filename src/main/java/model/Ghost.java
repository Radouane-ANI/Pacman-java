package model;

import  config.MazeConfig;
import java.util.Random;

import geometry.IntCoordinates;
import geometry.RealCoordinates;

public enum Ghost implements Critter {

    // TODO: implement a different AI for each ghost, according to the description in Wikipedia's page
    BLINKY, INKY, PINKY, CLYDE;

    static final MazeConfig config = MazeConfig.makeExample1();
    private RealCoordinates pos;
    private Direction direction = Direction.NONE;
    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public void setPos(RealCoordinates newPos) {
        pos = newPos;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public double getSpeed() { // vitesse des fantomes
        return 1;
    }

    public static void updateGhostPositions() { // fais bouger les fantomes dans une direction aleatoires
        Random rd = new Random();
        for (Ghost ghost : Ghost.values()) {
            switch(rd.nextInt(4)){
                case 0 -> ghost.direction = Direction.NORTH;
                case 1 -> ghost.direction = Direction.EAST;
                case 2 -> ghost.direction = Direction.SOUTH;
                case 3 -> ghost.direction = Direction.WEST;
            }
        }
    }

    public static void updatePinkyPositions() { //déplacements de pinky
        IntCoordinates posPacMan = config.getPacManPos();
        IntCoordinates posPinky = config.getPinkyPos();
        char zonePacMan = (
            posPacMan.x() > config.getHeight() ? 
            (posPacMan.y() > config.getWidth() ? 'a' : 'b') : 
            (posPacMan.y() > config.getWidth() ? 'c': 'd')
            );
        char zonePinky = (
            posPinky.x() > config.getHeight() ? 
            (posPinky.y() > config.getWidth() ? 'a' : 'b') : 
            (posPinky.y() > config.getWidth() ? 'c': 'd')
            );
        if (zonePacMan == zonePinky){
            //1.0 Recuperer segment de pacman (le couloir dans lequel il est)
            //1.1 Recuperer la direction
            //1.2 Definir la prochaine intersection qu'il rencontrera (coord)
            //1.3 Prendre le chemin le plus cours pour y aller
        }else{
            //2.0 Se diriger vers cette zone
        }
    }

}