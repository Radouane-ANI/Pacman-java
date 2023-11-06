package gui;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.Critter;
import model.Direction;
import model.Ghost;
import model.MazeState;
import model.PacMan;

public final class CritterGraphicsFactory {
    private final double scale;
    private final MazeState state;

    public CritterGraphicsFactory(double scale, MazeState state) {
        this.scale = scale;
        this.state = state;
    }

    public GraphicsUpdater makeGraphics(Critter critter) {
        var size = 0.7;
        var url = (critter instanceof PacMan) ? "pacman.png" : switch ((Ghost) critter) {
            case BLINKY -> "ghost_blinky.png";
            case CLYDE -> "ghost_clyde.png";
            case INKY -> "ghost_inky.png";
            case PINKY -> "ghost_pinky.png";
        };
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        return new GraphicsUpdater() {
            @Override
            public void update() {
                // Change le skin des fantomes en fonction de l'etat energized de pacman
                int skin = critter.changeSkin();
                if (!(critter instanceof PacMan) && skin == 0) {
                    var url = switch ((Ghost) critter) {
                        case BLINKY -> "ghost_blinky.png";
                        case CLYDE -> "ghost_clyde.png";
                        case INKY -> "ghost_inky.png";
                        case PINKY -> "ghost_pinky.png";
                    };
                    // Créez une instance de FadeTransition
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), image);

                    // Définissez la valeur de départ de l'opacité
                    fadeTransition.setFromValue(0.2);

                    // Définissez la valeur d'arrivée de l'opacité
                    fadeTransition.setToValue(1.0);

                    // Jouez l'animation
                    fadeTransition.play();
                    image.setImage(new Image(url, scale * size, scale * size, true, true));
                } else if (!(critter instanceof PacMan) && skin == 1) {
                    image.setImage(new Image("vulnerable_ghost.png", scale * size, scale * size, true, true));
                }
                image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                if (critter instanceof PacMan) {
                    if (state.GetBoulbi()) {
                        image.setRotate(0);
                        image.setScaleX(1);
                    } else {
                        switch (critter.getDirection()) {
                            case SOUTH:
                                image.setRotate(90);
                                image.setScaleX(1);
                                break;
                            case NORTH:
                                image.setRotate(270);
                                image.setScaleX(1);
                                break;
                            case EAST:
                                image.setRotate(0);
                                image.setScaleX(1);
                                break;
                            case WEST:
                                image.setRotate(0);
                                image.setScaleX(-1);
                                break;
                        }
                    }
                }
            }

            @Override
            public Node getNode() {
                return image;
            }
        };
    }
}