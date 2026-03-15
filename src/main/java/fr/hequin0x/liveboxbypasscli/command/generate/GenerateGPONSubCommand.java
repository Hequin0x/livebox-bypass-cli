package fr.hequin0x.liveboxbypasscli.command.generate;

import com.google.common.collect.ImmutableMap;
import fr.hequin0x.liveboxbypasscli.command.BaseAuthenticatedCommand;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.gpon.VEIP0;
import fr.hequin0x.liveboxbypasscli.security.session.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

import java.util.Map;

import static fr.hequin0x.liveboxbypasscli.util.OutputFormatter.formatOutput;

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
        try {
            this.login();
            this.generateGPONConfiguration();
        } catch (Exception e) {
            LOG.error("An error occurred while generating GPON configuration", e);
        }
    }

    private void generateGPONConfiguration() {
        MIBsResponse mibsResponse = this.getLiveboxService().getMIBs(new MIBsRequest());
        VEIP0 veip0 = mibsResponse.status().gpon().veip0();

        Map<String, Map<String, String>> data = Map.of(
                "GPON Configuration", ImmutableMap.of(
                        "Serial Number", veip0.serialNumber(),
                        "Hardware Version", veip0.hardwareVersion(),
                        "Vendor ID", veip0.vendorId(),
                        "Software Version 0", veip0.ontSoftwareVersion0(),
                        "Software Version 1", veip0.ontSoftwareVersion1()
                )
        );

        LOG.info(formatOutput(data));
    }
}
