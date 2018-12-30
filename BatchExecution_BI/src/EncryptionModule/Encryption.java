/*
* @Author : VIKAS YADAV
 */
package EncryptionModule;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
/*
 * @Author : VIKAS YADAV	
 */
public class Encryption {
	
	private static final String UNICODE_FORMAT = "UTF8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private byte[] arrayBytes;
    private SecretKey key;

    public Encryption(){
        String myEncryptionKey = "ThisIsSpartaThisIsSparta";
        String myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        try {
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        try {
			ks = new DESedeKeySpec(arrayBytes);
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		}
        try {
			skf = SecretKeyFactory.getInstance(myEncryptionScheme);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        try {
			try {
				cipher = Cipher.getInstance(myEncryptionScheme);
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        try {
			key = skf.generateSecret(ks);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
    }
	
	public String encrypt(String unencryptedString) {
    String encryptedString = null;
    try {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
        byte[] encryptedText = cipher.doFinal(plainText);
        encryptedString = new String(Base64.encodeBase64(encryptedText));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return encryptedString;
}


	public String decrypt(String encryptedString) {
    String decryptedText=null;
    try {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedText=Base64.decodeBase64(encryptedString);
        byte[] plainText = cipher.doFinal(encryptedText);
        decryptedText= new String(plainText);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decryptedText;
}

}
