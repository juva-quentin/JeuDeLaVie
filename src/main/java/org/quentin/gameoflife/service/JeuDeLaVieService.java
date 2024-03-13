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
    private final int size = 10;
    private volatile boolean running = false;
    private Thread gameThread;

    @Getter
    private Set<Integer> reglesSurvie = new HashSet<>(Arrays.asList(2, 3));
    @Getter
    private Set<Integer> reglesNaissance = new HashSet<>(Arrays.asList(3));


    private static final Logger logger = LoggerFactory.getLogger(JeuDeLaVieService.class);

    @Autowired
    private SimpMessagingTemplate template;

    public JeuDeLaVieService() {
        this.plateau = new boolean[size][size];
        initGame();
    }

    public synchronized void initGame() {
        if (running) {
            stopGame();
        }
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                plateau[i][j] = rand.nextBoolean();
            }
        }
        logger.info("Jeu initialisé avec un plateau de size {}x{}", size, size);
    }

    private int counteInLife(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int dx = x + i;
                int dy = y + j;
                if (dx >= 0 && dx < size && dy >= 0 && dy < size && plateau[dx][dy]) {
                    count++;
                }
            }
        }
        return count;
    }

    public synchronized void countNextState() {
        if (!running) return;
        boolean[][] nouveauPlateau = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int voisinsVivants = counteInLife(i, j);
                if(plateau[i][j]) {
                    nouveauPlateau[i][j] = reglesSurvie.contains(voisinsVivants);
                } else {
                    nouveauPlateau[i][j] = reglesNaissance.contains(voisinsVivants);
                }
            }
        }

        plateau = nouveauPlateau;

        if (stableOrDead()) {
            stopGame();
            return;
        }

        template.convertAndSend("/topic/jeu", this.plateau);
    }

    private boolean stableOrDead() {
        for (boolean[] ligne : plateau) {
            for (boolean cellule : ligne) {
                if (cellule) {
                    return false;
                }
            }
        }
        return true;
    }

    public void startGame() {
        if (running) {
            logger.info("Le jeu est déjà en cours.");
            return;
        }
        running = true;
        gameThread = new Thread(() -> {
            while (running) {
                countNextState();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Thread du jeu interrompu", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.start();
    }

    public synchronized void stopGame() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
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
