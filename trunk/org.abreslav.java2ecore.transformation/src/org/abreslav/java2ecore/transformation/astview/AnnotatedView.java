package org.abreslav.java2ecore.transformation.astview;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class AnnotatedView {

	private final Map<String, AnnotationView> myAnnotations = new HashMap<String, AnnotationView>();
	private final ASTNode myDeclaration;
	
	public AnnotatedView(ASTNode declaration) {
		super();
		myDeclaration = declaration;
	}

	public AnnotationView getAnnotation(Class<? extends Annotation> clazz) {
		return myAnnotations.get(clazz.getCanonicalName());
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
		return myAnnotations.containsKey(clazz.getCanonicalName());
	}

	protected void addAnnotation(AnnotationView view) {
		myAnnotations.put(view.getQualifiedName(), view);
	}
	
	public ASTNode getDeclaration() {
		return myDeclaration;
	}
}