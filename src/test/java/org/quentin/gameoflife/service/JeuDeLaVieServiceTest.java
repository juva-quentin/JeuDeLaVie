package org.quentin.gameoflife.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

class JeuDeLaVieServiceTest {

    @InjectMocks
    private JeuDeLaVieService jeuDeLaVieService;

    @Mock
    private SimpMessagingTemplate template;

    @BeforeEach
    void setUp() {
        initMocks(this);
        jeuDeLaVieService.initialiserJeu(); // Assurez-vous d'initialiser le jeu pour chaque test
    }

    @Test
    void whenGameStarts_thenGridShouldNotBeEmpty() {
        jeuDeLaVieService.demarrerJeu();
        assertFalse(isGridEmpty(jeuDeLaVieService.getPlateau()), "La grille ne devrait pas être vide après le démarrage du jeu.");
    }

    @Test
    void whenRulesAreChanged_thenNewRulesShouldApply() {
        Set<Integer> newSurviveRules = new HashSet<>(Arrays.asList(2, 3, 4));
        Set<Integer> newBirthRules = new HashSet<>(Arrays.asList(3, 6));

        jeuDeLaVieService.setReglesSurvie(newSurviveRules);
        jeuDeLaVieService.setReglesNaissance(newBirthRules);

        assertEquals(newSurviveRules, jeuDeLaVieService.getReglesSurvie(), "Les nouvelles règles de survie devraient être appliquées.");
        assertEquals(newBirthRules, jeuDeLaVieService.getReglesNaissance(), "Les nouvelles règles de naissance devraient être appliquées.");
    }

    private boolean isGridEmpty(boolean[][] grid) {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                if (cell) return false; // Si une cellule est vivante, la grille n'est pas vide
            }
        }
        return true; // Si aucune cellule vivante n'est trouvée, la grille est vide
    }
}
