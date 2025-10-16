package fr.hequin0x.liveboxbypasscli.command.generate;

import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.extension.WireMockExtensions;
import fr.hequin0x.liveboxbypasscli.security.session.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
class GenerateDHCPSubCommandTest {

    @InjectSpy
    @RestClient
    LiveboxService liveboxService;

    @Inject
    LiveboxAuthSession liveboxAuthSession;

    @Test
    void shouldRunAndFetchMIBs() {
        GenerateDHCPSubCommand generateDHCPSubCommand = spy(new GenerateDHCPSubCommand(liveboxService, liveboxAuthSession));

        String[] args = {"--password", "password"};
        new CommandLine(generateDHCPSubCommand).execute(args);

        verify(generateDHCPSubCommand).run();
        verify(liveboxService).getMIBs(any(MIBsRequest.class));
    }

}
