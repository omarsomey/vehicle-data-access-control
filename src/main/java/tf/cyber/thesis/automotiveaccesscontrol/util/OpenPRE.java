package tf.cyber.thesis.automotiveaccesscontrol.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface OpenPRE extends Library {
    OpenPRE INSTANCE = (OpenPRE)
            Native.load((Platform.isLinux() ? "crypto" : "c"),
                    OpenPRE.class);
    void cryptoContextGen(String schemeName, String cryptoFolder, String filename, int plaintextModulus);
    void keysGen(String cryptoContext, String destinationPath);
    void  Encrypt(String publickey, String plaintext, String destinationPath);
    String Decrypt(String secretKey, String ciphertext);
    void ReKeyGen(String secretKey, String publicKey, String destinationPath);
    void ReEncrypt(String ciphertext, String reEncryptionKey, String destinationPath);
}
