package config;
// Définir un enregistrement (record) nommé Cell avec les attributs spécifiés

public record Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {

    // Enumeration des differents types de contenu possible presente au sein d'une case(cellule) : Rien , Point, Energizer.
    public enum Content { NOTHING, DOT, ENERGIZER}
    //---------------------------------------------------
    // FIXME: all these factories are convenient, but it is not very "economic" to have so many methods!
    //---------------------------------------------------
    // Méthode de fabrique pour créer une cellule ouverte avec le contenu spécifié
    public static Cell open(Content c) { return new Cell(false, false, false, false, c); }
    // Méthode de fabrique pour créer une cellule fermée avec le contenu spécifié
    public static Cell closed(Content c) { return new Cell(true, true, false, false, c); }
    // straight pipes // Méthodes de fabrique pour des tuyaux droits avec le contenu spécifié
    public static Cell hPipe(Content c) { return new Cell(true, false, true, false, c); }
    public static Cell vPipe(Content c) { return new Cell(false, true, false, true, c); }
    // corner cells   // Méthodes de fabrique pour des cellules d'angle avec le contenu spécifié
    public static Cell swVee(Content c) { return new Cell(true, true, false, false, c); }
    public static Cell nwVee(Content c) { return new Cell(false, true, true, false, c); }
    public static Cell neVee(Content c) { return new Cell(false, false, true, true, c); }
    public static Cell seVee(Content c) { return new Cell(true, false, false, true, c); }
    // T-shaped cells // Méthodes de fabrique pour des cellules en forme de T avec le contenu spécifié
    public static Cell nU(Content c) { return new Cell(false, true, true, true, c); }
    public static Cell eU(Content c) { return new Cell(true, false, true, true, c); }
    public static Cell sU(Content c) { return new Cell(true, true, false, true, c); }
    public static Cell wU(Content c) { return new Cell(true, true, true, false, c); }
    // U-shaped cells // Méthodes de fabrique pour des cellules en forme de U avec le contenu spécifié 
    public static Cell nTee(Content c) { return new Cell(true, false, false, false, c); }
    public static Cell eTee(Content c) { return new Cell(false, true, false, false, c); }
    public static Cell sTee(Content c) { return new Cell(false, false, true, false, c); }
    public static Cell wTee(Content c) { return new Cell(false, false, false, true, c); }
    public boolean isDot(){
        return (initialContent == Content.DOT);
    }
    public Cell updateNextItemType(Content c){
        return new Cell(northWall, eastWall, southWall, westWall, c);
    }
}


