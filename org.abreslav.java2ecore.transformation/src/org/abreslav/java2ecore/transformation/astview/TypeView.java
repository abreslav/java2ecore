package org.abreslav.java2ecore.transformation.astview;

import java.util.HashMap;
import java.util.Map;

public class TypeView {
	private final Map<String, AnnotationView> myAnnotations = new HashMap<String, AnnotationView>();
	private final String mySimpleName;
	private final String myQualifiedName;
	private final boolean myInterface; 
	private boolean myAbstract; 
	

	public TypeView(String simpleName, String qualifiedName, boolean isInterface) {
		mySimpleName = simpleName;
		myQualifiedName = qualifiedName;
		myInterface = isInterface;
	}

	public String getSimpleName() {
		return mySimpleName;
	}
	
	public AnnotationView getAnnotation(String className) {
		return myAnnotations.get(className);
	}

	public boolean isAbstract() {
		return myAbstract;
	}
	
	public boolean isInterface() {
		return myInterface;
	}
	
	public String getQualifiedName() {
		return myQualifiedName;
	}
	
	/*package*/ void setAbstract(boolean value) {
		myAbstract = value;
	}
	
	/*package*/ void addAnnotation(AnnotationView view) {
		myAnnotations.put(view.getQualifiedName(), view);
	}
}
