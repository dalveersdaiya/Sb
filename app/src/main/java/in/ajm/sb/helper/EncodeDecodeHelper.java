package in.ajm.sb.helper;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * Class used to encode/decode string into Base64 or MD5
 *
 * @see Base64 for more details
 */
public class EncodeDecodeHelper
{
	private static final String HASH_ALGORITHM = "MD5";
	private static final int RADIX = 10 + 26; // 10 digits + 26 letters

	/**
	 * Function encode provided string into Base64 string
	 *
	 * @param strToEncode String to encode into base64
	 */
	public static String encodeString(String strToEncode)
	{
		if (strToEncode != null && strToEncode.length() > 0)
		{
			byte[] encodeTextBytes = strToEncode.getBytes();
			byte[] encodedBytes = Base64.encode(encodeTextBytes, Base64.DEFAULT);
			String encodedStr = new String(encodedBytes);
			encodedStr = encodedStr.replace("\n", "");
			return encodedStr;
		}
		return strToEncode;
	}

	/**
	 * Function decode provided Base64 encoded string to normal string
	 *
	 * @param strToDecode String to decode into base64
	 */
	public static String decodeString(String strToDecode)
	{
		if (strToDecode != null && strToDecode.length() > 0)
		{
			byte[] decodedTextBytes = Base64.decode(strToDecode, Base64.DEFAULT);
			return new String(decodedTextBytes);
		}
		return strToDecode;
	}

	/**
	 * Function encode provided string using MD5
	 *
	 * @param strToEncode String to encode
	 */
	public static String encodeStringUsingMd5(String strToEncode)
	{
		if (strToEncode != null && strToEncode.length() > 0)
		{
			try
			{
				byte[] encodeTextBytes = strToEncode.getBytes();
				MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
				digest.update(encodeTextBytes);
				byte[] encodedBytes = digest.digest();
				BigInteger bi = new BigInteger(encodedBytes).abs();
				return bi.toString(RADIX);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return strToEncode;
		}
		return strToEncode;
	}

	public static String urlEncoding(String msg){
		try
		{
			return URLEncoder.encode(msg, "UTF-8");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
			return "";
		}
	}
}
