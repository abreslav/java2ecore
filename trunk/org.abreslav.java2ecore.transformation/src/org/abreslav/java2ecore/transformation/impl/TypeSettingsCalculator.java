package org.abreslav.java2ecore.transformation.impl;

import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.ATTRIBUTE_SPECIFIED_BY_FEATURE_TYPE;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.LOWERBOUND_CANNOT_BE_INFINITE;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.LOWERBOUND_GREATER_THAN_UPPERBOUND;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.MULTIDIM_ARRAYS_ARE_NOT_SUPPORTED;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.RAW_COLLECTION_TYPE_WILL_BE_WRAPPED;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.WRONG_BOUNDS_ARRAY_CONTENT;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.WRONG_NUMBER_FORMAT;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.java2ecore.annotations.sfeatures.Bounds;
import org.abreslav.java2ecore.annotations.sfeatures.Ordered;
import org.abreslav.java2ecore.annotations.sfeatures.Unique;
import org.abreslav.java2ecore.multiplicities.Infinity;
import org.abreslav.java2ecore.multiplicities.MCollection;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities.MSet;
import org.abreslav.java2ecore.multiplicities.Unspecified;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;

public class TypeSettingsCalculator {
	
	private static final Map<String, ITypeSettings> ourFeatureSettingsMap = new HashMap<String, ITypeSettings>();
	static {
		ourFeatureSettingsMap.put(
				Collection.class.getCanonicalName(), 
				new TypeSettings(true, true, true, 0, -1, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(
				Set.class.getCanonicalName(), 
				new TypeSettings(true, true, false, 0, -1, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(
				List.class.getCanonicalName(), 
				new TypeSettings(true, false, true, 0, -1, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(
				MCollection.class.getCanonicalName(), 
				new TypeSettings(false, true, true, 0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(
				MSet.class.getCanonicalName(), 
				new TypeSettings(false, true, false, 0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(
				MList.class.getCanonicalName(), 
				new TypeSettings(false, false, true, 0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
	}

	private final IDiagnostics myDiagnostics;

	public TypeSettingsCalculator(IDiagnostics diagnostics) {
		myDiagnostics = diagnostics;
	}

	public ITypeSettings calculateTypeSettings(Type type,
			AnnotatedView annotatedView) {
		IOverridableTypeSettings typeSettings = getTypeSettingsImpliedByJavaType(type);

		calculateUniqueAndOrdered(annotatedView, typeSettings);
		calculateBounds(annotatedView, typeSettings);
		return typeSettings;
	}

	private void calculateBounds(AnnotatedView annotatedView,
			IOverridableTypeSettings typeSettings) {
		AnnotationView bounds = annotatedView.getAnnotation(Bounds.class);
		if (bounds != null) {
			int lowerBound;
			int upperBound;
			Object boundsAttribute = bounds.getDefaultAttribute();
			if (boundsAttribute instanceof Integer) {
				Integer value = (Integer) boundsAttribute;
				lowerBound = value;
				upperBound = value;
			} else {
				Object[] boundsValue = (Object[]) boundsAttribute;
				if (boundsValue.length != 2) {
					myDiagnostics.reportError(bounds.getAnnotation(), WRONG_BOUNDS_ARRAY_CONTENT);
					return;
				} else {
					lowerBound = (Integer) boundsValue[0];
					upperBound = (Integer) boundsValue[1];
					checkBoundsCorrespondence(bounds.getAnnotation(), lowerBound, upperBound);
				}
			}
			checkOverridablility(typeSettings, bounds, lowerBound,
					upperBound);
			checkLowerBound(bounds, lowerBound);
			typeSettings.setLowerBound(lowerBound);
			typeSettings.setUpperBound(upperBound);
		}
	}

	private void checkBoundsCorrespondence(ASTNode node,
			int lowerBound, int upperBound) {
		if (lowerBound > upperBound && upperBound > 0) {
			myDiagnostics.reportErrorFormatted(node, LOWERBOUND_GREATER_THAN_UPPERBOUND, lowerBound, upperBound);
		}
	}

	private void checkLowerBound(AnnotationView bounds, int lowerBound) {
		if (lowerBound < 0) {
			myDiagnostics.reportError(bounds.getAnnotation(), LOWERBOUND_CANNOT_BE_INFINITE);
		}
	}

	private void checkOverridablility(IOverridableTypeSettings typeSettings,
			AnnotationView bounds, int lowerBound, int upperBound) {
		if ((lowerBound != typeSettings.getLowerBound() 
				|| upperBound != typeSettings.getUpperBound()) 
				&& !typeSettings.isBoundsOverridable()) {
			myDiagnostics.reportErrorFormatted(bounds.getAnnotation(), ATTRIBUTE_SPECIFIED_BY_FEATURE_TYPE, "Bounds");
		}
	}

	private void calculateUniqueAndOrdered(AnnotatedView annotatedView,
			IOverridableTypeSettings typeSettings) {
		AnnotationView unique = annotatedView.getAnnotation(Unique.class);
		AnnotationView ordered = annotatedView.getAnnotation(Ordered.class);
		
		if (unique != null && !typeSettings.isUniqueOverridable()) {
			if (!unique.getDefaultAttribute().equals(typeSettings.isUnique())) {
				myDiagnostics.reportErrorFormatted(unique.getAnnotation(), ATTRIBUTE_SPECIFIED_BY_FEATURE_TYPE, "eUnique");
			}
		}
		if (ordered != null && !typeSettings.isOrderedOverridable()) {
			if (!ordered.getDefaultAttribute().equals(typeSettings.isOrdered())) {
				myDiagnostics.reportErrorFormatted(ordered.getAnnotation(), ATTRIBUTE_SPECIFIED_BY_FEATURE_TYPE, "eOrdered");
			}
		}
		
		if (unique != null) {
			typeSettings.setUnique((Boolean) unique.getDefaultAttribute());
		}
		if (ordered != null) {
			typeSettings.setOrdered((Boolean) ordered.getDefaultAttribute());
		}
	}

	private IOverridableTypeSettings getTypeSettingsImpliedByJavaType(Type type) {
		ITypeBinding binding = type.resolveBinding();
		if (binding.isArray()) {
			if (binding.getDimensions() > 1) {
				myDiagnostics.reportError(type, MULTIDIM_ARRAYS_ARE_NOT_SUPPORTED);
			}
			return new TypeSettings(true, true, true, 0, -1, true, true, IUnwrapStrategy.UNWRAP_ARRAY);
		}
		
		String fqn = binding.getErasure().getQualifiedName();
		ITypeSettings settingsProto = ourFeatureSettingsMap.get(fqn);
		if (settingsProto == null) {
			return TypeSettings.DEFAULT.getWorkingCopy();
		}
		IOverridableTypeSettings typeSettings = settingsProto.getWorkingCopy();
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		if (typeArguments.length == 0) {
			myDiagnostics.reportWarning(type, RAW_COLLECTION_TYPE_WILL_BE_WRAPPED);
			return TypeSettings.DEFAULT.getWorkingCopy();
		}

		if (typeSettings.getUpperBound() == TypeSettings.BOUNDS_SPECIFIED_BY_TYPE) {
			calculateTypeSettingsFromMCollection(type, typeSettings, typeArguments);
		}
		
		return typeSettings.getWorkingCopy();
	}

	private void calculateTypeSettingsFromMCollection(Type type,
			IOverridableTypeSettings typeSettings, ITypeBinding[] typeArguments) {
		try {
			Integer lowerBound = Integer.valueOf(typeArguments[1].getName().substring(1));
			Integer upperBound;
			if (Infinity.class.getCanonicalName().equals(typeArguments[2].getQualifiedName())) {
				upperBound = ETypedElement.UNBOUNDED_MULTIPLICITY;
			} else if (Unspecified.class.getCanonicalName().equals(typeArguments[2].getQualifiedName())) {
				upperBound = ETypedElement.UNSPECIFIED_MULTIPLICITY;
			} else {
				upperBound = Integer.valueOf(typeArguments[2].getName().substring(1));
				checkBoundsCorrespondence(type, lowerBound, upperBound);
			}
			typeSettings.setLowerBound(lowerBound);
			typeSettings.setUpperBound(upperBound);
		} catch (NumberFormatException e) {
			myDiagnostics.reportErrorFormatted(type, WRONG_NUMBER_FORMAT, e.getMessage());
		}
	}
}
