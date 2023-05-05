package in.fiberstory.tfsplaytv.utility;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SonyAES256CBCEncryption {
    //private static String data = "Tn0uWAt9YpNcho+FvnanoF1ORQIrh4lCK2PKWWdAcl8=";
    public  String data = "8500443177|1657435332000";
    public  String aesKey = "TjWnZr4u7x!A%D*G-KaNdRgUkXp2s5v8";
    public  String iv = "NdRgUkXp2r5u8x/A";
    public  byte[] _key, _iv;
    public  Cipher _cx;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        String decryptionData;
        String encryptionData;
        try {
            _key = new byte[32]; //256 bit key space
            _iv = new byte[16]; //128 bit IV
            _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //decryptionData = decrypt(data,aesKey,iv);
            encryptionData = encrypt(data, aesKey, iv);
            //System.out.println("decryptionData   "+ decryptionData);
            System.out.println("encryptionData   " + encryptionData);
        } catch (InvalidKeyException | UnsupportedEncodingException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static enum EncryptMode {
        ENCRYPT, DECRYPT;
    }

    // cipher to be used for encryption and decryption

    // encryption key and initialization vector


    public SonyAES256CBCEncryption() {
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        try {
            _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        _key = new byte[32]; //256 bit key space
        _iv = new byte[16]; //128 bit IV
    }


    public static final String md5(final String inputString) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(inputString.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private  String encryptDecrypt(String _inputText, String _encryptionKey,
                                         EncryptMode _mode, String _initVector) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String _out = "";// output string
        //_encryptionKey = md5(_encryptionKey);
        //System.out.println("key="+_encryptionKey);

        int len = _encryptionKey.getBytes("UTF-8").length; // length of the key	provided

        if (_encryptionKey.getBytes("UTF-8").length > _key.length)
            len = _key.length;

        int ivlen = _initVector.getBytes("UTF-8").length;

        if (_initVector.getBytes("UTF-8").length > _iv.length)
            ivlen = _iv.length;

        System.arraycopy(_encryptionKey.getBytes("UTF-8"), 0, _key, 0, len);
        System.arraycopy(_initVector.getBytes("UTF-8"), 0, _iv, 0, ivlen);
        //KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
        //_keyGen.init(128);

        SecretKeySpec keySpec = new SecretKeySpec(_key, "AES"); // Create a new SecretKeySpec
        // for the
        // specified key
        // data and
        // algorithm
        // name.

        IvParameterSpec ivSpec = new IvParameterSpec(_iv); // Create a new
        // IvParameterSpec
        // instance with the
        // bytes from the
        // specified buffer
        // iv used as
        // initialization
        // vector.

        // encryption
        if (_mode.equals(EncryptMode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older.
            // Read
            // for more info.
            _cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
            byte[] results = _cx.doFinal(_inputText.getBytes("UTF-8")); // Finish
            // multi-part
            // transformation
            // (encryption)
            _out = new String(java.util.Base64.getMimeEncoder().encode(results),
                    StandardCharsets.UTF_8);
            // output

            ;


        }

        // decryption
        if (_mode.equals(EncryptMode.DECRYPT)) {
            _cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this ipher instance

            byte[] decodedValue = java.util.Base64.getMimeDecoder().decode(_inputText.getBytes(StandardCharsets.UTF_8));
            byte[] decryptedVal = _cx.doFinal(decodedValue); // Finish
            // multi-part
            // transformation
            // (decryption)
            _out = new String(decryptedVal);
        }
        System.out.println(_out);
        return _out; // return encrypted/decrypted string
    }


    public static String SHA256(String text, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        StringBuffer result = new StringBuffer();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        //return result.toString();

        if (length > result.toString().length()) {
            resultStr = result.toString();
        } else {
            resultStr = result.toString().substring(0, length);
        }

        return resultStr;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String encrypt(String _plainText, String _key, String _iv)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(_plainText, _key, EncryptMode.ENCRYPT, _iv);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String decrypt(String _encryptedText, String _key, String _iv)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {

        return encryptDecrypt(_encryptedText, _key, EncryptMode.DECRYPT, _iv);
    }


    public static String generateRandomIV(int length) {
        SecureRandom ranGen = new SecureRandom();
        byte[] aesKey = new byte[16];
        ranGen.nextBytes(aesKey);
        StringBuffer result = new StringBuffer();
        for (byte b : aesKey) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        if (length > result.toString().length()) {
            return result.toString();
        } else {
            return result.toString().substring(0, length);
        }
    }
}
