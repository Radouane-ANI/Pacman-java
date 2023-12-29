package gui;

import java.io.File;

import datagame.Data;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.ButtonAction;

public class PauseBouton {
    private final Label pause = new Label("Pause");
    
    double width = Data.getWidth();
    double height = Data.getHeight();

    public PauseBouton(ButtonAction action) {
        setupButton(pause, action);

        // Positionnez les boutons et l'image à l'emplacement souhaité
        pause.setLayoutX(width*0.24);
        pause.setLayoutY(height*0.86);
    }

    /**
     * Permet d'affecter une action a un bouton
     * @param button 
     * @param action
     */
    private void setupButton(Label button, ButtonAction action) {
        button.setOnMouseClicked(e -> action.performAction());
        button.setStyle("-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
        gestionButton(button);
    }

    /**
     * Methode qui permet de donner un style au bouton et de jouer un son lorsque l'on passe ou clique dessus
     * @param b bouton sur lequel les changemetns seront appliqués
     */
    private void gestionButton(Label b){
        Media survolMedia = new Media(new File("src/main/resources/onclic.mp3").toURI().toString());
        MediaPlayer survolPlayer = new MediaPlayer(survolMedia);

        // Gestion du survol
        b.setOnMouseEntered(e -> {
            b.setStyle("-fx-text-fill: yellow;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
            survolPlayer.stop(); // Arrêter la lecture en cours, s'il y en a une
            survolPlayer.play(); // Lire le son de survol
        });

        // Gérer la sortie du survol du bouton
        b.setOnMouseExited(e -> {
            b.setStyle("-fx-text-fill: white;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;");
            survolPlayer.stop();
        });

        // Gérer clic souris
        b.setOnMousePressed(e -> b.setStyle("-fx-text-fill: yellow;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;"));
        b.setOnMouseReleased(e -> b.setStyle("-fx-text-fill: yellow;-fx-alignment: center;-fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;"));
    }

    public Label getFinalScreenLayout(){return pause;}
}
