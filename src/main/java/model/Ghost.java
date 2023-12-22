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
import datagame.*;
import javafx.geometry.NodeOrientation;
import javafx.scene.effect.Blend;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public enum Ghost implements Critter {

    // TODO: implement a different AI for each ghost, according to the description
    // in Wikipedia's page
    BLINKY, INKY, PINKY, CLYDE;

    static MazeConfig config = MazeConfig.makeExample1();
    private RealCoordinates pos;
    private Direction direction = Direction.NONE;
    private Direction newDirection = Direction.NONE;
    private IntCoordinates anciennePos;
    private double speed = 2;
    private boolean isVisible = true;
    // tableau passerBlinky de la taille de la carte qui dit si blinky est deja
    // passer par la
    // (utile pour le bactracking) :

    public static boolean[][] visiter = new boolean[config.getHeight()][config.getWidth()];
    public static IntCoordinates lastPos;
    static int compteurFrameInky = 0;
    static int compteurFrameClyde = 0;
    static int nbFrame = 120;
    static boolean suitPacInky = false;
    static boolean suitPacClyde = false;

    private int skinVulnerable;
    private boolean manger = false;

    static IntCoordinates cibleRandomClyde = null;
    
    // image des fantomes vulnerables
    private final Image spritev1 = new Image("vulnerable_ghost.png", Data.getScale() * 0.82, Data.getScale() * 0.82, true, true); // bleu
    private final Image spritev2 = new Image("vulnerable_ghost1.png", Data.getScale() * 0.82, Data.getScale() * 0.82, true, true); // blanc
    private Image currentSpritev = spritev1;
    // timer pour qu'il puisse clignoter
    private Timer blinkTimer;
    // fonction qui gere le clignottement des fantomes
    /**
     * fonction qui gere le clignottement des fantomes
     * @param image : image du fantome
     */
    public  void startBlinking(ImageView image) {
        if (blinkTimer != null) {
            blinkTimer.cancel();
        }
        blinkTimer = new Timer();
        blinkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                
                if (currentSpritev == spritev1) {
                    currentSpritev = spritev2;
                    image.setImage(currentSpritev);
                } else {
                    currentSpritev = spritev1;
                    image.setImage(currentSpritev);
                }
            }
        }, 6000, 200); // Change sprite every 500 milliseconds
    }
    /**
     * fonction qui arrete le clignottement des fantomes
     * @param image : image du fantome
     */
    public void stopBlinking(ImageView image) {
        if (blinkTimer != null) {
            blinkTimer.cancel();
            blinkTimer = null;
        }
        currentSpritev = spritev1; // Reset to default sprite
        if(image != null)
        image.setImage(spritev1);
    }


    public Image getCurrentSprite() {
        return currentSpritev;
    }

    

    // définit toutes les cases visitables depuis le point curretnCase
    // dans un tableau de tableau de boolean
    static boolean[][] caseVisitable = new boolean[config.getHeight()][config.getWidth()];
    static boolean caseVisitableFill = false;

    static void recCaseVisitable(IntCoordinates currentCase) {
        if (!caseVisitable[currentCase.y()][currentCase.x()]) {
            caseVisitable[currentCase.y()][currentCase.x()] = true;
            if (!config.getCell(currentCase).iseastWall() && currentCase.x() < config.getWidth() - 1) {
                recCaseVisitable(currentCase.plus(IntCoordinates.EAST_UNIT));
            } else if (!config.getCell(currentCase).iswestWall() && currentCase.x() >= 0) {
                recCaseVisitable(currentCase.plus(IntCoordinates.WEST_UNIT));
            } else if (!config.getCell(currentCase).isnorthWall() && currentCase.y() >= 0) {
                recCaseVisitable(currentCase.plus(IntCoordinates.NORTH_UNIT));
            } else if (!config.getCell(currentCase).issouthWall() && currentCase.y() < config.getHeight() - 1) {
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

    public void reset() {
        this.newDirection = Direction.NONE;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public double getSpeed() { // vitesse des fantomes
        return speed;
    }

    /**
     * Définit si le fantôme est en mode "manger" ou non.
     *
     * @param manger True si le fantôme est en mode "manger", sinon False.
     */
    public void setManger(boolean manger) {
        this.manger = manger;
    }

    /**
     * Obtient l'étàt actuelle du fantôme manger ou non.
     *
     * @return True si le fantôme est en mode "manger", sinon False.
     */
    public boolean getManger() {
        return manger;
    }

    public void setSpeed(double s) {
        speed = s;
    }

    public int getSkinVulnerable() {
        return skinVulnerable;
    }
    /**
     * Détermine si le fantome est en état de fuite
     * 
     * @return : true si en état de fuite, false sinon
     */
    public boolean enFuite(){return Data.ghostFuite.contains(this);}

    /**
     * Change le skin du fantôme en fonction de l'état énergisé de Pac-Man.
     *
     * @return Un entier indiquant le changement de skin : 0 pour un skin normal, 1
     *         pour un skin vulnérable, 2 pour aucun changement.
     */
    public int changeSkin() {
        if (PacMan.INSTANCE.isEnergized() && skinVulnerable != 1 && enFuite()) {
            skinVulnerable = 1; // Changement de skin requis
            return 1;
        } else if ((!PacMan.INSTANCE.isEnergized() && skinVulnerable != 0) || (PacMan.INSTANCE.isEnergized() && !enFuite() && !manger && skinVulnerable != 0)) {
            skinVulnerable = 0; // Changement de skin requis
            return 0;
        }
        return 2; // ne rien faire
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;

    }

    /**
     * Make all ghosts invisible
     * 
     */
    public static void makeGhostsInvisible() {
        for (Ghost ghost : Ghost.values()) {
            ghost.setVisible(false);

        }
    }

    /**
     * Make all ghosts visible
     * 
     */
    public static void makeGhostVisibleAgain() {
        for (Ghost ghost : Ghost.values()) {
            ghost.setVisible(true);

        }
    }

    /**
     * Actualise la prochaine direction des fantomes selon leur IA
     */
    public void iaBlinky() { // déplacements de blinky
        if (manger){
            changeDirection(Direction.fromChar(retour()));
            return;
        }
        setSpeed(enFuite() ? 3 : (
            (pos.round().x() == PacMan.INSTANCE.getPos().round().x() ||
                        pos.round().y() == PacMan.INSTANCE.getPos().round().y() &&
                                Data.getDifficulty()) ? 4 : 2.5));
        if (anciennePos == null) {anciennePos = pos.round();}
        if (isVisible){
            if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null) {
                    changeDirection(newDirection);
            } else {
                Direction path = (enFuite()? prochainePositionRandom() : cheminVersCible(PacMan.INSTANCE.getPos().round())); 
                newDirection = path;
                anciennePos = pos.round();
                changeDirection(path);
            }
        }
        
    }

    public void iaPinky() { // déplacements de pinky
        if (manger){
            changeDirection(Direction.fromChar(retour()));
            return;
        }
        setSpeed(enFuite() ? 3 : 2);
        if (anciennePos == null) {anciennePos = pos.round();}
        if (isVisible){
            if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null) {
                    changeDirection(newDirection);
            } else {
                Direction path = (enFuite()? prochainePositionRandom() : prochainePositionPinky()); 
                newDirection = path;
                anciennePos = pos.round();
                changeDirection(path);
            }
        }
        
    }

    public void iaInky() { // déplacements de Inky
        if (manger){
            changeDirection(Direction.fromChar(retour()));
            return;
        }
        setSpeed(enFuite() ? 3 : 2);
        compteurFrameInky++;
        compteurFrameInky = (compteurFrameInky == nbFrame ? 0 : compteurFrameInky);
        suitPacInky = (compteurFrameInky == 0 && (suitPacInky ? new Random().nextInt(2) == 0 : true) ? !suitPacInky
                : suitPacInky);
        if (anciennePos == null) {anciennePos = pos.round();}
        if (isVisible){
            if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null) {
                    changeDirection(newDirection);
            } else {
                Direction path = (enFuite() ? prochainePositionRandom() : 
                    (suitPacInky? cheminVersCible(PacMan.INSTANCE.getPos().round()) : prochainePositionOppose(PacMan.INSTANCE.getPos().round()))); 
                newDirection = path;
                anciennePos = pos.round();
                changeDirection(path);
            }
        }
        
    }

    public void iaClyde() { // déplacements de Clyde
        if (manger){
            changeDirection(Direction.fromChar(retour()));
            return;
        }
        setSpeed(enFuite() ? 3 : 2);
        if (!caseVisitableFill) {
            recCaseVisitable(pos.round());
            caseVisitableFill = true;
        }
        compteurFrameClyde++;
        compteurFrameClyde = (compteurFrameClyde == nbFrame ? 0 : compteurFrameClyde);
        if ((!suitPacClyde && CLYDE.getPos().round() == cibleRandomClyde) || cibleRandomClyde == null) {
            cibleRandomClyde = trouverPointAleatoire();
        }
        if (compteurFrameClyde == 0) {
            suitPacClyde = (new Random().nextInt(2) == 0 ? !suitPacClyde : suitPacClyde);
            if (!suitPacClyde) {
                cibleRandomClyde = trouverPointAleatoire();
            }
        }
        if (anciennePos == null) {anciennePos = pos.round();}
        if (isVisible){
            if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null) {
                changeDirection(newDirection);
            } else {
                Direction path = (enFuite() || suitPacClyde? prochainePositionRandom() : cheminVersCible(PacMan.INSTANCE.getPos().round())); 
                newDirection = path;
                anciennePos = pos.round();
                changeDirection(path);
            }
        }
        
    }

    /**
     * Détermine la prochaine direction la plus pratique a prendre selon les possibilités depuis la position du fantomes
     * @param cible : case que le fantome a pour cible
     * @return : la prochaie Direction a prendre
     */
    public Direction cheminVersCible(IntCoordinates cible) {
        IntCoordinates depart = this.pos.round();
        Cell c = config.getCell(pos.round());
        int[] directions = {
                calculDistance(depart.plus(IntCoordinates.NORTH_UNIT), cible),
                calculDistance(depart.plus(IntCoordinates.WEST_UNIT), cible),
                calculDistance(depart.plus(IntCoordinates.SOUTH_UNIT), cible),
                calculDistance(depart.plus(IntCoordinates.EAST_UNIT), cible)
        };
        int directionActuelle = directionToInt(this.direction);
        int directionOppose = directionActuelle + (directionActuelle < 2 ? 2 : -2);
        int solution = -1;
        for (int i = 0 ; i < directions.length ; i++){
            if ((i != directionOppose || c.isUCell()) && estPossible(intToDirection(i))){
                if (solution == -1){
                    solution = i;
                } else {
                    if (directions[i] < directions[solution] ||
                            (directions[i] == directions[solution] && i == directionActuelle)) {
                        solution = i;
                    }
                }
            }
        }
        return intToDirection(solution);
    }

    /**
     * Affecte un int, de 0 a 3 a une direction
     * @param n : int
     * @return : direction selon n (0 -> North, 1 -> West, 2 -> South, 3 -> East, le reste -> None)
     */
    public static Direction intToDirection(int n) {
        switch (n) {
            case 0:
                return Direction.NORTH;
            case 1:
                return Direction.WEST;
            case 2:
                return Direction.SOUTH;
            case 3:
                return Direction.EAST;
            default:
                return Direction.NONE;
        }
    }

    /**
     * Affecte une Direction a un int
     * @param d : Direction
     * @return : int selon la direction d (North -> 0, West -> 1, South -> 2, East -> 3)
     */
    public static int directionToInt(Direction d) {
        switch (d) {
            case NORTH:
                return 0;
            case WEST:
                return 1;
            case SOUTH:
                return 2;
            case EAST:
                return 3;
            default:
                return -1;
        }
    }

    /**
     * Affecte un int, de 0 a 3 a une direction en Coordonnées
     * @param n : int
     * @return : coordonnées selon n (0 -> NORTH_UNIT, 1 -> WEST_UNIT, 2 -> SOUTH_UNIT, 3 -> EAST_UNIT, le reste -> IntCoordinates(0, 0))
     */
    public static IntCoordinates intToMoveCoordinates(int i) {
        switch (i) {
            case 0:
                return IntCoordinates.NORTH_UNIT;
            case 1:
                return IntCoordinates.WEST_UNIT;
            case 2:
                return IntCoordinates.SOUTH_UNIT;
            case 3:
                return IntCoordinates.EAST_UNIT;
            default:
                return new IntCoordinates(0, 0);
        }

    }

    /**
     * Determine le chemin le plus court entre Pinky et la premiere intersection/mur
     * que pointe la direction de PacMan
     * 
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionPinky() {
        IntCoordinates predict = predictionNextMove(PacMan.INSTANCE);
        IntCoordinates cible = (calculDistance(PINKY.getPos().round(), PacMan.INSTANCE.getPos().round()) <= 10 ? PacMan.INSTANCE.getPos().round() : predict);
        return cheminVersCible(cible);
    }

    /**
     * Change la direction du fantôme, s'alignant avec les tunnels si le fantôme est
     * à un angle.
     *
     * @param dir   : La nouvelle direction pour le fantôme.
     */
    public void changeDirection(Direction dir) {
        double vitesse = 0.03 * 2; // 0.016 est la distance parcourue entre chaque frame par les fantomes a vitesse
                                   // 1
        if (direction != dir) {
            if ((direction == Direction.WEST || direction == Direction.EAST)
                    && (dir != Direction.WEST && dir != Direction.EAST)) {
                if (Math.abs(pos.x() - (int) pos.x()) < vitesse * getSpeed()) { // attend le dernier moment pour
                                                                                // teleporter le fantomer des les angles
                    pos = pos.floorX(); // arrondie la coordonnee en x pour etre face au trou
                } else {
                    dir = direction;
                }
            }
            if ((direction == Direction.NORTH || direction == Direction.SOUTH)
                    && (dir != Direction.NORTH && dir != Direction.SOUTH)) {
                if (Math.abs(pos.y() - (int) pos.y()) < vitesse * getSpeed()) { // attend le dernier moment pour
                                                                                // teleporter le fantomer des les angles
                    pos = pos.floorY(); // arrondie la coordonnee en y pour etre face au trou
                } else {
                    dir = direction;
                }
            }
            direction = dir; // applique la nouvelle direction au phantome donne en argument
        }
    }

    /**
     * Retourne la prochaine direction du fantome aléatoirement selon ses possibilités
     * @return : prochaine direction a suivre
     */
    public Direction prochainePositionRandom() {
        Random rd = new Random();
        ArrayList<Direction> mouvement = new ArrayList<>();
        int directionActuelle = directionToInt(direction);
        int directionOppose = directionActuelle + (directionActuelle < 2 ? 2 : -2);
        Cell c = config.getCell(pos.round());
        for (int i = 0 ; i < 4 ; i++){
            if ((i != directionOppose || (i == directionOppose && c.isUCell())) && estPossible(intToDirection(i))){
                mouvement.add(intToDirection(i));
            }
        }
        
        if (mouvement.size() == 0){
            return Direction.NONE;
        } else {
            return mouvement.get(rd.nextInt(mouvement.size()));
        }
    }

    /**
     * Détermine la prochaine direction la plus pratique a prendre selon les possibilités depuis la position du fantomes
     * @param cible : case que le fantome a pour cible
     * @return : la prochaie Direction a prendre
     */
    public Direction prochainePositionOppose(IntCoordinates cible) {
        IntCoordinates depart = this.pos.round();
        Cell c = config.getCell(pos.round());
        int[] directions = {
                calculDistance(depart.plus(IntCoordinates.NORTH_UNIT), cible),
                calculDistance(depart.plus(IntCoordinates.WEST_UNIT), cible),
                calculDistance(depart.plus(IntCoordinates.SOUTH_UNIT), cible),
                calculDistance(depart.plus(IntCoordinates.EAST_UNIT), cible)
        };
        int directionActuelle = directionToInt(this.direction);
        int directionOppose = directionActuelle + (directionActuelle < 2 ? 2 : -2);
        int solution = -1;
        for (int i = 0 ; i < directions.length ; i++){
            if ((i != directionOppose || c.isUCell()) && estPossible(intToDirection(i))){
                if (solution == -1){
                    solution = i;
                } else {
                    if (directions[i] > directions[solution] ||
                            (directions[i] == directions[solution] && i == directionActuelle)) {
                        solution = i;
                    }
                }
            }
        }
        return intToDirection(solution);
    }

    /**
     * Détermine la prochaine position de PacMan en en regardant ou pointe sa
     * direction
     * 
     * @param p : PacMan
     * @return les coordonnées de la première intersection/mur que rencontrerai
     *         PacMan si il continuait dans sa directions actuelles
     */
    public static IntCoordinates predictionNextMove(PacMan p) {// prédit la prochaine position de pacman
        Direction direction = (p.getDirection());
        IntCoordinates currentGuess = p.getPos().round();
        Cell c = config.getCell(currentGuess);
        if (c.isIntersection() || direction == Direction.NONE) {
            return currentGuess;
        } else {
            if (direction == Direction.EAST) {
                while (!c.isIntersection() && !c.iseastWall() && currentGuess.x() < config.getWidth() - 1) {
                    currentGuess = currentGuess.plus(IntCoordinates.EAST_UNIT);
                    c = config.getCell(currentGuess);
                }
            } else if (direction == Direction.WEST) {
                while (!c.isIntersection() && !c.iswestWall() && currentGuess.x() > 0) {
                    currentGuess = currentGuess.plus(IntCoordinates.WEST_UNIT);
                    c = config.getCell(currentGuess);
                }
            } else if (direction == Direction.NORTH) {
                while (!c.isIntersection() && !c.isnorthWall() && currentGuess.y() > 0) {
                    currentGuess = currentGuess.plus(IntCoordinates.NORTH_UNIT);
                    c = config.getCell(currentGuess);
                }
            } else if (direction == Direction.SOUTH) {
                while (!c.isIntersection() && !c.issouthWall() && currentGuess.y() < config.getHeight() - 1) {
                    currentGuess = currentGuess.plus(IntCoordinates.SOUTH_UNIT);
                    c = config.getCell(currentGuess);
                }
            }
            return currentGuess;
        }
    }

    /**
     * Détermine la direction à prendre pour retourner à la position de départ du
     * fantôme,
     *
     * @return La direction ('n', 'e', 's' ou 'w') vers la position de départ du
     *         fantôme.
     */
    public Character retour() {
        char r = 'n';
        IntCoordinates posDepart = null;

        switch (this) {
            case BLINKY:
                posDepart = config.getBlinkyPos();
                break;
            case CLYDE:
                posDepart = config.getClydePos();
                break;
            case INKY:
                posDepart = config.getInkyPos();
                break;
            case PINKY:
                posDepart = config.getPinkyPos();
                break;
        }

        if (posDepart != null) {
            if (this.pos.round().equals(posDepart)) {
                manger = false;
                speed = 2;
                skinVulnerable = 2;
                setDirection(Direction.NONE);
                reset();
            } else {
                r = AStar.findPath(config, this.pos.cast(), posDepart);
                speed = 8;
            }
        }

        return r;
    }

    /**
     * Définit la vitesse du fantôme.
     *
     * @param speed La nouvelle vitesse du fantôme.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Détermine le point le plus éloigné du point x,y
     * 
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
     * 
     * @param x : position x sur dans le labyrinthe (ordonnée)
     * @param y : position y sur dans le labyrinthe (abcisse)
     * @return les coordonnées d'un point aléatoirement mais assez éloigné du point
     *         x,y
     */
    public static IntCoordinates trouverPointAleatoire() {
        ArrayList<IntCoordinates> coordonneesTrue = new ArrayList<>();
        int n = caseVisitable.length;
        int m = caseVisitable[0].length;
        // Parcours le tableau et ajoute les coordonnées (x, y) des cases 'true' avec
        // une distance minimale
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (caseVisitable[i][j]) {
                    coordonneesTrue.add(new IntCoordinates(i, j));
                }
            }
        }
        // Génère un index aléatoire
        Random random = new Random();
        int indexAleatoire = random.nextInt(coordonneesTrue.size());
        // Récupère les coordonnées (x, y) de la case correspondante
        return coordonneesTrue.get(indexAleatoire);
    }

    /**
     * Calcul la distance euclidienne entre deux points a et b
     * 
     * @param a : point a sur le labyrinthe
     * @param b : point b sur le labyrinthe
     * @return la distance euclidienne entre les points a et b
     */
    public static int calculDistance(IntCoordinates a, IntCoordinates b) {
        return (a.x() - b.x()) * (a.x() - b.x()) + (a.y() - b.y()) * (a.y() - b.y());
    }
    public boolean estDansSpawn(MazeConfig Lab){
        IntCoordinates p1 = Lab.getBlinkyPos();
        IntCoordinates p2 = Lab.getClydePos();
        IntCoordinates p3 = Lab.getInkyPos();
        IntCoordinates p4 = Lab.getPinkyPos();
        int xMin = Math.min(Math.min(p1.x(), p2.x()), Math.min(p3.x(), p4.x()));
        int yMin = Math.min(Math.min(p1.y(), p2.y()), Math.min(p3.y(), p4.y()));
        int xMax = Math.max(Math.max(p1.x(), p2.x()), Math.max(p3.x(), p4.x()));
        int yMax = Math.max(Math.max(p1.y(), p2.y()), Math.max(p3.y(), p4.y()));
        
        return pos.round().x() >= xMin && pos.round().x() <= xMax && pos.round().y() >= yMin && pos.round().y() <= yMax;
    }

    public boolean estPossible(Direction direction){
        var Gpos = this.pos.round();
        switch(direction){
            case NORTH:
                
                return !config.getCell(Gpos).isnorthWall() || (config.getCell(Gpos).isnorthWhite() && (manger || estDansSpawn(config))) ;
            case SOUTH:
                
                return !config.getCell(Gpos).issouthWall() || (config.getCell(Gpos).issouthWhite() && (manger || estDansSpawn(config))) ;
            case EAST:
                
                return !config.getCell(Gpos).iseastWall() || (config.getCell(Gpos).iseastWhite() && (manger || estDansSpawn(config))) ;
            case WEST:
               
                return !config.getCell(Gpos).iswestWall() || (config.getCell(Gpos).iswestWhite() && (manger || estDansSpawn(config))) ;
            default: return false;
        }
    }

}
