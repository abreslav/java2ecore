package org.abreslav.java2ecore.transformation.astview;

import java.util.HashMap;
import java.util.Map;

public class AnnotationView {
	private final String myQualifiedName;
	private final Map<String, Object> myAttributes = new HashMap<String, Object>();
	
	public AnnotationView(String name) {
		myQualifiedName = name;
	}
	
	public String getQualifiedName() {
		return myQualifiedName;
	}
	
	public Object getAttribute(String name) {
		return myAttributes.get(name);
	}
	
	/*package*/ Map<String, Object> getAttributes() {
		return myAttributes;
	}
}
