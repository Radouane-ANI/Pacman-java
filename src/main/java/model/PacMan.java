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
    private int speed = 4;
    private int skin;

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

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
    public void setEnergized(boolean energized) {
        if (energized) {
            if (this.energized) { // verifie si pacman a deja pris un energiseur
                this.reEnergized = true;
            }

            // Programme une tâche pour désactiver l'état énergisé après 5 secondes
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!reEnergized) { // si pacman a deja pris un energiseur ne desactive pas l'energiseur
                        PacMan.INSTANCE.setEnergized(false);
                        timer.cancel();
                    }
                    reEnergized = false;
                }
            }, 5000); // 5 000 millisecondes = 5 secondes
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

}
