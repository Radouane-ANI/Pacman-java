package config;
import org.junit.jupiter.api.Test;

import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import static org.junit.jupiter.api.Assertions.*;

public class LabyrintheTest {
     
    @Test
    public void testFile(){ //Test de la lecture du fichier
        MazeConfig Labyrinthetest = new MazeConfig("src/main/resources/testmaze.txt",false);
        //Test de la longueur du labyrinthe dans notre cas c'est 6
        assertEquals(Labyrinthetest.getHeightFile("src/main/resources/testmaze.txt"),Labyrinthetest.getHeight());
        //Test de la largeur du labyrinthe dans notre cas c'est aussi 6
        assertEquals(Labyrinthetest.getWidthFile("src/main/resources/testmaze.txt"),Labyrinthetest.getWidth());
        //Test de la position de PacMan dans notre cas c'est (2,5)
        assertEquals((new IntCoordinates(2, 5)),(Labyrinthetest.getPacManPos()));
        //Test de la position de Blinky dans notre cas c'est (5,0)
        assertEquals(new IntCoordinates(5, 0),Labyrinthetest.getBlinkyPos());
        //Test de la position de Pinky dans notre cas c'est (0,5)
        assertEquals(new IntCoordinates(0, 5),Labyrinthetest.getPinkyPos());
        //Test de la position de Inky dans notre cas c'est (0,2)
        assertEquals(new IntCoordinates(0, 2),Labyrinthetest.getInkyPos());
        //Test de la position de Clyde dans notre cas c'est (5,2)
        assertEquals(new IntCoordinates(5, 2),Labyrinthetest.getClydePos());

    }
}

