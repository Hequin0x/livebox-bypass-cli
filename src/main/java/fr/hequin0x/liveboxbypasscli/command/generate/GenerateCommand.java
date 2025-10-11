package fr.hequin0x.liveboxbypasscli.command.generate;

import picocli.CommandLine.Command;

@Command(
        name = "generate",
        subcommands = {
                GenerateDHCPSubCommand.class,
                GenerateGPONSubCommand.class,
                GenerateAuthenticationCommand.class
        })
public final class GenerateCommand { }
