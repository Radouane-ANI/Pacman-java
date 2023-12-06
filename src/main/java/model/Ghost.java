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
    // tableau passerBlinky de la taille de la carte qui dit si blinky est deja
    // passer par la
    // (utile pour le bactracking) :

    public static boolean[][] visiter = new boolean[config.getHeight()][config.getWidth()];
    public static List<Character> cheminCourt = new ArrayList<Character>();
    public static IntCoordinates lastPos;
    static int compteurFrameInky = 0;
    static int compteurFrameClyde = 0;
    static int nbFrame = 120;
    static boolean suitPacInky = false;
    static boolean suitPacClyde = false;

    private int skinVulnerable;
    private boolean manger;

    static IntCoordinates cibleRandomClyde = null;

    //définit toutes les cases visitables par pacman depuis son point de départ dans un tableau de tableau de boolean
    static boolean[][] caseVisitable = new boolean[config.getHeight()][config.getWidth()];
    static boolean caseVisitableFill = false;

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

    public void reset(){
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
     * @return Les coordonnées réelles du fantôme.
     */
    public boolean getManger() {
        return manger;
    }

    public void setSpeed(double s){
        speed = s;
    }

    /**
     * Change le skin du fantôme en fonction de l'état énergisé de Pac-Man.
     *
     * @return Un entier indiquant le changement de skin : 0 pour un skin normal, 1
     *         pour un skin vulnérable, 2 pour aucun changement.
     */
    public int changeSkin() {
        if (PacMan.INSTANCE.isEnergized() && skinVulnerable != 1) {
            skinVulnerable = 1; // Changement de skin requis
            return 1;
        } else if (!PacMan.INSTANCE.isEnergized() && skinVulnerable != 0) {
            skinVulnerable = 0; // Changement de skin requis
            return 0;
        }
        return 2; // ne rien faire
    }

    /**
     * Actualise la prochaine direction des fantomes selon leur IA
     */
    public void iaBlinky() {    //déplacements de blinky
        setSpeed(
            (pos.round().x() == PacMan.INSTANCE.getPos().round().x() || 
            pos.round().y() == PacMan.INSTANCE.getPos().round().y() &&
            Data.getDifficulty())?
            4 : 2.5);
        if (anciennePos == null){
            anciennePos = pos.round();
        }
        if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null){
            changeDirection(newDirection);
        }else{ 
            Direction path = prochainePositionBlinky();
            newDirection = path;
            anciennePos = pos.round();
            changeDirection(path);
        }
    }

    public void iaPinky() { // déplacements de pinky
        setSpeed(2);
        if (anciennePos == null){
            anciennePos = pos.round();
        }
        if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null){
            changeDirection(newDirection);
        }else{ 
            Direction path = prochainePositionPinky();
            newDirection = path;
            anciennePos = pos.round();
            changeDirection(path);
        }
    }
    

    public void iaInky() { // déplacements de inky
        setSpeed(2);
        compteurFrameInky++;
        compteurFrameInky = (compteurFrameInky == nbFrame ? 0 : compteurFrameInky);
        suitPacInky = (
            compteurFrameInky == 0 && (suitPacInky ? new Random().nextInt(2) == 0 : true) ?
            !suitPacInky : suitPacInky
        );
        if (anciennePos == null){
            anciennePos = pos.round();
        }
        if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null){
            changeDirection(newDirection);
        }else{ 
            Direction path = prochainePositionInky();
            newDirection = path;
            anciennePos = pos.round();
            changeDirection(path);
        }
    }
    

    public void iaClyde(){
        setSpeed(2);
        if (!caseVisitableFill){
            recCaseVisitable(pos.round());
            caseVisitableFill = true;
        }
        compteurFrameClyde++;
        compteurFrameClyde = (compteurFrameClyde == nbFrame ? 0 : compteurFrameClyde);
        if ((!suitPacClyde && CLYDE.getPos().round() == cibleRandomClyde) || cibleRandomClyde == null){
            cibleRandomClyde = trouverPointAleatoire();
        }
        if (compteurFrameClyde == 0){
            suitPacClyde = (new Random().nextInt(2) == 0 ? !suitPacClyde : suitPacClyde);
            if (!suitPacClyde){
                cibleRandomClyde = trouverPointAleatoire();
            }
        }
        if (anciennePos == null){
            anciennePos = pos.round();
        }
        if (anciennePos.equals(pos.round()) && newDirection != Direction.NONE && newDirection != null){
            changeDirection(newDirection);
        }else{ 
            Direction path = prochainePositionClyde();
            newDirection = path;
            anciennePos = pos.round();
            changeDirection(path);
        }
    }

    public Direction cheminVersCible(IntCoordinates cible){
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
            if ((i != directionOppose || c.isUCell()) &&
                !config.getCell(depart).murEnFaceDirection(intToDirection(i))){
                if (solution == -1){
                    solution = i;
                }else{
                    if (directions[i] < directions[solution] || 
                    (directions[i] == directions[solution] && i == directionActuelle)){
                        solution = i;
                    }
                }
            }
        }
        return intToDirection(solution);
    }

    public static Direction intToDirection(int n){
        switch (n) {
            case 0 : return Direction.NORTH;
            case 1 : return Direction.WEST;
            case 2 : return Direction.SOUTH;
            case 3 : return Direction.EAST;
            default : return Direction.NONE;
        }
    }

    public static int directionToInt(Direction d){
        switch (d) {
            case NORTH : return 0;
            case WEST : return 1;
            case SOUTH : return 2;
            case EAST : return 3;
            default : return -1;
        }
    }

    public static IntCoordinates intToMoveCoordinates(int i){
        switch (i) {
            case 0 : return IntCoordinates.NORTH_UNIT;
            case 1 : return IntCoordinates.WEST_UNIT;
            case 2 : return IntCoordinates.SOUTH_UNIT;
            case 3 : return IntCoordinates.EAST_UNIT;
            default : return new IntCoordinates(0,0);
        }

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
        if (y > 0 && !config.getCell(p).northWall() && visiter[y-1][x] == false) {
            possible.add('n');
        }
        if (y < visiter.length - 1 && !config.getCell(p).southWall() && visiter[y+1][x] == false) {
            possible.add('s');
        }
        if (x < visiter[0].length - 1 && !config.getCell(p).eastWall() && visiter[y][x+1] == false) {
            possible.add('e');
        }
        if (x > 0 && !config.getCell(p).westWall() && visiter[y][x-1] == false) {
            possible.add('w');
        }
        return possible; // renvoie la liste de toute les directions des intersection
    }

    public boolean possible(IntCoordinates a){
        return config.getCell(a).isClosed();
    }

    /**
    /**
     * Determine le chemin le plus court entre Blinky et PacMan
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionBlinky() {
        cheminCourt.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        return BLINKY.cheminVersCible(PacMan.INSTANCE.getPos().round());
    }

    /**
     * Determine le chemin le plus court entre Pinky et la premiere intersection/mur que pointe la direction de PacMan
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionPinky() {
        cheminCourt.clear(); // vide le tableau pour ne pas laisser le chemin d'un position enteriere
        IntCoordinates posP = PacMan.INSTANCE.getPos().round();
        IntCoordinates cible = (calculDistance(PINKY.getPos().round(), posP) <= 10 ? posP : predictionNextMove(PacMan.INSTANCE));
        return PINKY.cheminVersCible(cible);
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
        return INKY.cheminVersCible(cible);
    }

    /**
     * Determine le chemin le plus court entre Clyde et PacMan ou un point aléatoire dans le labyrinthe
     * @return la prochaine direction a suivre selon cheminCourt
     */
    public Direction prochainePositionClyde() {
        recCaseVisitable(PacMan.INSTANCE.getPos().round());
        IntCoordinates cible = (suitPacClyde ? PacMan.INSTANCE.getPos().round() : cibleRandomClyde);
        return CLYDE.cheminVersCible(cible);
    }

    /** 
     * Change la direction du fantôme, s'alignant avec les tunnels si le fantôme est
     * à un angle.
     *
     * @param dir   La nouvelle direction pour le fantôme.
     * @param ghost Le fantôme dont la direction est modifiée.
     */
    public void changeDirection(Direction dir) {
        double vitesse = 0.03*2; //0.016 est la distance parcourue entre chaque frame par les fantomes a vitesse 1
        if (direction != dir) {
            if ((direction == Direction.WEST || direction == Direction.EAST)
                    && (dir != Direction.WEST && dir != Direction.EAST)) {
                if (Math.abs(pos.x() - (int) pos.x()) < vitesse*getSpeed()) {  // attend le dernier moment pour teleporter le fantomer des les angles
                    pos = pos.floorX(); // arrondie la coordonnee en x pour etre face au trou
                } else {
                    dir = direction;
                }
            }
            if ((direction == Direction.NORTH || direction == Direction.SOUTH)
                    && (dir != Direction.NORTH && dir != Direction.SOUTH)) {
                if (Math.abs(pos.y() - (int) pos.y()) < vitesse*getSpeed()) { // attend le dernier moment pour teleporter le fantomer des les angles
                    pos = pos.floorY(); // arrondie la coordonnee en y pour etre face au trou
                } else {
                    dir = direction;
                }
            }
            direction = dir; // applique la nouvelle direction au phantome donne en argument
        }
    }

    public static void fuite(){
        for (Ghost ghost : Ghost.values()){
            if (ghost.manger){
                ghost.changeDirection(Direction.fromChar(ghost.retour()));
            }else{
                ghost.setSpeed(3);
                if (ghost.anciennePos == null){
                    ghost.anciennePos = ghost.pos.round();
                }
                if(
                    ghost.anciennePos.equals(ghost.pos.round()) && 
                    ghost.newDirection != Direction.NONE && 
                    ghost.newDirection != null
                ){
                    ghost.changeDirection(ghost.newDirection);

                }else{
                    Direction path = ghost.prochainePositionFuite();
                    System.out.println(path+" "+ghost.pos.round());
                    ghost.newDirection = path;
                    ghost.anciennePos = ghost.pos.round();
                    ghost.changeDirection(path);
                }
            }
            
        }
    }

    public Direction prochainePositionFuite(){
        Random rd = new Random();
        ArrayList<Direction> mouvement = new ArrayList<>();
        int directionActuelle = directionToInt(direction);
        int directionOppose = directionActuelle + (directionActuelle < 2 ? 2 : -2);
        Cell c = config.getCell(pos.round());
        for (int i = 0 ; i < 4 ; i++){
            if ((i != directionOppose || (i == directionOppose && c.isUCell())) && !c.murEnFaceDirection(intToDirection(i))){
                mouvement.add(intToDirection(i));
            }
        }
        System.out.println(mouvement.toString());
        if (mouvement.size() == 0){
            return Direction.NONE;
        }else{
            return mouvement.get(rd.nextInt(mouvement.size()));
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
        if (c.isIntersection() || direction == Direction.NONE){
            return currentGuess;
        }else{
            if (direction == Direction.EAST){
                while (!c.isIntersection() && !c.eastWall() && currentGuess.x() >= config.getWidth()-1){
                    currentGuess.plus(IntCoordinates.EAST_UNIT);
                    c = config.getCell(currentGuess);
                }
            }else if (direction == Direction.WEST){
                while (!c.isIntersection() && !c.westWall() && currentGuess.x() <= 0){
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
    public static IntCoordinates trouverPointAleatoire() {
        ArrayList<IntCoordinates> coordonneesTrue = new ArrayList<>();
        int n = caseVisitable.length;
        int m = caseVisitable[0].length;
        // Parcours le tableau et ajoute les coordonnées (x, y) des cases 'true' avec une distance minimale
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (caseVisitable[i][j]) {
                    coordonneesTrue.add(new IntCoordinates(i,j));
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
     * @param a : point a sur le labyrinthe
     * @param b : point b sur le labyrinthe
     * @return la distance euclidienne entre les points a et b 
     */
    public static int calculDistance(IntCoordinates a, IntCoordinates b){
        return (a.x() - b.x())*(a.x() - b.x()) + (a.y() - b.y())*(a.y() - b.y());
    }

    

}