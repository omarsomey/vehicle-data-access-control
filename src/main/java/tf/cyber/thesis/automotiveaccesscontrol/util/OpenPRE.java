package tf.cyber.thesis.automotiveaccesscontrol.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface OpenPRE extends Library {
    OpenPRE INSTANCE = (OpenPRE)
            Native.load((Platform.isLinux() ? "crypto" : "c"),
                    OpenPRE.class);
    void cryptoContextGen(String schemeName, String cryptoFolder, String filename, int plaintextModulus, int multiplicativeDepth);
    void keysGen(String cryptoFolder, String cryptoContext, String filename);
    String  Encrypt(String publickey, String plaintext, String cryptoFolder, String cryptoContext);
    String Decrypt(String secretKey, String ciphertext, String cryptoFolder, String cryptoContext);
    void ReKeyGen(String secretKey, String publicKey, String cryptoFolder, String cryptoContext, String filename);
    String ReEncrypt(String ciphertext, String reEncryptionKey, String cryptoFolder, String cryptoContext);
}
