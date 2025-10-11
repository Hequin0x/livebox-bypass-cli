package fr.hequin0x.liveboxbypasscli.command.provider;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import picocli.CommandLine.IVersionProvider;

@Singleton
public class AppVersionProvider implements IVersionProvider {

    @ConfigProperty(name = "quarkus.application.version", defaultValue = "unknown")
    String version;

    @Override
    public String[] getVersion() throws Exception {
        return new String[] { this.version };
    }
}
