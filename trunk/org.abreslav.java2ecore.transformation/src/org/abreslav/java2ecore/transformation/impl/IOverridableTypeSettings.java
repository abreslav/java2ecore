package org.abreslav.java2ecore.transformation.impl;

interface IOverridableTypeSettings extends ITypeSettings {

	void setLowerBound(int lowerBound);

	void setUpperBound(int upperBound);

	void setUnique(boolean isUnique);

	void setOrdered(boolean isOrdered);

}