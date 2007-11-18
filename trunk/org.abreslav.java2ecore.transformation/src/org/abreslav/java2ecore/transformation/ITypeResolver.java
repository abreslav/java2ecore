package org.abreslav.java2ecore.transformation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

public interface ITypeResolver {

	public abstract EClass getEClass(String fqn);

	public abstract EDataType getEDataType(String fqn);

}