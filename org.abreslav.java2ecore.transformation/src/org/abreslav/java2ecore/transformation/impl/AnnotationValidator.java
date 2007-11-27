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
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnosticMessage;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.*;

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
		doCheckAnnotations(annotatedView, diagnostics, ourAnnotationsForbiddenForEAttributes, NOT_ALLOWED_FOR_ATTRIBUTES);
	}

	public static void checkEReferenceAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourAnnotationsForbiddenForEReferences, NOT_ALLOWED_FOR_REFERENCES);
	}

	private static void doCheckAnnotations(AnnotatedView annotatedView,
			IDiagnostics diagnostics,
			Set<Class<? extends Annotation>> forbiddenAnnotations, 
			IDiagnosticMessage message) {
		for (Class<? extends Annotation> annotationClass : forbiddenAnnotations) {
			AnnotationView annotation = annotatedView.getAnnotation(annotationClass);
			if (annotation != null) {
				diagnostics.reportError(annotation.getAnnotation(), message);
			}
		}
	}
	
}
