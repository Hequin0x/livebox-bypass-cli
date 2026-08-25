use livebox_bypass_cli::api::responses::{
    Authentication, ClientIdentifier, UserClass, VendorClass,
};

#[test]
fn option_transforms_produce_correct_values() {
    let vendor_class = VendorClass {
        value: "736167656d".into(),
    };
    let client_identifier = ClientIdentifier {
        value: "01000000000000".into(),
    };
    let user_class = UserClass { value: "2b46535644534c5f6c697665626f782e496e7465726e65742e736f66746174686f6d652e4c697665626f7837".into() };
    let authentication = Authentication { value: "00000000000000000000001A0900000558010341010B6674692F6C6F67696E3C12313233343536373839303132333435360313416324D17241350C0C74F222E3E7CDC13C".into() };

    assert_eq!(vendor_class.dhcpv4_value().unwrap(), "sagem");
    assert_eq!(vendor_class.dhcpv6_value(), "0000040E0005736167656D");

    assert_eq!(client_identifier.dhcpv4_value(), "00:00:00:00:00:00");
    assert_eq!(
        client_identifier.dhcpv6_value(),
        "00:03:00:01:00:00:00:00:00:00"
    );

    assert_eq!(
        user_class.dhcp_value().unwrap(),
        "FSVDSL_livebox.Internet.softathome.Livebox7"
    );

    assert_eq!(
        authentication.dhcpv4_value(),
        "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:0B:66:74:69:2F:6C:6F:67:69:6E:3C:12:31:32:33:34:35:36:37:38:39:30:31:32:33:34:35:36:03:13:41:63:24:D1:72:41:35:0C:0C:74:F2:22:E3:E7:CD:C1:3C"
    );
    assert_eq!(
        authentication.dhcpv6_value(),
        "00000000000000000000001A0900000558010341010B6674692F6C6F67696E3C12313233343536373839303132333435360313416324D17241350C0C74F222E3E7CDC13C"
    );
}
