package config;
// Définir un enregistrement (record) nommé Cell avec les attributs spécifiés

public record Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {

    // Enumeration des differents types de contenu possible presente au sein d'une case(cellule) : Rien , Point, Energizer.
    public enum Content {
        NOTHING, DOT, ENERGIZER
    }
    /**
     * methode qui permet de mettre a jour le type de contenu d'une case(cellule)
     * @param c type de contenu a mettre a jour
     */
    public Cell updateNextItemType(Content c){
        return new Cell(northWall, eastWall, southWall, westWall, c);
    }

   
}


