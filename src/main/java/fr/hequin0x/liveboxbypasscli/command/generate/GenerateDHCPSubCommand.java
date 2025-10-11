package fr.hequin0x.liveboxbypasscli.command.generate;

import com.github.freva.asciitable.AsciiTable;
import fr.hequin0x.liveboxbypasscli.command.BaseAuthenticatedCommand;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.SentOption;
import fr.hequin0x.liveboxbypasscli.security.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

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
        this.login();
        this.generateDHCPOptions();
    }

    private void generateDHCPOptions() {
        MIBsResponse mibsResponse = this.getLiveboxService().getMIBs(new MIBsRequest());

        Integer dhcpCos = mibsResponse.status().dhcp().dhcpData().priorityMark();
        Integer wanVlanID = mibsResponse.status().vlan().gvlanMulti().vlanID();
        SentOption sentOption = mibsResponse.status().dhcp().dhcpData().sentOption();

        String[][] dhcpWANOptions = {
                {"CoS", dhcpCos.toString(), "VLAN ID", wanVlanID.toString()}
        };

        String[][] dhcpv4Options = {
                {"60", sentOption.option60().dhcpv4Value()},
                {"61", sentOption.option61().dhcpv4Value()},
                {"77", sentOption.option77().value()},
                {"90", sentOption.option90().dhcpv4Value()}
        };

        String[][] dhcpv6Options = {
                {"16", sentOption.option60().dhcpv6Value()},
                {"1", sentOption.option61().dhcpv6Value()},
                {"15", sentOption.option77().value()},
                {"11", sentOption.option90().dhcpv6Value()}
        };

        String dhcpWANTable = AsciiTable.getTable(new String[] {"DHCPv4/v6 Option", "Value", "WAN Option", "Value"}, dhcpWANOptions);
        String dhcpv4Table = AsciiTable.getTable(new String[] {"DHCPv4 Option", "Value"}, dhcpv4Options);
        String dhcpv6Table = AsciiTable.getTable(new String[] {"DHCPv6 Option", "Value"}, dhcpv6Options);

        LOG.infof("\n%s\n%s\n%s", dhcpWANTable, dhcpv4Table, dhcpv6Table);
    }

}
