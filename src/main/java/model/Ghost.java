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
    // tableau passerBlinky de la taille de la carte qui dit si blinky est deja
    // passer par la
    // (utile pour le bactracking) :
    private static boolean[][] passerBlinky = new boolean[config.getHeight()][config.getWidth()];
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
        passerBlinky = new boolean[config.getHeight()][config.getWidth()];
        if (possible((int) BLINKY.pos.x(), (int) BLINKY.pos.y()).size() > 0 || BLINKY.direction == Direction.NONE) {
            Direction path = prochainePositionBlinky();
            changeDirection(path, BLINKY);
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

    // algorithm de backtracking qui trouve tous les chemin ves pacman et les mets
    // dans la liste satique TousCheminVersPacman
    public void cheminVersPacman(int x, int y, List<Character> chemin) {
        passerBlinky[x][y] = true; // marque la position comme visite

        if (PacMan.INSTANCE.getPos().round().equals(new IntCoordinates(x, y))) {
            TousCheminVersPacman.add(new ArrayList<>(chemin)); // si on a atteint pacman sauvegarde le chemin
            passerBlinky[x][y] = false;
            return;
        }

        for (Character character : possible(x, y)) { // essayer toutes les positions possible depuis cette position
            if (character == 'n' && passerBlinky[x][y - 1] == false) {
                chemin.add('n');
                cheminVersPacman(x, y - 1, chemin);
            }
            if (character == 'e' && passerBlinky[x + 1][y] == false) {
                chemin.add('e');
                cheminVersPacman(x + 1, y, chemin);
            }
            if (character == 's' && passerBlinky[x][y + 1] == false) {
                chemin.add('s');
                cheminVersPacman(x, y + 1, chemin);
            }
            if (character == 'w' && passerBlinky[x - 1][y] == false) {
                chemin.add('w');
                cheminVersPacman(x - 1, y, chemin);
            }
            if (chemin.size() > 0) { // quand on a explore la position la retire
                chemin.remove(chemin.size() - 1);
            }
        }
        passerBlinky[x][y] = false; // marque la position comme non visite

    }

    // verifie toute les intersection a une coordonnee x et y
    public List<Character> possible(int x, int y) {
        List<Character> possible = new ArrayList<Character>();
        IntCoordinates p = new IntCoordinates(x, y);
        // verifie que l'on ne depasse pas du tableau, l'absence de mur et si on est deja passer
        if (y > 0 && !config.getCell(p).northWall() && passerBlinky[x][y - 1] == false) {
            possible.add('n');
        }
        if (y < passerBlinky.length - 1 && !config.getCell(p).southWall() && passerBlinky[x][y + 1] == false) {
            possible.add('s');
        }
        if (x < passerBlinky[0].length - 1 && !config.getCell(p).eastWall() && passerBlinky[x + 1][y] == false) {
            possible.add('e');
        }
        if (x > 0 && !config.getCell(p).westWall() && passerBlinky[x - 1][y] == false) {
            possible.add('w');
        }
        return possible; // renvoie la liste de toute les directions des intersection
    }

    // applique l'algorithme de bactracking et renvoie la premiere Direction du plus
    // court chemin vers pacman
    public Direction prochainePositionBlinky() {
        TousCheminVersPacman.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        cheminVersPacman((int) BLINKY.pos.x(), (int) BLINKY.pos.y(), new ArrayList<Character>()); // calcule tous les
                                                                                                  // chemin

        if (TousCheminVersPacman.size() > 0) { // prend le chemin le plus court pour renvoyer la premiere position
            List<Character> min = TousCheminVersPacman.get(0);
            for (List<Character> chemin : TousCheminVersPacman) {
                if (chemin.size() < min.size()) {
                    min = chemin;
                }
            }
            if (min.size() > 0) {
                return switch (min.get(0)) {
                    case 'n' -> Direction.NORTH;
                    case 's' -> Direction.SOUTH;
                    case 'e' -> Direction.EAST;
                    case 'w' -> Direction.WEST;
                    default -> Direction.NONE;
                };
            }
        }
        return Direction.NONE; // renvoie None si pacman est innacessible ou si on est sur lui
    }

    // change la direction de n'importe quel phantome donner en argument en
    // s'alignant avec les tunnels
    public void changeDirection(Direction dir, Ghost ghost) {
        if (ghost.direction != dir) {
            if (ghost.direction == Direction.WEST || ghost.direction == Direction.EAST) {
                ghost.pos = ghost.pos.floorX(); // arrondie la coordonnee en x pur etre face au trou
            }
            if (ghost.direction == Direction.NORTH || ghost.direction == Direction.SOUTH) {
                ghost.pos = ghost.pos.floorY(); // arrondie la coordonnee en y pur etre face au trou
            }
            ghost.direction = dir; // applique la nouvelle direction au phantome donne en argument
        }

    }

}