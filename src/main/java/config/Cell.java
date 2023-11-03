package config;
// Définir un enregistrement (record) nommé Cell avec les attributs spécifiés

public record Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {

    // Enumeration des differents types de contenu possible presente au sein d'une case(cellule) : Rien , Point, Energizer.
    public enum Content {
        NOTHING, DOT, ENERGIZER
    }
    public static Cell lacase(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {
        return new Cell(northWall, eastWall, southWall, westWall, initialContent);
    }
    //---------------------------------------------------
    // FIXME: all these factories are convenient, but it is not very "economic" to have so many methods!
    //---------------------------------------------------
    

    public Cell updateNextItemType(Content c){
        return new Cell(northWall, eastWall, southWall, westWall, c);
    }
}


