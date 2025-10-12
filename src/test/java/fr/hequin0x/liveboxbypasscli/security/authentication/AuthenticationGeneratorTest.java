package fr.hequin0x.liveboxbypasscli.security.authentication;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthenticationGeneratorTest {

    @Test
    void testValidAuthenticationGeneration() throws NoSuchAlgorithmException {
        String RANDOM = "4682b8985d10791c";
        String EXPECTED_AUTH = "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:0D:66:74:69:2F:78:78:78:78:" +
                "78:78:78:3C:12:34:36:38:32:62:38:39:38:35:64:31:30:37:39:31:63:03:13:34:B5:A2:94:FB:AE:" +
                "B7:68:0C:04:E7:38:E9:B3:49:F3:3F";

        AuthenticationGenerator authenticationGenerator = spy(AuthenticationGenerator.class);
        when(authenticationGenerator.generateRandom()).thenReturn(RANDOM);

        String generatedAuth = authenticationGenerator.generateAuthentication("fti/xxxxxxx", "xxxxxxx");

        assertEquals(EXPECTED_AUTH, generatedAuth);
    }

}
