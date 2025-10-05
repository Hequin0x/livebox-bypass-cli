package fr.hequin0x.liveboxbypasscli.command;

import fr.hequin0x.liveboxbypasscli.provider.AppVersionProvider;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;

@TopCommand
@Command(
        mixinStandardHelpOptions = true,
        versionProvider = AppVersionProvider.class,
        subcommands = {
                GenerateDHCPCommand.class,
                GenerateGPONCommand.class,
                WanLinkTypeCommand.class
        })
public final class DefaultCommand {
}
