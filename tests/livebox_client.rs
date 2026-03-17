use livebox_bypass_cli::api::livebox_client::{AuthSession, LiveboxClient};
use mockito::Server;
use url::Url;

#[test]
fn login_requires_set_cookie_header() {
    let mut server = Server::new();

    let _mock = server
        .mock("POST", "/ws")
        .with_status(200)
        .with_header("content-type", "application/json")
        .with_body(r#"{"data":{"contextID":"ctx-1"}}"#)
        .create();

    let client = LiveboxClient::new(Url::parse(&format!("{}/ws", server.url())).unwrap()).unwrap();
    let error = client.login("secret").unwrap_err().to_string();

    assert!(error.contains("missing Set-Cookie header"));
}

#[test]
fn get_mibs_parses_response() {
    let mut server = Server::new();

    let _mock = server
        .mock("POST", "/ws")
        .match_header("x-context", "ctx-1")
        .match_header("cookie", "session=abc123")
        .with_status(200)
        .with_header("content-type", "application/json")
        .with_body(
            r#"{
                "status": {
                    "dhcp": {
                        "dhcp_data": {
                            "PriorityMark": 6,
                            "SentOption": {
                                "60": { "Value": "736167656d" },
                                "61": { "Value": "01000000000000" },
                                "77": { "Value": "2b46535644534c5f6c697665626f782e496e7465726e65742e736f66746174686f6d652e4c697665626f7837" },
                                "90": { "Value": "00000000000000000000001A0900000558010341010B6674692F6C6F67696E3C12313233343536373839303132333435360313416324D17241350C0C74F222E3E7CDC13C" }
                            }
                        }
                    },
                    "gpon": {
                        "veip0": {
                            "SerialNumber": "ABCD1234",
                            "HardwareVersion": "1.0",
                            "VendorId": "SAGEM",
                            "ONTSoftwareVersion0": "v0",
                            "ONTSoftwareVersion1": "v1"
                        }
                    },
                    "vlan": {
                        "gvlan_multi": {
                            "VLANID": 832
                        }
                    }
                }
            }"#,
        )
        .create();

    let client = LiveboxClient::new(Url::parse(&format!("{}/ws", server.url())).unwrap()).unwrap();
    let session = AuthSession {
        context_id: "ctx-1".into(),
        cookie: "session=abc123".into(),
    };

    let mibs = client.get_mibs(&session).unwrap();

    assert_eq!(mibs.status.dhcp.dhcp_data.priority_mark, 6);
    assert_eq!(mibs.status.vlan.gvlan_multi.vlan_id, 832);
    assert_eq!(mibs.status.gpon.veip0.serial_number, "ABCD1234");
}