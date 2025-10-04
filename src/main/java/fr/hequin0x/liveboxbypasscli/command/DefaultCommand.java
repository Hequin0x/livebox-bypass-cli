package fr.hequin0x.liveboxbypasscli.command;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;

@TopCommand
@Command(mixinStandardHelpOptions = true, subcommands = {
        GenerateDHCPCommand.class,
        GenerateGPONCommand.class,
        WanLinkTypeCommand.class
})
public final class DefaultCommand {
}
