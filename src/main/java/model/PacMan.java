package model;

import static model.Ghost.config;
import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import model.MazeState;
/**
 * Implements Pac-Man character using singleton pattern. FIXME: check whether singleton is really a good idea.
 */
public final class PacMan implements Critter {
    
    private Direction direction = Direction.NONE;
    private RealCoordinates pos;
    private boolean energized;
    private RealCoordinates prevpos;
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
     * @param direction direction du pacman
     * @return un bool si il y'a un mur en face de l'instance du PACMAN
     */
    public static boolean  estPossible(Direction direction){
        var pacPos = PacMan.INSTANCE.getPos().round();
        switch(direction){
            case NORTH:
                
                return !config.getCell(pacPos).northWall();
            case SOUTH:
                
                return !config.getCell(pacPos).southWall();
            case EAST:
                
                return !config.getCell(pacPos).eastWall();
            case WEST:
               
                return !config.getCell(pacPos).westWall();
            default: return false;
        }
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
        this.energized = energized;
    }
}
