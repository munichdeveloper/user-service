package de.munichdeveloper.user.magic;

import com.google.gson.Gson;
import de.munichdeveloper.user.exception.IncorrectSignerException;
import de.munichdeveloper.user.exception.MalformedTokenException;
import org.json.JSONArray;

import java.time.Instant;
import java.util.Base64;

public class DIDTokenService {
    private final static Integer LEEWAY_TIME_SECONDS = 300; // 300 seconds


    public static DIDToken parseAndValidateToken(String didToken) {
        ParsedDIDToken parsedDIDToken = parseDIDToken(didToken);
        validateDIDToken(parsedDIDToken);
        return parsedDIDToken.getParsedDIDToken();
    }

    private static ParsedDIDToken parseDIDToken(String didToken) {
        byte[] decodedBytes = Base64.getDecoder().decode(didToken);
        String decodedString = new String(decodedBytes);
        JSONArray jsonArray = new JSONArray(decodedString);
        String signature = (String) jsonArray.get(0);
        String claim = (String) jsonArray.get(1);
        try {
            DIDToken parsedDidToken = new Gson().fromJson(claim, DIDToken.class);
            return ParsedDIDToken.builder().parsedDIDToken(parsedDidToken).signature(signature).claim(claim).build();
        } catch (Exception ex) {
            throw new MalformedTokenException();
        }
    }

    private static void validateDIDToken(ParsedDIDToken parsedDIDToken) {
        String claimedIssuer;

        try {
            claimedIssuer = parsedDIDToken.getParsedDIDToken().getIssuer().split(":")[2];
        } catch (Exception e) {
            throw new MalformedTokenException("Failed to parse issuer");
        }

        Instant now = Instant.now();
        long expiredAt = parsedDIDToken.getParsedDIDToken().getExpiredAt();
        long notBefore = parsedDIDToken.getParsedDIDToken().getNotBefore();

        validateClaim(claimedIssuer, parsedDIDToken);
        validateEXT(Instant.ofEpochSecond(expiredAt), now);
        validateNBF(Instant.ofEpochSecond(notBefore), now);
    }

    private static void validateClaim(String claimedIssuer, ParsedDIDToken parsedDIDToken) throws IncorrectSignerException {
        var claim = parsedDIDToken.getClaim();
        var signature = parsedDIDToken.getSignature();

        var recoveredClaimAddress = Cryptography.ecRecover(claim, signature);

        if (!recoveredClaimAddress.equals(claimedIssuer)) {
            throw new IncorrectSignerException("Addresses don't match");
        }
    }

    private static void validateEXT(Instant expiredAt, Instant now) throws IncorrectSignerException {
        if (expiredAt.isBefore(now)) {
            // Token expiration date is before current time, i.e. token has expired
            throw new IncorrectSignerException("Token is expired");
        }
    }

    private static void validateNBF(Instant notBefore, Instant now) throws IncorrectSignerException {
        if (now.isBefore(notBefore.minusSeconds(LEEWAY_TIME_SECONDS))) {
            // Token has not been activated yet, hence it is invalid
            throw new IncorrectSignerException("Token is not yet valid");
        }
    }
}
