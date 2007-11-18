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
	private static class FeatureSettings {
		int upperBound = 1;
		boolean isUnique = true;
		boolean isOrdered = true;
		
		public FeatureSettings(int upperBound, boolean isUnique,
				boolean isOrdered) {
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
		String fqn = binding.getErasure().getQualifiedName();

		FeatureSettings featureSettings = ourFeatureSettingsMap.get(fqn);
		if (featureSettings != null) {
			ITypeBinding[] typeArguments = binding.getTypeArguments();
			if (typeArguments.length > 0) {
				binding = typeArguments[0];
			} else {
				featureSettings = new FeatureSettings(1, true, true);
				myDiagnostics.reportWarning("Raw collection type will be wrapped into a simple EDatatType", node.getType());
			}
		} else {
			featureSettings = new FeatureSettings(1, true, true);
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
			feature.setLowerBound(0);
			feature.setUpperBound(featureSettings.upperBound);
			feature.setUnique(featureSettings.isUnique);
			feature.setOrdered(featureSettings.isOrdered);
			
			setDefaultValue(feature, fragment.getInitializer());

			myEClass.getEStructuralFeatures().add(feature);
		}
		return false;
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
