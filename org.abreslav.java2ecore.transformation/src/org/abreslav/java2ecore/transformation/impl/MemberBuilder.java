package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abreslav.java2ecore.annotations.sfeatures.Containment;
import org.abreslav.java2ecore.annotations.sfeatures.Derived;
import org.abreslav.java2ecore.annotations.sfeatures.ID;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;
import org.abreslav.java2ecore.annotations.sfeatures.Unsettable;
import org.abreslav.java2ecore.multiplicities.Infinity;
import org.abreslav.java2ecore.multiplicities.Unspecified;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MemberBuilder extends ASTVisitor {
	
	private static final Map<String, TypeSettings> ourFeatureSettingsMap = new HashMap<String, TypeSettings>();
	static {
		ourFeatureSettingsMap.put(Collection.class.getCanonicalName(), new TypeSettings(0, -1, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(Set.class.getCanonicalName(), new TypeSettings(0, -1, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(List.class.getCanonicalName(), new TypeSettings(0, -1, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MCollection.class.getCanonicalName(), new TypeSettings(0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MSet.class.getCanonicalName(), new TypeSettings(0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, true, false, IUnwrapStrategy.UNWRAP_GENERIC));
		ourFeatureSettingsMap.put(org.abreslav.java2ecore.multiplicities.MList.class.getCanonicalName(), new TypeSettings(0, TypeSettings.BOUNDS_SPECIFIED_BY_TYPE, false, true, IUnwrapStrategy.UNWRAP_GENERIC));
	}

	private final EClass myEClass;
	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;
	private final TypeParameterIndex myTypeParameterIndex;

	public MemberBuilder(EClass class1, ITypeResolver typeResolver,
			IDiagnostics diagnostics, TypeParameterIndex parameterIndex) {
		myEClass = class1;
		myTypeResolver = typeResolver;
		myDiagnostics = diagnostics;
		myTypeParameterIndex = parameterIndex;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		TypeSettings typeSettings = getTypeSettingsImpliedByJavaType(node.getType());
		final ITypeBinding binding = typeSettings.getUnwrapStrategy().unwrap(node.getType().resolveBinding());
		
		EGenericType eGenericType = myTypeResolver.resolveEGenericType(binding, false, myTypeParameterIndex);
		IFeatureFactory factory = createFeatureFactory(node, eGenericType);

		boolean isFinal = (node.getModifiers() & Modifier.FINAL) != 0;
		boolean isTransient = (node.getModifiers() & Modifier.TRANSIENT) != 0;
		boolean isVolatile = (node.getModifiers() & Modifier.VOLATILE) != 0;

		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment fragment : fragments) {
			if (fragment.getExtraDimensions() > 0) {
				myDiagnostics.reportError("Specify array dimensions at the field type", fragment);
			}
			EStructuralFeature feature = factory.createStructuralFeature();
			
			feature.setEGenericType(eGenericType);
			feature.setName(fragment.getName().getIdentifier());
			feature.setChangeable(isFinal);
			feature.setTransient(isTransient);
			feature.setVolatile(isVolatile);
			
			applyTypeSettings(feature, typeSettings);
			
			setDefaultValue(feature, fragment.getInitializer());

			myEClass.getEStructuralFeatures().add(feature);
		}
		return false;
	}

	private void applyTypeSettings(ETypedElement eTypedElement,
			TypeSettings typeSettings) {
		eTypedElement.setLowerBound(typeSettings.getLowerBound());
		eTypedElement.setUpperBound(typeSettings.getUpperBound());
		eTypedElement.setUnique(typeSettings.isUnique());
		eTypedElement.setOrdered(typeSettings.isOrdered());
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		EOperation eOperation = EcoreFactory.eINSTANCE.createEOperation();
		eOperation.setName(node.getName().getIdentifier());
		
		TypeParameterIndex typeParameterIndex = new TypeParameterIndex(myTypeParameterIndex);
		
		@SuppressWarnings("unchecked")
		List<TypeParameter> typeParameters = (List<TypeParameter>) node.getStructuralProperty(MethodDeclaration.TYPE_PARAMETERS_PROPERTY);
		List<ITypeBinding> parameterTypeBindings = new ArrayList<ITypeBinding>();
		for (TypeParameter typeParameter : typeParameters) {
			parameterTypeBindings.add(typeParameter.resolveBinding());
		}
		Collection<ETypeParameter> eTypeParameters = myTypeResolver.createETypeParameters(typeParameterIndex, parameterTypeBindings);
		eOperation.getETypeParameters().addAll(eTypeParameters);
		
		setUpType(eOperation, node.getReturnType2(), typeParameterIndex);

		@SuppressWarnings("unchecked")
		List<SingleVariableDeclaration> parameters = (List<SingleVariableDeclaration>) node.getStructuralProperty(MethodDeclaration.PARAMETERS_PROPERTY);
		for (SingleVariableDeclaration parameter : parameters) {
			EParameter eParameter = EcoreFactory.eINSTANCE.createEParameter();
			eParameter.setName(parameter.getName().getIdentifier());
			setUpType(eParameter, parameter.getType(), typeParameterIndex);
			eOperation.getEParameters().add(eParameter);
		}
		
		@SuppressWarnings("unchecked")
		List<Name> exceptionClassNames = (List<Name>) node.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTIONS_PROPERTY);
		for (Name name : exceptionClassNames) {
			ITypeBinding exceptionType = name.resolveTypeBinding();
			EGenericType genericExceptionType = myTypeResolver.resolveEGenericType(exceptionType, false, typeParameterIndex);
			eOperation.getEGenericExceptions().add(genericExceptionType);
		}
		
		myEClass.getEOperations().add(eOperation);		
		return false;
	}

	private void setUpType(ETypedElement eTypedElement, Type type, TypeParameterIndex typeParameterIndex) {
		TypeSettings typeSettings = getTypeSettingsImpliedByJavaType(type);
		ITypeBinding binding = typeSettings.getUnwrapStrategy().unwrap(type.resolveBinding());
		
		applyTypeSettings(eTypedElement, typeSettings);
		eTypedElement.setEGenericType(myTypeResolver.resolveEGenericType(binding, false, typeParameterIndex));
	}
	
	private IFeatureFactory createFeatureFactory(BodyDeclaration node,
			EGenericType eGenericType) {
		AnnotatedView annotations = ASTViewFactory.INSTANCE.createAnnotatedView(node);
		if (eGenericType.getEClassifier() instanceof EDataType) {
			final boolean isId = annotations.isAnnotationPresent(ID.class);
			return new IFeatureFactory() {
				public EStructuralFeature createStructuralFeature() {
					EAttribute result = EcoreFactory.eINSTANCE.createEAttribute();
					result.setID(isId);
					return result;
				}
			};
		} else {
			final boolean isContainment = annotations.isAnnotationPresent(Containment.class);
			final boolean isDerived = annotations.isAnnotationPresent(Derived.class);
			final boolean isUnsettable = annotations.isAnnotationPresent(Unsettable.class);
			final boolean isResolveProxies = annotations.isAnnotationPresent(ResolveProxies.class);
			return new IFeatureFactory() {
				public EStructuralFeature createStructuralFeature() {
					EReference result = EcoreFactory.eINSTANCE.createEReference();
					result.setContainment(isContainment);
					result.setDerived(isDerived);
					result.setResolveProxies(isResolveProxies);
					result.setUnsettable(isUnsettable);
					return result;
				}
			};
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
		}
		
		return featureSettings;
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
