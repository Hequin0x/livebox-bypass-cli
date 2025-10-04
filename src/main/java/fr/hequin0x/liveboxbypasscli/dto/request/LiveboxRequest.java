package fr.hequin0x.liveboxbypasscli.dto.request;

import java.util.Map;

public abstract class LiveboxRequest {
    private final String service;
    private final String method;
    private final Map<String, String> parameters;

    protected LiveboxRequest(final String service, final String method) {
        this(service, method, Map.of());
    }

    protected LiveboxRequest(final String service, final String method, final Map<String, String> parameters) {
        this.service = service;
        this.method = method;
        this.parameters = parameters;
    }

    public String getService() {
        return this.service;
    }

    public String getMethod() {
        return this.method;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }
}
