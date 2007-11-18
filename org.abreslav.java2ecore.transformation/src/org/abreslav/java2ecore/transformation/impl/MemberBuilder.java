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
	private static final FeatureSettings DEFAULT_FEATURE_SETTINGS = new FeatureSettings(1, true, true);
	
	private static final int SPECIFIED_FEATURE_SETTINGS = -2;

	private static class FeatureSettings {
		int lowerBound = 0;
		int upperBound = 1;
		boolean isUnique = true;
		boolean isOrdered = true;
		
		public FeatureSettings(int lowerBound, int upperBound, FeatureSettings fs) {
			this(lowerBound, upperBound, fs.isUnique, fs.isOrdered);
		}
		
		public FeatureSettings(int upperBound, boolean isUnique,
				boolean isOrdered) {
			this(0, upperBound, isUnique, isOrdered);
		}
		
		public FeatureSettings(int lowerBound, int upperBound, boolean isUnique,
				boolean isOrdered) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.isUnique = isUnique;
			this.isOrdered = isOrdered;
		}	
		
		
	}
	
	private static final Map<String, FeatureSettings> ourFeatureSettingsMap = new HashMap<String, FeatureSettings>();
	static {
		ourFeatureSettingsMap.put(Collection.class.getCanonicalName(), new FeatureSettings(-1, false, false));
		ourFeatureSettingsMap.put(Set.class.getCanonicalName(), new FeatureSettings(-1, true, false));
		ourFeatureSettingsMap.put(List.class.getCanonicalName(), new FeatureSettings(-1, false, true));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MCollection.class.getCanonicalName(), new FeatureSettings(SPECIFIED_FEATURE_SETTINGS, false, false));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MSet.class.getCanonicalName(), new FeatureSettings(SPECIFIED_FEATURE_SETTINGS, true, false));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MList.class.getCanonicalName(), new FeatureSettings(SPECIFIED_FEATURE_SETTINGS, false, true));
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

		FeatureSettings featureSettings = getFeatureSettings(node);
		if (featureSettings != DEFAULT_FEATURE_SETTINGS) {
			ITypeBinding[] typeArguments = binding.getTypeArguments();
			binding = typeArguments[0];
		}
		
		EGenericType eGenericType = myTypeResolver.resolveEGenericType(binding, myTypeParameterIndex);
		boolean isFinal = (node.getModifiers() & Modifier.FINAL) != 0;
		boolean isTransient = (node.getModifiers() & Modifier.TRANSIENT) != 0;
		boolean isVolatile = (node.getModifiers() & Modifier.VOLATILE) != 0;

		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment fragment : fragments) {
			EStructuralFeature feature = createEStructuralFeature(eGenericType);
			
			feature.setEGenericType(eGenericType);
			feature.setName(fragment.getName().getIdentifier());
			feature.setChangeable(isFinal);
			feature.setTransient(isTransient);
			feature.setVolatile(isVolatile);
			feature.setLowerBound(featureSettings.lowerBound);
			feature.setUpperBound(featureSettings.upperBound);
			feature.setUnique(featureSettings.isUnique);
			feature.setOrdered(featureSettings.isOrdered);
			
			setDefaultValue(feature, fragment.getInitializer());

			myEClass.getEStructuralFeatures().add(feature);
		}
		return false;
	}

	private FeatureSettings getFeatureSettings(FieldDeclaration fieldDeclaration) {
		ITypeBinding binding = fieldDeclaration.getType().resolveBinding();
		String fqn = binding.getErasure().getQualifiedName();
		FeatureSettings featureSettings = ourFeatureSettingsMap.get(fqn);
		if (featureSettings == null) {
			return DEFAULT_FEATURE_SETTINGS;
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		if (typeArguments.length == 0) {
			myDiagnostics.reportWarning("Raw collection type will be wrapped into a simple EDatatType", fieldDeclaration.getType());
			return DEFAULT_FEATURE_SETTINGS;
		}

		if (featureSettings.upperBound == SPECIFIED_FEATURE_SETTINGS) {
			try {
				Integer lowerBound = Integer.valueOf(typeArguments[1].getName().substring(1));
				Integer upperBound = Integer.valueOf(typeArguments[2].getName().substring(1));
				if (lowerBound > upperBound) {
					myDiagnostics.reportError("Lower bound " + lowerBound + " is greater than upper bound " + upperBound, fieldDeclaration.getType());
				}
				featureSettings = new FeatureSettings(lowerBound, upperBound, featureSettings);
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
