package org.abreslav.java2ecore.transformation.impl;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.abreslav.java2ecore.annotations.sfeatures.Containment;
import org.abreslav.java2ecore.annotations.sfeatures.ID;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;

public class AnnotationValidator {

	private static final Set<Class<? extends Annotation>> ourAnnotationsForbiddenForEAttributes = new HashSet<Class<? extends Annotation>>();
	static {
		ourAnnotationsForbiddenForEAttributes.add(Containment.class);
		ourAnnotationsForbiddenForEAttributes.add(Opposite.class);
		ourAnnotationsForbiddenForEAttributes.add(ResolveProxies.class);
	}

	private static final Set<Class<? extends Annotation>> ourAnnotationsForbiddenForEReferences = new HashSet<Class<? extends Annotation>>();
	static {
		ourAnnotationsForbiddenForEReferences.add(ID.class);
	}
	
	public static void checkEAttributeAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourAnnotationsForbiddenForEAttributes, "This annotation is not allowed for attributes");
	}

	public static void checkEReferenceAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourAnnotationsForbiddenForEReferences, "This annotation is not allowed for references");
	}

	private static void doCheckAnnotations(AnnotatedView annotatedView,
			IDiagnostics diagnostics,
			Set<Class<? extends Annotation>> forbiddenAnnotations, String message) {
		for (Class<? extends Annotation> annotationClass : forbiddenAnnotations) {
			AnnotationView annotation = annotatedView.getAnnotation(annotationClass);
			if (annotation != null) {
				diagnostics.reportError(message, annotation.getAnnotation());
			}
		}
	}
	
}
