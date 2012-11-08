package pile.test;


import static org.junit.Assert.*;

import org.junit.Test;

import pile.util.TextToByteConverter;

public class TextToByteConverterTest {
	
	/**
	 * Tests byte array to char array conversions.
	 */
	@Test
	public void test1() {		
		TextToByteConverter converter = new TextToByteConverter("US-ASCII");
		
		// Testing US-ASCII
		String hexValStr1 = "61"; // us-ascii hexa value of (lower case) character 'a'
		byte byte1 = (byte) Integer.parseInt(hexValStr1, 16);
		String hexValStr2 = "35"; // us-ascii hexa value of character '5'
		byte byte2 = (byte) Integer.parseInt(hexValStr2, 16);
		String hexValStr3 = "24"; // us-ascii hexa value of character '$'
		byte byte3 = (byte) Integer.parseInt(hexValStr3, 16);
		
		byte[] byteArr1 = { byte1, byte2, byte3 };
		char[] chrs1 = converter.byteArrayToCharArray(byteArr1);
		assertEquals(3, chrs1.length);
		assertEquals('a', chrs1[0]);
		assertEquals('5', chrs1[1]);
		assertEquals('$', chrs1[2]);
		
		
		// Testing UTF-8 up to binary values of "01111111" but not higher
		converter.setCharsetName("UTF-8");
		
		String hexValStr4 = "61"; // utf-8 hexa value of (lower case) character 'a'
		byte byte4 = (byte) Integer.parseInt(hexValStr4, 16);
		String hexValStr5 = "35"; // utf-8 hexa value of character '5'
		byte byte5 = (byte) Integer.parseInt(hexValStr5, 16);
		String hexValStr6 = "24"; // utf-8 hexa value of character '$'
		byte byte6 = (byte) Integer.parseInt(hexValStr6, 16);
		
		byte[] byteArr2 = { byte4, byte5, byte6 };
		char[] chrs2 = converter.byteArrayToCharArray(byteArr2);
		assertEquals(3, chrs2.length);
		assertEquals('a', chrs2[0]);
		assertEquals('5', chrs2[1]);
		assertEquals('$', chrs2[2]);
		
		
		// utf-8 hexa value of (lower case) German Umlaut character 'ae'. Consists
		// of 2 bytes!
		String hexValStr7a = "C3";
		String hexValStr7b = "A4";
		int intVal7a = Integer.parseInt(hexValStr7a, 16);
		int intVal7b = Integer.parseInt(hexValStr7b, 16);
		byte byte7a = (byte) intVal7a;
		byte byte7b = (byte) intVal7b;
		byte[] byteArr3 = { byte7a, byte7b };
		char[] chrs3 = converter.byteArrayToCharArray(byteArr3);
		assertEquals(1, chrs3.length);
		assertEquals('\u00E4', chrs3[0]);
		
		// Shorter version of the same code:
		//String hexValStr7 = "C3A4";
		//int intVal7 = Integer.parseInt(hexValStr7, 16);
		//byte byte7a = (byte) (intVal7 >> 8);
		//byte byte7b = (byte) intVal7;
		//byte[] byteArr3 = { byte7a, byte7b };
		//char[] chrs3 = converter.byteArrayToCharArray(byteArr3);
		
		
		
		// Testing Strings with characters with different byte lengths.
		
		// utf-8 hexa value of Euro currency sign. Consists of 3 bytes!
		String hexValStr8 = "E282AC";
		int intVal8 = Integer.parseInt(hexValStr8, 16);
		byte byte8a = (byte) (intVal8 >> 16);
		byte byte8b = (byte) (intVal8 >> 8);	
		byte byte8c = (byte) intVal8;
		
		// utf-8 hexa value of copyright sign. Consists of 2 bytes!
		String hexValStr9 = "C2A9";
		int intVal9 = Integer.parseInt(hexValStr9, 16);
		byte byte9a = (byte) (intVal9 >> 8);
		byte byte9b = (byte) intVal9;
		
		// Insert a one-byte character 'a' in the middle!
		byte[] byteArr4 = { byte8a, byte8b, byte8c, byte4, byte9a, byte9b };
		char[] chrs4 = converter.byteArrayToCharArray(byteArr4);
		assertEquals(3, chrs4.length);
		assertEquals('\u20AC', chrs4[0]);	// Euro currency sign
		assertEquals('\u0061', chrs4[1]);	// lower case character 'a'
		assertEquals('\u00A9', chrs4[2]);	// Copyright sign
	}
	
	
	/**
	 * Tests String to byte array conversions.
	 */
	@Test
	public void test2() {
		TextToByteConverter converter = new TextToByteConverter("US-ASCII");
		
		String s1 = new String(new char[] {'a', '5', '$'});
		byte[] bs1 = converter.stringToByteArray(s1);
		assertEquals(3, bs1.length);
		
		String hexValStr1 = "61"; // us-ascii hexa value of (lower case) character 'a'
		byte byte1 = (byte) Integer.parseInt(hexValStr1, 16);
		assertEquals(byte1, bs1[0]);
		
		String hexValStr2 = "35"; // us-ascii hexa value of character '5'
		byte byte2 = (byte) Integer.parseInt(hexValStr2, 16);
		assertEquals(byte2, bs1[1]);
		
		String hexValStr3 = "24"; // us-ascii hexa value of character '$'
		byte byte3 = (byte) Integer.parseInt(hexValStr3, 16);
		assertEquals(byte3, bs1[2]);
		
		
		// Testing UTF-8 up to binary values of "01111111"
		converter.setCharsetName("UTF-8");
		
		String s2 = new String(new char[] {'a', '5', '$'});
		byte[] bs2 = converter.stringToByteArray(s2);
		assertEquals(3, bs2.length);
		
		String hexValStr4 = "61"; // utf-8 hexa value of (lower case) character 'a'
		byte byte4 = (byte) Integer.parseInt(hexValStr4, 16);
		assertEquals(byte4, bs1[0]);
		
		String hexValStr5 = "35"; // utf-8 hexa value of character '5'
		byte byte5 = (byte) Integer.parseInt(hexValStr5, 16);
		assertEquals(byte5, bs1[1]);
		
		String hexValStr6 = "24"; // utf-8 hexa value of character '$'
		byte byte6 = (byte) Integer.parseInt(hexValStr6, 16);
		assertEquals(byte6, bs1[2]);
		
		
		// utf-8 hexa value of (lower case) German Umlaut character 'ae'. Consists
		// of 2 bytes!
		String s3 = new String(new char[] {'\u00E4'});
		byte[] bs3 = converter.stringToByteArray(s3);
		assertEquals(2, bs3.length);
		
		String hexValStr7 = "C3A4";
		int intVal7 = Integer.parseInt(hexValStr7, 16);
		byte byte7a = (byte) (intVal7 >> 8);
		byte byte7b = (byte) intVal7;
		assertEquals(byte7a, bs3[0]);
		assertEquals(byte7b, bs3[1]);
		
		
		// Testing Strings with characters with different byte lengths.
		// 1. utf-8 hexa value of Euro currency sign. Consists of 3 bytes!
		// 2. utf-8 character 'a'. Consists of 1 byte.
		// 3. utf-8 hexa value of copyright sign. Consists of 2 bytes!
		
		String s4 = new String(new char[] {'\u20AC', '\u0061', '\u00A9'});
		byte[] bs4 = converter.stringToByteArray(s4);
		assertEquals(6, bs4.length);
		
		// Euro currency sign
		String hexValStr8 = "E282AC";
		int intVal8 = Integer.parseInt(hexValStr8, 16);
		byte byte8a = (byte) (intVal8 >> 16);
		byte byte8b = (byte) (intVal8 >> 8);	
		byte byte8c = (byte) intVal8;
		assertEquals(byte8a, bs4[0]);
		assertEquals(byte8b, bs4[1]);
		assertEquals(byte8c, bs4[2]);
		
		// lower case letter 'a'
		assertEquals(byte4, bs4[3]);
		
		// Copyright sign
		String hexValStr9 = "C2A9";
		int intVal9 = Integer.parseInt(hexValStr9, 16);
		byte byte9a = (byte) (intVal9 >> 8);
		byte byte9b = (byte) intVal9;
		assertEquals(byte9a, bs4[4]);
		assertEquals(byte9b, bs4[5]);
	}
	
	
	/**
	 * Testing char array to byte array conversion
	 */
	@Test
	public void test3() {
		TextToByteConverter converter = new TextToByteConverter("US-ASCII");
		
		char[] cs1 = new char[] {'a', '5', '$'};
		byte[] bs1 = converter.charArrayToByteArray(cs1);
		assertEquals(3, bs1.length);
		
		String hexValStr1 = "61"; // us-ascii hexa value of (lower case) character 'a'
		byte byte1 = (byte) Integer.parseInt(hexValStr1, 16);
		assertEquals(byte1, bs1[0]);
		
		String hexValStr2 = "35"; // us-ascii hexa value of character '5'
		byte byte2 = (byte) Integer.parseInt(hexValStr2, 16);
		assertEquals(byte2, bs1[1]);
		
		String hexValStr3 = "24"; // us-ascii hexa value of character '$'
		byte byte3 = (byte) Integer.parseInt(hexValStr3, 16);
		assertEquals(byte3, bs1[2]);
		
		
		// Testing UTF-8 up to binary values of "01111111"
		converter.setCharsetName("UTF-8");
		
		char[] cs2 = new char[] {'a', '5', '$'};
		byte[] bs2 = converter.charArrayToByteArray(cs2);
		assertEquals(3, bs2.length);
		
		String hexValStr4 = "61"; // utf-8 hexa value of (lower case) character 'a'
		byte byte4 = (byte) Integer.parseInt(hexValStr4, 16);
		assertEquals(byte4, bs1[0]);
		
		String hexValStr5 = "35"; // utf-8 hexa value of character '5'
		byte byte5 = (byte) Integer.parseInt(hexValStr5, 16);
		assertEquals(byte5, bs1[1]);
		
		String hexValStr6 = "24"; // utf-8 hexa value of character '$'
		byte byte6 = (byte) Integer.parseInt(hexValStr6, 16);
		assertEquals(byte6, bs1[2]);
		
		
		// utf-8 hexa value of (lower case) German Umlaut character 'ae'. Consists
		// of 2 bytes!
		char[] cs3 = new char[] {'\u00E4'};
		byte[] bs3 = converter.charArrayToByteArray(cs3);
		assertEquals(2, bs3.length);
		
		String hexValStr7 = "C3A4";
		int intVal7 = Integer.parseInt(hexValStr7, 16);
		byte byte7a = (byte) (intVal7 >> 8);
		byte byte7b = (byte) intVal7;
		assertEquals(byte7a, bs3[0]);
		assertEquals(byte7b, bs3[1]);
		
		
		
		// Testing Strings with characters with different byte lengths.
		// 1. utf-8 hexa value of Euro currency sign. Consists of 3 bytes!
		// 2. utf-8 character 'a'. Consists of 1 byte.
		// 3. utf-8 hexa value of copyright sign. Consists of 2 bytes!
		
		char[] cs4 = new char[] {'\u20AC', '\u0061', '\u00A9'};
		byte[] bs4 = converter.charArrayToByteArray(cs4);
		assertEquals(6, bs4.length);
		
		// Euro currency sign
		String hexValStr8 = "E282AC";
		int intVal8 = Integer.parseInt(hexValStr8, 16);
		byte byte8a = (byte) (intVal8 >> 16);
		byte byte8b = (byte) (intVal8 >> 8);	
		byte byte8c = (byte) intVal8;
		assertEquals(byte8a, bs4[0]);
		assertEquals(byte8b, bs4[1]);
		assertEquals(byte8c, bs4[2]);
		
		// lower case letter 'a'
		assertEquals(byte4, bs4[3]);
		
		// Copyright sign
		String hexValStr9 = "C2A9";
		int intVal9 = Integer.parseInt(hexValStr9, 16);
		byte byte9a = (byte) (intVal9 >> 8);
		byte byte9b = (byte) intVal9;
		assertEquals(byte9a, bs4[4]);
		assertEquals(byte9b, bs4[5]);
	}
	
	@Test
	public void test4() {
		TextToByteConverter converter = new TextToByteConverter("US-ASCII");
		
		for (int i = 0; i < 256; i++) {
			byte b = new byte[] { (byte)i }[0];
			System.out.println(i + ". " + converter.byteToChar(b));
			assertEquals((char)(byte)i, converter.byteToChar(b));
		}
	}
}
