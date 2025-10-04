package fr.hequin0x.liveboxbypasscli.dto.request.mibs;

import fr.hequin0x.liveboxbypasscli.dto.request.LiveboxRequest;

public final class MIBsRequest extends LiveboxRequest {
    private static final String SERVICE = "NeMo.Intf.data";
    private static final String METHOD = "getMIBs";

    public MIBsRequest() {
        super(SERVICE, METHOD);
    }
}
