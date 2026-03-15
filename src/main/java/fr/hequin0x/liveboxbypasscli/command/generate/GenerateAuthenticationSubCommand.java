package fr.hequin0x.liveboxbypasscli.command.generate;

import fr.hequin0x.liveboxbypasscli.command.formatting.FormattableOutput;
import fr.hequin0x.liveboxbypasscli.service.AuthenticationGeneratorService;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.LinkedHashMap;
import java.util.Map;

@Command(
        name = "authentication",
        description = "Generates DHCPV4/V6 Authentication with the provided login (fti/xxx) and password."
)
public final class GenerateAuthenticationSubCommand extends FormattableOutput implements Runnable {

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

            String[][] dhcpv4Options = {
                    {"90", authentication}
            };

            String[][] dhcpv6Options = {
                    {"11", authentication.replace(":", "")}
            };

            Map<String[], String[][]> tables = new LinkedHashMap<>();
            tables.put(new String[]{"DHCPv4 Option", "Value"}, dhcpv4Options);
            tables.put(new String[]{"DHCPv6 Option", "Value"}, dhcpv6Options);

            LOG.info(this.formatOutput(tables));
        } catch (Exception e) {
            LOG.error("An error occurred while generating the authentication", e);
        }
    }
}
