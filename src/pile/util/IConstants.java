package pile.util;

public interface IConstants {
	
	/**
	 * denotes the number of bits to be reserved for the Qualifier of any relation resp.
	 * how many most significant bits (0..32) are interpreted as a partition in the 2D space
	 */
	public static final short N_QUALIFIER_BITS = 8;

	/**
	 * Typical values are: "US-ASCII", "UTF-8" (using "UTF-16" or "ISO-8859-1" might
	 * lead to problems)
	 */
	public static final String CHARSET_NAME = "UTF-8"; //"US-ASCII";
	
	public static final String NEWLINE = System.getProperty("line.separator");
	
	/**
	 * Turn this on to get some debug information written to System.out
	 */
	public static final boolean DEBUG = true;
}
