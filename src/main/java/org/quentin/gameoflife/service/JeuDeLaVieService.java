package org.quentin.gameoflife.service;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class JeuDeLaVieService {

    @Getter
    private boolean[][] plateau;
    private final int taille = 10;
    private volatile boolean enCours = false;
    private Thread threadJeu;

    // Règles personnalisables pour la survie et la naissance
    @Getter
    private Set<Integer> reglesSurvie = new HashSet<>(Arrays.asList(2, 3));
    @Getter
    private Set<Integer> reglesNaissance = new HashSet<>(Arrays.asList(3));


    private static final Logger logger = LoggerFactory.getLogger(JeuDeLaVieService.class);

    @Autowired
    private SimpMessagingTemplate template;

    public JeuDeLaVieService() {
        this.plateau = new boolean[taille][taille];
        initialiserJeu();
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
        return count;
    }

    public synchronized void calculerEtatSuivant() {
        if (!enCours) return;
        boolean[][] nouveauPlateau = new boolean[taille][taille];
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                int voisinsVivants = compterVoisinsVivants(i, j);
                if(plateau[i][j]) {
                    nouveauPlateau[i][j] = reglesSurvie.contains(voisinsVivants);
                } else {
                    nouveauPlateau[i][j] = reglesNaissance.contains(voisinsVivants);
                }
            }
        }

        plateau = nouveauPlateau;

        if (estPlateauStableOuMort()) {
            stopperJeu();
            return;
        }

        template.convertAndSend("/topic/jeu", this.plateau);
    }

    private boolean estPlateauStableOuMort() {
        // Logique pour vérifier si le plateau est stable ou toutes les cellules sont mortes
        for (boolean[] ligne : plateau) {
            for (boolean cellule : ligne) {
                if (cellule) {
                    return false;
                }
            }
        }
        return true;
    }

    public void demarrerJeu() {
        if (enCours) {
            logger.info("Le jeu est déjà en cours.");
            return;
        }
        enCours = true;
        threadJeu = new Thread(() -> {
            while (enCours) {
                calculerEtatSuivant();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Thread du jeu interrompu", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        threadJeu.start();
    }

    public synchronized void stopperJeu() {
        enCours = false;
        if (threadJeu != null) {
            threadJeu.interrupt();
        }
    }

    public void setReglesSurvie(Set<Integer> nouvellesRegles) {
        synchronized (this.reglesSurvie) {
            this.reglesSurvie.clear();
            this.reglesSurvie.addAll(nouvellesRegles);
        }
    }

    public void setReglesNaissance(Set<Integer> nouvellesRegles) {
        synchronized (this.reglesNaissance) {
            this.reglesNaissance.clear();
            this.reglesNaissance.addAll(nouvellesRegles);
        }
    }
}
