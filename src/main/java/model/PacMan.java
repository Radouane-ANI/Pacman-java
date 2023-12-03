package model;

import geometry.RealCoordinates;

/**
 * Implements Pac-Man character using singleton pattern. FIXME: check whether
 * singleton is really a good idea.
 */
public final class PacMan implements Critter {
    private Direction direction = Direction.NONE;
    private RealCoordinates pos;
    private boolean energized;
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
        return isEnergized() ? 6 : 4;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        // System.out.println("Pacman direction: " + direction);
        this.direction = direction;
    }

    @Override
    public void setPos(RealCoordinates pos) {
        this.pos = pos;
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
