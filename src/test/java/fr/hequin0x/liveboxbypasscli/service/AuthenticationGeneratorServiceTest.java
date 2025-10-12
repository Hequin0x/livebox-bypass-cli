package fr.hequin0x.liveboxbypasscli.service;

import fr.hequin0x.liveboxbypasscli.configuration.AuthenticationGeneratorConfiguration;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthenticationGeneratorServiceTest {

    private static final String FIXED_RANDOM = "4682b8985d10791c";
    private static final String EXPECTED_AUTH = "0D:66:74:69:2F:78:78:78:78:78:78:78:3C:12:34:36:38:32:62:38:39:38:35:64:31:30:37:39:31:63:03:13:34:B5:A2:94:FB:AE:B7:68:0C:04:E7:38:E9:B3:49:F3:3F";
    private static final String TEST_USERNAME = "fti/xxxxxxx";
    private static final String TEST_PASSWORD = "xxxxxxx";

    @Inject
    AuthenticationGeneratorConfiguration configuration;

    @Test
    void testValidAuthenticationGeneration() throws NoSuchAlgorithmException {
        AuthenticationGeneratorService authenticationGeneratorService = spy(new AuthenticationGeneratorService(configuration));
        when(authenticationGeneratorService.generateRandom()).thenReturn(FIXED_RANDOM);

        String generatedAuth = authenticationGeneratorService.generateAuthentication(TEST_USERNAME, TEST_PASSWORD);

        assertNotNull(generatedAuth);
        assertEquals(this.configuration.prefix() + EXPECTED_AUTH, generatedAuth);
    }

}
