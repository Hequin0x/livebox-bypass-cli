package fr.hequin0x.liveboxbypasscli.service;

import fr.hequin0x.liveboxbypasscli.dto.request.login.LoginRequest;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.login.LoginResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.SentOption;
import fr.hequin0x.liveboxbypasscli.extension.WireMockExtensions;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
class LiveboxServiceTest {

    @RestClient
    LiveboxService liveboxService;

    @Test
    void shouldLoginSuccessfully() {
        try(RestResponse<LoginResponse> restResponse = liveboxService.login(new LoginRequest("password"))) {
            assertNotNull(restResponse);
            assertEquals(200, restResponse.getStatus());
            assertNotNull(restResponse.getEntity());
            assertEquals("randomValue", restResponse.getEntity().data().contextID());
            assertEquals("SessionID", restResponse.getHeaderString("Set-Cookie"));
        }
    }

    @Test
    void shouldRetrieveMIBsSuccessfully() {
        MIBsResponse response = liveboxService.getMIBs(new MIBsRequest());
        SentOption sentOption = response.status().dhcp().dhcpData().sentOption();

        assertNotNull(response);
        assertNotNull(sentOption);

        // DHCP Option 60
        assertEquals("sagem", sentOption.option60().dhcpv4Value());
        assertEquals(22, sentOption.option60().dhcpv6Value().length());
        assertTrue(sentOption.option60().dhcpv6Value().matches("[0-9a-fA-F]+"));
        assertTrue(sentOption.option60().dhcpv6Value().startsWith("0000040E"));

        // DHCP Option 61
        assertEquals(17, sentOption.option61().dhcpv4Value().length());
        assertEquals(29, sentOption.option61().dhcpv6Value().length());
        assertTrue(sentOption.option61().dhcpv4Value().matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$"));
        assertTrue(sentOption.option61().dhcpv6Value().matches("^([0-9A-F]{2}:){9}[0-9A-F]{2}$"));
        assertTrue(sentOption.option61().dhcpv6Value().startsWith("00:03:00:01"));

        // DHCP Option 77
        assertEquals("FSVDSL_livebox.Internet.softathome.Livebox7", sentOption.option77().value());

        // DHCP Option 90
        assertEquals(140, sentOption.option90().dhcpv6Value().length());
        assertTrue(sentOption.option90().dhcpv4Value().matches("^([0-9A-F]{2}[:-]){69}([0-9A-F]{2})$"));
    }
}
