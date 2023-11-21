package gui;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Critter;
import model.Direction;
import model.Ghost;
import model.MazeState;
import model.PacMan;

/**
 * La classe CritterGraphicsFactory est utilisée pour créer et
 * mettre à jour les éléments graphiques des créatures dans l'interface utilisateur.
 */

public final class CritterGraphicsFactory {
    private final double scale;
    private final MazeState state;
    private boolean mouthOpen = true;
    private long lastImageChangeTime = 0;
    private static final long IMAGE_CHANGE_INTERVAL = 180; // Temps en millisecondes entre les changements d'image

    /**
     * Constructeur de la classe CritterGraphicsFactory.
     * @param scale L'échelle de taille des graphiques.
     * @param state L'état du labyrinthe.
     */

    public CritterGraphicsFactory(double scale, MazeState state) {
        this.scale = scale;
        this.state = state;
    }

    /**
     * Crée un objet GraphicsUpdater pour une créature donnée.
     * @param critter La créature pour laquelle créer les graphiques.
     * @return Un objet GraphicsUpdater pour mettre à jour les graphiques de la créature.
     */
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
                // Met à jour la position de l'image selon la position de la créature
                image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                if (critter instanceof PacMan) {
                    if (critter.getDirection() == Direction.NONE) {
                        // Afficher l'image de la bouche ouverte
                        image.setImage(new Image("pacman.png", scale * size, scale * size, true, true));
                    } else if (System.currentTimeMillis() - lastImageChangeTime > IMAGE_CHANGE_INTERVAL) {
                        mouthOpen = !mouthOpen; // Inverse l'état de la bouche
                        var imageUrl = mouthOpen ? "pacman.png" : "pacmanfermer2.png";
                        image.setImage(new Image(imageUrl, scale * size, scale * size, true, true));
                        lastImageChangeTime = System.currentTimeMillis();
                    }

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