package model;

import org.junit.jupiter.api.Test;

import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;

import static org.junit.jupiter.api.Assertions.*;

public class GhostTest {

    @Test
    public void testChangeDirection() {
        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests
        ghost.setPos(new RealCoordinates(0, 0));

        // Test de changement de direction de None en direction du NORD
        ghost.changeDirection(Direction.NORTH);
        assertEquals(Direction.NORTH, ghost.getDirection());

        // Test de changement de direction du NORD en direction du SUD
        ghost.changeDirection(Direction.SOUTH);
        assertEquals(Direction.SOUTH, ghost.getDirection());

        // Test de changement de direction du SUD en direction de l'EST
        ghost.changeDirection(Direction.EAST);
        assertEquals(Direction.EAST, ghost.getDirection());

        // Test de changement de direction de l'EST en direction de l'OUEST
        ghost.changeDirection(Direction.WEST);
        assertEquals(Direction.WEST, ghost.getDirection());

        // Test de mettre la direction a None
        ghost.changeDirection(Direction.NONE);
        assertEquals(Direction.NONE, ghost.getDirection());
    }

    @Test
    public void testCheminVersCible() { 
        MazeConfig Labyrinthetest = new MazeConfig("src/main/resources/testmaze.txt",false);
        Ghost.config = Labyrinthetest; // initialise un labyrinthe pour les test

        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests
        PacMan pacMan = PacMan.INSTANCE; // Initialise Pacman pour les tests
        Direction testDirection;
        pacMan.setPos(new RealCoordinates(3, 0));

        // Test quand pacman est à l'Ouest de Blinky
        ghost.setDirection(Direction.NONE);
        ghost.setPos(new RealCoordinates(5, 0));
        testDirection = ghost.cheminVersCible(pacMan.getPos().round());
        assertEquals(Direction.WEST, testDirection);

        //Test quand pacman est à l'Est de Blinky
        ghost.setDirection(Direction.NONE);
        ghost.setPos(new RealCoordinates(0, 0));
        testDirection = ghost.cheminVersCible(pacMan.getPos().round());
        assertEquals(Direction.EAST, testDirection);

        // Test quand pacman est au Sud de Blinky
        ghost.setDirection(Direction.NONE);
        ghost.setPos(new RealCoordinates(0, 5));
        pacMan.setPos(new RealCoordinates(0, 3));
        testDirection = ghost.cheminVersCible(pacMan.getPos().round());
        assertEquals(Direction.NORTH, testDirection);

        // Test quand pacman est au Nord de Blinky
        ghost.setDirection(Direction.NONE);
        ghost.setPos(new RealCoordinates(0, 0));
        testDirection = ghost.cheminVersCible(pacMan.getPos().round());
        assertEquals(Direction.SOUTH, testDirection);

        // Test pour verifier que le fantome ne fasse pas de demi tour
        ghost.setDirection(Direction.NORTH);
        ghost.setPos(new RealCoordinates(0, 5));
        pacMan.setPos(new RealCoordinates(0, 3));
        testDirection = ghost.cheminVersCible(pacMan.getPos().round());
        assertNotEquals(Direction.SOUTH, testDirection);

    }

    @Test
    public void testProchainePosistionOppose() { 
        MazeConfig Labyrinthetest = new MazeConfig("src/main/resources/testmaze.txt",false);
        Ghost.config = Labyrinthetest; // initialise un labyrinthe pour les test

        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests
        PacMan pacMan = PacMan.INSTANCE; // Initialise Pacman pour les tests
        Direction testDirection;
        ghost.setPos(new RealCoordinates(3, 0));

        // Test quand pacman est à l'Est de Blinky
        ghost.setDirection(Direction.NONE);
        pacMan.setPos(new RealCoordinates(5, 0));
        testDirection = ghost.prochainePositionOppose(pacMan.getPos().round());
        assertEquals(Direction.WEST, testDirection);

        //Test quand pacman est à l'Ouest de Blinky
        ghost.setDirection(Direction.NONE);
        pacMan.setPos(new RealCoordinates(0, 0));
        testDirection = ghost.prochainePositionOppose(pacMan.getPos().round());
        assertEquals(Direction.EAST, testDirection);

        // Test quand pacman est au Sud de Blinky
        ghost.setDirection(Direction.NONE);
        pacMan.setPos(new RealCoordinates(0, 5));
        ghost.setPos(new RealCoordinates(0, 3));
        testDirection = ghost.prochainePositionOppose(pacMan.getPos().round());
        assertEquals(Direction.NORTH, testDirection);

        // Test quand pacman est au Nord de Blinky
        ghost.setDirection(Direction.NONE);
        pacMan.setPos(new RealCoordinates(0, 0));
        testDirection = ghost.prochainePositionOppose(pacMan.getPos().round());
        assertEquals(Direction.SOUTH, testDirection);

        // Test pour verifier que le fantome ne fasse pas de demi tour
        ghost.setDirection(Direction.SOUTH);
        ghost.setPos(new RealCoordinates(0, 5));
        pacMan.setPos(new RealCoordinates(0, 3));
        testDirection = ghost.prochainePositionOppose(pacMan.getPos().round());
        assertNotEquals(Direction.NORTH, testDirection);

    }

    @Test
    public void testChangeSkin() {
        Ghost ghost = Ghost.BLINKY; // Initialise un fantôme pour les tests

        // Test lorsque Pac-Man n'est pas énergisé
        PacMan.INSTANCE.setEnergized(false,0);
        int resultNormalSkin = ghost.changeSkin();
        assertEquals(2, resultNormalSkin);

        // Test lorsque Pac-Man est énergisé
        PacMan.INSTANCE.setEnergized(true,1000);
        int resultVulnerableSkin = ghost.changeSkin();
        assertEquals(1, resultVulnerableSkin);

        // Test lorsque Pac-Man n'est plus énergisé
        PacMan.INSTANCE.setEnergized(false,0);
        int resultNoChange = ghost.changeSkin();
        assertEquals(0, resultNoChange);
    }

    @Test
    void testRetour() {
        MazeConfig Labyrinthetest = new MazeConfig("src/main/resources/testmaze.txt", false);

        // Initialiser les positions et l'état des Ghosts pour le test
        Ghost.BLINKY.setPos(new RealCoordinates(0, 0));
        Ghost.BLINKY.setManger(true);
        // Appeler la méthode retour() sur Ghost.BLINKY
        char direction = Ghost.BLINKY.retour();
        
        // Vérifier que la direction retournée est correcte
        assertEquals('e', direction);
        // Vérifier que l'etat du Ghost est correcte
        assertEquals(true, Ghost.BLINKY.getManger());
        assertEquals(8, Ghost.BLINKY.getSpeed());

        // Réinitialiser les positions et l'état des Ghosts pour le test suivant
        Ghost.BLINKY.setPos(Labyrinthetest.getBlinkyPos().toRealCoordinates(1));
        Ghost.BLINKY.setManger(true);
        // Appeler la méthode retour() sur Ghost.BLINKY
        direction = Ghost.BLINKY.retour();

        // Vérifier que la direction retournée est correcte
        assertEquals('n', direction);
        // Vérifier que l'etat du Ghost est correcte
        assertEquals(false, Ghost.BLINKY.getManger());
        assertEquals(2, Ghost.BLINKY.getSpeed());


    }
}