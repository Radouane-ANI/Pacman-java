package model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import geometry.IntCoordinates;

/**
 * Enumération représentant les différents types de bonus dans le jeu.
 */
public enum Bonus {

    CERISE, ECLAIR, COEUR;

    private boolean apparut;
    private boolean actif;
    private IntCoordinates pos;

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

    /**
     * Génère un bonus aléatoire en fonction d'une probabilité.
     *
     * @return Un bonus généré aléatoirement ou null s'il n'y a pas de bonus.
     */
    public static Bonus generateRandomBonus() {
        int rd = random.nextInt(10);

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
                b.pos = new IntCoordinates(3, 3);
            }
        }
    }

    /**
     * Gère l'effet de manger un bonus, applique les récompenses associées.
     */
    public void manger() {
        switch (this) {
            case CERISE:
                MazeState.addScore(100);
                break;
            case COEUR:
                MazeState.addLives(1);
                break;
            case ECLAIR:
                PacMan.INSTANCE.setSpeed(8);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PacMan.INSTANCE.setSpeed(4);
                        timer.cancel();
                    }
                }, 7000); // 7 secondes
                break;
        }
        time = System.currentTimeMillis();
        this.actif = false;
    }
}
