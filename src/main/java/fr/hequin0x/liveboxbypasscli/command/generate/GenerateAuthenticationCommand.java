package fr.hequin0x.liveboxbypasscli.command.generate;

import com.github.freva.asciitable.AsciiTable;
import org.jboss.logging.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.*;

@Command(
        name = "authentication",
        description = "Generates DHCPV4/V6 Authentication with the provided login (fti/xxx) and password."
)
public final class GenerateAuthenticationCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(GenerateAuthenticationCommand.class);
    private static final String AUTH_PREFIX = "00:00:00:00:00:00:00:00:00:00:00:1A:09:00:00:05:58:01:03:41:01:";

    @Option(names = {"-l", "--login"}, description = "Orange login (fti/xxx)", required = true)
    private String login;

    @Option(names = {"-p", "--password"}, description = "Orange password", arity = "0..1", required = true, interactive = true)
    private String password;

    @Override
    public void run() {
        try {
            String authentication = this.generateAuthentication(this.login, this.password);

            String dhcpv4Table = AsciiTable.getTable(new String[] {"DHCPv4 Option", "Value"}, new String[][] {
                    {"90", authentication}
            });

            String dhcpv6Table = AsciiTable.getTable(new String[] {"DHCPv6 Option", "Value"}, new String[][] {
                    {"11", authentication.replace(":", "")}
            });

            LOG.infof("\n%s\n%s", dhcpv4Table, dhcpv6Table);
        } catch (Exception e) {
            LOG.error("An error occurred while generating the authentication", e);
        }
    }

    private String generateAuthentication(final String login, final String password) throws NoSuchAlgorithmException {
        String random = this.generateRandom();
        String id = random.substring(0, 1);

        String randomHex = toHex(random);
        String idHex = toHex(id);

        String digestInput = id + password + random;
        String digest = this.computeDigest(digestInput.getBytes(StandardCharsets.UTF_8));

        String authChain = this.buildAuthenticationChain(randomHex, idHex, digest);

        String loginHex = toHex(login);
        String loginPayload = to1ByteHexLength(loginHex) + loginHex;

        String payload = loginPayload + authChain;

        return AUTH_PREFIX + addSeparators(payload).toUpperCase();
    }

    private String buildAuthenticationChain(final String randomHex, final String idHex, final String digest) {
        String randomHexLength = to1ByteHexLength(randomHex);
        String idHexLength = to1ByteHexLength(idHex);
        String chainLength = to1ByteHex((randomHex.length() + digest.length()) - 4);
        String digestWithIdLength = to1ByteHexLength(digest + idHexLength);

        return chainLength +
                randomHexLength + randomHex +
                idHexLength + digestWithIdLength +
                idHex + digest;
    }

    private String generateRandom() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[1024];
        random.nextBytes(bytes);

        return this.computeDigest(bytes).substring(0, 16);
    }

    private String computeDigest(final byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(data);

        return toHex(hash);
    }
}
