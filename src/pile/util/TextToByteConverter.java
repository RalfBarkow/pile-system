package pile.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class TextToByteConverter implements IConstants {
	
	private String charsetName;
	
	/**
	 * Creates a new TextToByteConverter instance using the given charset
	 * name (e.g. "UTF-8" or "US-ASCII") for conversions.
	 */
	public TextToByteConverter(String charsetName) {
		this.charsetName = charsetName;
	}
	
	/**
	 * Creates a new TextToByteConverter instance using the JVM's default
	 * charset name.
	 */
	public TextToByteConverter() {
		this(Charset.defaultCharset().displayName());
	}
	
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}
	
	public String getCharsetName() {
		return charsetName;
	}
	
	/**
	 * This method only accepts bytes representing characters using a single byte,
	 * thus only the characters represented by value 0x00..0xFF
	 * can be represented.
	 * @param b the byte representing a char
	 * @return the character for this byte
	 */
	public char byteToChar(byte b) {
		return (char) b;
	}
//	public char byteToChar(byte b) {
//		char c = 0;
//		try {
//			c = new String(new byte[] { b }, charsetName).charAt(0);
//		}
//		catch (UnsupportedEncodingException e) {
//			throw new IllegalArgumentException(
//					"TextToByteConverter.byteToChar: Unable to convert byte to char.", e);
//		}
//		return c;
//	}
	
	/**
	 * Accepts a char as an input and returns its second byte.
	 * @param c the char
	 * @return the char's second byte
	 */
	public byte charToByte(char c) {
		return (byte) c;
	}
     
	/**
	 * Converts a byte array to a char array using the given charset
	 * @pre the given byte array may only contain characters made up of a single byte
	 *   (from binary 00000000 up to and including 11111111). Be aware that in UTF-8
	 *   beyond 01111111 there are multi-byte characters. These are not accepted here,
	 *   because per definition we only accept 256 different qualifier characters.
	 *   Also be aware that in US-ASCII there are control characters starting from
	 *   0x00 (00000000) up to 0x1F (00011111).
	 * @param byteArr the byte array to convert
	 * @return the char array
	 */
    public char[] byteArrayToCharArray(byte[] byteArr) {
    	char[] cs = null;
    	try {
	        String s = new String(byteArr, charsetName);
	        cs = s.toCharArray();
    	}
    	catch (UnsupportedEncodingException e) {
    		throw new IllegalArgumentException(
    				"TextToByteConverter.byteArrayToCharArray: Unable to convert " +
    				"byte array to char array.", e);
    	}
    	return cs;
    }
    
    /**
     * Converts a String to a byte array using the given charset name
     * @param str the String to convert
     * @return a byte array of the given String
     */
    public byte[] stringToByteArray(String str) {
    	byte[] bs = null;
    	try {
    		bs = str.getBytes(charsetName);
    	}
    	catch (UnsupportedEncodingException e) {
    		
    	}
    	return bs;
    }
    
    /**
     * Converts a char array to a byte array using the given charset name.
     * If a character set is selected which converts certain chars with more
     * than one byte (like UTF-8), then the byte array will contain multiple
     * subsequent entries for each byte.
     * @param cs the char array to convert
     * @param charsetName the charset name to use (e.g. "US-ASCII", "UTF-8")
     * @return a byte array of the given char array
     */
    public byte[] charArrayToByteArray(char[] cs) {
    	return stringToByteArray(new String(cs));
    }

}
