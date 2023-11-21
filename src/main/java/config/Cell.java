package config;
// Définir un enregistrement (record) nommé Cell avec les attributs spécifiés

public record Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {

    // Enumeration des differents types de contenu possible presente au sein d'une case(cellule) : Rien , Point, Energizer.
    public enum Content {
        NOTHING, DOT, ENERGIZER
    }
    
    public Cell updateNextItemType(Content c){
        return new Cell(northWall, eastWall, southWall, westWall, c);
    }


    //Verifie si la cellule est T-shaped ou Open
    public boolean isIntersection(){
        return (northWall ? 0 : 1) + (eastWall ? 0 : 1) + (southWall ? 0 : 1)  + (westWall ? 0 : 1) >= 3 ;
    }

}


