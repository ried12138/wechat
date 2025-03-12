package xin.pwdkeeper.wechat.toolutil;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加解密工具
 * @author Tyreal
 * @date 2020/5/11
 */
@Component
public class AesUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String PASSWORD = "!@#ASDASF$W%&$23";

    //出参加密
    public static String encrypt(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        byte[] contentByte = content.getBytes("utf-8");
        cipher.init(1, getSecretKey());
        byte[] encryptByte = cipher.doFinal(contentByte);
        return new String(Base64.encodeBase64(encryptByte, false, true));
    }
    //入参解密
    public static String decrypt(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(2, getSecretKey());
        byte[] decryptByte = cipher.doFinal(Base64.decodeBase64(content));
        return new String(decryptByte, "utf-8");
    }

    private static Key getSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = null;
        keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(PASSWORD.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
}
