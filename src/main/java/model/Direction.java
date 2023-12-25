package model;

public enum Direction {
    NONE, NORTH, EAST, SOUTH, WEST;

    /**
     * Convertit un caractère en une direction.
     *
     * @param direction Le caractère représentant la direction ('n', 'e', 's' ou 'w').
     * @return La direction correspondante.
     */
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
                return NORTH;
        }
    }

}
