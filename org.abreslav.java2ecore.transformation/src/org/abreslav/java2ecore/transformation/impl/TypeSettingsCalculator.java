package org.abreslav.java2ecore.transformation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.java2ecore.annotations.sfeatures.Bounds;
import org.abreslav.java2ecore.annotations.sfeatures.Ordered;
import org.abreslav.java2ecore.annotations.sfeatures.Unique;
import org.abreslav.java2ecore.multiplicities.Infinity;
import org.abreslav.java2ecore.multiplicities.Unspecified;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;

public class TypeSettingsCalculator {
	
	private static final Map<String, TypeSettings> ourFeatureSettingsMap = new HashMap<String, TypeSettings>();
	static {
		ourFeatureSettingsMap.put(Collection.class.getCanonicalName(), new TypeSettings(0, -1, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(Set.class.getCanonicalName(), new TypeSettings(0, -1, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(List.class.getCanonicalName(), new TypeSettings(0, -1, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MCollection.class.getCanonicalName(), new TypeSettings(0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MSet.class.getCanonicalName(), new TypeSettings(0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MList.class.getCanonicalName(), new TypeSettings(0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
	}

	private final IDiagnostics myDiagnostics;

	public TypeSettingsCalculator(IDiagnostics diagnostics) {
		myDiagnostics = diagnostics;
	}

	public TypeSettings calculateTypeSettings(Type type,
			AnnotatedView annotatedView) {
		boolean orderedPresent = annotatedView.isAnnotationPresent(Ordered.class);
		boolean uniquePresent = annotatedView.isAnnotationPresent(Unique.class);
		AnnotationView bounds = annotatedView.getAnnotation(Bounds.class);
		if (bounds != null) {
			Object[] boundsValue = (Object[]) bounds.getDefaultAttribute();
			if (boundsValue.length != 2) {
				myDiagnostics.reportError("Bounds array must contain two integer values", bounds.getAnnotation());
			}
			int lowerBound = (Integer) boundsValue[0];
			int upperBound = (Integer) boundsValue[1];
			if (lowerBound < 0) {
				myDiagnostics.reportError("Lower bound cannot be infinite or unspecified", bounds.getAnnotation());
			}
			if (lowerBound > upperBound && upperBound > 0) {
				myDiagnostics.reportError("Lower bound is greater than upper bound", bounds.getAnnotation());
			}
			return new TypeSettings(lowerBound, upperBound, uniquePresent, orderedPresent, IUnwrapStrategy.NO_UNWRAP);
		} else {
			return getTypeSettingsImpliedByJavaType(type);
		}
	}

	private TypeSettings getTypeSettingsImpliedByJavaType(Type type) {
		ITypeBinding binding = type.resolveBinding();
		if (binding.isArray()) {
			if (binding.getDimensions() > 1) {
				myDiagnostics.reportError("Multidimentional arrays are not supported", type);
			}
			return new TypeSettings(0, -1, false, true, IUnwrapStrategy.UNWRAP_ARRAY);
		}
		
		String fqn = binding.getErasure().getQualifiedName();
		TypeSettings featureSettings = ourFeatureSettingsMap.get(fqn);
		if (featureSettings == null) {
			return TypeSettings.DEFAULT;
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		if (typeArguments.length == 0) {
			myDiagnostics.reportWarning("Raw collection type will be wrapped into a simple EDatatType", type);
			return TypeSettings.DEFAULT;
		}

		if (featureSettings.getUpperBound() == TypeSettings.BOUNDS_SPECIFIED_BY_TYPE) {
			featureSettings = calculateTypeSettingsFromMCollection(type,
					featureSettings, typeArguments);
		}
		
		return featureSettings;
	}

	private TypeSettings calculateTypeSettingsFromMCollection(Type type,
			TypeSettings featureSettings, ITypeBinding[] typeArguments) {
		try {
			Integer lowerBound = Integer.valueOf(typeArguments[1].getName().substring(1));
			Integer upperBound;
			if (Infinity.class.getCanonicalName().equals(typeArguments[2].getQualifiedName())) {
				upperBound = ETypedElement.UNBOUNDED_MULTIPLICITY;
			} else if (Unspecified.class.getCanonicalName().equals(typeArguments[2].getQualifiedName())) {
				upperBound = ETypedElement.UNSPECIFIED_MULTIPLICITY;
			} else {
				upperBound = Integer.valueOf(typeArguments[2].getName().substring(1));
				if (lowerBound > upperBound) {
					myDiagnostics.reportError("Lower bound " + lowerBound + " is greater than upper bound " + upperBound, type);
				}
			}
			featureSettings = new TypeSettings(lowerBound, upperBound, featureSettings);
		} catch (NumberFormatException e) {
			myDiagnostics.reportError("Wrong number format. " + e.getMessage(), type);
		}
		return featureSettings;
	}
}
