package org.abreslav.java2ecore.transformation.astview;


public class TypeView extends AnnotatedView {
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
}
