package gui;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.Bonus;
import model.Critter;
import model.Direction;
import model.Ghost;
import model.MazeState;
import model.PacMan;
import datagame.Data;

public final class CritterGraphicsFactory {
    private final double scale;
    private final MazeState state;

    public CritterGraphicsFactory(double scale, MazeState state) {
        this.scale = scale;
        this.state = state;
    }

    public GraphicsUpdater makeGraphics(Critter critter) {
        var size = 0.82;
        var url = (critter instanceof PacMan) ? switch (Data.getskin()){//switch qui sert a choisir l'image correspond a la créature et a son skin
            case 1 -> "pacman.png";
            case 2 -> "pacmanblue.png";
            case 3 -> "pacmangreen.png";
            default -> "pacman.png";
        } : switch ((Ghost) critter) {
            case BLINKY -> "BlinkyB.png";
            case CLYDE -> "ClydeH.png";
            case INKY -> "InkyG.png";
            case PINKY -> "PinkyD.png";
        };
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        return new GraphicsUpdater() {
            @Override
            public void update() {
                // Change le skin des fantomes en fonction de l'etat energized de pacman
                int skin = critter.changeSkin();
                if (!(critter instanceof PacMan) && skin == 0) {
                    var url = switch ((Ghost) critter) {
                        case BLINKY -> "BlinkyB.png";
                        case CLYDE -> "ClydeH.png";
                        case INKY -> "InkyG.png";
                        case PINKY -> "PinkyD.png";
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
                }else if (!(critter instanceof PacMan) && skin == 1) {
                    image.setImage(new Image("vulnerable_ghost.png", scale * size, scale * size, true, true));
                } else if ((critter instanceof PacMan) && PacMan.INSTANCE.changeSkin() != Data.getskin()) {
                    var url = switch (Data.getskin()) {// switch qui sert a choisir l'image correspond a la créature et
                                                       // a son skin
                        case 1 -> "pacman.png";
                        case 2 -> "pacmanblue.png";
                        case 3 -> "pacmangreen.png";
                        default -> "pacman.png";
                    };PacMan.INSTANCE.setSkin(Data.getskin());
                    image.setImage(new Image(url, scale * size, scale * size, true, true));
                }else if(critter instanceof Ghost && ((Ghost) critter).getManger()){
                    image.setImage(new Image("yeux.png", scale * size, scale * size, true, true));
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
                }else if(!PacMan.INSTANCE.isEnergized()){
                    image.setImage(setCritterImage(critter, size));
                }
            }

            @Override
            public Node getNode() {
                return image;
            }
        };
    }

    /**
 * Crée un GraphicsUpdater pour un bonus donné, permettant de mettre à jour
 * l'affichage graphique du bonus.
 *
 * @param bonus Le bonus pour lequel créer le GraphicsUpdater.
 * @return Un GraphicsUpdater pour le bonus spécifié.
 */
public GraphicsUpdater makeGraphics(Bonus bonus) {
    // Définit la taille de l'image
    var size = 0.7;

    // Récupère l'URL de l'image en fonction du type de bonus
    var url = switch (bonus) {
        case CERISE -> "cerise.png";
        case COEUR -> "coeur.png";
        case ECLAIR -> "eclair.png";
    };

    // Crée une nouvelle ImageView avec l'image correspondante
    var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
    image.setVisible(false);

    // Retourne un nouveau GraphicsUpdater personnalisé pour le bonus
    return new GraphicsUpdater() {
        /**
         * Met à jour l'affichage en fonction de l'état du bonus.
         */
        @Override
        public void update() {
            if (bonus.isActif()) {
                // Positionne l'image en fonction des coordonnées du bonus
                image.setTranslateX((bonus.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((bonus.getPos().y() + (1 - size) / 2) * scale);
                image.setVisible(true);
            } else {
                // Cache l'image si le bonus n'est pas actif
                image.setVisible(false);
            }
        }

        /**
         * Récupère le Node associé à l'image du bonus.
         *
         * @return Le Node représentant l'image du bonus.
         */
        @Override
        public Node getNode() {
            return image;
        }
    };
}

    /**
     * Définit l'image pour la créature donnée en fonction de sa direction.
     *
     * @param critter La créature pour laquelle définir l'image.
     * @param size    Le facteur de taille pour l'image de la créature.
     * @return Un objet Image représentant la créature avec la direction spécifiée.
     */
    public Image setCritterImage(Critter critter, double size) {
        Direction direction = critter.getDirection();
        String imageFileName = "";

        switch ((Ghost) critter) {
            case BLINKY:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "BlinkyB.png";
                        break;
                    case NORTH:
                        imageFileName = "BlinkyH.png";
                        break;
                    case EAST:
                        imageFileName = "BlinkyD.png";
                        break;
                    case WEST:
                        imageFileName = "BlinkyG.png";
                        break;
                    default:
                        imageFileName = "BlinkyB.png";
                        break;
                }
                break;
            case INKY:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "InkyB.png";
                        break;
                    case NORTH:
                        imageFileName = "InkyH.png";
                        break;
                    case EAST:
                        imageFileName = "InkyD.png";
                        break;
                    case WEST:
                        imageFileName = "InkyG.png";
                        break;
                    default:
                        imageFileName = "InkyG.png";
                        break;
                }
                break;
            case PINKY:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "PinkyB.png";
                        break;
                    case NORTH:
                        imageFileName = "PinkyH.png";
                        break;
                    case EAST:
                        imageFileName = "PinkyD.png";
                        break;
                    case WEST:
                        imageFileName = "PinkyG.png";
                        break;
                    default:
                        imageFileName = "PinkyD.png";
                        break;
                }
                break;
            case CLYDE:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "ClydeB.png";
                        break;
                    case NORTH:
                        imageFileName = "ClydeH.png";
                        break;
                    case EAST:
                        imageFileName = "ClydeD.png";
                        break;
                    case WEST:
                        imageFileName = "ClydeG.png";
                        break;
                    default:
                        imageFileName = "ClydeH.png";
                        break;
                }
                break;
        }

        return new Image(imageFileName, scale * size, scale * size, true, true);
    }

}