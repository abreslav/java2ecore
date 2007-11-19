package org.abreslav.java2ecore.transformation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MemberBuilder extends ASTVisitor {
	
	private static final Map<String, FeatureTypeSettings> ourFeatureSettingsMap = new HashMap<String, FeatureTypeSettings>();
	static {
		ourFeatureSettingsMap.put(Collection.class.getCanonicalName(), new FeatureTypeSettings(0, -1, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(Set.class.getCanonicalName(), new FeatureTypeSettings(0, -1, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(List.class.getCanonicalName(), new FeatureTypeSettings(0, -1, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MCollection.class.getCanonicalName(), new FeatureTypeSettings(0, FeatureTypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MSet.class.getCanonicalName(), new FeatureTypeSettings(0, FeatureTypeSettings.BOUNDS_SPECIFIED_BY_TYPE, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MList.class.getCanonicalName(), new FeatureTypeSettings(0, FeatureTypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
	}

	private final EClass myEClass;
	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;
	private final EClassTypeParameterIndex myTypeParameterIndex;

	public MemberBuilder(EClass class1, ITypeResolver typeResolver,
			IDiagnostics diagnostics, EClassTypeParameterIndex parameterIndex) {
		myEClass = class1;
		myTypeResolver = typeResolver;
		myDiagnostics = diagnostics;
		myTypeParameterIndex = parameterIndex;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		ITypeBinding binding = node.getType().resolveBinding();

		FeatureTypeSettings typeSettings = getFeatureSettingsImpliedByJavaType(node);
		binding = typeSettings.getUnwrapStrategy().unwrap(binding);
		
		EGenericType eGenericType = myTypeResolver.resolveEGenericType(binding, false, myTypeParameterIndex);
		boolean isFinal = (node.getModifiers() & Modifier.FINAL) != 0;
		boolean isTransient = (node.getModifiers() & Modifier.TRANSIENT) != 0;
		boolean isVolatile = (node.getModifiers() & Modifier.VOLATILE) != 0;

		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment fragment : fragments) {
			if (fragment.getExtraDimensions() > 0) {
				myDiagnostics.reportError("Specify array dimensions at the field type", fragment);
			}
			EStructuralFeature feature = createEStructuralFeature(eGenericType);
			
			feature.setEGenericType(eGenericType);
			feature.setName(fragment.getName().getIdentifier());
			feature.setChangeable(isFinal);
			feature.setTransient(isTransient);
			feature.setVolatile(isVolatile);
			feature.setLowerBound(typeSettings.getLowerBound());
			feature.setUpperBound(typeSettings.getUpperBound());
			feature.setUnique(typeSettings.isUnique());
			feature.setOrdered(typeSettings.isOrdered());
			
			setDefaultValue(feature, fragment.getInitializer());

			myEClass.getEStructuralFeatures().add(feature);
		}
		return false;
	}

	private FeatureTypeSettings getFeatureSettingsImpliedByJavaType(FieldDeclaration fieldDeclaration) {
		ITypeBinding binding = fieldDeclaration.getType().resolveBinding();
		if (binding.isArray()) {
			if (binding.getDimensions() > 1) {
				myDiagnostics.reportError("Multidimentional arrays are not supported", fieldDeclaration.getType());
			}
			return new FeatureTypeSettings(0, -1, false, true, IUnwrapStrategy.UNWRAP_ARRAY);
		}
		
		String fqn = binding.getErasure().getQualifiedName();
		FeatureTypeSettings featureSettings = ourFeatureSettingsMap.get(fqn);
		if (featureSettings == null) {
			return FeatureTypeSettings.DEFAULT;
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		if (typeArguments.length == 0) {
			myDiagnostics.reportWarning("Raw collection type will be wrapped into a simple EDatatType", fieldDeclaration.getType());
			return FeatureTypeSettings.DEFAULT;
		}

		if (featureSettings.getUpperBound() == FeatureTypeSettings.BOUNDS_SPECIFIED_BY_TYPE) {
			try {
				Integer lowerBound = Integer.valueOf(typeArguments[1].getName().substring(1));
				Integer upperBound = Integer.valueOf(typeArguments[2].getName().substring(1));
				if (lowerBound > upperBound) {
					myDiagnostics.reportError("Lower bound " + lowerBound + " is greater than upper bound " + upperBound, fieldDeclaration.getType());
				}
				featureSettings = new FeatureTypeSettings(lowerBound, upperBound, featureSettings);
			} catch (NumberFormatException e) {
				myDiagnostics.reportError("Wrong number format. " + e.getMessage(), fieldDeclaration.getType());
			}
		}
		
		return featureSettings;
	}

	private EStructuralFeature createEStructuralFeature(
			EGenericType eGenericType) {
		EStructuralFeature feature;
		if (eGenericType.getEClassifier() instanceof EDataType) {
			feature = EcoreFactory.eINSTANCE.createEAttribute();
		} else {
			feature = EcoreFactory.eINSTANCE.createEReference();
		}
		return feature;
	}

	private void setDefaultValue(EStructuralFeature feature,
			Expression initializer) {
		if (initializer != null) {
			Object constant = initializer.resolveConstantExpressionValue();
			if (constant != null) {
				feature.setDefaultValueLiteral(constant.toString());
			} else {
				myDiagnostics.reportError("Non-constant values are not allowed", initializer);
			}
		}
	}


}
