package geometry;

public record IntCoordinates(int x, int y) {
    public RealCoordinates toRealCoordinates(double scale) {
        return new RealCoordinates(x * scale, y * scale);
    }

    public IntCoordinates add(int x, int y) {
        return new IntCoordinates(this.x + x, this.y + y);
    }

    public int distance(IntCoordinates other) {
        return (int)Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
    
}
