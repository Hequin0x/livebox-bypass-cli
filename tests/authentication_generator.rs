use livebox_bypass_cli::generators::authentication_generator::{AuthenticationGenerator, AUTH_PREFIX};

#[test]
fn generates_expected_authentication() {
    let generator = AuthenticationGenerator::new();
    let actual = generator
        .generate_authentication_with_random("fti/xxxxxxx", "xxxxxxx", "4682b8985d10791c")
        .unwrap();

    let expected_suffix = "0D:66:74:69:2F:78:78:78:78:78:78:78:3C:12:34:36:38:32:62:38:39:38:35:64:31:30:37:39:31:63:03:13:34:B5:A2:94:FB:AE:B7:68:0C:04:E7:38:E9:B3:49:F3:3F";
    assert_eq!(actual, format!("{AUTH_PREFIX}{expected_suffix}"));
}

#[test]
fn rejects_empty_login() {
    let generator = AuthenticationGenerator::new();
    let error = generator
        .generate_authentication_with_random("", "password", "4682b8985d10791c")
        .unwrap_err();

    assert!(error.to_string().contains("login and password must be provided"));
}

#[test]
fn rejects_empty_password() {
    let generator = AuthenticationGenerator::new();
    let error = generator
        .generate_authentication_with_random("fti/test", "", "4682b8985d10791c")
        .unwrap_err();

    assert!(error.to_string().contains("login and password must be provided"));
}