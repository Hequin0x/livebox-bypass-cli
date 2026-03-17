use livebox_bypass_cli::api::models::{Option60, Option61, Option77, Option90};
use livebox_bypass_cli::renderers::options::{
    option60_dhcpv4_value, option60_dhcpv6_value,
    option61_dhcpv4_value, option61_dhcpv6_value,
    option77_value, option90_dhcpv4_value, option90_dhcpv6_value,
};

#[test]
fn option_transforms_produce_correct_values() {
    let o60 = Option60 { value: "736167656d".into() };
    let o61 = Option61 { value: "01000000000000".into() };
    let o77 = Option77 { value: "2b46535644534c5f6c697665626f782e496e7465726e65742e736f66746174686f6d652e4c697665626f7837".into() };
    let o90 = Option90 { value: "00000000000000000000001A0900000558010341010B6674692F6C6F67696E3C12313233343536373839303132333435360313416324D17241350C0C74F222E3E7CDC13C".into() };

    assert_eq!(option60_dhcpv4_value(&o60).unwrap(), "sagem");
    assert_eq!(option60_dhcpv6_value(&o60), "0000040E0005736167656D");

    assert_eq!(option61_dhcpv4_value(&o61), "00:00:00:00:00:00");
    assert_eq!(option61_dhcpv6_value(&o61), "00:03:00:01:00:00:00:00:00:00");

    assert_eq!(option77_value(&o77).unwrap(), "FSVDSL_livebox.Internet.softathome.Livebox7");

    assert_eq!(
        option90_dhcpv4_value(&o90),
        "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:0B:66:74:69:2F:6C:6F:67:69:6E:3C:12:31:32:33:34:35:36:37:38:39:30:31:32:33:34:35:36:03:13:41:63:24:D1:72:41:35:0C:0C:74:F2:22:E3:E7:CD:C1:3C"
    );
    assert_eq!(
        option90_dhcpv6_value(&o90),
        "00000000000000000000001A0900000558010341010B6674692F6C6F67696E3C12313233343536373839303132333435360313416324D17241350C0C74F222E3E7CDC13C"
    );
}
