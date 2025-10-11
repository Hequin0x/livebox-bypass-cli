package fr.hequin0x.liveboxbypasscli.security.filter;

import fr.hequin0x.liveboxbypasscli.security.LiveboxAuthSession;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class LiveboxAuthRequestFilter implements ClientRequestFilter {

    private final LiveboxAuthSession liveboxAuthSession;

    public LiveboxAuthRequestFilter(final LiveboxAuthSession liveboxAuthSession) {
        this.liveboxAuthSession = liveboxAuthSession;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) {
        if (this.liveboxAuthSession.isAuthenticated()) {
            requestContext.getHeaders().add("Cookie", this.liveboxAuthSession.getCookie());
            requestContext.getHeaders().add("X-Context", this.liveboxAuthSession.getContextID());
        }
    }
}
