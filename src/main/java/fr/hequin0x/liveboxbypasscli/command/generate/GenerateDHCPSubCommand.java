package fr.hequin0x.liveboxbypasscli.command.generate;

import com.google.common.collect.ImmutableMap;
import fr.hequin0x.liveboxbypasscli.command.BaseAuthenticatedCommand;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.SentOption;
import fr.hequin0x.liveboxbypasscli.security.session.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

import java.util.Map;

import static fr.hequin0x.liveboxbypasscli.util.OutputFormatter.formatOutput;

@Command(
        name = "dhcp",
        description = "Generates DHCPv4 and DHCPv6 configurations for bypassing the Livebox."
)
public final class GenerateDHCPSubCommand extends BaseAuthenticatedCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(GenerateDHCPSubCommand.class);

    public GenerateDHCPSubCommand(@RestClient final LiveboxService liveboxService, final LiveboxAuthSession liveboxAuthSession) {
        super(liveboxService, liveboxAuthSession);
    }

    @Override
    public void run() {
        try {
            this.login();
            this.generateDHCPOptions();
        } catch (Exception e) {
            LOG.error("An error occurred while generating DHCP options", e);
        }
    }

    private void generateDHCPOptions() {
        MIBsResponse mibsResponse = this.getLiveboxService().getMIBs(new MIBsRequest());

        Integer dhcpCos = mibsResponse.status().dhcp().dhcpData().priorityMark();
        Integer wanVlanID = mibsResponse.status().vlan().gvlanMulti().vlanID();
        SentOption sentOption = mibsResponse.status().dhcp().dhcpData().sentOption();

        Map<String, Map<String, String>> data = ImmutableMap.of(
                "DHCPv4/v6 Options", Map.of("CoS", dhcpCos.toString()),
                "WAN Options", Map.of("VLAN ID", wanVlanID.toString()),
                "DHCPv4 Options", ImmutableMap.of(
                        "60", sentOption.option60().dhcpv4Value(),
                        "61", sentOption.option61().dhcpv4Value(),
                        "77", sentOption.option77().value(),
                        "90", sentOption.option90().dhcpv4Value()
                ),
                "DHCPv6 Options", ImmutableMap.of(
                        "16", sentOption.option60().dhcpv6Value(),
                        "1", sentOption.option61().dhcpv6Value(),
                        "15", sentOption.option77().value(),
                        "11", sentOption.option90().dhcpv6Value()
                ));

        LOG.info(formatOutput(data));
    }

}
