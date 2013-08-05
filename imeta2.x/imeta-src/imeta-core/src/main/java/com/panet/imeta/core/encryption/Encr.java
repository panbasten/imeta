package com.panet.imeta.core.encryption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.jetty.security.Password;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.util.StringUtil;

public class Encr {
	private static final int RADIX = 16;
	private static final String SEED = "0933910847463829827159347601486730416058";

	public static final int ENCRYPT_TYPES_DES = 0;
	public static final int ENCRYPT_TYPES_3DES = 1;
	public static final int ENCRYPT_TYPES_BLOWFISH = 2;
	public static final int ENCRYPT_TYPES_EASY = 3;
	

	public static final String[] ENCRYPT_TYPES = new String[] { "DES",
			"3DES（24位密钥）","Blowfish", "简单加密" };

	public static final int ENCRYPT_OPTION_TYPE_NONE = 0;
	public static final int ENCRYPT_OPTION_TYPE_ENCRYPT = 1;
	public static final int ENCRYPT_OPTION_TYPE_DECRYPT = 2;

	public static final String[] ENCRYPT_OPTION_TYPE = new String[] { "无操作",
			"加密", "解密" };

	public Encr() {
	}

	public boolean init() {
		return true;
	}

	public String buildSignature(String mac, String username, String company,
			String products) {
		try {
			BigInteger bi_mac = new BigInteger(mac.getBytes());
			BigInteger bi_username = new BigInteger(username.getBytes());
			BigInteger bi_company = new BigInteger(company.getBytes());
			BigInteger bi_products = new BigInteger(products.getBytes());

			BigInteger bi_r0 = new BigInteger(SEED);
			BigInteger bi_r1 = bi_r0.xor(bi_mac);
			BigInteger bi_r2 = bi_r1.xor(bi_username);
			BigInteger bi_r3 = bi_r2.xor(bi_company);
			BigInteger bi_r4 = bi_r3.xor(bi_products);

			return bi_r4.toString(RADIX);
		} catch (Exception e) {
			return null;
		}
	}

	public static final boolean checkSignatureShort(String signature,
			String verify) {
		return getSignatureShort(signature).equalsIgnoreCase(verify);
	}

	public static final String getSignatureShort(String signature) {
		String retval = "";
		if (signature == null)
			return retval;
		int len = signature.length();
		if (len < 6)
			return retval;
		retval = signature.substring(len - 5, len);

		return retval;
	}

	public static final String encryptPassword(String password) {
		if (password == null)
			return "";
		if (password.length() == 0)
			return "";

		BigInteger bi_passwd = new BigInteger(password.getBytes());

		BigInteger bi_r0 = new BigInteger(SEED);
		BigInteger bi_r1 = bi_r0.xor(bi_passwd);

		return bi_r1.toString(RADIX);
	}

	public static final String decryptPassword(String encrypted) {
		if (encrypted == null)
			return "";
		if (encrypted.length() == 0)
			return "";

		BigInteger bi_confuse = new BigInteger(SEED);

		try {
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);

			return new String(bi_r0.toByteArray());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * The word that is put before a password to indicate an encrypted form. If
	 * this word is not present, the password is considered to be NOT encrypted
	 */
	public static final String PASSWORD_ENCRYPTED_PREFIX = "Encrypted ";

	/**
	 * Encrypt the password, but only if the password doesn't contain any
	 * variables.
	 * 
	 * @param password
	 *            The password to encrypt
	 * @return The encrypted password or the
	 */
	public static final String encryptPasswordIfNotUsingVariables(
			String password) {
		String encrPassword = "";
		List<String> varList = new ArrayList<String>();
		StringUtil.getUsedVariables(password, varList, true);
		if (varList.isEmpty()) {
			encrPassword = PASSWORD_ENCRYPTED_PREFIX
					+ Encr.encryptPassword(password);
		} else {
			encrPassword = password;
		}

		return encrPassword;
	}

	/**
	 * Decrypts a password if it contains the prefix "Encrypted "
	 * 
	 * @param password
	 *            The encrypted password
	 * @return The decrypted password or the original value if the password
	 *         doesn't start with "Encrypted "
	 */
	public static final String decryptPasswordOptionallyEncrypted(
			String password) {
		if (!Const.isEmpty(password)
				&& password.startsWith(PASSWORD_ENCRYPTED_PREFIX)) {
			return Encr.decryptPassword(password
					.substring(PASSWORD_ENCRYPTED_PREFIX.length()));
		}
		return password;
	}

	/**
	 * Create an encrypted password
	 * 
	 * @param args
	 *            the password to encrypt
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			printOptions();
			System.exit(9);
		}

		String option = args[0];
		String password = args[1];

		if (Const.trim(option).substring(1).equalsIgnoreCase("imeta")) {
			// imeta password obfuscation
			//
			String obfuscated = Encr.encryptPassword(password);
			System.out.println(PASSWORD_ENCRYPTED_PREFIX + obfuscated);
			System.exit(0);

		} else if (Const.trim(option).substring(1).equalsIgnoreCase("password")) {
			// peter password obfuscation
			//
			String obfuscated = Password.obfuscate(password);
			System.out.println(obfuscated);
			System.exit(0);

		} else {
			// Unknown option, print usage
			//
			System.err.println("Unknown option '" + option + "'\n");
			printOptions();
			System.exit(1);
		}

	}

	private static void printOptions() {
		System.err.println("encr usage:\n");
		System.err.println("  encr <-kettle|-carte> <password>");
		System.err.println("  Options:");
		System.err
				.println("    -imeta: generate an obfuscated password to include in Kettle XML files");
		System.err
				.println("    -peter: generate an obfuscated password to include in the carte password file 'pwd/kettle.pwd'");
		System.err
				.println("\nThis command line tool obfuscates a plain text password for use in XML and password files.");
		System.err
				.println("Make sure to also copy the '"
						+ PASSWORD_ENCRYPTED_PREFIX
						+ "' prefix to indicate the obfuscated nature of the password.");
		System.err
				.println("Kettle will then be able to make the distinction between regular plain text passwords and obfuscated ones.");
		System.err.println();
	}

}
