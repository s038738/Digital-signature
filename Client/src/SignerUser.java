import java.security.*;

public class SignerUser {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    public PublicKey getPubKey() {
        return publicKey;
    }

    public SignerUser() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom secRan = new SecureRandom();
        kpg.initialize(512, secRan);
        KeyPair keyP = kpg.generateKeyPair();
        this.publicKey= keyP.getPublic();
        this.privateKey = keyP.getPrivate();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

}
