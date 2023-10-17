package geometry;

public record IntCoordinates(int x, int y) {

    public static final IntCoordinates ZERO = new IntCoordinates(0, 0);
    public static final IntCoordinates NORTH_UNIT = new IntCoordinates(0, -1);
    public static final IntCoordinates EAST_UNIT = new IntCoordinates(1, 0);
    public static final IntCoordinates SOUTH_UNIT = new IntCoordinates(0, 1);
    public static final IntCoordinates WEST_UNIT = new IntCoordinates(-1, 0);

    public RealCoordinates toRealCoordinates(double scale) {
        return new RealCoordinates(x * scale, y * scale);
    }

    public IntCoordinates plus(IntCoordinates other) {
        return new IntCoordinates(x + other.x, y + other.y);
    }
}
