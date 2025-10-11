package fr.hequin0x.liveboxbypasscli.command;

import fr.hequin0x.liveboxbypasscli.dto.request.login.LoginRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.login.LoginResponse;
import fr.hequin0x.liveboxbypasscli.security.LiveboxAuthSession;
import fr.hequin0x.liveboxbypasscli.service.LiveboxService;
import org.jboss.resteasy.reactive.RestResponse;
import picocli.CommandLine.Option;

public abstract class BaseAuthenticatedCommand {

    private final LiveboxService liveboxService;
    private final LiveboxAuthSession liveboxAuthSession;

    @Option(names = {"-p", "--password"}, description = "Livebox admin password", arity = "0..1", required = true, interactive = true)
    private String password;

    protected BaseAuthenticatedCommand(final LiveboxService liveboxService, final LiveboxAuthSession liveboxAuthSession) {
        this.liveboxService = liveboxService;
        this.liveboxAuthSession = liveboxAuthSession;
    }

    public LiveboxService getLiveboxService() {
        return this.liveboxService;
    }

    public void login() {
        if (this.liveboxAuthSession.isAuthenticated()) {
            return;
        }

        try (RestResponse<LoginResponse> loginResponse = this.liveboxService.login(new LoginRequest(this.password))) {
            String cookie = loginResponse.getHeaderString("Set-Cookie");
            String contextID = loginResponse.getEntity().data().contextID();

            this.liveboxAuthSession.setCookie(cookie);
            this.liveboxAuthSession.setContextID(contextID);
        } catch (Exception e) {
            throw new RuntimeException("Failed to login to Livebox", e);
        }
    }
}
