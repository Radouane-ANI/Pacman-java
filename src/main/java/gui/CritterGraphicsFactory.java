package gui;

import javafx.animation.FadeTransition;
import static model.Ghost.BLINKY;
import static model.Ghost.CLYDE;
import static model.Ghost.INKY;
import static model.Ghost.PINKY;
import static model.Ghost.makeGhostVisibleAgain;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.Critter;
import model.Direction;
import model.Ghost;
import model.MazeState;
import model.PacMan;
import datagame.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * La classe CritterGraphicsFactory est utilisée pour créer et
 * mettre à jour les éléments graphiques des créatures dans l'interface
 * utilisateur.
 */

public final class CritterGraphicsFactory {
    private final double scale;
    private final MazeState state;
    // private static Map<String, String> GhostAnimationDictionary = new
    // HashMap<>();
    private static Map<String, String> GhostAnimationDictionary = new HashMap<>();
    private static int tick = 0;
    private boolean mouthOpen = true;
    private long lastImageChangeTime = 0;
    private static final long IMAGE_CHANGE_INTERVAL = 180; // Temps en millisecondes entre les changements d'image
    private int starttick;

    /**
     * Constructeur de la classe CritterGraphicsFactory.
     * 
     * @param scale L'échelle de taille des graphiques.
     * @param state L'état du labyrinthe.
     */

    public CritterGraphicsFactory(double scale, MazeState state) {
        this.scale = scale;
        this.state = state;
    }

    /**
     * Crée un objet GraphicsUpdater pour une créature donnée.
     * 
     * @param critter La créature pour laquelle créer les graphiques.
     * @return Un objet GraphicsUpdater pour mettre à jour les graphiques de la
     *         créature.
     */
    public GraphicsUpdater makeGraphics(Critter critter) {
        var size = 0.82;
        Data.setSize(size);
        var url = (critter instanceof PacMan) ? switch (Data.getskin()) {// switch qui sert a choisir l'image correspond
                                                                         // a la créature et a son skin
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
        System.out.println(critter);
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        if (!(critter instanceof PacMan)) {
            ((Ghost) critter).setImage(image);
        }
        Data.setImageView(image);
        Image anim1 = new Image("pacmansuper1.png", scale * size, scale * size, true,
                true);
        Image anim2 = new Image("pacmansuper2bis.png", scale * size, scale * size,
                true, true);
        Image anim3 = new Image("pacmansuper3bis.png", scale * size, scale * size,
                true, true);
        Image anim4 = new Image("pacmansuper4.png", scale * size, scale * size, true,
                true);
        Image anim5 = new Image("pacmansuper5.png", scale * size, scale * size, true,
                true);
        Image animoriginal = new Image("pacman.png", scale * size, scale * size, true,
                true);
        Image anim6 = new Image("pacmansuper5.png", scale * size, scale * size, true,
                true);

        Image anim1B = new Image("src_main_resources_pacmanbluesuper1.png", scale * size, scale * size, true,
                true);
        Image anim2B = new Image("src_main_resources_pacmanbluesuper2.png", scale * size, scale * size, true,
                true);
        Image anim3B = new Image("src_main_resources_pacmanbluesuper3.png", scale * size, scale * size, true,
                true);
        Image anim4B = new Image("src_main_resources_pacmanbluesuper4.png", scale * size, scale * size, true,
                true);
        Image anim5B = new Image("src_main_resources_pacmanbluesuper5.png", scale * size, scale * size, true,
                true);
        Image animoriginalB = new Image("pacmanblue.png", scale * size, scale * size, true,
                true);
        Image anim6B = new Image("src_main_resources_pacmanbluesuper5.png", scale * size, scale * size, true,
                true);

        Image anim1G = new Image("src_main_resources_pacmangreensuper1.png", scale * size, scale * size, true, true);
        Image anim2G = new Image("src_main_resources_pacmangreensuper2.png", scale * size, scale * size, true, true);
        Image anim3G = new Image("src_main_resources_pacmangreensuper3.png", scale * size, scale * size, true, true);
        Image anim4G = new Image("src_main_resources_pacmangreensuper4.png", scale * size, scale * size, true, true);
        Image anim5G = new Image("src_main_resources_pacmangreensuper5.png", scale * size, scale * size, true, true);
        Image animoriginalG = new Image("pacmangreen.png", scale * size, scale * size, true, true);
        Image anim6G = new Image("src_main_resources_pacmangreensuper5.png", scale * size, scale * size, true, true);

        Image[] framesNORMAL = { anim1, anim2, anim3, anim4, anim5, anim6, animoriginal };
        Image[] framesBLUE = { anim1B, anim2B, anim3B, anim4B, anim5B, anim6B, animoriginalB };
        Image[] framesGREEN = { anim1G, anim2G, anim3G, anim4G, anim5G, anim6G, animoriginalG };

        int[] index = { 0 };
        // **Fonction qui crée une nouvelle instance de timeline qui execute un certain
        // bloc de code tout les 200ms*/
        Image[] framestest = switch (Data.getskin()) {// switch qui sert a choisir l'image correspond a la
                                                      // créature et
            // a son skin
            case 1 -> framesNORMAL;
            case 2 -> framesBLUE;
            case 3 -> framesGREEN;
            default -> framesNORMAL;
        };
        Timeline timelineN = new Timeline(new KeyFrame(Duration.millis(200), e -> {

            image.setImage(framesNORMAL[index[0]]);

            index[0] = (index[0] + 1) % framesNORMAL.length;

            if (index[0] == 6) {
                image.setVisible(false);

            }

        }));

        Timeline timelineB = new Timeline(new KeyFrame(Duration.millis(200), e -> {

            image.setImage(framesBLUE[index[0]]);

            index[0] = (index[0] + 1) % framesBLUE.length;

            if (index[0] == 6) {
                image.setVisible(false);

            }

        }));

        Timeline timelineG = new Timeline(new KeyFrame(Duration.millis(200), e -> {

            image.setImage(framesGREEN[index[0]]);

            index[0] = (index[0] + 1) % framesGREEN.length;

            if (index[0] == 6) {
                image.setVisible(false);

            }

        }));

        return new GraphicsUpdater() {
            @Override
            public void update() {
                tick++;
                // Change le skin des fantomes en fonction de l'etat energized de pacman
                int skin = critter.changeSkin();
                if (critter instanceof PacMan) {
                    if (PacMan.INSTANCE.changeSkin() != Data.getskin()) {
                        var url1 = switch (Data.getskin()) {// switch qui sert a choisir l'image correspond a la
                                                            // créature et
                                                            // a son skin
                            case 1 -> "pacman.png";
                            case 2 -> "pacmanblue.png";
                            case 3 -> "pacmangreen.png";
                            default -> "pacman.png";
                        };

                        PacMan.INSTANCE.setSkin(Data.getskin());
                        image.setImage(new Image(url1, scale * size, scale * size, true, true));
                    }
                    // Met à jour la position de l'image selon la position de la créature
                    // System.out.println(state.GetPacmanMort());
                    image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                    image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                    var url12 = switch (Data.getskin()) {// switch qui sert a choisir l'image correspond a la créature
                                                         // et
                                                         // a son skin
                        case 1 -> "pacman.png";
                        case 2 -> "pacmanblue.png";
                        case 3 -> "pacmangreen.png";
                        default -> "pacman.png";
                    };

                    var url2 = switch (Data.getskin()) {
                        case 1 -> "pacmanfermer2.png";
                        case 2 -> "pacmanblue_fermer.png";
                        case 3 -> "pacmangreen_fermer.png";
                        default -> "pacmanfermer2.png";
                    };

                    if (!state.GetPacmanMort()) {
                        if (critter.getDirection() == Direction.NONE) {
                            // Afficher l'image de la bouche ouverte
                            image.setImage(new Image(url12, scale * size, scale * size, true, true));
                        } else if (System.currentTimeMillis() - lastImageChangeTime > IMAGE_CHANGE_INTERVAL) {
                            mouthOpen = !mouthOpen; // Inverse l'état de la bouche
                            var imageUrl = mouthOpen ? url12 : url2;
                            image.setImage(new Image(imageUrl, scale * size, scale * size, true, true));
                            lastImageChangeTime = System.currentTimeMillis();
                        }
                    }
                    if (state.GetPacmanMort()) {
                        image.setRotate(270);
                        image.setScaleX(1);
                        if (!state.GetAudioPlayed()) {
                            String soundFile = "src/main/resources/PacmanDie.wav";
                            Media sound = new Media(new File(soundFile).toURI().toString());
                            MediaPlayer mediaPlayer = new MediaPlayer(sound);
                            mediaPlayer.play();
                            state.SetAudioPlayed(true);

                        }
                        PacMan.INSTANCE.freeze();
                        if (Data.getskin() == 1) {
                            timelineN.setCycleCount(framesNORMAL.length);
                            timelineN.play();
                            timelineN.setOnFinished(e -> {
                                image.setVisible(true);
                                state.SetPacmanMort(false);
                                state.playerLost();
                                image.setRotate(0);
                                image.setScaleX(1);
                                PacMan.INSTANCE.unfreeze();

                            });
                        } else if (Data.getskin() == 2) {
                            timelineB.setCycleCount(framesBLUE.length);
                            timelineB.play();
                            timelineB.setOnFinished(e -> {
                                image.setVisible(true);
                                state.SetPacmanMort(false);
                                state.playerLost();
                                image.setRotate(0);
                                image.setScaleX(1);
                                PacMan.INSTANCE.unfreeze();

                            });
                        } else if (Data.getskin() == 3) {
                            timelineG.setCycleCount(framesGREEN.length);
                            timelineG.play();
                            timelineG.setOnFinished(e -> {
                                image.setVisible(true);
                                state.SetPacmanMort(false);
                                state.playerLost();
                                image.setRotate(0);
                                image.setScaleX(1);
                                PacMan.INSTANCE.unfreeze();

                            });
                        } else {
                            timelineN.setCycleCount(framesNORMAL.length);
                            timelineN.play();
                            timelineN.setOnFinished(e -> {
                                image.setVisible(true);
                                state.SetPacmanMort(false);
                                state.playerLost();
                                image.setRotate(0);
                                image.setScaleX(1);
                                PacMan.INSTANCE.unfreeze();

                            });
                        }
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

                } else {
                    if (critter instanceof Ghost && ((Ghost) critter).getManger()) {
                        image.setImage(new Image("yeux.png", scale * size, scale * size, true, true));
                    } else if (skin == 1) {
                        image.setImage(new Image("vulnerable_ghost.png", scale * size, scale * size, true, true));
                    } else if (skin == 0) {
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
                    }
                    // Met à jour la position de l'image selon la position de la créature
                    // System.out.println(state.GetPacmanMort());
                    image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                    image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                    if (!((Ghost) critter).enFuite() && !((Ghost) critter).getManger()) {
                        String CritterImage = setCritterImage(critter, size);
                        if (tick / 30 % 2 == 0) { // on change le sprite du fantome en fonction du tick
                            image.setImage(new Image(CritterImage + "1.png", scale * size, scale * size, true, true));
                        } // sprite 2
                        else {
                            image.setImage(new Image(CritterImage + ".png", scale * size, scale * size, true, true));
                        } // sprite 1
                    }
                    Ghost ghost = (Ghost) critter;
                    image.setVisible(ghost.isVisible());

                }
            }

            @Override
            public Node getNode() {
                return image;
            }
        };
    }

    /**
     * Définit le debut du nom du fichier image pour la créature donnée en fonction
     * de sa direction.
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
