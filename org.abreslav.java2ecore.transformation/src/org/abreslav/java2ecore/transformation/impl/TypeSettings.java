/**
 * 
 */
package org.abreslav.java2ecore.transformation.impl;

class TypeSettings {
	public static final int BOUNDS_SPECIFIED_BY_TYPE = -3;
	public static final TypeSettings DEFAULT = new TypeSettings(0, 1, true, true, IUnwrapStrategy.NO_UNWRAP);
	
	private final int myLowerBound;
	private final int myUpperBound;
	private final boolean myIsUnique;
	private final boolean myIsOrdered;
	private final IUnwrapStrategy myUnwrapStrategy;
	
	public TypeSettings(int lowerBound, int upperBound, TypeSettings fts) {
		this(lowerBound, upperBound, fts.myIsUnique, fts.myIsOrdered, fts.myUnwrapStrategy);
	}
	
	public TypeSettings(int lowerBound, int upperBound, boolean isUnique, boolean isOrdered,
			IUnwrapStrategy unwrapStrategy) {
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
}