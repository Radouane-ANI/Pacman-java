package gui;

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

        Image[] frames = { anim1, anim2, anim3, anim4, anim5, anim6, animoriginal };
        int[] index = { 0 };
        // **Fonction qui crée une nouvelle instance de timeline qui execute un certain
        // bloc de code tout les 200ms*/

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            // int index = (int) ((System.currentTimeMillis() / 200) % frames.length);
            // image.setImage(frames[index]);
            // System.out.println("index : " + index);
            image.setImage(frames[index[0]]);
            // System.out.println("index : " + index[0]);
            index[0] = (index[0] + 1) % frames.length;

            if (index[0] == 6) {
                image.setVisible(false);

            }

        }));

        return new GraphicsUpdater() {
            @Override
            public void update() {
                // System.out.println(state.GetPacmanMort());
                image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                if (critter instanceof PacMan) {
                    if (state.GetPacmanMort()) {
                        image.setRotate(270);
                        image.setScaleX(1);
                        timeline.setCycleCount(frames.length);
                        timeline.play();
                        if (!state.GetAudioPlayed()) {
                            try { // Lorsque le pacman mange un dot il emet un son.
                                File soundFile = new File("src/main/resources/PacmanDie.wav");
                                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                                Clip clip = AudioSystem.getClip();
                                clip.open(audioIn);
                                clip.start();
                                state.SetAudioPlayed(true);
                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                e.printStackTrace();
                            }
                        }
                        PacMan.INSTANCE.freeze();
                        timeline.setOnFinished(e -> {
                            image.setVisible(true);
                            state.SetPacmanMort(false);
                            state.playerLost();
                            image.setRotate(0);
                            image.setScaleX(1);
                            PacMan.INSTANCE.unfreeze();

                        });
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
                if (critter instanceof Ghost) {
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
}