package model;

import config.MazeConfig;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import javafx.scene.effect.Blend;

public enum Ghost implements Critter {

    // TODO: implement a different AI for each ghost, according to the description
    // in Wikipedia's page
    BLINKY, INKY, PINKY, CLYDE;

    static final MazeConfig config = MazeConfig.makeExample1();
    private RealCoordinates pos;
    private Direction direction = Direction.NONE;
    private static boolean[][] passe = new boolean[config.getHeight()][config.getWidth()];
    private static List<List<Character>> TousCheminVersPacman = new ArrayList<>();

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
        passe = new boolean[config.getHeight()][config.getWidth()];
        if (possible((int) BLINKY.pos.x(), (int) BLINKY.pos.y()).size() > 0 || BLINKY.direction == Direction.NONE) {
            char path = prochainePositionBlinky();
            switch (path) {
                case 'w':
                    if (BLINKY.direction != Direction.WEST) {
                        if (BLINKY.direction == Direction.EAST) {
                            BLINKY.pos = BLINKY.pos.floorX();
                        }
                        if (BLINKY.direction == Direction.NORTH) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                        if (BLINKY.direction == Direction.SOUTH) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                    }
                    BLINKY.direction = Direction.WEST;
                    break;
                case 'e':
                    if (BLINKY.direction != Direction.EAST) {
                        if (BLINKY.direction == Direction.WEST) {
                            BLINKY.pos = BLINKY.pos.floorX();
                        }
                        if (BLINKY.direction == Direction.NORTH) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                        if (BLINKY.direction == Direction.SOUTH) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                    }
                    BLINKY.direction = Direction.EAST;
                    break;
                case 'n':
                    if (BLINKY.direction != Direction.NORTH) {
                        if (BLINKY.direction == Direction.WEST) {
                            BLINKY.pos = BLINKY.pos.floorX();
                        }
                        if (BLINKY.direction == Direction.EAST) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                        if (BLINKY.direction == Direction.SOUTH) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                    }
                    BLINKY.direction = Direction.NORTH;
                    break;
                case 's':
                    if (BLINKY.direction != Direction.SOUTH) {
                        if (BLINKY.direction == Direction.WEST) {
                            BLINKY.pos = BLINKY.pos.floorX();
                        }
                        if (BLINKY.direction == Direction.EAST) {
                            BLINKY.pos = BLINKY.pos.floorX();
                        }
                        if (BLINKY.direction == Direction.NORTH) {
                            BLINKY.pos = BLINKY.pos.floorY();
                        }
                    }
                    BLINKY.direction = Direction.SOUTH;
                    break;
                default:
                    BLINKY.direction = BLINKY.direction;
                    break;
            }
        }
    }

    public static void updatePinkyPositions() { // déplacements de pinky
        IntCoordinates posPacMan = config.getPacManPos();
        IntCoordinates posPinky = config.getPinkyPos();
        char zonePacMan = (posPacMan.x() > config.getHeight() ? (posPacMan.y() > config.getWidth() ? 'a' : 'b')
                : (posPacMan.y() > config.getWidth() ? 'c' : 'd'));
        char zonePinky = (posPinky.x() > config.getHeight() ? (posPinky.y() > config.getWidth() ? 'a' : 'b')
                : (posPinky.y() > config.getWidth() ? 'c' : 'd'));
        if (zonePacMan == zonePinky) {
            // 1.0 Recuperer segment de pacman (le couloir dans lequel il est)
            // 1.1 Recuperer la direction
            // 1.2 Definir la prochaine intersection qu'il rencontrera (coord)
            // 1.3 Prendre le chemin le plus cours pour y aller
        } else {
            // 2.0 Se diriger vers cette zone
        }
    }

    public void cheminVersPacman(int x, int y, List<Character> chemin) {
        passe[x][y] = true;
        if (PacMan.INSTANCE.getPos().round().equals(new IntCoordinates(x, y))) {
            TousCheminVersPacman.add(new ArrayList<>(chemin));
            passe[x][y] = false;
            return;
        }
        for (Character character : possible(x, y)) {
            passe[x][y] = true;
            if (character == 'n' && passe[x][y - 1] == false) {
                chemin.add('n');
                cheminVersPacman(x, y - 1, chemin);
            }
            if (character == 'e' && passe[x + 1][y] == false) {
                chemin.add('e');
                cheminVersPacman(x + 1, y, chemin);
            }
            if (character == 's' && passe[x][y + 1] == false) {
                chemin.add('s');
                cheminVersPacman(x, y + 1, chemin);
            }
            if (character == 'w' && passe[x - 1][y] == false) {
                chemin.add('w');
                cheminVersPacman(x - 1, y, chemin);
            }
            passe[x][y] = false;
            if (chemin.size() > 0) {
                chemin.remove(chemin.size() - 1);
            }
        }

    }

    public List<Character> possible(int x, int y) {
        List<Character> possible = new ArrayList<Character>();
        IntCoordinates pos = new IntCoordinates(x, y);
        if (y > 0 && !config.getCell(pos).northWall() && passe[x][y - 1] == false) {
            possible.add('n');
        }
        if (y < passe[0].length - 1 && !config.getCell(pos).southWall() && passe[x][y + 1] == false) {
            possible.add('s');
        }
        if (x < passe.length - 1 && !config.getCell(pos).eastWall() && passe[x + 1][y] == false) {
            possible.add('e');
        }
        if (x > 0 && !config.getCell(pos).westWall() && passe[x - 1][y] == false) {
            possible.add('w');
        }
        return possible;
    }

    public char prochainePositionBlinky() {
        TousCheminVersPacman.clear();
        cheminVersPacman((int) BLINKY.pos.x(), (int) BLINKY.pos.y(), new ArrayList<Character>());
        if (TousCheminVersPacman.size() > 0) {
            List<Character> min = TousCheminVersPacman.get(0);
            for (List<Character> chemin : TousCheminVersPacman) {
                if (chemin.size() < min.size()) {
                    min = chemin;
                }
            }
            if (min.size() > 0) {
                return min.get(0);
            }
        }
        return 'a';
    }

}