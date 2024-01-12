package model;

import geometry.IntCoordinates;
import java.util.Timer;
import java.util.TimerTask;
import static model.Ghost.config;
import config.MazeConfig;
import datagame.Data;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import model.MazeState;

/**
 * Implements Pac-Man character using singleton pattern. FIXME: check whether
 * singleton is really a good idea.
 * Implements Pac-Man character using singleton pattern. FIXME: check whether
 * singleton is really a good idea.
 */
public final class PacMan implements Critter {

    private Direction direction = Direction.NONE;
    private RealCoordinates pos;
    private boolean energized;
    private boolean reEnergized;
    private int speed = 4;
    private int skin;
    private long dernierEnergiseur;

    public long getDernierEnergiseur() {
        return dernierEnergiseur;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private RealCoordinates prevpos;
    public boolean PacmanMort;

    private PacMan() {
    }

    public static final PacMan INSTANCE = new PacMan();

    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public double getSpeed() {
        return isEnergized() ? speed + 2 : speed;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.prevpos = this.pos;
        this.direction = direction;
    }

    @Override
    public void setPos(RealCoordinates pos) {
        this.prevpos = this.pos;
        this.pos = pos;
    }

    /**
     * Fonction qui renvoie un bool si il y'a un mur en face de l'instance du PACMAN
     * 
     * @param direction direction du pacman
     * @return un bool si il y'a un mur en face de l'instance du PACMAN
     */
    public boolean estPossible(Direction direction) {
        var pacPos = PacMan.INSTANCE.getPos().round();
        switch (direction) {
            case NORTH:

                return !config.getCell(pacPos).isnorthWall() && !config.getCell(pacPos).isnorthWhite();
            case SOUTH:

                return !config.getCell(pacPos).issouthWall() && !config.getCell(pacPos).issouthWhite();
            case EAST:

                return !config.getCell(pacPos).iseastWall() && !config.getCell(pacPos).iseastWhite();
            case WEST:

                return !config.getCell(pacPos).iswestWall() && !config.getCell(pacPos).iswestWhite();
            default:
                return false;
        }
    }

    /**
     * @param gridState
     * @return retourne s'il y a un dot a l'endroit ou pacman se trouve
     */
    public boolean PacManDot(boolean[][] gridState) {
        IntCoordinates pacPos = PacMan.INSTANCE.getPos().round();
        if (!gridState[pacPos.y()][pacPos.x()]) {
            gridState[pacPos.y()][pacPos.x()] = true;
            return true;
        }
        return false;
    }

    /**
     *
     * @return whether Pac-Man just ate an energizer
     */
    public boolean isEnergized() {
        // TODO handle timeout!
        return energized;
    }

    /**
     * Définit l'état énergisé de Pac-Man et programme une tâche pour désactiver
     * l'état énergisé après 5 secondes.
     *
     * @param energized Le nouvel état énergisé.
     */
    public void setEnergized(boolean energized, int duree) {
        if (energized) {
            dernierEnergiseur = System.currentTimeMillis();
            if (this.energized) { // verifie si pacman a deja pris un energiseur
                this.reEnergized = true;
                
                for (var ghost : Ghost.values()) {
                    
                    ghost.stopBlinking();
                    ghost.startBlinking(8000);
                    
                }
            }

            for (var ghost : Ghost.values()) {
                if (!Data.ghostFuite.contains(ghost)){
                    Data.ghostFuite.add(ghost);
                }
            }

            for (Ghost ghost : Ghost.values()){
                if (!Data.ghostFuite.contains(ghost)){
                    Data.ghostFuite.add(ghost);
                }
                Data.ghostFuiteSet = true;
            }

            // Programme une tâche pour désactiver l'état énergisé après 5 secondes
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!reEnergized) { // si pacman a deja pris un energiseur ne desactive pas l'energiseur
                        PacMan.INSTANCE.setEnergized(false,0);
                        
                        timer.cancel();
                        for (var ghost : Ghost.values()) {
                            ghost.stopBlinking();
                            
                            ghost.setSpeed(2);
                            ghost.setManger(false);
                        }
                    }
                    reEnergized = false;
                }
            }, duree); 
        
        }
        

        
        this.energized = energized;
    }

    /**
     * Change l'apparence de Pac-Man.
     *
     * @return La valeur de l'apparence.
     */
    public int changeSkin() {
        return skin;
    }

    /**
     *
     * Set direction None
     */
    public void freeze() {
        this.PacmanMort = true;
        this.direction = Direction.NONE;
    }

    public void unfreeze() {
        this.PacmanMort = false;
    }

    public boolean SiPacmanMort() {
        return PacmanMort;
    }

    public void SetPacmanMort(boolean PacmanMort) {
        this.PacmanMort = PacmanMort;
    }
}
