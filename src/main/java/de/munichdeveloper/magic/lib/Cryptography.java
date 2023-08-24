package de.munichdeveloper.magic.lib;

import de.munichdeveloper.user.exception.MalformedTokenException;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.security.SignatureException;
import java.util.Arrays;

@Slf4j
public class Cryptography {
    public static String ecRecover(String claim, String signature) {
        var msgHash = Sign.getEthereumMessageHash(claim.getBytes());
        var sigData = sigFromByteArray(signature);

        try {
            var recoveredKey = Sign.signedMessageHashToKey(msgHash, sigData);
            var address = Keys.getAddress(recoveredKey);
            return Keys.toChecksumAddress(address);
        } catch (SignatureException se) {
            log.error("se: " + se);
            return "";
        }
    }

    private static Sign.SignatureData sigFromByteArray(String signature) {
        var sigBytes = Numeric.hexStringToByteArray(signature);

        if (sigBytes.length != 65) {
            throw new MalformedTokenException();
        }

        var v = sigBytes[64];
        var r = Arrays.copyOfRange(sigBytes, 0, 32);
        var s = Arrays.copyOfRange(sigBytes, 32, 64);

        return new Sign.SignatureData(v, r, s);
    }
}
