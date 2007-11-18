package org.abreslav.java2ecore.transformation.impl;

import java.util.HashMap;

import org.eclipse.emf.ecore.ETypeParameter;

public class EClassTypeParameterIndex {

	private HashMap<String, ETypeParameter> myTypeParameterMap = new HashMap<String, ETypeParameter>();
	
	public ETypeParameter getETypeParameter(String name) {
		return myTypeParameterMap.get(name);
	}

	public void registerTypeParameter(ETypeParameter eTypeParameter) {
		myTypeParameterMap.put(eTypeParameter.getName(), eTypeParameter);
	}


}
