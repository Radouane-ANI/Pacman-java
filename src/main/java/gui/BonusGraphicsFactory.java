package gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Bonus;

public final class BonusGraphicsFactory {
    private final double scale;

    public BonusGraphicsFactory(double scale) {
        this.scale = scale;
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
        var size = 0.82;

        // Récupère l'URL de l'image en fonction du type de bonus
        var url = switch (bonus) {
            case CERISE -> "cerise.png";
            case COEUR -> "coeur.png";
            case ECLAIR -> "eclair.png";
            case ENCRE -> "poulpe.png";
            case GLACON -> "glacon.png";
            case PERDU -> "perdu.png";
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
     * Crée un GraphicsUpdater pour un bonus l'encre des bonus
     *
     * @return Un GraphicsUpdater pour le bonus spécifié.
     */
    public GraphicsUpdater makeGraphics() {
        boolean afficher = Bonus.ENCRE.isEncrer();
        // Définit la taille de l'image
        var size = 20;

        // Récupère l'URL de l'image
        var url = "encre.png";

        // Crée une nouvelle ImageView avec l'image correspondante
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        image.setVisible(afficher);

        // Retourne un nouveau GraphicsUpdater personnalisé pour le bonus
        return new GraphicsUpdater() {
            /**
             * Met à jour l'affichage en fonction de l'état du bonus.
             */
            @Override
            public void update() {
                boolean afficher = Bonus.ENCRE.isEncrer();
                if (afficher) {
                    image.setTranslateX((Bonus.ENCRE.getPos().x() + (1 - size) / 2) * scale);
                    image.setTranslateY((Bonus.ENCRE.getPos().y() + (1 - size) / 2) * scale);
                }
                image.setVisible(afficher);
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

}