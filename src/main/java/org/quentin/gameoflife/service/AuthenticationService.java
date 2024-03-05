package org.quentin.gameoflife.service;

import jakarta.servlet.http.HttpSession;
import org.quentin.gameoflife.model.Utilisateur;
import org.quentin.gameoflife.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public boolean authenticate(HttpSession session, String username, String password) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username).orElse(null);
        if (utilisateur != null && utilisateur.verifyPassword(password)) {
            session.setAttribute("currentUser", utilisateur.getUsername()); // Store username in session
            return true;
        }
        return false;
    }

    public void logout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.invalidate();
    }

    public Utilisateur register(String username, String password) {
        if (utilisateurRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Nom d'utilisateur déjà pris");
        }

        Utilisateur newUtilisateur = new Utilisateur(username, password);
        return utilisateurRepository.save(newUtilisateur);
    }
}
