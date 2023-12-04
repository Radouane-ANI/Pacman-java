package model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import config.Cell;
import config.MazeConfig;
import datagame.Data;
import geometry.IntCoordinates;

/**
 * Enumération représentant les différents types de bonus dans le jeu.
 */
public enum Bonus {

    CERISE, ECLAIR, COEUR, ENCRE, GLACON, PERDU;

    private boolean apparut;
    private boolean actif;
    private IntCoordinates pos;
    private boolean perdu;
    private boolean encrer;

    /**
     * Obtient l'etat encrer ou non 
     * 
     * @return L'ecran est en etat encrer ou non 
     */
    public boolean isEncrer() {
        return encrer;
    }

    /**
     * Obtient la position du bonus.
     * 
     * @return Pacman est en etat perdu ou non
     */
    public boolean isPerdu() {
        return perdu;
    }

    /**
     * modifie la valeur de apparut
     *
     * @param apparut boolean bonus apparut ou non
     */
    public void setApparut(boolean apparut) {
        this.apparut = apparut;
    }

    /**
     * modifie la valeur de actif
     *
     * @param actif boolean bonus actif ou non
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * Obtient la position du bonus.
     *
     * @return Les coordonnées du bonus.
     */
    public IntCoordinates getPos() {
        return pos;
    }

    /**
     * Vérifie si le bonus est actif.
     *
     * @return True si le bonus est actif, sinon False.
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Vérifie si le bonus a déjà été généré.
     *
     * @return True si le bonus a déjà été généré, sinon False.
     */
    public boolean isApparut() {
        return apparut;
    }

    private static long time = System.currentTimeMillis();
    private static Random random = new Random();
    static MazeConfig config = MazeConfig.makeExample1();

    /**
     * Génère un bonus aléatoire en fonction d'une probabilité.
     *
     * @return Un bonus généré aléatoirement ou null s'il n'y a pas de bonus.
     */
    private static Bonus generateRandomBonus() {
        int rd = random.nextInt(6);

        if (rd == 1) {
            Bonus bonus = chooseRandomBonus();

            if (!bonus.apparut) {
                bonus.apparut = true;
                return bonus;
            }
        }

        return null;
    }

    /**
     * Choisi un type de bonus aléatoire parmi les types disponibles.
     *
     * @return Un type de bonus choisi aléatoirement.
     */
    private static Bonus chooseRandomBonus() {
        Bonus[] Bonuss = Bonus.values();
        int randomIndex = random.nextInt(Bonuss.length);
        return Bonuss[randomIndex];
    }

    /**
     * Génère et active un bonus aléatoire sous certaines conditions.
     */
    public static void spawnBonus() {
        boolean autorise = true;

        for (Bonus bonus : Bonus.values()) {
            if (bonus.actif) {
                autorise = false;
            }
        }

        if (System.currentTimeMillis() - time > 5000 && autorise) {
            time = System.currentTimeMillis();
            Bonus b = generateRandomBonus();
            if (b != null) {
                b.actif = true;
                b.pos = spawnPosition();
            }
        }
    }

    /**
     * Gère l'effet de manger un bonus, applique les récompenses associées.
     */
    public void manger() {
        Timer timer = new Timer();

        switch (this) {
            case CERISE:
                Data.setScore(100);
                break;
            case COEUR:
                Data.setLive(1);
                break;
            case ECLAIR:
                PacMan.INSTANCE.setSpeed(8);

                // Timer pour rétablir la vitesse après un certain délai
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PacMan.INSTANCE.setSpeed(4);
                        timer.cancel();
                    }
                }, 10000); // 10 secondes
                break;
            case ENCRE:
                encrer = true;

                // Timer pour rétablir les directions de pacman
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        encrer = false;
                    }
                }, 7000); // 7 secondes
                break;
            case GLACON:
                PacMan.INSTANCE.setSpeed(2);

                // Timer pour rétablir la vitesse après un certain délai
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PacMan.INSTANCE.setSpeed(4);
                    }
                }, 8000); // 8 secondes

                break;
            case PERDU:
                perdu = true;

                // Timer pour rétablir les directions de pacman
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        perdu = false;
                    }
                }, 7000); // 7 secondes
                break;
            default:
                break;
        }
        time = System.currentTimeMillis();
        this.actif = false;
    }

    /**
     * Génère une position de spawn acessible pour le bonus.
     *
     * @return Les coordonnées de la position de spawn.
     */
    private static IntCoordinates spawnPosition() {
        IntCoordinates i;
        do {
            int x = random.nextInt(config.getWidth());
            int y = random.nextInt(config.getHeight());
            i = new IntCoordinates(x, y);
        } while (config.getCell(i).initialContent() != Cell.Content.DOT);
        return i;
    }
}
