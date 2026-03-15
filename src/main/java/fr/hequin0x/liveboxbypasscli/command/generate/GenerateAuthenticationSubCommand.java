package fr.hequin0x.liveboxbypasscli.command.generate;

import com.google.common.collect.ImmutableMap;
import fr.hequin0x.liveboxbypasscli.service.AuthenticationGeneratorService;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Map;

import static fr.hequin0x.liveboxbypasscli.util.OutputFormatter.formatOutput;

@Command(
        name = "authentication",
        description = "Generates DHCPV4/V6 Authentication with the provided login (fti/xxx) and password."
)
public final class GenerateAuthenticationSubCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(GenerateAuthenticationSubCommand.class);

    private final AuthenticationGeneratorService authenticationGeneratorService;

    @Option(names = {"-l", "--login"}, description = "Orange login (fti/xxx)", required = true)
    private String login;

    @Option(names = {"-p", "--password"}, description = "Orange password", arity = "0..1", required = true, interactive = true)
    private String password;

    public GenerateAuthenticationSubCommand(final AuthenticationGeneratorService authenticationGeneratorService) {
        this.authenticationGeneratorService = authenticationGeneratorService;
    }

    @Override
    public void run() {
        try {
            String authentication = this.authenticationGeneratorService.generateAuthentication(this.login, this.password);
            String dhcpv6Authentication = authentication.replace(":", "");

            Map<String, Map<String, String>> data = ImmutableMap.of(
                    "DHCPv4 Options", Map.of("90", authentication),
                    "DHCPv6 Options", Map.of("11", dhcpv6Authentication)
            );

            LOG.info(formatOutput(data));
        } catch (Exception e) {
            LOG.error("An error occurred while generating the authentication", e);
        }
    }
}
