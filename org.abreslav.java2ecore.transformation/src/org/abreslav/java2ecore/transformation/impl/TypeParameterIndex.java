package org.abreslav.java2ecore.transformation.impl;

import java.util.HashMap;

import org.eclipse.emf.ecore.ETypeParameter;

public class TypeParameterIndex {
	private final TypeParameterIndex myParent;
	private final HashMap<String, ETypeParameter> myTypeParameterMap = new HashMap<String, ETypeParameter>();
	
	public TypeParameterIndex(TypeParameterIndex parent) {
		myParent = parent;
	}

	public ETypeParameter getETypeParameter(String name) {
		ETypeParameter typeParameter = myTypeParameterMap.get(name);
		if (typeParameter == null && myParent != null) {
			typeParameter = myParent.getETypeParameter(name);
		}
		return typeParameter;
	}

	public void registerTypeParameter(ETypeParameter eTypeParameter) {
		myTypeParameterMap.put(eTypeParameter.getName(), eTypeParameter);
	}


}
