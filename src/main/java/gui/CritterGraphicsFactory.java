package gui;

import static model.Ghost.BLINKY;
import static model.Ghost.CLYDE;
import static model.Ghost.INKY;
import static model.Ghost.PINKY;
import static model.Ghost.remarcheLe;

import java.util.concurrent.TimeUnit;

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
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            // int index = (int) ((System.currentTimeMillis() / 200) % frames.length);
            // image.setImage(frames[index]);
            // System.out.println("index : " + index);
            image.setImage(frames[index[0]]);
            System.out.println("index : " + index[0]);
            index[0] = (index[0] + 1) % frames.length;

            if (index[0] == 6) {
                image.setVisible(false);

            }

        }));

        return new GraphicsUpdater() {
            @Override
            public void update() {
                System.out.println(state.GetBoulbi());
                image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                if (critter instanceof PacMan) {
                    if (state.GetBoulbi()) {
                        image.setRotate(270);
                        image.setScaleX(1);
                        timeline.setCycleCount(frames.length);
                        timeline.play();
                        PacMan.INSTANCE.freeze();
                        timeline.setOnFinished(e -> {
                            image.setVisible(true);
                            state.SetBoulbi(false);
                            state.playerLost();
                        });

                        // System.out.println("aaa");
                        // Image anim1 = new Image("pacmansuper1.png", scale * size, scale * size, true,
                        // true);
                        // image.setImage(anim1);
                        // System.out.println("anim1");
                        // Image anim2 = new Image("pacmansuper2bis.png", scale * size, scale * size,
                        // true, true);
                        // image.setImage(anim2);
                        // System.out.println("anim2");
                        // Image anim3 = new Image("pacmansuper3bis.png", scale * size, scale * size,
                        // true, true);
                        // image.setImage(anim3);
                        // System.out.println("anim3");
                        // Image anim4 = new Image("pacmansuper4.png", scale * size, scale * size, true,
                        // true);
                        // image.setImage(anim4);
                        // System.out.println("anim4");
                        // Image anim5 = new Image("pacmansuper5.png", scale * size, scale * size, true,
                        // true);
                        // image.setImage(anim5);

                    } else {
                        // image.setImage(animoriginal);
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
                    for (Ghost ghost : Ghost.values()) {
                        if (!ghost.isVisible()) {
                            switch ((Ghost) critter) {
                                case BLINKY -> image.setVisible(BLINKY.isVisible());
                                case CLYDE -> image.setVisible(CLYDE.isVisible());
                                case INKY -> image.setVisible(INKY.isVisible());
                                case PINKY -> image.setVisible(PINKY.isVisible());

                            }

                        } else {
                            switch ((Ghost) critter) {
                                case BLINKY -> image.setVisible(BLINKY.isVisible());
                                case CLYDE -> image.setVisible(CLYDE.isVisible());
                                case INKY -> image.setVisible(INKY.isVisible());
                                case PINKY -> image.setVisible(PINKY.isVisible());

                            }

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