package model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import geometry.IntCoordinates;

public enum Bonus {

    CERISE, ECLAIR, COEUR;

    private boolean apparut;
    private boolean actif;
    private IntCoordinates pos;

    public IntCoordinates getPos() {
        return pos;
    }

    public boolean isActif() {
        return actif;
    }

    public boolean isApparut() {
        return apparut;
    }

    private static long time = System.currentTimeMillis();
    private static Random random = new Random();

    public static Bonus generateRandomBonus() {
        // Générez un nombre aléatoire pour décider si un bonus doit apparaître
        int rd = random.nextInt(3);

        if (rd == 1) { // 1 chance sur 3 qu'un bonus apparait
            // Générez un nombre aléatoire pour choisir le type de bonus
            Bonus bonus = chooseRandomBonus();

            // Vérifiez si le bonus n'est pas déjà apparu et n'est pas actif
            if (!bonus.apparut) {
                bonus.apparut = true; // Marquez le bonus comme apparu
                return bonus;
            }
        }

        return null; // Aucun bonus n'apparaît ou tous les bonus sont déjà apparus
    }

    private static Bonus chooseRandomBonus() {
        // Générez un nombre aléatoire pour choisir le type de bonus
        Bonus[] Bonuss = Bonus.values();
        int randomIndex = random.nextInt(Bonuss.length);
        return Bonuss[randomIndex];
    }

    public static void spawnBonus() {
        boolean autorise = true; // n'autorise pas deux bonus actif en parallel
        for (Bonus bonus : Bonus.values()) {
            if(bonus.actif){
                autorise = false; 
            }
        }
        if (System.currentTimeMillis() - time > 3000 && autorise) {
            time = System.currentTimeMillis();
            Bonus b = generateRandomBonus();
            if (b != null) {
                b.actif = true;
                b.pos = new IntCoordinates(0, 0);

                // Planifiez une tâche pour désactiver le bonus après 10 secondes
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        b.actif = false;
                        timer.cancel();
                    }

                }, 10000); // 10 000 millisecondes = 10 secondes

            }
        }
    }

    
}
