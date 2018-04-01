import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    // used for making signed transaction. We should keep it on secret
    public PrivateKey privateKey;
    // out address
    public PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    // requires key pairing using Elliptic-curve cryptography
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // initialize out key generator and generate a keyPair
            keyPairGenerator.initialize(ecSpec, random);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }
}
