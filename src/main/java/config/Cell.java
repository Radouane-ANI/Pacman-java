package config;
// Définir un enregistrement (record) nommé Cell avec les attributs spécifiés

import java.lang.constant.DirectMethodHandleDesc;
import model.Direction;

public record Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {

    // Enumeration des differents types de contenu possible presente au sein d'une case(cellule) : Rien , Point, Energizer.
    public enum Content {
        NOTHING, DOT, ENERGIZER
    }
    /**
     * methode qui permet de mettre a jour le type de contenu d'une case(cellule)
     * @param c type de contenu a mettre a jour
     */
    public boolean isDot(){
        return (initialContent == Content.DOT);
    }
    public boolean isEnergizer(){
        return (initialContent == Content.ENERGIZER);
    }
    
    public Cell updateNextItemType(Content c){
        return new Cell(northWall, eastWall, southWall, westWall, c);
    }


    //Verifie si la cellule est T-shaped ou Open
    public boolean isIntersection(){
        return (northWall ? 0 : 1) + (eastWall ? 0 : 1) + (southWall ? 0 : 1)  + (westWall ? 0 : 1) >= 3 ;
    }

    public boolean isUCell(){
        return (northWall ? 0 : 1) + (eastWall ? 0 : 1) + (southWall ? 0 : 1)  + (westWall ? 0 : 1) == 1 ;
    }

    public boolean isClosed(){
        return (northWall ? 0 : 1) + (eastWall ? 0 : 1) + (southWall ? 0 : 1)  + (westWall ? 0 : 1) == 0 ;
    }

    public boolean murEnFaceDirection(Direction d){
        switch (d){
            case NORTH : return northWall;
            case WEST : return westWall;
            case SOUTH : return southWall;
            case EAST : return eastWall;
            default : return true;
        }
    }

}


