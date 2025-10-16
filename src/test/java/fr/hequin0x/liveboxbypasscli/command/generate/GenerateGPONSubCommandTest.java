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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
class GenerateGPONSubCommandTest {

    @InjectSpy
    @RestClient
    LiveboxService liveboxService;

    @Inject
    LiveboxAuthSession liveboxAuthSession;

    @Test
    void shouldRunAndFetchMIBs() {
        GenerateGPONSubCommand generateGPONSubCommand = spy(new GenerateGPONSubCommand(liveboxService, liveboxAuthSession));

        String[] args = {"--password", "password"};
        new CommandLine(generateGPONSubCommand).execute(args);

        verify(generateGPONSubCommand).run();
        verify(liveboxService).getMIBs(any(MIBsRequest.class));
    }

}
