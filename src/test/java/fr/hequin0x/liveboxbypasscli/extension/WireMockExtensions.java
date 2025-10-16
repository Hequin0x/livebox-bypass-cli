package fr.hequin0x.liveboxbypasscli.extension;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;

public class WireMockExtensions implements QuarkusTestResourceLifecycleManager {

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        setupLoginStub(wireMockServer);
        setupMIBsStub(wireMockServer);

        return Map.of("quarkus.rest-client.livebox-api.url", wireMockServer.baseUrl());
    }

    private static void setupLoginStub(WireMockServer server) {
        server.stubFor(post(urlEqualTo("/"))
                .withRequestBody(matchingJsonPath("$.service", equalTo("sah.Device.Information")))
                .withRequestBody(matchingJsonPath("$.method", equalTo("createContext")))
                .withRequestBody(matchingJsonPath("$.parameters.applicationName", equalTo("webui")))
                .withRequestBody(matchingJsonPath("$.parameters.username", equalTo("admin")))
                .withRequestBody(matchingJsonPath("$.parameters.password", equalTo("password")))
                .withHeader("Content-Type", equalTo("application/x-sah-ws-4-call+json"))
                .withHeader("Authorization", equalTo("X-Sah-Login"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/x-sah-ws-4-call+json")
                        .withHeader("Set-Cookie", "SessionID")
                        .withBody("""
                                {
                                  "data": {
                                    "contextID": "randomValue"
                                  }
                                }
                                """)));
    }

    private static void setupMIBsStub(WireMockServer server) {
        server.stubFor(post(urlEqualTo("/"))
                .withRequestBody(matchingJsonPath("$.service", equalTo("NeMo.Intf.data")))
                .withRequestBody(matchingJsonPath("$.method", equalTo("getMIBs")))
                .withHeader("Content-Type", equalTo("application/x-sah-ws-4-call+json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/x-sah-ws-4-call+json")
                        .withTransformers("response-template")
                        .withBody("""
                                {
                                  "status": {
                                    "dhcp": {
                                      "dhcp_data": {
                                        "PriorityMark": 6,
                                        "SentOption": {
                                          "60": { "Value": "736167656d" },
                                          "61": { "Value": "01000000000000" },
                                          "77": { "Value": "2b46535644534c5f6c697665626f782e496e7465726e65742e736f66746174686f6d652e4c697665626f7837" },
                                          "90": { "Value": "{{randomValue length=140 type='HEXADECIMAL'}}" }
                                        }
                                      }
                                    },
                                    "gpon": { "veip0": { "SerialNumber": "SMBSXXXXXXXX", "HardwareVersion": "SMBSXLB7270400", "VendorId": "SMBS" } },
                                    "vlan": { "gvlan_multi": { "VLANID": 832 } }
                                  }
                                }
                                """)
                )
        );
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}
