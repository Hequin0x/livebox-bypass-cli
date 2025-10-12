package fr.hequin0x.liveboxbypasscli.security.session;

import jakarta.inject.Singleton;

@Singleton
public final class LiveboxAuthSession {
    private String contextID;
    private String cookie;

    public String getContextID() {
        return this.contextID;
    }

    public void setContextID(final String contextID) {
        this.contextID = contextID;
    }

    public String getCookie() {
        return this.cookie;
    }

    public void setCookie(final String cookie) {
        this.cookie = cookie;
    }

    public boolean isAuthenticated() {
        return this.contextID != null && this.cookie != null;
    }
}
