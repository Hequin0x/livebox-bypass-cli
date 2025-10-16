package fr.hequin0x.liveboxbypasscli.service;

import fr.hequin0x.liveboxbypasscli.dto.request.login.LoginRequest;
import fr.hequin0x.liveboxbypasscli.dto.request.mibs.MIBsRequest;
import fr.hequin0x.liveboxbypasscli.dto.response.login.LoginResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.MIBsResponse;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.dhcp.SentOption;
import fr.hequin0x.liveboxbypasscli.dto.response.mibs.gpon.VEIP0;
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

        Integer dhcpCoS = response.status().dhcp().dhcpData().priorityMark();
        Integer vlanId = response.status().vlan().gvlanMulti().vlanID();

        SentOption sentOption = response.status().dhcp().dhcpData().sentOption();
        VEIP0 veip0 = response.status().gpon().veip0();

        assertNotNull(response);
        assertNotNull(dhcpCoS);
        assertNotNull(vlanId);
        assertNotNull(sentOption);
        assertNotNull(veip0);

        // DHCP Option 60
        assertEquals("sagem", sentOption.option60().dhcpv4Value());
        assertEquals("0000040E0005736167656D", sentOption.option60().dhcpv6Value());

        // DHCP Option 61
        assertEquals("00:00:00:00:00:00", sentOption.option61().dhcpv4Value());
        assertEquals("00:03:00:01:00:00:00:00:00:00", sentOption.option61().dhcpv6Value());

        // DHCP Option 77
        assertEquals("FSVDSL_livebox.Internet.softathome.Livebox7", sentOption.option77().value());

        // DHCP Option 90
        assertEquals("00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:0B:66:74:69:2F:6C:6F:67:69:6E:3C:12:31:32:33:34:35:36:37:38:39:30:31:32:33:34:35:36:03:13:41:63:24:D1:72:41:35:0C:0C:74:F2:22:E3:E7:CD:C1:3C", sentOption.option90().dhcpv4Value());
        assertEquals("00000000000000000000001A0900000558010341010B6674692F6C6F67696E3C12313233343536373839303132333435360313416324D17241350C0C74F222E3E7CDC13C", sentOption.option90().dhcpv6Value());

        // DHCP CoS
        assertEquals(6, dhcpCoS);

        // VLAN ID
        assertEquals(832, vlanId);
        
        // VEIP0 Serial Number
        assertEquals("SMBSXXXXXXXX", veip0.serialNumber());
        assertEquals("SMBSXLB7270400", veip0.hardwareVersion());
        assertEquals("SMBS", veip0.vendorId());
    }
}
