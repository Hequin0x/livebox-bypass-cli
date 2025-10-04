package fr.hequin0x.liveboxbypasscli.command;

import com.github.freva.asciitable.AsciiTable;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.SentOption;
import fr.hequin0x.liveboxbypasscli.security.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

@Command(
        name = "generate-dhcp",
        description = "Generate the necessary DHCPv4 and DHCPv6 configuration options to bypass Orange Livebox."
)
public final class GenerateDHCPCommand extends AuthenticatedCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(GenerateDHCPCommand.class);

    public GenerateDHCPCommand(@RestClient final LiveboxService liveboxService, final LiveboxAuthSession liveboxAuthSession) {
        super(liveboxService, liveboxAuthSession);
    }

    @Override
    public void run() {
        this.login();
        this.generateDHCPOptions();
    }

    private void generateDHCPOptions() {
        MIBsResponse mibsResponse = this.getLiveboxService().getMIBs(new MIBsRequest());
        SentOption sentOption = mibsResponse.status().dhcp().dhcpData().sentOption();

        String[][] dataV4 = {
                {"60", sentOption.option60().dhcpv4Value()},
                {"61", sentOption.option61().dhcpv4Value()},
                {"77", sentOption.option77().value()},
                {"90", sentOption.option90().dhcpv4Value()}
        };

        String[][] dataV6 = {
                {"16", sentOption.option60().dhcpv6Value()},
                {"1", sentOption.option61().dhcpv6Value()},
                {"15", sentOption.option77().value()},
                {"11", sentOption.option90().dhcpv6Value()}
        };

        String[] headersV4 = {"Option V4", "Value V4"};
        String[] headersV6 = {"Option V6", "Value V6"};

        LOG.infof("\n%s\n%s", AsciiTable.getTable(headersV4, dataV4), AsciiTable.getTable(headersV6, dataV6));
    }

}
