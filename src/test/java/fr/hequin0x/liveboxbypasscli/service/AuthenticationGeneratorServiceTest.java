package fr.hequin0x.liveboxbypasscli.service;

import fr.hequin0x.liveboxbypasscli.configuration.AuthenticationGeneratorConfiguration;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthenticationGeneratorServiceTest {

    private static final String EXPECTED_AUTH = "0D:66:74:69:2F:78:78:78:78:78:78:78:3C:12:34:36:38:32:62:38:39:38:35:64:31:30:37:39:31:63:03:13:34:B5:A2:94:FB:AE:B7:68:0C:04:E7:38:E9:B3:49:F3:3F";

    @Inject
    AuthenticationGeneratorConfiguration configuration;

    @Test
    void shouldGenerateValidAuthentication() throws NoSuchAlgorithmException {
        String random = "4682b8985d10791c";

        AuthenticationGeneratorService authenticationGeneratorService = spy(new AuthenticationGeneratorService(configuration));
        when(authenticationGeneratorService.generateRandom()).thenReturn(random);

        String generatedAuth = authenticationGeneratorService.generateAuthentication("fti/xxxxxxx", "xxxxxxx");

        assertNotNull(generatedAuth);
        assertEquals(this.configuration.prefix() + EXPECTED_AUTH, generatedAuth);
    }

    @Test
    void shouldGenerateRandomOfLength16WithHexCharacters() throws NoSuchAlgorithmException {
        AuthenticationGeneratorService authenticationGeneratorService = new AuthenticationGeneratorService(configuration);
        String random = authenticationGeneratorService.generateRandom();

        assertNotNull(random);
        assertEquals(16, random.length());
        assertTrue(random.matches("[0-9a-fA-F]+"));
    }

    @Test
    void shouldNotGenerateExpectedAuthentication() throws NoSuchAlgorithmException {
        AuthenticationGeneratorService authenticationGeneratorService = new AuthenticationGeneratorService(configuration);
        String generatedAuth = authenticationGeneratorService.generateAuthentication("fti/xxxxxxx", "xxxxxxx");

        assertNotNull(generatedAuth);
        assertNotEquals(this.configuration.prefix() + EXPECTED_AUTH, generatedAuth);
    }

    @Test
    void shouldThrowExceptionForInvalidLoginOrPassword() {
        AuthenticationGeneratorService authenticationGeneratorService = new AuthenticationGeneratorService(configuration);

        assertThrows(IllegalArgumentException.class, () -> authenticationGeneratorService.generateAuthentication(null, "password"));
        assertThrows(IllegalArgumentException.class, () -> authenticationGeneratorService.generateAuthentication("login", null));
        assertThrows(IllegalArgumentException.class, () -> authenticationGeneratorService.generateAuthentication("", "password"));
        assertThrows(IllegalArgumentException.class, () -> authenticationGeneratorService.generateAuthentication("login", ""));
        assertThrows(IllegalArgumentException.class, () -> authenticationGeneratorService.generateAuthentication("   ", "password"));
        assertThrows(IllegalArgumentException.class, () -> authenticationGeneratorService.generateAuthentication("login", "   "));
    }

}
