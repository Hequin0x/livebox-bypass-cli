package fr.hequin0x.liveboxbypasscli.service;

import fr.hequin0x.liveboxbypasscli.dto.request.login.LoginRequest;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.login.LoginResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.security.filter.LiveboxAuthRequestFilter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;

@RegisterRestClient(configKey = "livebox-api")
@RegisterProvider(LiveboxAuthRequestFilter.class)
@Consumes("application/x-sah-ws-4-call+json")
public interface LiveboxService {

    @POST
    @ClientHeaderParam(name = "Authorization", value = "X-Sah-Login")
    RestResponse<LoginResponse> login(LoginRequest request);

    @POST
    MIBsResponse getMIBs(MIBsRequest request);
}
