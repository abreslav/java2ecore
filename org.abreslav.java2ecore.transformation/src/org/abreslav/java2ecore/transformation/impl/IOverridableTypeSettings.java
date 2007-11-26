package org.abreslav.java2ecore.transformation.impl;

interface IOverridableTypeSettings extends ITypeSettings {

	public abstract void setLowerBound(int lowerBound);

	public abstract void setUpperBound(int upperBound);

	public abstract void setUnique(boolean isUnique);

	public abstract void setOrdered(boolean isOrdered);

}