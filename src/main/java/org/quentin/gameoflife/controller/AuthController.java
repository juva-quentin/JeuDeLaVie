package org.quentin.gameoflife.controller;

import jakarta.servlet.http.HttpSession;
import org.quentin.gameoflife.repository.UserRepository;
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
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(HttpSession session) {

        if (session.getAttribute("currentUser") != null) {

            return "redirect:/jeudelavie";
        } else {

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
            return "redirect:/jeudelavie";
        } else {
            return "redirect:/connexion?error=true";
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
            return "redirect:/connexion";
        } catch (IllegalStateException e) {
            return "redirect:/inscription?error=Nom d'utilisateur déjà pris";
        }
    }

}
