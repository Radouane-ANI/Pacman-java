package model;

import config.MazeConfig;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import config.MazeConfig;
import config.Cell;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import gui.PacmanController;
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

    public static boolean[][] visiter = new boolean[config.getHeight()][config.getWidth()];
    public static List<Character> cheminCourt = new ArrayList<Character>();
    static int compteurFrameInky = 0;
    static int compteurFrameClyde = 0;
    static int nbFrame = 120;
    static boolean suitPacInky = false;
    static boolean suitPacClyde = false;

    static IntCoordinates cibleRandomClyde = null;

    //définit toutes les cases visitables par pacman depuis son point de départ dans un tableau de tableau de boolean
    static boolean[][] caseVisitable = new boolean[config.getHeight()][config.getWidth()];

    static void recCaseVisitable(IntCoordinates currentCase){
        if (!caseVisitable[currentCase.y()][currentCase.x()]){
            caseVisitable[currentCase.y()][currentCase.x()] = true;
            if (!config.getCell(currentCase).eastWall() && currentCase.x() < config.getWidth()-1){
                recCaseVisitable(currentCase.plus(IntCoordinates.EAST_UNIT));
            }else if (!config.getCell(currentCase).westWall() && currentCase.x() >= 0){
                recCaseVisitable(currentCase.plus(IntCoordinates.WEST_UNIT));
            }else if (!config.getCell(currentCase).northWall() && currentCase.y() >= 0){
                recCaseVisitable(currentCase.plus(IntCoordinates.NORTH_UNIT));
            }else if (!config.getCell(currentCase).southWall() && currentCase.y() < config.getHeight()-1){
                recCaseVisitable(currentCase.plus(IntCoordinates.SOUTH_UNIT));
            }
        }
    }

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

    /**
     * Actualise la prochaine direction des fantomes selon leur IA
     */
    public void iaBlinky() {
        //passerBlinky = new boolean[config.getHeight()][config.getWidth()];
        if (possible((int) BLINKY.pos.x(), (int) BLINKY.pos.y()).size() > 0 || BLINKY.direction == Direction.NONE) {
            Direction path = prochainePositionBlinky();
            changeDirection(path, BLINKY);
        }
    }

    public void iaPinky() { // déplacements de pinky
        if (possible((int) PINKY.pos.x(), (int) PINKY.pos.y()).size() > 0 || PINKY.direction == Direction.NONE) {
            Direction path = prochainePositionPinky();
            changeDirection(path, PINKY);
        }
    }

    public void iaInky() { // déplacements de inky
        compteurFrameInky++;
        compteurFrameInky = (compteurFrameInky == nbFrame ? 0 : compteurFrameInky);
        suitPacInky = (
            compteurFrameInky == 0 && (suitPacInky ? new Random().nextInt(2) == 0 : true) ?
            !suitPacInky : suitPacInky
        );
        if ((possible((int) INKY.pos.x(), (int) INKY.pos.y()).size() > 0 || INKY.direction == Direction.NONE)) {
            Direction path = prochainePositionInky();
            changeDirection(path, INKY);
        }
    }

    public void iaClyde(){
        compteurFrameClyde++;
        compteurFrameClyde = (compteurFrameClyde == nbFrame ? 0 : compteurFrameClyde);
        if (compteurFrameClyde == 0){
            suitPacClyde = (new Random().nextInt(2) == 0 ? !suitPacClyde : suitPacClyde);
            if (!suitPacClyde){
                cibleRandomClyde = trouverPointAleatoire(CLYDE.getPos().round().x(),CLYDE.getPos().round().y());
            }
        }
        if ((possible((int) CLYDE.pos.x(), (int) CLYDE.pos.y()).size() > 0 || CLYDE.direction == Direction.NONE)) {
            Direction path = prochainePositionClyde();
            changeDirection(path, CLYDE);
        }
    }


    /**
     * Trouve le chemin le plus court dans le labyrinthe, entre un point A et un point B et le retourne dans la variable cheminCourt
     * @param currentPos : point A, soit le point de départ a partir duquel on recherche
     * @param cible : point B, soit le point d'arrivée qu'on cherche a atteindre de la plus courte des manieres
     * @param chemin : le chemin actuel qui est en train d'etre determiné et comparé a cheminCourt
     */
    public void cheminVersPacman(IntCoordinates currentPos, IntCoordinates cible, ArrayList<Character> chemin) {
        int x = currentPos.x();
        int y = currentPos.y();
        visiter[x][y] = true;

        if (currentPos.equals(cible)){
            cheminCourt = new ArrayList<>(chemin);
            visiter[x][y] = false;
            return;
        }

        if (chemin.size() < cheminCourt.size() || cheminCourt.size() == 0){
            for (Character character : possible(x, y)) { // essayer toutes les positions possible depuis cette position
            if (character == 'n' && visiter[x][y - 1] == false) {
                chemin.add('n');
                cheminVersPacman(new IntCoordinates(x, y-1), cible, chemin);
            }
            if (character == 'e' && visiter[x + 1][y] == false) {
                chemin.add('e');
                cheminVersPacman(new IntCoordinates(x + 1, y), cible, chemin);
            }
            if (character == 's' && visiter[x][y + 1] == false) {
                chemin.add('s');
                cheminVersPacman(new IntCoordinates(x, y + 1), cible, chemin);
            }
            if (character == 'w' && visiter[x - 1][y] == false) {
                chemin.add('w');
                cheminVersPacman(new IntCoordinates(x - 1, y), cible, chemin);
            }
            if (chemin.size() > 0) { // quand on a explore la position la retire
                chemin.remove(chemin.size() - 1);
                }
            }

        }
        visiter[x][y] = false;
    }

    /**
     * Détermine toutes les directions possibles a prendre a partir d'un point x,y
     * @param x : position x sur dans le labyrinthe (ordonnée)
     * @param y : position y sur dans le labyrinthe (abcisse)
     * @return la liste des initiales des directions possibles ('n', 's', 'w' ou 'e')
     */
    public List<Character> possible(int x, int y) {
        List<Character> possible = new ArrayList<Character>();
        IntCoordinates p = new IntCoordinates(x, y);
        // verifie que l'on ne depasse pas du tableau, l'absence de mur et si on est deja passer
        if (y > 0 && !config.getCell(p).northWall() && visiter[x][y - 1] == false) {
            possible.add('n');
        }
        if (y < visiter.length - 1 && !config.getCell(p).southWall() && visiter[x][y + 1] == false) {
            possible.add('s');
        }
        if (x < visiter[0].length - 1 && !config.getCell(p).eastWall() && visiter[x + 1][y] == false) {
            possible.add('e');
        }
        if (x > 0 && !config.getCell(p).westWall() && visiter[x - 1][y] == false) {
            possible.add('w');
        }
        return possible; // renvoie la liste de toute les directions des intersection
    }

    /**
     * Determine le chemin le plus court entre Blinky et PacMan
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionBlinky() {
        cheminCourt.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        cheminVersPacman(BLINKY.getPos().round(), PacMan.INSTANCE.getPos().round(), new ArrayList<Character>()); //trouve le chemin le plus court
        if (cheminCourt.size() > 0) {
            return switch (cheminCourt.get(0)) {
                case 'n' -> Direction.NORTH;
                case 's' -> Direction.SOUTH;
                case 'e' -> Direction.EAST;
                case 'w' -> Direction.WEST;
                default -> Direction.NONE;
            };
        }
        return Direction.NONE; //Renvoie none si pacman est inaccessible ou sur lui
    }

    /**
     * Determine le chemin le plus court entre Pinky et la premiere intersection/mur que pointe la direction de PacMan
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionPinky() {
        cheminCourt.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        cheminVersPacman(PINKY.getPos().round(), predictionNextMove(PacMan.INSTANCE), new ArrayList<Character>()); //trouve le chemin le plus court
        if (cheminCourt.size() > 0) {
            return switch (cheminCourt.get(0)) {
                case 'n' -> Direction.NORTH;
                case 's' -> Direction.SOUTH;
                case 'e' -> Direction.EAST;
                case 'w' -> Direction.WEST;
                default -> Direction.NONE;
            };
        }
        return Direction.NONE; // renvoie None si pacman est innacessible ou si on est sur lui
    }

    /**
     * Determine le chemin le plus court entre Inky et PacMan ou le point le plus eloigne de ce dernier
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionInky() {
        recCaseVisitable(PacMan.INSTANCE.getPos().round());
        int x = PacMan.INSTANCE.getPos().round().x();
        int y = PacMan.INSTANCE.getPos().round().y();
        IntCoordinates cible = (suitPacInky ? PacMan.INSTANCE.getPos().round() : trouverCasePlusEloignee(x, y));
        cheminCourt.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        cheminVersPacman(INKY.getPos().round(), cible, new ArrayList<Character>()); //trouve le chemin le plus court
        if (cheminCourt.size() > 0) {
            return switch (cheminCourt.get(0)) {
                case 'n' -> Direction.NORTH;
                case 's' -> Direction.SOUTH;
                case 'e' -> Direction.EAST;
                case 'w' -> Direction.WEST;
                default -> Direction.NONE;
            };
        }
        return Direction.NONE; //Renvoie none si pacman est inaccessible ou sur lui
    }

    /**
     * Determine le chemin le plus court entre Clyde et PacMan ou un point aléatoire dans le labyrinthe assez éloigné
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionClyde() {
        recCaseVisitable(PacMan.INSTANCE.getPos().round());
        IntCoordinates cible = (suitPacClyde ? PacMan.INSTANCE.getPos().round() : cibleRandomClyde);
        cheminCourt.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        cheminVersPacman(CLYDE.getPos().round(), cible, new ArrayList<Character>()); //trouve le chemin le plus court
        if (cheminCourt.size() > 0) {
            return switch (cheminCourt.get(0)) {
                case 'n' -> Direction.NORTH;
                case 's' -> Direction.SOUTH;
                case 'e' -> Direction.EAST;
                case 'w' -> Direction.WEST;
                default -> Direction.NONE;
            };
        }
        return Direction.NONE; //Renvoie none si pacman est inaccessible ou sur lui
    }

    /**
     * Change la direction du fantome entré en parametres si il ne rentre pas dans un mur
     * @param dir : direction souhaité
     * @param ghost : fantome qui doit changé de direction
     */
    public void changeDirection(Direction dir, Ghost ghost) {
        if (ghost.direction != dir) {
            if (
                (ghost.direction == Direction.WEST || ghost.direction == Direction.EAST) &&
                (dir != Direction.WEST && dir != Direction.EAST)
                ) {
                ghost.pos = ghost.pos.floorX(); // arrondie la coordonnee en x pur etre face au trou
            }
            if (
                (ghost.direction == Direction.NORTH || ghost.direction == Direction.SOUTH) &&
                (dir != Direction.NORTH && dir != Direction.SOUTH)
                ) {
                ghost.pos = ghost.pos.floorY(); // arrondie la coordonnee en y pur etre face au trou
            }
            ghost.direction = dir; // applique la nouvelle direction au phantome donne en argument
        }

    }

    /**
     * Détermine la prochaine position de PacMan en en regardant ou pointe sa direction
     * @param p : PacMan
     * @return les coordonnées de la première intersection/mur que rencontrerai PacMan si il continuait dans sa directions actuelles
     */
    public static IntCoordinates predictionNextMove(PacMan p){//prédit la prochaine position de pacman 
        Direction direction = (p.getDirection());
        IntCoordinates currentGuess = p.getPos().round();
        Cell c = config.getCell(currentGuess);
        if (c.isIntersection() || direction == Direction.NONE ){
            return currentGuess;
        }else{
            if (direction == Direction.EAST){
                while (!c.isIntersection() && !c.eastWall() && currentGuess.x() >= config.getWidth()-1){
                    System.out.println("pb ici e : "+currentGuess.x()+", "+currentGuess.y());
                    currentGuess.plus(IntCoordinates.EAST_UNIT);
                    c = config.getCell(currentGuess);
                }
            }else if (direction == Direction.WEST){
                while (!c.isIntersection() && !c.westWall() && currentGuess.x() <= 0){
                    System.out.println("pb ici w : "+currentGuess.x()+", "+currentGuess.y());
                    currentGuess.plus(IntCoordinates.WEST_UNIT);
                    c = config.getCell(currentGuess);
                }
            }else if (direction == Direction.NORTH){
                while (!c.isIntersection() && !c.northWall() && currentGuess.y() <= 0){
                    currentGuess.plus(IntCoordinates.NORTH_UNIT);
                    c = config.getCell(currentGuess);
                }
            }else if (direction == Direction.SOUTH){
                while (!c.isIntersection() && !c.southWall() && currentGuess.y() >= config.getHeight()-1){
                    currentGuess.plus(IntCoordinates.SOUTH_UNIT);
                    c = config.getCell(currentGuess);
                }
            }
            return currentGuess;
        }
    }

    /**
     * Détermine le point le plus éloigné du point x,y 
     * @param x : position x sur dans le labyrinthe (ordonnée)
     * @param y : position y sur dans le labyrinthe (abcisse)
     * @return les coordonnées du point le plus éloigné du point x,y
     */
    public static IntCoordinates trouverCasePlusEloignee(int x, int y) {
        int n = caseVisitable.length;
        int m = caseVisitable[0].length;
        int distanceMax = -1;
        IntCoordinates caseLaPlusEloignee = null;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (caseVisitable[i][j]) { // On ne considère que les cases avec la valeur true
                    int distance = (x - j) * (x - j) + (y - i) * (y - i); // Distance euclidienne
                    if (distance > distanceMax) {
                        distanceMax = distance;
                        caseLaPlusEloignee = new IntCoordinates(i, j);
                    }
                }
            }
        }

        return caseLaPlusEloignee;
    }

    /**
     * Détermine un point aléatoirement assez éloigné du point x,y 
     * @param x : position x sur dans le labyrinthe (ordonnée)
     * @param y : position y sur dans le labyrinthe (abcisse)
     * @return les coordonnées d'un point aléatoirement mais assez éloigné du point x,y
     */
    public static IntCoordinates trouverPointAleatoire(int x, int y) {
        ArrayList<IntCoordinates> coordonneesTrueAvecDistanceMin = new ArrayList<>();
        int distanceMin = config.getHeight() + config.getWidth() / 2 - 1;
        int n = caseVisitable.length;
        int m = caseVisitable[0].length;
        // Parcours le tableau et ajoute les coordonnées (x, y) des cases 'true' avec une distance minimale
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (caseVisitable[i][j]) {
                    int distance = (x - j) * (x - j) + (y - i) * (y - i);
                    if (distance >= distanceMin) {
                        coordonneesTrueAvecDistanceMin.add(new IntCoordinates(i,j));
                    }
                }
            }
        }
        // Génère un index aléatoire
        Random random = new Random();
        int indexAleatoire = random.nextInt(coordonneesTrueAvecDistanceMin.size());
        // Récupère les coordonnées (x, y) de la case correspondante
        return coordonneesTrueAvecDistanceMin.get(indexAleatoire);
    }

}