package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.abreslav.java2ecore.annotations.sfeatures.Containment;
import org.abreslav.java2ecore.annotations.sfeatures.Derived;
import org.abreslav.java2ecore.annotations.sfeatures.ID;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;
import org.abreslav.java2ecore.annotations.sfeatures.Unsettable;
import org.abreslav.java2ecore.transformation.ITypeResolver;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.deferred.IDeferredActions;
import org.abreslav.java2ecore.transformation.deferred.SetOppositeAction;
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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
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
	public boolean visit(FieldDeclaration node) {
		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
		for (VariableDeclarationFragment fragment : fragments.subList(1, fragments.size())) {
			myDiagnostics.reportError("Only one feature per declaration is allowed", fragment);
		}
		
		VariableDeclarationFragment fragment = fragments.get(0);
		if (fragment.getExtraDimensions() > 0) {
			myDiagnostics.reportError("Specify array dimensions at the field type", fragment);
		}

		ETypedElement temporaryTypedElement = new MyTypedElement();
		setUpTypedElement(temporaryTypedElement, node.getType(), node, myTypeParameterIndex);

		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(node);
		EGenericType eGenericType = temporaryTypedElement.getEGenericType();
		
		EStructuralFeature feature = createFeature(annotatedView, eGenericType);;
		
		feature.setEGenericType(temporaryTypedElement.getEGenericType());
		feature.setLowerBound(temporaryTypedElement.getLowerBound());
		feature.setOrdered(temporaryTypedElement.isOrdered());
		feature.setUnique(temporaryTypedElement.isUnique());
		feature.setUpperBound(temporaryTypedElement.getUpperBound());
		
		feature.setName(fragment.getName().getIdentifier());

		feature.setChangeable((node.getModifiers() & Modifier.FINAL) != 0);
		feature.setTransient((node.getModifiers() & Modifier.TRANSIENT) != 0);
		feature.setVolatile((node.getModifiers() & Modifier.VOLATILE) != 0);
		
		setDefaultValue(feature, fragment.getInitializer());

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
		List<ITypeBinding> parameterTypeBindings = new ArrayList<ITypeBinding>();
		for (TypeParameter typeParameter : typeParameters) {
			parameterTypeBindings.add(typeParameter.resolveBinding());
		}
		Collection<ETypeParameter> eTypeParameters = myTypeResolver.createETypeParameters(typeParameterIndex, parameterTypeBindings);
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
			EGenericType genericExceptionType = myTypeResolver.resolveEGenericType(exceptionType, false, typeParameterIndex);
			eOperation.getEGenericExceptions().add(genericExceptionType);
		}
		
		myEClass.getEOperations().add(eOperation);		
		return false;
	}

	private EStructuralFeature createFeature(AnnotatedView annotations,
			EGenericType eGenericType) {
		if (eGenericType.getEClassifier() instanceof EDataType) {
			boolean isId = annotations.isAnnotationPresent(ID.class);
			EAttribute result = EcoreFactory.eINSTANCE.createEAttribute();
			result.setID(isId);
			return result;
		} else {
			boolean isContainment = annotations.isAnnotationPresent(Containment.class);
			boolean isDerived = annotations.isAnnotationPresent(Derived.class);
			boolean isUnsettable = annotations.isAnnotationPresent(Unsettable.class);
			boolean isResolveProxies = annotations.isAnnotationPresent(ResolveProxies.class);

			AnnotationView oppositeAnnotation = annotations.getAnnotation(Opposite.class);
			EReference result = EcoreFactory.eINSTANCE.createEReference();
			result.setContainment(isContainment);
			result.setDerived(isDerived);
			result.setResolveProxies(isResolveProxies);
			result.setUnsettable(isUnsettable);
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
		
		ITypeBinding typeBinding = type.resolveBinding();
		typeBinding = typeSettings.getUnwrapStrategy().unwrap(typeBinding);
		EGenericType eGenericType = myTypeResolver.resolveEGenericType(typeBinding, false, typeParameterIndex);
		eTypedElement.setEGenericType(eGenericType);
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
