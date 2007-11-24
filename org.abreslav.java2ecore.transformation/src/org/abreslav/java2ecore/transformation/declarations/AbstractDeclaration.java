package org.abreslav.java2ecore.transformation.declarations;


public abstract class AbstractDeclaration<D, E> implements IDeclaration {

	private final D myDeclaration;
	private final E myDeclaredElement;
	
	public AbstractDeclaration(D declaration, E declaredElement) {
		myDeclaration = declaration;
		myDeclaredElement = declaredElement;
	}

	public D getDeclaration() {
		return myDeclaration;
	}
	
	public E getDeclaredElement() {
		return myDeclaredElement;
	}
}
