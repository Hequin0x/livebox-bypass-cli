package fr.hequin0x.liveboxbypasscli.command.generate;

import fr.hequin0x.liveboxbypasscli.service.AuthenticationGeneratorService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import picocli.CommandLine;

import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
class GenerateAuthenticationSubCommandTest {

    @InjectSpy
    AuthenticationGeneratorService authenticationGeneratorService;

    @Test
    void shouldRunAndGenerateAuthentication() {
        try (MockedStatic<Logger> loggerMockedStatic = mockStatic(Logger.class)) {
            Logger mockLogger = mock(Logger.class);
            loggerMockedStatic.when(() -> Logger.getLogger(GenerateAuthenticationSubCommand.class)).thenReturn(mockLogger);

            GenerateAuthenticationSubCommand generateAuthenticationSubCommand = spy(new GenerateAuthenticationSubCommand(authenticationGeneratorService));
            String[] args = {"--login", "fti/xxx", "--password", "xxx"};
            new CommandLine(generateAuthenticationSubCommand).execute(args);

            verify(generateAuthenticationSubCommand).run();
            verify(authenticationGeneratorService).generateAuthentication("fti/xxx", "xxx");
            verify(mockLogger).infof(anyString(), any(), any());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
