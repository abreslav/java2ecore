/**
 * 
 */
package org.abreslav.java2ecore.transformation.impl;

class FeatureSettings {
	public static final int BOUNDS_SPECIFIED_BY_TYPE = -2;
	public static final FeatureSettings DEFAULT = new FeatureSettings(0, 1, true, true, IUnwrapStrategy.NO_UNWRAP);
	
	private final int myLowerBound;
	private final int myUpperBound;
	private final boolean myIsUnique;
	private final boolean myIsOrdered;
	private final IUnwrapStrategy myUnwrapStrategy;
	
	public FeatureSettings(int lowerBound, int upperBound, FeatureSettings fs) {
		this(lowerBound, upperBound, fs.myIsUnique, fs.myIsOrdered, fs.myUnwrapStrategy);
	}
	
	public FeatureSettings(int lowerBound, int upperBound, boolean isUnique, boolean isOrdered,
			IUnwrapStrategy unwrapStrategy) {
		this.myLowerBound = lowerBound;
		this.myUpperBound = upperBound;
		this.myIsUnique = isUnique;
		this.myIsOrdered = isOrdered;
		this.myUnwrapStrategy = unwrapStrategy;
		if (myUnwrapStrategy == null) {
			new Exception().printStackTrace();
		}
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