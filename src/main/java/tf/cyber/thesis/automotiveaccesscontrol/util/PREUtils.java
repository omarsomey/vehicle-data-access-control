package tf.cyber.thesis.automotiveaccesscontrol.util;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.apache.commons.lang3.RandomStringUtils;

public class PREUtils {

    private String cryptoFolder;
    private String dataFolder;
    private String schemeName ;
    private String ccPath;
    private int plainttextModulus;
    private int multiplicativeDepth;

    public PREUtils(String schemeName, int plainttextModulus, int multiplicativeDepth) {
        this.schemeName = schemeName;
        this.plainttextModulus = plainttextModulus;
        this.multiplicativeDepth = multiplicativeDepth;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    public String getCryptoFolder() {
        return cryptoFolder;
    }

    public void setCryptoFolder(String cryptoFolder) {
        this.cryptoFolder = cryptoFolder;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getCcPath() {
        return ccPath;
    }

    public void setCcPath(String ccPath) {
        this.ccPath = ccPath;
    }

    public int getPlainttextModulus() {
        return plainttextModulus;
    }

    public void setPlainttextModulus(int plainttextModulus) {
        this.plainttextModulus = plainttextModulus;
    }

    public int getMultiplicativeDepth() {
        return multiplicativeDepth;
    }

    public void setMultiplicativeDepth(int multiplicativeDepth) {
        this.multiplicativeDepth = multiplicativeDepth;
    }


    
    public static void main(String[] args) {

        PREUtils pre = new PREUtils("BFV", 256, 2);
        pre.setCryptoFolder("/home/somey/IdeaProjects/automotive-access-control/src/main/resources/crypto/");
        pre.setDataFolder("/home/somey/IdeaProjects/automotive-access-control/src/main/resources/data/");

        //Random String generator
        String generatedString = RandomStringUtils.randomAlphanumeric(300);
        OpenPRE.INSTANCE.cryptoContextGen(pre.getSchemeName(), pre.getCryptoFolder(),"cryptocontext", pre.getPlainttextModulus() );
        OpenPRE.INSTANCE.keysGen(pre.getCryptoFolder() + "cryptocontext", pre.getCryptoFolder()+"a" );
        OpenPRE.INSTANCE.Encrypt( pre.getCryptoFolder() + "a-public-key", generatedString, pre.getDataFolder() + "ciphertext");
        //String b = OpenPRE.INSTANCE.Decrypt(pre.getCryptoFolder() + "a-private-key", pre.getDataFolder() + "ciphertext");
        //System.out.print("Plaintext decrypted is : "+b);
        OpenPRE.INSTANCE.keysGen(pre.getCryptoFolder() + "cryptocontext", pre.getCryptoFolder()+"b");
        OpenPRE.INSTANCE.ReKeyGen( pre.getCryptoFolder() + "a-private-key", pre.getCryptoFolder() + "b-public-key", pre.getCryptoFolder() + "a2b");
        OpenPRE.INSTANCE.ReEncrypt(pre.getDataFolder() + "ciphertext", pre.getCryptoFolder() + "a2b-re-enc-key",  pre.getDataFolder() + "ciphertext-re");
        String d = OpenPRE.INSTANCE.Decrypt( pre.getCryptoFolder() + "b-private-key", pre.getDataFolder() + "ciphertext-re");
        System.out.print("Decryption of Ciphertext re encrypted : "+d);
    }
}