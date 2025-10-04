package fr.hequin0x.liveboxbypasscli.dto.request.wan;

import fr.hequin0x.liveboxbypasscli.dto.request.LiveboxRequest;

public final class WanStatusRequest extends LiveboxRequest  {
    private static final String SERVICE = "NMC";
    private static final String METHOD = "getWANStatus";

    public WanStatusRequest() {
        super(SERVICE, METHOD);
    }
}
