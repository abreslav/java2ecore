package org.abreslav.java2ecore.transformation.impl;

import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.CONFLICT_WITH_NO_DEFAULT_VALUE;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.DEFAULT_VALUE_IS_SPECIFIED_BY_AN_INITIALIZER;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.NESTED_TYPES_ARE_NOT_SUPPORTED_BY_ECORE;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.NON_CONSTANT_VALUES_ARE_NOT_ALLOWED;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.ONLY_ONE_FEATURE_PER_DECLARATION;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.SPECIFY_DIMENSIONS_AT_THE_TYPE;

import java.util.Collection;
import java.util.List;

import org.abreslav.java2ecore.annotations.sfeatures.Containment;
import org.abreslav.java2ecore.annotations.sfeatures.DefaultValueLiteral;
import org.abreslav.java2ecore.annotations.sfeatures.Derived;
import org.abreslav.java2ecore.annotations.sfeatures.ID;
import org.abreslav.java2ecore.annotations.sfeatures.NoDefaultValue;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;
import org.abreslav.java2ecore.annotations.sfeatures.Unsettable;
import org.abreslav.java2ecore.transformation.ITypeResolver;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.deferred.IDeferredActions;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.impl.deferred.SetOppositeAction;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MemberBuilder extends ASTVisitor {
	private final EClass myEClass;
	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;
	private final TypeParameterIndex myTypeParameterIndex;
	private final IDeferredActions myDeferredActions;
	private final TypeSettingsCalculator myTypeSettingsCalculator;
	
	public MemberBuilder(EClass eClass, ITypeResolver typeResolver,
			IDiagnostics diagnostics, TypeParameterIndex typeParameterIndex,
			IDeferredActions deferredActions) {
		myEClass = eClass;
		myTypeResolver = typeResolver;
		myDiagnostics = diagnostics;
		myTypeParameterIndex = typeParameterIndex;
		myDeferredActions = deferredActions;
		myTypeSettingsCalculator = new TypeSettingsCalculator(myDiagnostics);
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		myDiagnostics.reportError(node, NESTED_TYPES_ARE_NOT_SUPPORTED_BY_ECORE);
		return false;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment fragment : fragments.subList(1, fragments.size())) {
			myDiagnostics.reportError(fragment, ONLY_ONE_FEATURE_PER_DECLARATION);
		}
		
		VariableDeclarationFragment fragment = fragments.get(0);
		if (fragment.getExtraDimensions() > 0) {
			myDiagnostics.reportError(fragment, SPECIFY_DIMENSIONS_AT_THE_TYPE);
		}

		ETypedElement temporaryTypedElement = new MyTypedElement();
		setUpTypedElement(temporaryTypedElement, node.getType(), node, myTypeParameterIndex);

		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(node);
		EGenericType eGenericType = temporaryTypedElement.getEGenericType();
		
		EStructuralFeature feature = createFeature(annotatedView, eGenericType);
		
		feature.setEGenericType(temporaryTypedElement.getEGenericType());
		feature.setLowerBound(temporaryTypedElement.getLowerBound());
		feature.setOrdered(temporaryTypedElement.isOrdered());
		feature.setUnique(temporaryTypedElement.isUnique());
		feature.setUpperBound(temporaryTypedElement.getUpperBound());
		
		feature.setName(fragment.getName().getIdentifier());

		feature.setChangeable((node.getModifiers() & Modifier.FINAL) == 0);
		feature.setTransient((node.getModifiers() & Modifier.TRANSIENT) != 0);
		feature.setVolatile((node.getModifiers() & Modifier.VOLATILE) != 0);
		
		feature.setDerived(annotatedView.isAnnotationPresent(Derived.class));
		feature.setUnsettable(annotatedView.isAnnotationPresent(Unsettable.class));
		
		setDefaultValue(feature, annotatedView, fragment.getInitializer());

		myEClass.getEStructuralFeatures().add(feature);
		return false;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		EOperation eOperation = EcoreFactory.eINSTANCE.createEOperation();
		eOperation.setName(node.getName().getIdentifier());
		
		TypeParameterIndex typeParameterIndex = new TypeParameterIndex(myTypeParameterIndex);
		
		@SuppressWarnings("unchecked")
		List<TypeParameter> typeParameters = (List<TypeParameter>) node.getStructuralProperty(MethodDeclaration.TYPE_PARAMETERS_PROPERTY);
		Collection<ETypeParameter> eTypeParameters = myTypeResolver.createETypeParameters(typeParameterIndex, typeParameters);
		eOperation.getETypeParameters().addAll(eTypeParameters);
		
		setUpTypedElement(eOperation, node.getReturnType2(), node, typeParameterIndex);

		@SuppressWarnings("unchecked")
		List<SingleVariableDeclaration> parameters = (List<SingleVariableDeclaration>) node.getStructuralProperty(MethodDeclaration.PARAMETERS_PROPERTY);
		for (SingleVariableDeclaration parameter : parameters) {
			EParameter eParameter = EcoreFactory.eINSTANCE.createEParameter();
			eParameter.setName(parameter.getName().getIdentifier());
			
			setUpTypedElement(eParameter, parameter.getType(), parameter, typeParameterIndex);
			
			eOperation.getEParameters().add(eParameter);
		}
		
		@SuppressWarnings("unchecked")
		List<Name> exceptionClassNames = (List<Name>) node.getStructuralProperty(MethodDeclaration.THROWN_EXCEPTIONS_PROPERTY);
		for (Name name : exceptionClassNames) {
			ITypeBinding exceptionType = name.resolveTypeBinding();
			EClassifier exception = myTypeResolver.resolveEClassifier(exceptionType, name, false);
			if (exception != null) {
				eOperation.getEExceptions().add(exception);
			}
		}
		
		myEClass.getEOperations().add(eOperation);		
		return false;
	}

	private EStructuralFeature createFeature(AnnotatedView annotations,
			EGenericType eGenericType) {
		if (eGenericType.getEClassifier() instanceof EDataType) {
			AnnotationValidator.checkEAttributeAnnotations(annotations, myDiagnostics);
			boolean isId = annotations.isAnnotationPresent(ID.class);
			EAttribute result = EcoreFactory.eINSTANCE.createEAttribute();
			result.setID(isId);
			return result;
		} else {
			AnnotationValidator.checkEReferenceAnnotations(annotations, myDiagnostics);
			boolean isContainment = annotations.isAnnotationPresent(Containment.class);
			AnnotationView resolveProxies = annotations.getAnnotation(ResolveProxies.class);
			boolean isResolveProxies = 
				(resolveProxies == null) 
					? true
					: (Boolean) resolveProxies.getDefaultAttribute();
			AnnotationView oppositeAnnotation = annotations.getAnnotation(Opposite.class);
			EReference result = EcoreFactory.eINSTANCE.createEReference();
			result.setContainment(isContainment);
			result.setResolveProxies(isResolveProxies);
			if (oppositeAnnotation != null) {
				myDeferredActions.addAction(
						new SetOppositeAction(
								result, oppositeAnnotation.getAnnotation(), 
								(EClass) eGenericType.getEClassifier(), 
								(String) oppositeAnnotation.getDefaultAttribute(),
								true
						));
			}
			return result;
		}
	}

	private void setUpTypedElement(ETypedElement eTypedElement, Type type, ASTNode declaration, TypeParameterIndex typeParameterIndex) {
		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(declaration);
		ITypeSettings typeSettings = myTypeSettingsCalculator.calculateTypeSettings(type, annotatedView);
		
		eTypedElement.setLowerBound(typeSettings.getLowerBound());
		eTypedElement.setUpperBound(typeSettings.getUpperBound());
		eTypedElement.setUnique(typeSettings.isUnique());
		eTypedElement.setOrdered(typeSettings.isOrdered());
		
		type = typeSettings.getUnwrapStrategy().unwrap(type);
		EGenericType eGenericType = myTypeResolver.resolveEGenericType(type, false, typeParameterIndex);
		eTypedElement.setEGenericType(eGenericType);
	}
	
	private void setDefaultValue(EStructuralFeature feature,
			AnnotatedView annotatedView, Expression initializer) {
		boolean noDefaultValue = annotatedView.isAnnotationPresent(NoDefaultValue.class);
		AnnotationView annotation = annotatedView.getAnnotation(DefaultValueLiteral.class);
		if (annotation != null) {
			if (noDefaultValue) {
				myDiagnostics.reportError(annotation.getAnnotation(), CONFLICT_WITH_NO_DEFAULT_VALUE);
			} else if (initializer != null 
					&& false == initializer instanceof NullLiteral) {
				myDiagnostics.reportError(annotation.getAnnotation(), DEFAULT_VALUE_IS_SPECIFIED_BY_AN_INITIALIZER);
			} else {
				feature.setDefaultValueLiteral((String) annotation.getDefaultAttribute());
				return;
			}
		}
		if (initializer != null && !noDefaultValue) {
			Object constant = initializer.resolveConstantExpressionValue();
			if (constant != null) {
				feature.setDefaultValue(constant);
				return;
			} 
			if (initializer instanceof Name) {
				Name name = (Name) initializer;
				IBinding binding = name.resolveBinding();
				if (binding instanceof IVariableBinding) {
					IVariableBinding variableBinding = (IVariableBinding) binding;
					if (variableBinding.isEnumConstant()) {
						feature.setDefaultValueLiteral(variableBinding.getName());
						return;
					}
				}
			} 
			if (initializer instanceof NullLiteral) {
				return;
			} 
			myDiagnostics.reportError(initializer, NON_CONSTANT_VALUES_ARE_NOT_ALLOWED);
		}
	}

}
