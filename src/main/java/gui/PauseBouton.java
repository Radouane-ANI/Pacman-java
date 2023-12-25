package gui;

import java.io.File;

import datagame.Data;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.ButtonAction;

public class PauseBouton {
    private final Group FinalScreenLayout = new Group();
    private final Button pause = new Button("Pause");
    
    double width = Data.getWidth();
    double height = Data.getHeight();

    public PauseBouton(ButtonAction action) {
        setupButton(pause, action);

        // Positionnez les boutons et l'image à l'emplacement souhaité
        pause.setLayoutX(width*0.24);
        pause.setLayoutY(height*0.16);

        // Ajoutez les boutons et l'image au conteneur
        FinalScreenLayout.getChildren().addAll(pause);
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

    public Node getFinalScreenLayout(){return FinalScreenLayout;}
}
