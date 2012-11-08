package pile.engine;

import java.util.List;

/**
 *
 * 
 * A Pile Engine implements the basic functionality to manage a Pile of relations.
 * 
 * A Pile Engine is responsible for creating relations including Tops to represent
 * Terminal Values (Tv).
 * 
 * A Pile Engine knows about normative/associative manners and children of parent relations.
 * It thus is a manifestation of the "Pile theory" and terminology.
 * 
 * A non-existend/empty relation is represented by a zero (0) value.
 * 
 * Functions defined on a Pile:
 * -Create a Top relation for a Tv -> CreateTop()
 * -Create a child relation for two parent relations -> CreateChild()
 * -Retrieve the one child of two parent relations -> GetChild()
 * -Retrieve the parent relations of a child relation -> GetParents()
 * -Find all child relation in a Manner and within a Qualifier range -> FindChildren()
 * -Determine the Qualifier of a given relation -> GetQualifier()
 */
public interface IPileEngine {
    
	/**
	 * Representation of the two parents of a relation
	 */
    public interface IParents {
    	
    	/**
    	 * @return the normative parent
    	 */
    	public int getNormParent();
    	
    	/**
    	 * @return the associative parent
    	 */
    	public int getAssocParent();
    }
	
    
	/**
	 * Definition of the Manners a relation can be used in
	 */
	enum Manners {
		NORMATIVE, ASSOCIATIVE;
	}
	

	/**
	 * Create a relation for a certain Qualifier
     * The handle values of relations should be created sequentially for each
     * Qualifier starting from 0 to 2^(32-nQualifierBits)-1.
	 * @param qualifier qualifier to put in most significant bits of relation handle;
	 *   qualifier needs to be in range of 0..2^nQualifierBits-1
	 * @return new relation
	 */
	public int createTop(int qualifier);
	
	/**
	 * Create a child relation for two parent relations.
	 * If such a child relation already exists, then just return it (even though
	 * it might have a different Qualifier) - otherwise create a new one.
	 * @post if a child has really been created by the pile engine, a subsequent call
	 *   to the wasCreated method will return true, otherwise it will return false
	 * @param normParent normative parent for the child relation
	 * @param assocParent associative parent for the child relation
	 * @param qualifier qualifier to use if really a new relation needs to be created
	 * @return the child relation
	 */
	public int createChild(int normParent, int assocParent, int qualifier);
	
	/**
	 * @return true if the createChild method had really created a new child before;
	 *   false if a call to createChild had only returned an already existing child
	 */
	public boolean wasChildCreated();
	
	/**
	 * Find the one child relation of two parent relations
	 * @param normParent normative parent of the child relation
	 * @param assocParent associative parent of the child relation
	 * @return a handle for child relation or 0 if it does not exist
	 */
	public int getChild(int normParent, int assocParent);
	
	/**
	 * Determine the Qualifier of a given relation according to the number
	 * of Qualifier bits
	 * @param relation relation to get Qualifier of
	 * @return Qualifier value
	 */
	public int getQualifier(int relation);
 
	/**
	 * Get the normative and associative parent relations of a child relation
	 * @param child the child relation
	 * @return IParents object containing the parent relations - or null if
	 *   child is a Top relation and has no parents
	 */
	IParents getParents(int child);

	/**
	 * Find all children (within a Qualifier range) of a parent relation,
	 * which is either normative or associative parent. The list of children is
	 * not ordered.
	 * @param parent either the normative or the associative parent
	 * @param manner determines whether parent is to interpreted as normative or associative
	 * @param qFrom from range of Qualifiers for child relations
	 * @param qUntil until range of Qualifiers for child relations
	 * @return list of child relations matching the criteria; if there are none the list is empty
	 */
	public List<Integer> findChildren(int parent, Manners manner, int qFrom, int qUntil);
	
	public IPileSpace getPileSpace();
}
