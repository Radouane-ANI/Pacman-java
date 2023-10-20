package gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        var url = (critter instanceof PacMan) ? "pacman.png" : 
        switch ((Ghost) critter) {
            case BLINKY -> (PacMan.INSTANCE.isEnergized() ? "pacman.png" : "ghost_blinky.png");
            case CLYDE -> (PacMan.INSTANCE.isEnergized() ? "pacman.png" : "ghost_clyde.png");
            case INKY -> (PacMan.INSTANCE.isEnergized() ? "pacman.png" : "ghost_inky.png");
            case PINKY -> (PacMan.INSTANCE.isEnergized() ? "pacman.png" : "ghost_pinky.png");
        };
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        return new GraphicsUpdater() {
            @Override
            public void update() {
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