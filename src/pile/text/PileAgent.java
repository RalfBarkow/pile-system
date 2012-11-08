package pile.text;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pile.engine.IPileEngine;
import pile.engine.PileEngine;
import pile.engine.IPileEngine.Manners;
import pile.util.IConstants;
import pile.util.TextToByteConverter;


/**
 * There are several types of relations in a pile agent:
 * 1. Top: no parents, representation of a top value (Tv)
 * 2. (Top, Top): called System Definer (SD)
 * 3. (Np, Top): called (Normative) Root (Nr)
 * 4. (Top, Ap): called (Associative) Root (Ar)
 * 5. (Np, Ap): a "common" relation
 * 
 * A top is a relation without normative and associative parent. It represents a
 * top value residing outside the system. The association must be stored outside
 * the pile (for instance, as is the case here, in a Map). Currently, this pile
 * agent only allows working with 256 different characters which can be
 * represented using one byte (thus US-ASCII characters would be allowed as an
 * input but not UTF-8 characters).
 * A top is represented as a 32 bit int value with 8 leading bits reserved for
 * the qualifier and the following 24 bits reserved for an integer value. There
 * are thus 2 ^ 24 possible int values per qualifier.
 */
public class PileAgent implements IConstants {
	
    private IPileEngine pe;
    private TextToByteConverter converter;

    /**
     * Handle to the segment in the 2D space where combinations of letters are stored
     */
    private int sdLetterRoots;
    
    /**
     * Handle to the segment in the 2D space where (combinations of) texts are stored
     */
    private int sdText;
    
    private int[] abcTops, abcRoots;
    
    /**
     * Contains the "relations" between tops and the top values. Here, the top
     * values are common alphabetic characters. Top values usually do not directly
     * belong to a pile are stored outside of it (as in this Map).
     */
    private Map<Integer, Character> abcHandles;


    /**
     * There are qualifiers for
     * 1. roots: ???
     * 2. letters: combinations of characters
     * 3. lines of text: starting a fresh line and ending with the
     *   newline character ???
     * 4. text: ???
     * 
     * The qualifier integer value 0 is used for the initialization of tops.
     */
    public static enum Qualifiers {
    	ROOTS(1, "ROOTS"),
    	LETTERS(2, "LETTERS"),
    	LINES(3, "LINES"),
    	TEXT(4, "TEXT");
    	
    	String name;
    	int qualifier;
    	Qualifiers(int q, String name) {
    		qualifier = q;
    		this.name = name;
    	}
    	public int value() {
    		return qualifier;
    	}
    	public String toString() {
    		return name;
    	}
    }
    
    
    public PileAgent() {
    	init();
    }
    
    private void init() {
    	converter = new TextToByteConverter(IConstants.CHARSET_NAME);
        pe = new PileEngine();

        // Create an initial handle to the segment in the 2D space where combinations of
        // letters are stored. This handle can be used to initialize all relations
        // between system definers and tops.
        sdLetterRoots = pe.createChild(pe.createTop(0), pe.createTop(0), Qualifiers.LETTERS.value());
        
        // Create an initial handle to the segment in the 2D space where combinations of
        // texts are stored. This handle can be used to initialize all relations
        // between whole texts.
        sdText = pe.createChild(pe.createTop(0), pe.createTop(0), Qualifiers.TEXT.value());

        // This pile agent accepts 256 different characters (US-ASCII chars).
        abcTops = new int[256];
        abcRoots = new int[256];
        abcHandles = new HashMap<Integer, Character>();

        // Repeat for every one of the 256 characters representable by 1 byte...
        for (int i = 0; i < 256; i++)
        {
        	// Initialize tops using qualifier 0.
        	// (Because 0 as a qualifier is by default represented by 8 bits (00000000
        	// or 0x00), and because calling createTop for the first time starts filling
        	// the remaining 24 bits from the value (00000000 00000000 00000001
        	// or 0x000001) the tops will be filled with integer values 1..256. (Be aware
        	// that this is only true if createTop(0) has not been called before. Because
        	// it has been called multiple times before, the tops will in fact be filled
        	// with values 5...260.)
            abcTops[i] = pe.createTop(0);
            
            // Create a child relation for every combination of sdLetterRoots and every top
            abcRoots[i] = pe.createChild(sdLetterRoots, abcTops[i], Qualifiers.ROOTS.value());
            
            // Store the "relation" between a top and the top value (= the character
            // it represents).
            // Attention: In the original code, this is done slightly different!
            //original code: byte b = new byte[] { (byte)i }[0];
            byte b = (byte)i; // drops the first 24 bits
            char c = converter.byteToChar(b);
            
            abcHandles.put(abcTops[i], c);
        }
    }
    
    /**
     * Clears the internal state of the pile agent and performs then calls the
     * JVM's garbage collector.
     */
    public void clear() {
    	init();
    	System.gc();
    }
    
    /**
     * @return the character stored for this handle
     * @throws IllegalArgumentException if the given handle is not pointing to a top
     */
    public Character getTopValue(int topHandle) {
    	IPileEngine.IParents parents = pe.getParents(topHandle);
    	if (parents != null) {
    		throw new IllegalArgumentException(
    				"PileAgent.getTopValue: The given handle " + topHandle + " is not a top!");
    	}
    	return abcHandles.get(topHandle);
    }
      
    /**
     * @post the given Reader will NOT be closed by this method!
     * @param reader a LineNumberReader to read from
     * @return the last read line number (?)
     * @throws IOException
     */
    public int storeText(LineNumberReader reader) throws IOException {
    	
    	// sdText is the handle to the segment where the whole text stored
    	// in the 2D space is sdText
        sdText = pe.createChild(sdText, sdText, Qualifiers.TEXT.value());

        String line;
        while ((line = reader.readLine()) != null) {
        	
        	// Get a handle to the segment in the 2D space where the lines
        	// (of text) are stored
        	int rLineTextRoot = storeLine(line);
        	
        	// Store the text using...
        	// 1. as a normative parent handle: the handle pointing to the
        	//    segment in the 2D space where texts are stored
        	// 2. as an associative parent handle: the handle pointing to the
        	//    place in the 2D segment where the next read line is stored
        	pe.createChild(sdText, rLineTextRoot, Qualifiers.LINES.value());
        }
        
        // Return the handle to the segment in the 2D space where texts are stored
        return sdText;
    }
    
    public int storeText(String text) throws IOException {
    	int result = -1;
    	LineNumberReader reader = new LineNumberReader(new StringReader(text));
    	
    	try {   	
    		result = storeText(reader);
    	}
    	finally {
    		reader.close();
    	}
    	
    	return result;
    }
    
    /**
     * For every next pair of two characters in a line, use the first one
     * as a normative parent and the second one as an associative parent.
     * Probably this method would work only for one byte characters (thus
     * not allowing for UTF-8 texts).
     */
    private int storeLine(String text)
    {
        if ("".equals(text)) text = " ";

        List<Integer> nextRels = new ArrayList<Integer>();
        byte[] textbytes = converter.stringToByteArray(text);

        // First, build relations for all subsequent duples of characters in text:
        // "zweistein" --> [('z','w'),('e','i'),('s','t'),('e','i'),'n']
        for (int i = 0; i < textbytes.length; i += 2)
        {
            int n, a;
            
            // Handle to the normative parent character
            n = abcRoots[textbytes[i]];
            if (i < textbytes.length - 1) 
            {
            	// Handle to the associative parent character
                a = abcRoots[textbytes[i + 1]];
                nextRels.add(pe.createChild(n, a, Qualifiers.LETTERS.value()));
            }
            else
            	// The last character's handle in a line with an odd number of characters
                nextRels.add(n);
        }


        List<Integer> currRels;
        
        // Second, build relation-tree on top of letter duples
        // [('z','w'),('e','i'),('s','t'),('e','i'),n]
        //   --> [(((('z','w'),('e','i')),(('s','t'),('e','i'))),'n')]
        while (nextRels.size() > 1) {
        	
            currRels = nextRels;
            
            // TODO: Maybe using a LinkedList here could perform better?
            nextRels = new ArrayList<Integer>();

            
            for (int i = 0; i < currRels.size(); i += 2)  {
            	
                int n, a;
                n = currRels.get(i);
                if (i < currRels.size() - 1)
                {
                    a = currRels.get(i + 1);
                    int h = pe.createChild(n, a, Qualifiers.LETTERS.value());
                    nextRels.add(h);
                    
                    if (DEBUG) {
	                    System.out.println("--sn:" + constructLine(n).toString());
	                    System.out.println("--sn:" + constructLine(a).toString());
	                    System.out.println("--sn:" + constructLine(h).toString());
                    }
                }
                else {
                    nextRels.add(n);
                    if (DEBUG) System.out.println("--sn2:" + constructLine(n).toString());
                }
            }
        }

        // return root node for line´s text
        return nextRels.get(0);
    }
    
    /**
     * "Retrieves" a text
     */
    public String retrieveText(int handleOfText)
    {
        StringBuilder sb = new StringBuilder();

        // Find all children for the given (normative) handle, but restrict the search
        // results to only those with a line qualifier
        List<Integer> children = pe.findChildren(handleOfText, Manners.NORMATIVE, Qualifiers.LINES.value(), Qualifiers.LINES.value());
        for (int rLine : children )
        {
            if (sb.length() > 0) sb.append(NEWLINE);
            IPileEngine.IParents pr = pe.getParents(rLine);
            constructStringFromRelations(sb, pr.getAssocParent());
        }

        return sb.toString();
    }
    
    public String retrieveLine(int handleOfLine)
    {
        IPileEngine.IParents pr = pe.getParents(handleOfLine);
        return constructLine(pr.getAssocParent()).toString();
    }

    private StringBuilder constructLine(int handle)
    {
        StringBuilder sb = new StringBuilder();
        constructStringFromRelations(sb, handle);
        return sb;
    }

    /**
     * Constructs a String for the given handle
     * @param handle
     * @return the String this handle represents
     */
    public String constructStringFromRelations(int handle) {
    	StringBuilder sb = new StringBuilder();
    	constructStringFromRelations(sb, handle);
    	return sb.toString();
    }
    
    private void constructStringFromRelations(StringBuilder sb, int handle)
    {
        IPileEngine.IParents pp = pe.getParents(handle);
        if (pp == null) {
        	
            // If there are not parents, it's a top.
            if (abcHandles.containsKey(handle)) {
            	
            	// Just get the top value and append it.
                sb.append((char)abcHandles.get(handle));
            }
        }
        else {
        	
        	// There are parents, thus it's not a top. Traverse the tree
        	// for both parents.
            constructStringFromRelations(sb, pp.getNormParent());
            constructStringFromRelations(sb, pp.getAssocParent());
        }
    }
    
    
    //TODO: searching for "aro" does not work: searchValley() cannot locate "o" in "nt_aroun"
    //TODO: does not work 100%: if "peter" as in "peter piper" and (!) "peter went" is looked for, only one node pointing to a "peter" pattern is returned
    //TODO: finds 3 occurrences of "etr" in "peter petra petro" instead of only 2!
    //TODO: finds an occurrence of "etr" in "peter piper picked a pack of peckles"
    // Attention: the multiple calls to "converter.charArrayToByteArray(new char[] { pattern.charAt(0) })[0]"
    // could be written easier as "converter.charToByte(pattern.charAt(0))
    public Collection<TextFound> findTexts(String pattern, boolean caseSensitiveComparison)
    {
        List<WholeNode> firsts, nextFirsts;
        List<CaseAgnosticHandle> followers;

        Date s = new Date(System.currentTimeMillis());
      

        // Use the first character in the search pattern and find its handle. This handle
        // will be used as an "entry point" for the rest of the search. If the search
        // is case sensitive, there will be 2 possible first characters: a lower case
        // and an upper case version of the same character. Store these two characters
        // in a list "firsts". Thus, initially firsts will never contain more than 2
        // entries! (Later on in the code it however might.)
        // TODO: Characters that do not have a lower AND an upper case (such as question
        // marks etc.) do not need to be inserted twice into "firsts" for case sensitive
        // searches.
        firsts = new ArrayList<WholeNode>();
        if (caseSensitiveComparison) {
        	
        	// Convert the first character in the search pattern to its handle value
        	int h = abcRoots[converter.charArrayToByteArray(new char[] { pattern.charAt(0) })[0]];
            firsts.add(new WholeNode(h));
        }
        else
        {
        	// Convert the first character in the search pattern to its (upper case) handle value
        	int h1 = abcRoots[converter.charArrayToByteArray(new char[] { Character.toUpperCase(pattern.charAt(0)) })[0]];
            firsts.add(new WholeNode(h1));
            
            // Convert the first character in the search pattern to its (lower case) handle value
            int h2 = abcRoots[converter.charArrayToByteArray(new char[] { Character.toLowerCase(pattern.charAt(0)) })[0]];
            firsts.add(new WholeNode(h2));
        }

        
        // Create CaseAgnosticHandles for all characters in the search pattern from
        // pos 1..searchPattern.length() (the first character in pos 0 has already
        // been processed and added to "firsts"). Store the handles (as instances
        // of CaseAgnosticHandle) in "followers".
        followers = new ArrayList<CaseAgnosticHandle>();
        for (int i = 1; i < pattern.length(); i++)
        {
        	// upper case handle and lower case handle
            int uch, lch;

        	// Find handles for the next character in search pattern
            if (caseSensitiveComparison)
            {      
                //uch = abcRoots[converter.charArrayToByteArray(new char[] { pattern.charAt(i) })[0]];
                //lch = abcRoots[converter.charArrayToByteArray(new char[] { pattern.charAt(i) })[0]];
            	uch = lch = abcRoots[converter.charArrayToByteArray(new char[] { pattern.charAt(i) })[0]];
            }
            else
            {
                uch = abcRoots[converter.charArrayToByteArray(new char[] { Character.toUpperCase(pattern.charAt(i)) })[0]];
                lch = abcRoots[converter.charArrayToByteArray(new char[] { Character.toLowerCase(pattern.charAt(i)) })[0]];
            }

            followers.add(new CaseAgnosticHandle(uch, lch));
        }

        // Example: We now have (for a case sensitive search) stored handle values
        // for firsts=['p','P'] and CaseAgnosticHandles for followers=['e','t','e','r'].
        // Repeat for all followers 'e','t','e' and 'r'...
        while (followers.size() > 0)
        {
        	// TODO: Maybe using a LinkedList would be more efficient here?
            nextFirsts = new ArrayList<WholeNode>();

            // Search for a "path" between a first character and all of its followers.
            // Thus we look for paths/relations 'p'->'e', 'P'->'e' Store the handle
            // for these paths/relations in nextFirsts thus replacing 'p' and 'P' by
            // the relations ('p','e') and ('P','e'). The next time, this loop is
            // called it will look for search paths/relations between ('p','e')->'t'
            // and ('P','e')->'t', then for (('p','e'),'t')->'e' and
            // (('P','e'),'t')->'e' etc.
            for (WholeNode n : firsts) {
                nextFirsts.addAll(searchValley(n, followers.get(0)));
            }

            followers.remove(0);

            firsts = nextFirsts;
        }

        long elapsedTime = System.currentTimeMillis() - s.getTime();
        System.out.println("PileAgent.findText: search time " + elapsedTime + "ms");

        // collect texts for nodes found (wholes) that represent the pattern
        Map<Integer, TextFound> textNodes = new HashMap<Integer, TextFound>();
        
        // Firsts now contains handles for the paths/relations
        // [(((('p','e'),'t'),'e'),'r'), (((('P','e'),'t'),'e'),'r')]. These two
        // entries represent the whole search pattern. Re-create the texts for
        // their handles.
        for (WholeNode parent : firsts)
            collectTexts(parent.handle(), textNodes);

        return textNodes.values();
    }
    
    private List<WholeNode> searchValley(WholeNode n, CaseAgnosticHandle a)
    {
        if (DEBUG) System.out.println("Valley orig: " + constructLine(n.handle()).toString() +
        		", target: " + constructLine(a.handle()).toString());

        List<WholeNode> newFirsts = new ArrayList<WholeNode>();

        if (matchUp(n, a)) {
            newFirsts.add(n);
        }
        else {
            List<Integer> newFirstHandles = new ArrayList<Integer>();
            checkAcNodes(n.handle(), a, newFirstHandles, 0);
            for (Integer h : newFirstHandles)
            {
                WholeNode wn = new WholeNode(h);
                wn.followers().add(a);
                newFirsts.add(wn);
            }
        }

        return newFirsts;
    }
    
    private void checkAcNodes(int o, CaseAgnosticHandle a, List<Integer> newFirsts, int indent)
    {
    	if (DEBUG) System.out.println(space(indent) + "Nc/Ac of: " + constructLine(o).toString());

        for (int c : pe.findChildren(o, Manners.ASSOCIATIVE, 0, 255))
        {
            checkAcNodes(c, a, newFirsts, indent + 1);
        }
        checkNcNodes(o, a, newFirsts, indent + 1);
    }
    
    private void checkNcNodes(int o, CaseAgnosticHandle a, List<Integer> newFirsts, int indent)
    {
    	if (DEBUG) System.out.println(space(indent) + "Nc of: " + constructLine(o).toString());

        for (int c : pe.findChildren(o, Manners.NORMATIVE, 0, 255))
        {
            if (checkApNode(c, a, indent + 1))
                newFirsts.add(c);
        }
    }
    
    private boolean checkApNode(int o, CaseAgnosticHandle a, int indent)
    {
    	if (DEBUG) System.out.println(space(indent) + "Nc=Ap of: " + constructLine(o).toString());

        if (a.equals(o))
            return true;
        else
        {
            IPileEngine.IParents oParents = pe.getParents(o);
            if (oParents != null)
                return checkNpNodes(oParents.getAssocParent(), a, indent + 1);
            else
                return false;
        }
    }
    
    private boolean checkNpNodes(int o, CaseAgnosticHandle a, int indent)
    {
    	if (DEBUG) System.out.println(space(indent) + "Np of: " + constructLine(o).toString());

        if (a.equals(o))
            return true;
        else
        {
            IPileEngine.IParents oParents = pe.getParents(o);
            if (oParents != null)
                return checkNpNodes(oParents.getNormParent(), a, indent + 1);
            else
                return false;
        }
    }
    
    
    /**
     * There are several possible cases.
     * 
     * 1: o represents a single first character in a search pqttern without followers.
     *   Thus, the parameter a would be null. This case is already covered outside this
     *   method in the method findTexts.
     *   
     * 2: o represents a single character with exactly 1 follower. Check whether there
     *   is a path from o's associative parent to this follower.
     *   
     * 3. ???
     * 
     * @pre neither handle o nor its (case agnostic) follower a are allowed to be null
     * @param o a handle to a first character in a search pattern
     * @param a a (case agnostic) handle to a follower character in a search pattern
     * @return true if the a direct relation between the first and the following 
     *   character can be found in the stored pile
     */
    private boolean matchUp(WholeNode o, CaseAgnosticHandle a)
    {
        o.followers().add(a);
        
        ParamObj indexOfFollowerToCheck = new ParamObj(0);

        IPileEngine.IParents oParents = pe.getParents(o.handle());
        boolean matchUpResult = matchUp(oParents.getAssocParent(), o.followers(), indexOfFollowerToCheck);

        return matchUpResult && (indexOfFollowerToCheck.value() >= o.followers().size());
    }
    
    
    private boolean matchUp(int o, List<CaseAgnosticHandle> followers, ParamObj indexOfFollowerToCheck) {
    	
    	// Check whether handle o has still more followers to be checked or not.
    	if (indexOfFollowerToCheck.value() < followers.size()) {
    		
        	if (DEBUG) {
        		System.out.println(String.format(
    				"  Matchup %s, %s [%s]",
    				constructLine(o).toString(),
    				constructLine(followers.get(indexOfFollowerToCheck.value()).handle()).toString(),
    				indexOfFollowerToCheck.value()));
        	}
        	
        	// Check to see whether o is a handle to a root. If so, it has no parents.
        	if (pe.getQualifier(o) == Qualifiers.ROOTS.value()) {
        		
            	// Handle o has a ROOTS qualifier. It thus represents either a (Np,Top) or a
            	// (Top,Ap) relation.
        		return matchUpRoot(o, followers, indexOfFollowerToCheck);
        	}
        	else {
        		
	            	// Handle o has not a ROOTS qualifier. Check, whether it has parents.
	              IPileEngine.IParents oParents = pe.getParents(o);
	              if (oParents != null) {
	            	  
	            	  // Handle o has not a ROOTS qualifier but has parents. It is thus a "common"
	            	  // (Np,Ap) relation.
	            	  return matchUpCommonRelation(oParents, followers, indexOfFollowerToCheck);
	              }
	              else {
	            	  
					// Handle o has not a ROOTS qualifier and has no parents. It should thus
	            	// (theoretically) be a Top. Because no path was found between this top
	            	// and the currently indexed follower, we return false indicating no match. 
	            	return false;
	              }
        		
        	}
        	
    	}
    	else
    		// All followers for handle o have been checked.
    		return true;
    }
    
    
    /**
     * MatchUp method for root relations (Np,Top) or (Top,Ap).
     * @param o a handle to a relation which has the ROOTS qualifier set
     * @param followers
     * @param indexOfFollowerToCheck
     * @return
     */
    private boolean matchUpRoot(int o, List<CaseAgnosticHandle> followers, ParamObj indexOfFollowerToCheck) {

    	CaseAgnosticHandle a = followers.get(indexOfFollowerToCheck.value());
    	boolean rootFound = a.equals(o);
        if (rootFound) {
        	
        	// We have arrived at a root. Now let index point to the next follower.
        	indexOfFollowerToCheck.incr();
        }
        return rootFound; 	
    }
    
    /**
     * MatchUp method for "common" relations (Np,Ap). Handle o point to a common relation,
     * and oParents are the parents of this common relation.
     * @param oParents the parents of handle o
     * @param followers the followers of o (not the followers of o's parents!)
     * @param indexOfFollowerToCheck
     * @return
     */
    private boolean matchUpCommonRelation(IPileEngine.IParents oParents, List<CaseAgnosticHandle> followers, ParamObj indexOfFollowerToCheck) {

        if (matchUp(oParents.getNormParent(), followers, indexOfFollowerToCheck))  {
        	
            return matchUp(oParents.getAssocParent(), followers, indexOfFollowerToCheck);
        }
        else
        	
            return false;
    }

    
    private void collectTexts(int parent, Map<Integer, TextFound> textNodes) {
    	
        if (DEBUG) System.out.println("  -:" + constructLine(parent).toString());
        
        
        // Find children with LINES qualifier
        for (int l : pe.findChildren(
        		parent,
        		Manners.ASSOCIATIVE,
        		Qualifiers.LINES.value(),
        		Qualifiers.LINES.value())) {
            TextFound tf;
            int t = pe.getParents(l).getNormParent();
            if (textNodes.containsKey(t))
                tf = (TextFound)textNodes.get(t);
            else
            {
                tf = new TextFound(t);
                textNodes.put(t, tf);
            }
            tf.addLine(l);
        }

        // Find all children
        for (int c : pe.findChildren(
        		parent, 
        		Manners.NORMATIVE,
        		0,
        		255)) {
            collectTexts(c, textNodes);
        }
        
        // Find children with LETTERS qualifier
        for (int c : pe.findChildren(
        		parent,
        		Manners.ASSOCIATIVE,
        		Qualifiers.LETTERS.value(),
        		Qualifiers.LETTERS.value())) {
            collectTexts(c, textNodes);
        }
    }
    
    private String space(int numberOfSpaces) {
    	StringBuilder sb = new StringBuilder("  ");
        while (numberOfSpaces-- > 0)
        	sb.append("  ");
        return sb.toString();
    }
    
    
    /**
     * Class used to pass an integer value between methods
     */
    private class ParamObj {
    	private int indexOfFollowerToCheck;
    	
    	public ParamObj(int indexOfFollowerToCheck) {
    		this.indexOfFollowerToCheck = indexOfFollowerToCheck;
    	}
    	public int value() {
    		return indexOfFollowerToCheck;
    	}
    	public void incr() {
    		++indexOfFollowerToCheck;
    	}
    }
    
    /**
     * Attention: Do NOT store references to the internal pile engine from outside
     * the pile agent. Always only dynamically access it when needed.
     * @return the internal pile engine used by this agent.
     */
    public IPileEngine getPileEngine() {
    	return pe;
    }
    
//    public String toString() {
//    	StringBuilder sb = new StringBuilder();
//    	
//    	java.util.Iterator<Map.Entry<Integer, Character>> iter = abcHandles.entrySet().iterator();
//    	while (iter.hasNext()) {    		
//    		Map.Entry<Integer, Character> entry = iter.next();
//    		sb.append("<K=").append(entry.getKey()).append(", ");
//    		sb.append("V=").append(entry.getValue()).append(">");
//    		if (iter.hasNext()) {
//    			sb.append(NEWLINE);
//    		}
//    	}
//    	
//    	return sb.toString();
//    }

}