package fr.hequin0x.liveboxbypasscli.dto.request.login;

import fr.hequin0x.liveboxbypasscli.dto.request.LiveboxRequest;

import java.util.Map;

public final class LoginRequest extends LiveboxRequest {
    private static final String SERVICE = "sah.Device.Information";
    private static final String METHOD = "createContext";
    private static final String APPLICATION_NAME = "webui";
    private static final String USERNAME = "admin";

    public LoginRequest(final String password) {
        super(SERVICE, METHOD, Map.of(
                "applicationName", APPLICATION_NAME,
                "username", USERNAME,
                "password", password
        ));
    }
}
