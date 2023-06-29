package org.spray.keyauth.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.codec.binary.Base64;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class Encryption {

	public static String encrypt(String strToEncrypt, String enckey, String iv) {
		byte[] _enckey = sha256(enckey).substring(0, 32).getBytes();
		byte[] _iv = sha256(iv).substring(0, 16).getBytes();

		return encrypt_str(strToEncrypt, _enckey, _iv);
	}

	public static String encrypt_str(String strToEncrypt, byte[] enckey, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(enckey, "AES"), new IvParameterSpec(iv));

			return byteToHex(cipher.doFinal(strToEncrypt.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, String enckey, String iv) {
		byte[] _enckey = sha256(enckey).substring(0, 32).getBytes(Charset.defaultCharset());
		byte[] _iv = sha256(iv).substring(0, 16).getBytes(Charset.defaultCharset());

		return decrypt_byte(strToDecrypt, _enckey, _iv);
	}

	public static String decrypt_byte(String strToDecrypt, byte[] enckey, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE,
					new SecretKeySpec(enckey, "AES"),
					new IvParameterSpec(iv));
			byte [] cipherBytes = hexToByte(strToDecrypt);
	        byte[] contentBytes = Arrays.copyOfRange(cipherBytes, 16, cipherBytes.length);
			System.out.println("Hex: " + new String(contentBytes));
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(contentBytes));

			return new String(decrypted, Charset.defaultCharset());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String byteToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexToByte(String hexString) {
	     HexBinaryAdapter adapter = new HexBinaryAdapter();
	     byte[] bytes = adapter.unmarshal(hexString);
	     return bytes;
	}

	public static String sha256(String base) {
		MessageDigest digest;
		byte[] hash = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return byteToHex(hash);
	}

	public static String generateIv() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
