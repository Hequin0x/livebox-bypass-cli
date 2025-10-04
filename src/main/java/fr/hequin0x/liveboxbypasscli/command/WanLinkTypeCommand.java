package fr.hequin0x.liveboxbypasscli.command;

import fr.hequin0x.liveboxbypasscli.dto.request.wan.WanStatusRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.wan.WanStatusResponse;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;

@Command(
        name = "wan-link-type",
        description = "Display the current link type of the WAN interface."
)
public final class WanLinkTypeCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(WanLinkTypeCommand.class);

    private final LiveboxService liveboxService;

    public WanLinkTypeCommand(@RestClient final LiveboxService liveboxService) {
        this.liveboxService = liveboxService;
    }

    @Override
    public void run() {
        this.displayWanLinkType();
    }

    private void displayWanLinkType() {
        WanStatusResponse wanStatusResponse = this.liveboxService.getWanStatus(new WanStatusRequest());
        String linkType = wanStatusResponse.data().linkType();

        LOG.info(linkType);
    }
}
