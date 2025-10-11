package fr.hequin0x.liveboxbypasscli.command;

import fr.hequin0x.liveboxbypasscli.command.generate.GenerateCommand;
import fr.hequin0x.liveboxbypasscli.command.provider.AppVersionProvider;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;

@TopCommand
@Command(
        mixinStandardHelpOptions = true,
        versionProvider = AppVersionProvider.class,
        subcommands = {
                GenerateCommand.class
        })
public final class DefaultCommand { }
