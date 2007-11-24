package org.abreslav.java2ecore.transformation.astview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.Annotation;

public class AnnotationView {
	private final String myQualifiedName;
	private final Map<String, Object> myAttributes = new HashMap<String, Object>();
	private final Annotation myAnnotation;
	
	public AnnotationView(Annotation annotation) {
		myQualifiedName = annotation.resolveAnnotationBinding().getAnnotationType().getQualifiedName();
		myAnnotation = annotation;
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
	
	public Annotation getAnnotation() {
		return myAnnotation;
	}
}
