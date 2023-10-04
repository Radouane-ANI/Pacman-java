package model;

import java.util.Random;

import config.MazeConfig;
import geometry.RealCoordinates;
import javafx.scene.effect.Blend;

public enum Ghost implements Critter {

    // TODO: implement a different AI for each ghost, according to the description
    // in Wikipedia's page
    BLINKY, INKY, PINKY, CLYDE;

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
            switch (rd.nextInt(4)) {
                case 0 -> ghost.direction = Direction.NORTH;
                case 1 -> ghost.direction = Direction.EAST;
                case 2 -> ghost.direction = Direction.SOUTH;
                case 3 -> ghost.direction = Direction.WEST;
            }
        }
    }

    public void iaBlinky() {
        MazeConfig labyrinthe = MazeConfig.makeExample1();
        if (BLINKY.pos.entier() || BLINKY.direction == Direction.NONE) {
            System.out.println(BLINKY.pos);
            if (labyrinthe.getCell(BLINKY.pos.toIntCoordinates()).southWall()) {
                BLINKY.direction = Direction.EAST;
            } else {
                BLINKY.direction = Direction.SOUTH;
            }
        }

    }

}