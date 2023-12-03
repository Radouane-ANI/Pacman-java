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
import java.util.HashMap;
import java.util.Map;

public final class CritterGraphicsFactory {
    private final double scale;
    private final MazeState state;
    // private static Map<String, String> GhostAnimationDictionary = new HashMap<>();
    private static Map<String, String> GhostAnimationDictionary = new HashMap<>();
    private static int tick = 0;
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
                tick++;
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
                } else if (!(critter instanceof PacMan) && skin == 1) {
                    image.setImage(new Image("vulnerable_ghost.png", scale * size, scale * size, true, true));
                } else if ((critter instanceof PacMan) && PacMan.INSTANCE.changeSkin() != Data.getskin()) {
                    var url = switch (Data.getskin()) {// switch qui sert a choisir l'image correspond a la créature et
                                                       // a son skin
                        case 1 -> "pacman.png";
                        case 2 -> "pacmanblue.png";
                        case 3 -> "pacmangreen.png";
                        default -> "pacman.png";
                    };
                    PacMan.INSTANCE.setSkin(Data.getskin());
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
                    String CritterImage = setCritterImage(critter, size);
                    if(tick/30 % 2 == 0){ // on change le sprite du fantome en fonction du tick
                    image.setImage(new Image(CritterImage+"1.png", scale * size, scale * size, true, true));} //sprite 2
                    else{
                    image.setImage(new Image(CritterImage+".png", scale * size, scale * size, true, true));} // sprite 1
                    }
                }
            
            @Override
            public Node getNode() {
                return image;
            }
        };
    }


    /**
     * Définit le debut du nom du fichier image pour la créature donnée en fonction de sa direction.
     *
     * @param critter La créature pour laquelle définir l'image.
     * @param size    Le facteur de taille pour l'image de la créature.
     * @return un String qui correspond au nom de l'image de la créature
     */
    public String setCritterImage(Critter critter, double size) {
        Direction direction = critter.getDirection();
        String imageFileName = "";

        switch ((Ghost) critter) {
            case BLINKY:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "BlinkyB";
                        break;
                    case NORTH:
                        imageFileName = "BlinkyH";
                        break;
                    case EAST:
                        imageFileName = "BlinkyD";
                        break;
                    case WEST:
                        imageFileName = "BlinkyG";
                        break;
                    default:
                        imageFileName = "BlinkyB";
                        break;
                }
                break;
            case INKY:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "InkyB";
                        break;
                    case NORTH:
                        imageFileName = "InkyH";
                        break;
                    case EAST:
                        imageFileName = "InkyD";
                        break;
                    case WEST:
                        imageFileName = "InkyG";
                        break;
                    default:
                        imageFileName = "InkyG";
                        break;
                }
                break;
            case PINKY:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "PinkyB";
                        break;
                    case NORTH:
                        imageFileName = "PinkyH";
                        break;
                    case EAST:
                        imageFileName = "PinkyD";
                        break;
                    case WEST:
                        imageFileName = "PinkyG";
                        break;
                    default:
                        imageFileName = "PinkyD";
                        break;
                }
                break;
            case CLYDE:
                switch (direction) {
                    case SOUTH:
                        imageFileName = "ClydeB";
                        break;
                    case NORTH:
                        imageFileName = "ClydeH";
                        break;
                    case EAST:
                        imageFileName = "ClydeD";
                        break;
                    case WEST:
                        imageFileName = "ClydeG";
                        break;
                    default:
                        imageFileName = "ClydeH";
                        break;
                }
                break;
        }
        return imageFileName;
    }

}
