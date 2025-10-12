package fr.hequin0x.liveboxbypasscli.command.generate;

import com.github.freva.asciitable.AsciiTable;
import fr.hequin0x.liveboxbypasscli.command.BaseAuthenticatedCommand;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.gpon.VEIP0;
import fr.hequin0x.liveboxbypasscli.security.session.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

@Command(
        name = "gpon",
        description = "Generates GPON configurations for bypassing the Livebox."
)
public final class GenerateGPONSubCommand extends BaseAuthenticatedCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(GenerateGPONSubCommand.class);

    public GenerateGPONSubCommand(@RestClient final LiveboxService liveboxService, final LiveboxAuthSession liveboxAuthSession) {
        super(liveboxService, liveboxAuthSession);
    }

    @Override
    public void run() {
        this.login();
        this.generateGPONConfiguration();
    }

    private void generateGPONConfiguration() {
        MIBsResponse mibsResponse = this.getLiveboxService().getMIBs(new MIBsRequest());
        VEIP0 veip0 = mibsResponse.status().gpon().veip0();

        String[][] data = {
                {"Serial Number", veip0.serialNumber()},
                {"Hardware Version", veip0.hardwareVersion()},
                {"Vendor ID", veip0.vendorId()}
        };

        String[] headers = {"Option", "Value"};
        LOG.info("\n" + AsciiTable.getTable(headers, data));
    }
}
