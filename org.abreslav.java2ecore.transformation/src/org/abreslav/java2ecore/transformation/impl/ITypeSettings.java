package org.abreslav.java2ecore.transformation.impl;

interface ITypeSettings {

	int getLowerBound();

	int getUpperBound();

	boolean isUnique();

	boolean isOrdered();

	IUnwrapStrategy getUnwrapStrategy();

	boolean isBoundsOverridable();
	boolean isOrderedOverridable();
	boolean isUniqueOverridable();
	
	IOverridableTypeSettings getWorkingCopy();

}