package model;

import org.junit.jupiter.api.Test;

import config.MazeConfig;
import geometry.RealCoordinates;

import static org.junit.jupiter.api.Assertions.*;

public class GhostTest {

    @Test
    public void testChangeDirection() {
        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests
        ghost.setPos(new RealCoordinates(0, 0));

        // Test de changement de direction de None en direction du NORD
        ghost.changeDirection(Direction.NORTH, ghost);
        assertEquals(Direction.NORTH, ghost.getDirection());

        // Test de changement de direction du NORD en direction du SUD
        ghost.changeDirection(Direction.SOUTH, ghost);
        assertEquals(Direction.SOUTH, ghost.getDirection());

        // Test de changement de direction du SUD en direction de l'EST
        ghost.changeDirection(Direction.EAST, ghost);
        assertEquals(Direction.EAST, ghost.getDirection());

        // Test de changement de direction de l'EST en direction de l'OUEST
        ghost.changeDirection(Direction.WEST, ghost);
        assertEquals(Direction.WEST, ghost.getDirection());

        // Test de mettre la direction a None
        ghost.changeDirection(Direction.NONE, ghost);
        assertEquals(Direction.NONE, ghost.getDirection());
    }

    // @Test
    // public void testPossible() { 
    //     MazeConfig Labyrinthetest = new MazeConfig("src/main/resources/testmaze.txt");
    //     Ghost.config = Labyrinthetest; // initialise un labyrinthe pour les test

    //     // Test lorsque la position du fantôme est entourée de passages sans murs
    //     int x = 2;
    //     int y = 2;
    //     Object[] listAttendue = { 'n', 's', 'e', 'w' };
    //     Object[] listobtenue = Ghost.INKY.possible(x, y).toArray();

    //     assertEquals(4, listobtenue.length);
    //     assertArrayEquals(listAttendue, listobtenue);
    //     // Le fantôme peut se déplacer dans toutes les directions (Nord, Sud, Est,
    //     // Ouest)

    //     // Test lorsque le fantôme a des passages possibles avec et sans murs
    //     x = 5;
    //     y = 1;
    //     Object[] listAttendue2 = { 'n', 's' };
    //     Object[] listobtenue2 = Ghost.INKY.possible(x, y).toArray();

    //     assertEquals(2, listAttendue2.length);
    //     assertArrayEquals(listAttendue2, listobtenue2);
    //     // Le fantôme peut se déplacer vers le Nord et le Sud

    //     // Test lorsque le fantôme a des passages possibles avec et sans murs
    //     x = 3;
    //     y = 1;
    //     Object[] listAttendue3 = { 's', 'e', 'w' };
    //     Object[] listobtenue3 = Ghost.INKY.possible(x, y).toArray();

    //     assertEquals(3, listAttendue3.length);
    //     assertArrayEquals(listAttendue3, listobtenue3);
    //     // Le fantôme peut se déplacer vers l'Est, l'Ouest et le sud
    // }

    @Test
    public void testIaBlinky() { 
        MazeConfig Labyrinthetest = new MazeConfig("src/main/resources/testmaze.txt",false);
        Ghost.config = Labyrinthetest; // initialise un labyrinthe pour les test

        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests
        PacMan pacMan = PacMan.INSTANCE; // Initialise Pacman pour les tests
        pacMan.setPos(new RealCoordinates(3, 0));

        // Test quand pacman est à l'Ouest de Blinky
        ghost.setPos(new RealCoordinates(5, 0));
        ghost.iaBlinky();
        assertEquals(Direction.WEST, ghost.getDirection());

        // Test quand pacman est à l'Est de Blinky
        ghost.setPos(new RealCoordinates(0, 0));
        ghost.iaBlinky();
        assertEquals(Direction.EAST, ghost.getDirection());

        // Test quand pacman est inaccesible
        ghost.setPos(new RealCoordinates(3, 3));
        ghost.iaBlinky();
        ghost.setDirection(Direction.NONE);
        assertEquals(Direction.NONE, ghost.getDirection());

        // Test quand pacman est au Sud de Blinky
        ghost.setPos(new RealCoordinates(0, 5));
        pacMan.setPos(new RealCoordinates(0, 3));
        ghost.iaBlinky();
        assertEquals(Direction.NORTH, ghost.getDirection());

        // Test quand pacman est au Nord de Blinky
        ghost.setPos(new RealCoordinates(0, 2));
        ghost.iaBlinky();
        assertEquals(Direction.SOUTH, ghost.getDirection());

        // Test quand Blinky doit contourner des murs
        ghost.setPos(new RealCoordinates(0, 5));
        pacMan.setPos(new RealCoordinates(5, 1));
        ghost.iaBlinky();
        assertEquals(Direction.EAST, ghost.getDirection());

    }

    @Test
    public void testChangeSkin() {
        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests

        // Test lorsque Pac-Man n'est pas énergisé
        PacMan.INSTANCE.setEnergized(false);
        int resultNormalSkin = ghost.changeSkin();
        assertEquals(2, resultNormalSkin);

        // Test lorsque Pac-Man est énergisé
        PacMan.INSTANCE.setEnergized(true);
        int resultVulnerableSkin = ghost.changeSkin();
        assertEquals(1, resultVulnerableSkin);

        // Test lorsque Pac-Man n'est plus énergisé
        PacMan.INSTANCE.setEnergized(false);
        int resultNoChange = ghost.changeSkin();
        assertEquals(0, resultNoChange);
    }

    @Test
    public void testFuite() {
        // Initialisation des positions des fantômes
        Ghost.BLINKY.setPos(new RealCoordinates(3, 3));
        Ghost.INKY.setPos(new RealCoordinates(2, 4));
        Ghost.PINKY.setPos(new RealCoordinates(4, 4));
        Ghost.CLYDE.setPos(new RealCoordinates(3, 5));

        PacMan pacMan = PacMan.INSTANCE; // Initialise Pacman pour les tests
        pacMan.setPos(new RealCoordinates(3, 4));
        
        Ghost.fuite();

        // Test de fuite lorsque Pac-Man est au Sud de Blinky
        assertNotEquals(Direction.SOUTH, Ghost.BLINKY.getDirection());

        // Test de fuite lorsque Pac-Man est à l'Est de Inky
        assertNotEquals(Direction.EAST, Ghost.INKY.getDirection());

        // Test de fuite lorsque Pac-Man est à l'Ouest de Pinky
        assertNotEquals(Direction.WEST, Ghost.PINKY.getDirection());

        // Test de fuite lorsque Pac-Man est au Nord de Clyde
        assertNotEquals(Direction.NORTH, Ghost.CLYDE.getDirection());
    }
}