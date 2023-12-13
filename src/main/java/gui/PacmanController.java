package gui;

import model.Bonus;
import model.Direction;
import model.Ghost;
import model.PacMan;
import java.util.Stack;
import model.MazeState;
import geometry.RealCoordinates;
import javafx.scene.input.KeyEvent;

public class PacmanController {
    private static Direction directionProchaine;

    /**
     * Fonction qui gere l'evement d'une touche appuyée est fais en sorte de bien
     * faire bouger PacMan
     * dans des conditions normales
     * !PacMan.INSTANCE.SiPacmanMort()
     * 
     * @param event
     */
    public void keyPressedHandler(KeyEvent event) {
        var Pos = PacMan.INSTANCE.getPos();
        Direction DirectionFaire;
        if (Bonus.PERDU.isPerdu()) {
            DirectionFaire = switch (event.getCode()) {
                case LEFT -> Direction.SOUTH;
                case RIGHT -> Direction.WEST;
                case UP -> Direction.EAST;
                case DOWN -> Direction.NORTH;
                default -> PacMan.INSTANCE.getDirection(); // do nothing
            };
        } else if (PacMan.INSTANCE.SiPacmanMort()) {
            DirectionFaire = switch (event.getCode()) {
                case LEFT -> Direction.NONE;
                case RIGHT -> Direction.NONE;
                case UP -> Direction.NONE;
                case DOWN -> Direction.NONE;
                default -> PacMan.INSTANCE.getDirection(); // do nothing
            };
        } else {
            DirectionFaire = switch (event.getCode()) {
                case LEFT -> Direction.WEST;
                case RIGHT -> Direction.EAST;
                case UP -> Direction.NORTH;
                case DOWN -> Direction.SOUTH;
                default -> PacMan.INSTANCE.getDirection(); // do nothing
            };

        }

        if (DirectionFaire != PacMan.INSTANCE.getDirection()) {
            if (PacMan.INSTANCE.estPossible(DirectionFaire)) {
                PacMan.INSTANCE.setDirection(DirectionFaire);
                if (PacMan.INSTANCE.getPos().equals(Pos.round())) {
                    PacMan.INSTANCE.setDirection(PacMan.INSTANCE.getDirection());
                } else {
                    PacMan.INSTANCE.setPos(new RealCoordinates(Pos.round().x(), Pos.round().y()));
                }
            }
            directionProchaine = DirectionFaire;
        }

    }

    public void keyReleasedHandler(KeyEvent event) { // à chaque fois que l'on relache un bouton les fantomes bougent

        // Ghost.updateGhostPositions();

    }

    public static Direction getprochaDirection() {
        return directionProchaine;
    }

    public static void setprochaDirection(Direction direction) {
        directionProchaine = direction;
    }
}