/**
 * 
 */
package org.abreslav.java2ecore.transformation.impl.typesettings;

class TypeSettings implements IOverridableTypeSettings {
	public static final int BOUNDS_SPECIFIED_BY_TYPE = -3;
	public static final ITypeSettings DEFAULT = new TypeSettings(true, true, true, 0, 1, true, true, IUnwrapStrategy.NO_UNWRAP);
	
	private int myLowerBound;
	private int myUpperBound;
	private boolean myIsUnique;
	private boolean myIsOrdered;
	private final boolean myBoundsOverridable;
	private final boolean myOrderedOverridable;
	private final boolean myUniqueOverridable;
	private final IUnwrapStrategy myUnwrapStrategy;
	
	public TypeSettings(boolean boundsOverridable, boolean orderedOverridable, boolean uniqueOverridable, int lowerBound, int upperBound, boolean isUnique, boolean isOrdered,
			IUnwrapStrategy unwrapStrategy) {
		this.myBoundsOverridable = boundsOverridable;
		this.myOrderedOverridable = orderedOverridable;
		this.myUniqueOverridable = uniqueOverridable;
		this.myLowerBound = lowerBound;
		this.myUpperBound = upperBound;
		this.myIsUnique = isUnique;
		this.myIsOrdered = isOrdered;
		this.myUnwrapStrategy = unwrapStrategy;
	}

	public int getLowerBound() {
		return myLowerBound;
	}

	public int getUpperBound() {
		return myUpperBound;
	}

	public boolean isUnique() {
		return myIsUnique;
	}

	public boolean isOrdered() {
		return myIsOrdered;
	}

	public IUnwrapStrategy getUnwrapStrategy() {
		return myUnwrapStrategy;
	}	
	
	public boolean isBoundsOverridable() {
		return myBoundsOverridable;
	}
	
	public boolean isOrderedOverridable() {
		return myOrderedOverridable;
	}
	
	public boolean isUniqueOverridable() {
		return myUniqueOverridable;
	}
	
	public void setLowerBound(int lowerBound) {
		myLowerBound = lowerBound;
	}
	
	public void setUpperBound(int upperBound) {
		myUpperBound = upperBound;
	}
	
	public void setUnique(boolean isUnique) {
		myIsUnique = isUnique;
	}
	
	public void setOrdered(boolean isOrdered) {
		myIsOrdered = isOrdered;
	}
	
	public TypeSettings getWorkingCopy() {
		return new TypeSettings(myBoundsOverridable, myOrderedOverridable, myUniqueOverridable, myLowerBound, myUpperBound, myIsUnique, myIsOrdered, myUnwrapStrategy);
	}
}