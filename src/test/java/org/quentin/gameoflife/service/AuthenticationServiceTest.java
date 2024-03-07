package org.quentin.gameoflife.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quentin.gameoflife.model.Utilisateur;
import org.quentin.gameoflife.repository.UtilisateurRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticateShouldSucceedWithCorrectCredentials() {
        // Mocking and testing logic here
        MockHttpSession session = new MockHttpSession();
        String username = "testUser";
        String password = "password";

        when(utilisateurRepository.findByUsername(username)).thenReturn(java.util.Optional.of(new Utilisateur(username, password)));

        boolean result = authenticationService.authenticate(session, username, password);

        assertThat(result).isTrue();
        assertThat(session.getAttribute("currentUser")).isEqualTo(username);
    }
}
