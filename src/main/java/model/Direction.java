package model;

public enum Direction {
    NONE, NORTH, EAST, SOUTH, WEST;

    public static Direction fromChar(Character direction) {
        switch (direction) {
            case 'n':
                return NORTH;
            case 'e':
                return EAST;
            case 's':
                return SOUTH;
            case 'w':
                return WEST;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

}
