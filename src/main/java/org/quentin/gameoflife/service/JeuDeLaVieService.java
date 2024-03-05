package org.quentin.gameoflife.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Random;

@Service
public class JeuDeLaVieService {
    private boolean[][] plateau;
    private final int taille = 10;
    private volatile boolean enCours = false;
    private Thread threadJeu;

    private static final Logger logger = LoggerFactory.getLogger(JeuDeLaVieService.class);

    @Autowired
    private SimpMessagingTemplate template;

    public JeuDeLaVieService() {
        this.plateau = new boolean[taille][taille];
        initialiserJeu();
        logger.info("Jeu de la vie initialisé.");
    }

    public synchronized void initialiserJeu() {
        if (enCours) {
            stopperJeu();
        }
        Random rand = new Random();
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                plateau[i][j] = rand.nextBoolean();
            }
        }
        logger.info("Jeu initialisé avec un plateau de taille {}x{}", taille, taille);
    }

    private int compterVoisinsVivants(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int dx = x + i;
                int dy = y + j;
                if (dx >= 0 && dx < taille && dy >= 0 && dy < taille && plateau[dx][dy]) {
                    count++;
                }
            }
        }
        logger.debug("Nombre de voisins vivants pour la cellule ({}, {}): {}", x, y, count);
        return count;
    }

    public synchronized void calculerEtatSuivant() {
        if (!enCours) return;
        boolean[][] nouveauPlateau = new boolean[taille][taille];
        boolean estIdentique = true;
        logger.info("Calcul de l'état suivant du plateau.");
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                int voisinsVivants = compterVoisinsVivants(i, j);
                nouveauPlateau[i][j] = plateau[i][j] ? voisinsVivants == 2 || voisinsVivants == 3 : voisinsVivants == 3;
                if (nouveauPlateau[i][j] != plateau[i][j]) estIdentique = false;
            }
        }

        plateau = nouveauPlateau;

        if (estIdentique || estMort()) {
            stopperJeu();
            logger.info("Le jeu s'est arrêté car le plateau est stable ou tous les cellules sont mortes.");
            return;
        }

        template.convertAndSend("/topic/jeu", this.plateau);
        logger.info("État suivant du plateau envoyé avec succès.");
    }


    private boolean estMort() {
        for (boolean[] ligne : plateau) {
            for (boolean cellule : ligne) {
                if (cellule) {
                    return false; // Au moins une cellule est vivante.
                }
            }
        }
        return true; // Toutes les cellules sont mortes.
    }

    public void demarrerJeu() {
        if (enCours) {
            logger.info("Le jeu est déjà en cours.");
            return;
        }
        enCours = true;
        threadJeu = new Thread(() -> {
            logger.info("Thread du jeu démarré.");
            while (enCours) {
                calculerEtatSuivant();
                try {
                    Thread.sleep(1000); // Ajustez selon le rythme souhaité
                } catch (InterruptedException e) {
                    logger.error("Thread du jeu interrompu", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        threadJeu.start();
    }

    public synchronized void stopperJeu() {
        logger.info("Arrêt du jeu.");
        enCours = false;
        if (threadJeu != null) {
            threadJeu.interrupt();
        }
    }
}
