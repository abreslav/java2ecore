package org.abreslav.java2ecore.transformation.astview;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotatedView {

	private final Map<String, AnnotationView> myAnnotations = new HashMap<String, AnnotationView>();

	public AnnotationView getAnnotation(Class<? extends Annotation> clazz) {
		return myAnnotations.get(clazz.getCanonicalName());
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
		return myAnnotations.containsKey(clazz.getCanonicalName());
	}

	protected void addAnnotation(AnnotationView view) {
		myAnnotations.put(view.getQualifiedName(), view);
	}
}