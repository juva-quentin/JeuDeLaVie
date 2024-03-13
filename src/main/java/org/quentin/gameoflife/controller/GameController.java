package org.quentin.gameoflife.controller;

import jakarta.servlet.http.HttpSession;

import org.quentin.gameoflife.service.JeuDeLaVieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private JeuDeLaVieService jeuDeLaVieService;

    @GetMapping("/jeudelavie")
    public String jeuDeLaViePage(HttpSession session, Model model) {
        logger.info("Accès à la page du jeu de la vie.");
        String username = (String) session.getAttribute("currentUser");
        if (username == null) {
            logger.warn("Tentative d'accès à la page du jeu de la vie sans utilisateur connecté.");
            return "redirect:/connexion";
        }
        model.addAttribute("username", username);
        return "jeudelavie";
    }

    @MessageMapping("/demarrer")
    public void demarrerJeuWebSocket() {
        logger.info("Le démarrage du jeu a été demandé.");
        jeuDeLaVieService.startGame();
    }

    @MessageMapping("/stopper")
    public void stopperJeuWebSocket() {
        logger.info("Arrêt du jeu demandé via WebSocket.");
        jeuDeLaVieService.stopGame();
    }

    @MessageMapping("/reinitialiser")
    public void reinitialiserJeuWebSocket() {
        logger.info("Réinitialisation du jeu demandée via WebSocket.");
        jeuDeLaVieService.initGame();
        logger.info("Redémarrage du jeu après réinitialisation.");
        jeuDeLaVieService.startGame();
    }
}
