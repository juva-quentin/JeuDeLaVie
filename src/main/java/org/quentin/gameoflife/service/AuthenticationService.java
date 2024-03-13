package org.quentin.gameoflife.service;

import jakarta.servlet.http.HttpSession;
import org.quentin.gameoflife.model.User;
import org.quentin.gameoflife.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(HttpSession session, String username, String password) {
        User utilisateur = userRepository.findByUsername(username).orElse(null);
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

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Nom d'utilisateur déjà pris");
        }

        User newUtilisateur = new User(username, password);
        return userRepository.save(newUtilisateur);
    }
}
