package fr.hequin0x.liveboxbypasscli.configuration;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "liveboxbypasscli.authentication.generator")
public interface AuthenticationGeneratorConfiguration {
    String prefix();
}
