package config;

import model.Direction;
// Définir un enregistrement (record) nommé Cell avec les attributs spécifiés

public record Cell(Specs northWall, Specs eastWall, Specs southWall, Specs westWall, Cell.Content initialContent) {

    // Enumeration des differents types de contenu possible presente au sein d'une
    // case(cellule) : Rien , Point, Energizer.
    public enum Content {
        NOTHING, DOT, ENERGIZER
    }

    public enum Specs { // Enumeration des differents types de murs possible :
        WAY, WALL, WHITE
    }

    /**
     * methode qui permet de mettre a jour le type de contenu d'une case(cellule)
     * 
     * @param c type de contenu a mettre a jour
     */
    public Cell updateNextItemType(Content c) {
        return new Cell(northWall, eastWall, southWall, westWall, c);
    }

    public boolean isDot() {
        return (initialContent == Content.DOT);
    }

    public boolean isEnergizer() {
        return (initialContent == Content.ENERGIZER);
    }

    public boolean isnorthWall() {
        return (northWall == Specs.WALL);
    }

    public boolean iseastWall() {
        return (eastWall == Specs.WALL);
    }

    public boolean issouthWall() {
        return (southWall == Specs.WALL);
    }

    public boolean iswestWall() {
        return (westWall == Specs.WALL);
    }

    public boolean isnorthWhite() {
        return (northWall == Specs.WHITE);
    }

    public boolean iseastWhite() {
        return (eastWall == Specs.WHITE);
    }

    public boolean issouthWhite() {
        return (southWall == Specs.WHITE);
    }

    public boolean iswestWhite() {
        return (westWall == Specs.WHITE);
    }

    public boolean isnorthWay() {
        return (northWall == Specs.WAY);
    }

    public boolean iseastWay() {
        return (eastWall == Specs.WAY);
    }

    public boolean issouthWay() {
        return (southWall == Specs.WAY);
    }

    public boolean iswestWay() {
        return (westWall == Specs.WAY);
    }

    // Verifie si la cellule est T-shaped ou Open
    public boolean isIntersection() {
        return (isnorthWall() ? 0 : 1) + (iseastWall() ? 0 : 1) + (issouthWall() ? 0 : 1) + (iswestWall() ? 0 : 1) >= 3;
    }

    public boolean isUCell() {
        return (isnorthWall() ? 0 : 1) + (iseastWall() ? 0 : 1) + (issouthWall() ? 0 : 1) + (iswestWall() ? 0 : 1) == 1;
    }

    public boolean isClosed() {
        return (isnorthWall() ? 0 : 1) + (iseastWall() ? 0 : 1) + (issouthWall() ? 0 : 1) + (iswestWall() ? 0 : 1) == 0;
    }

    public boolean murEnFaceDirection(Direction d) {
        switch (d) {
            case NORTH:
                return isnorthWall();
            case WEST:
                return iswestWall();
            case SOUTH:
                return issouthWall();
            case EAST:
                return iseastWall();
            default:
                return true;
        }
    }

}
