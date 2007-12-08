package org.abreslav.java2ecore.transformation.impl;

import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.NOT_ALLOWED_FOR_ITEM;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.Import;
import org.abreslav.java2ecore.annotations.ImportGenModel;
import org.abreslav.java2ecore.annotations.sfeatures.Containment;
import org.abreslav.java2ecore.annotations.sfeatures.ID;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;
import org.abreslav.java2ecore.annotations.types.EClass;
import org.abreslav.java2ecore.annotations.types.EDataType;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnosticMessage;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.utils.SetFactory;

@SuppressWarnings("unchecked")
public class AnnotationValidator {

	private static final Set<Class<? extends Annotation>> ourEAttributeProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		Containment.class,
		Opposite.class,
		ResolveProxies.class
	);

	private static final Set<Class<? extends Annotation>> ourEReferenceProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		ID.class
	);
	
	private static final Set<Class<? extends Annotation>> ourEClassifierProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		EPackage.class,
		Import.class,
		ImportGenModel.class
	);
	
	private static final Set<Class<? extends Annotation>> ourEClassProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		ourEClassifierProhibitions,
		EDataType.class
	);
	
	private static final Set<Class<? extends Annotation>> ourEDataTypeProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		ourEClassifierProhibitions,
		InstanceTypeName.class,
		EClass.class
	);
	
	private static final Set<Class<? extends Annotation>> ourEEnumProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		ourEDataTypeProhibitions,
		EDataType.class
	);
	
	private static final Set<Class<? extends Annotation>> ourEPackageProhibitions = SetFactory.<Class<? extends Annotation>>createSet(
		EDataType.class,
		EClass.class,
		InstanceTypeName.class
	);
	
	public static void checkEAttributeAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourEAttributeProhibitions, NOT_ALLOWED_FOR_ITEM, "EAttributes");
	}

	public static void checkEReferenceAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourEReferenceProhibitions, NOT_ALLOWED_FOR_ITEM, "EReferences");
	}

	public static void checkEDataTypeAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourEDataTypeProhibitions, NOT_ALLOWED_FOR_ITEM, "EDataTypes");
	}

	public static void checkEEnumAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourEEnumProhibitions, NOT_ALLOWED_FOR_ITEM, "EEnums");
	}

	public static void checkEClassAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourEClassProhibitions, NOT_ALLOWED_FOR_ITEM, "EClasses");
	}
	
	public static void checkEPackageAnnotations(AnnotatedView annotatedView, IDiagnostics diagnostics) {
		doCheckAnnotations(annotatedView, diagnostics, ourEPackageProhibitions, NOT_ALLOWED_FOR_ITEM, "EPackage");
	}
	
	private static void doCheckAnnotations(AnnotatedView annotatedView,
			IDiagnostics diagnostics,
			Set<Class<? extends Annotation>> forbiddenAnnotations, 
			IDiagnosticMessage message,
			Object... args) {
		for (Class<? extends Annotation> annotationClass : forbiddenAnnotations) {
			AnnotationView annotation = annotatedView.getAnnotation(annotationClass);
			if (annotation != null) {
				diagnostics.reportErrorFormatted(annotation.getAnnotation(), message, args);
			}
		}
	}
	
}
