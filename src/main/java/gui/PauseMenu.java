package gui;
import datagame.*;
import model.ButtonAction;
import model.PacMan;

import java.io.File;

import datagame.Data;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PauseMenu {
    private final Group ScreenLayout = new Group();
    private final Button reprendreButton = new Button("Reprendre");
    private final Button playAgainButton = new Button("Recommencer");
    private final Button exitButton = new Button("Quitter");
    private final Button homeButton = new Button("Home");

    double width = Data.getWidth();
    double height = Data.getHeight();

    public PauseMenu() {
        Data.getpause().getFinalScreenLayout().setVisible(false);
        Stage primaryStage = Data.getprimaryStage();
        ButtonAction action = () -> {
            System.out.println("Reset game");
            Data.getMaze().restartGame();
        }; 
        ButtonAction action2 = ()-> {
            Data.getRoot().getChildren().remove(Data.getRoot().getChildren().size() - 1);
            Data.setRunning(true);
            long tempsRestant = Data.getHeurePause() - PacMan.INSTANCE.getDernierEnergiseur();
            if ( tempsRestant < 10000) {
                PacMan.INSTANCE.setEnergized(true, 10000 - (int) tempsRestant);
            }
            Data.getpause().getFinalScreenLayout().setVisible(true);
        };
        setupButton(playAgainButton, action);//À chaque nouvelle partie une nouvelle instance de Game est crée ce qui permet un réeinitialisation complete du jeu

        setupButton(exitButton, () -> {
            System.exit(0);});
        setupButton(homeButton, () -> {
            primaryStage.setScene(Data.getmainMenu().getScene());
            Data.getpause().getFinalScreenLayout().setVisible(true);
        });

        setupButton(reprendreButton, action2);

        Image pause_image = new Image("pause.png");
        ImageView imageView1 = new ImageView(pause_image);
        //imageView1.setFitHeight((pause_image.getHeight()/height)*0.1);
        //imageView1.setFitWidth((pause_image.getWidth()/width)*0.1);

        imageView1.setLayoutX(width*0.2);
        imageView1.setLayoutY(height*0.2);

        // Positionnez les boutons et l'image à l'emplacement souhaité
        playAgainButton.setLayoutX(width*0.52);
        playAgainButton.setLayoutY(height*0.65);
        exitButton.setLayoutX(width*0.70);
        exitButton.setLayoutY(height*0.65);
        homeButton.setLayoutX(width*0.42);
        homeButton.setLayoutY(height*0.65);
        reprendreButton.setLayoutX(width*0.28);
        reprendreButton.setLayoutY(height*0.65);
        // Ajoutez les boutons et l'image au conteneur
        ScreenLayout.getChildren().addAll(playAgainButton, exitButton, homeButton,reprendreButton,imageView1);
    }

    /**
     * Permet d'affecter une aciton a un bouton
     * @param button 
     * @param action
     */
    private void setupButton(Button button, ButtonAction action) {
        button.setOnAction(e -> action.performAction());
        button.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
        gestionButton(button);
    }

    /**
     * Methode qui permet de donner un style au bouton et de jouer un son lorsque l'on passe ou clique dessus
     * @param b bouton sur lequel les changemetns seront appliqués
     */
    private void gestionButton(Button b){
        Media survolMedia = new Media(new File("src/main/resources/onclic.mp3").toURI().toString());
        MediaPlayer survolPlayer = new MediaPlayer(survolMedia);

        // Gestion du survol
        b.setOnMouseEntered(e -> {
            b.setStyle("-fx-background-color: violet; -fx-text-fill: black; -fx-font-size: 16px;");
            survolPlayer.stop(); // Arrêter la lecture en cours, s'il y en a une
            survolPlayer.play(); // Lire le son de survol
        });

        // Gérer la sortie du survol du bouton
        b.setOnMouseExited(e -> {
            b.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;");
            survolPlayer.stop();
        });

        // Gérer clic souris
        b.setOnMousePressed(e -> b.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
        b.setOnMouseReleased(e -> b.setStyle("-fx-background-color: black; -fx-text-fill: violet; -fx-font-size: 16px;"));
    }

    public Node getScreenLayout(){return ScreenLayout;}
}
