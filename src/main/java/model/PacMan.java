package model;

import geometry.IntCoordinates;
import java.util.Timer;
import java.util.TimerTask;
import geometry.RealCoordinates;

/**
 * Implements Pac-Man character using singleton pattern. FIXME: check whether
 * singleton is really a good idea.
 */
public final class PacMan implements Critter {
    private Direction direction = Direction.NONE;
    private RealCoordinates pos;
    private boolean energized;
    private boolean reEnergized;

    private PacMan() {
    }

    public static final PacMan INSTANCE = new PacMan();

    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public double getSpeed() {
        return isEnergized() ? 6 : 4;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void setPos(RealCoordinates pos) {
        this.pos = pos;
    }

    /**
     * @param gridState
     * @return retourne s'il y a un dot a l'endroit ou pacman se trouve
     */
    public boolean PacManDot(boolean[][] gridState){
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

    public void setEnergized(boolean energized) {
        if (energized) {
            if (this.energized) { // verifie si pacman a deja prisun energiseur
                this.reEnergized = true;
            }
            // Planifiez une tâche pour désactiver energized après 5 secondes
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!reEnergized) { // si pacman a deja prisun energiseur ne desactive pas l'energiseur
                        PacMan.INSTANCE.setEnergized(false);
                        timer.cancel();
                    }
                    reEnergized = false;
                }
            }, 5000); // 5 000 millisecondes = 5 secondes
        }
        this.energized = energized;
    }

    public int changeSkin() {
        return 2;
    }

}
