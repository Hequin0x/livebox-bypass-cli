package fr.hequin0x.liveboxbypasscli.service;

import fr.hequin0x.liveboxbypasscli.configuration.AuthenticationGeneratorConfiguration;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static fr.hequin0x.liveboxbypasscli.util.FormatterUtils.*;

@Singleton
public final class AuthenticationGeneratorService {

    private final AuthenticationGeneratorConfiguration configuration;

    public AuthenticationGeneratorService(final AuthenticationGeneratorConfiguration configuration) {
        this.configuration = configuration;
    }

    public String generateAuthentication(final String login, final String password) throws NoSuchAlgorithmException {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Login and password must be provided");
        }

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

        return this.configuration.prefix() + addSeparators(payload).toUpperCase();
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

    public String generateRandom() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[1024];
        random.nextBytes(bytes);

        return this.computeDigest(bytes).substring(0, 16);
    }

    public String computeDigest(final byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(data);

        return toHex(hash);
    }
}
