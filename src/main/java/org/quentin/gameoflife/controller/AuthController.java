package org.quentin.gameoflife.controller;

import jakarta.servlet.http.HttpSession;
import org.quentin.gameoflife.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.quentin.gameoflife.service.AuthenticationService;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/")
    public String index(HttpSession session) {
        // Vérifier si un utilisateur est connecté
        if (session.getAttribute("currentUser") != null) {
            // Rediriger vers la page du jeu de la vie si un utilisateur est connecté
            return "redirect:/jeudelavie";
        } else {
            // Rediriger vers la page de connexion si aucun utilisateur n'est connecté
            return "redirect:/connexion";
        }
    }

    @GetMapping("/connexion")
    public String login() {
        return "connexion";
    }

    @PostMapping("/connexion")
    public String loginSubmit(HttpSession session, @RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = authenticationService.authenticate(session, username, password);
        if (isAuthenticated) {
            return "redirect:/jeudelavie"; // Assurez-vous que ce chemin est correct et mène à la page souhaitée après la connexion
        } else {
            return "redirect:/connexion?error=true"; // Ajoutez un paramètre de requête pour indiquer une erreur de connexion
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authenticationService.logout(session);
        return "redirect:/connexion";
    }

    @GetMapping("/inscription")
    public String showRegistrationForm() {
        return "inscription";
    }

    @PostMapping("/inscription")
    public String register(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            authenticationService.register(username, password);
            return "redirect:/connexion"; // Redirige vers la page de connexion après l'inscription réussie
        } catch (IllegalStateException e) {
            return "redirect:/inscription?error=Nom d'utilisateur déjà pris"; // Informe l'utilisateur de l'erreur
        }
    }

}
