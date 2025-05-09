package model;

import datagame.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class BonusTest {

    @Test
    public void testManger() {
        // Set up the test case
        Bonus bonus = Bonus.CERISE;
        bonus.setActif(true);

        // Perform the action to be tested
        bonus.manger();

        // Verify the expected outcomes
        assertEquals(100, Data.getScore()); // Adjust this based on the actual implementation
        assertFalse(bonus.isActif());

        bonus = Bonus.ECLAIR;
        bonus.setActif(true);

        // Perform the action to be tested
        bonus.manger();

        // Verify the expected outcomes
        assertEquals(8, PacMan.INSTANCE.getSpeed()); // Adjust this based on the actual implementation
        assertFalse(bonus.isActif());

        bonus = Bonus.COEUR;
        bonus.setActif(true);

        // Perform the action to be tested
        bonus.manger();
        // Verify the expected outcomes
        assertEquals(4, Data.getLive()); // Adjust this based on the actual implementation
        assertFalse(bonus.isActif());
    }
}
