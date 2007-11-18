package org.abreslav.java2ecore.transformation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EReference;
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

	public MemberBuilder(EClass class1, ITypeResolver typeResolver,
			IDiagnostics diagnostics, Collection<? super EDataType> wrappedTypes) {
		myEClass = class1;
		myTypeResolver = new WrappingTypeResolver(typeResolver, wrappedTypes);
		myDiagnostics = diagnostics;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		ITypeBinding binding = node.getType().resolveBinding();
		String fqn = binding.getErasure().getQualifiedName();

		System.out.println(fqn);
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

		EGenericType eGenericType = resolveEGenericType(binding);
		boolean isFinal = (node.getModifiers() & Modifier.FINAL) != 0;
		boolean isTransient = (node.getModifiers() & Modifier.TRANSIENT) != 0;
		boolean isVolatile = (node.getModifiers() & Modifier.VOLATILE) != 0;

		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment fragment : fragments) {
			EStructuralFeature feature;
			if (eGenericType.getEClassifier() instanceof EDataType) {
				EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
				feature = eAttribute;
			} else {
				EReference eReference = EcoreFactory.eINSTANCE.createEReference();
				feature = eReference;
			}
			
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

	private EGenericType resolveEGenericType(ITypeBinding binding) {
		EGenericType eGenericType = EcoreFactory.eINSTANCE.createEGenericType(); 
		String fqn = binding.getQualifiedName();
		
		EClass eClass = myTypeResolver.getEClass(fqn);
		if (eClass != null) {
			eGenericType.setEClassifier(eClass);
		} else {
			EDataType eDataType = myTypeResolver.getEDataType(fqn);
			if (eDataType != null) {
				eGenericType.setEClassifier(eDataType);
			}
		} 
		
		return eGenericType;
	}

}
