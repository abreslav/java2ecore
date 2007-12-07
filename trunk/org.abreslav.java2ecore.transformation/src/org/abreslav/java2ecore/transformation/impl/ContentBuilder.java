package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.java2ecore.annotations.types.InstanceTypeName;
import org.abreslav.java2ecore.transformation.ITypeResolver;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.deferred.IDeferredActions;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.*;

public class ContentBuilder {

	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;
	private final IDeferredActions myDeferredActions;

	public ContentBuilder(ITypeResolver typeResolver, IDiagnostics diagnostics,
			IDeferredActions deferredActions) {
		myTypeResolver = typeResolver;
		myDiagnostics = diagnostics;
		myDeferredActions = deferredActions;
	}

	public void buildEPackage(TypeDeclaration type, EPackage ePackage) {
		AnnotatedView view = ASTViewFactory.INSTANCE.createAnnotatedView(type);
		AnnotationValidator.checkEPackageAnnotations(view, myDiagnostics);
		ePackage.setName(type.getName().getIdentifier());
		AnnotationView annotation = view.getAnnotation(org.abreslav.java2ecore.annotations.EPackage.class);
		ePackage.setNsPrefix((String) annotation.getAttribute("nsPrefix"));
		ePackage.setNsURI((String) annotation.getAttribute("nsURI"));
		ITypeBinding declaringClass = type.resolveBinding().getDeclaringClass();
		if (declaringClass != null) {
			EPackage containingEPackage = myTypeResolver.getEPackage(declaringClass);
			containingEPackage.getESubpackages().add(ePackage);
		}
	}
	
	public void buildEEnum(final EnumDeclaration type, final EEnum eEnum) {
		setUpEClassifier(type, eEnum);

		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(type);
		AnnotationValidator.checkEEnumAnnotations(annotatedView, myDiagnostics);
		
		@SuppressWarnings("unchecked")
		List<Type> superInterfaces = (List<Type>) type.getStructuralProperty(EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
		for (Type superInterface : superInterfaces) {
			myDiagnostics.reportError(superInterface, ENUMS_DO_NOT_IMPLEMENT_INTERFACES);
		}
		type.accept(new ASTVisitor() {
			private int myIndex = 0;
			
			@Override
			public boolean visit(EnumConstantDeclaration node) {
				EEnumLiteral eEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
				eEnumLiteral.setLiteral(node.getName().getIdentifier());
				eEnumLiteral.setName(node.getName().getIdentifier());
				eEnumLiteral.setValue(myIndex++);
				eEnum.getELiterals().add(eEnumLiteral);
				return false;
			}
			
			@Override
			public void preVisit(ASTNode node) {
				if (node.getParent() == type) {
					if (node instanceof BodyDeclaration 
							&& false == node instanceof EnumConstantDeclaration) {
						myDiagnostics.reportError(node, NOTHING_BUT_LITERALS_ALLOWED_IN_ENUMS);
					}
				}
			}
		});	
	}

	public void buildEDataType(TypeDeclaration type, EDataType eDataType) {
		@SuppressWarnings("unchecked")
		List<BodyDeclaration> bodyDeclarations = (List<BodyDeclaration>) type.getStructuralProperty(TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
			myDiagnostics.reportError(bodyDeclaration, NO_CONTENT_ALLOWED_FOR_EDATATYPES);
		}

		setUpEClassifier(type, eDataType);
		
		AnnotatedView view = ASTViewFactory.INSTANCE.createAnnotatedView(type);
		AnnotationValidator.checkEDataTypeAnnotations(view, myDiagnostics);

		AnnotationView eDataTypeAnnotation = view.getAnnotation(org.abreslav.java2ecore.annotations.types.EDataType.class);
		eDataType.setInstanceTypeName((String) eDataTypeAnnotation.getDefaultAttribute());

		@SuppressWarnings("unchecked")
		List<TypeParameter> typeParameters = (List<TypeParameter>) type.getStructuralProperty(TypeDeclaration.TYPE_PARAMETERS_PROPERTY);
		myTypeResolver.createTypeParameters(eDataType, typeParameters);
	}
	
	public void buildEClass(TypeDeclaration type, EClass eClass) {
		setUpEClassifier(type, eClass);

		AnnotatedView view = ASTViewFactory.INSTANCE.createAnnotatedView(type);
		AnnotationValidator.checkEClassAnnotations(view, myDiagnostics);
		AnnotationView instanceTypeName = view.getAnnotation(InstanceTypeName.class);
		if (instanceTypeName != null) {
			eClass.setInstanceTypeName((String) instanceTypeName.getDefaultAttribute());
		}
		
		eClass.setAbstract((type.getModifiers() & Modifier.ABSTRACT) != 0);

		AnnotationView classAnnotation = view.getAnnotation(org.abreslav.java2ecore.annotations.types.EClass.class);
		if ((classAnnotation == null) || !type.isInterface()) {
			eClass.setInterface(type.isInterface());
		} else {
			eClass.setInterface(false);
		}

		@SuppressWarnings("unchecked")
		List<TypeParameter> typeParameters = (List<TypeParameter>) type.getStructuralProperty(TypeDeclaration.TYPE_PARAMETERS_PROPERTY);
		TypeParameterIndex typeParameterIndex = myTypeResolver.createTypeParameters(
				eClass, typeParameters);
		
		List<Type> supertypes = getSupertypes(type);
		for (Type supertype : supertypes) {
			EGenericType eSuperClass = myTypeResolver.resolveEGenericType(supertype, true, typeParameterIndex);
			eClass.getEGenericSuperTypes().add(eSuperClass);
		}
		
		TypeDeclaration[] types = type.getTypes();
		if (types.length > 0) {
			markNestedThings(type, types);
			type = types[0];
		}
		for (TypeDeclaration nested : type.getTypes()) {
			myDiagnostics.reportError(nested, NESTED_TYPES_ARE_NOT_SUPPORTED_BY_ECORE);
		}
		
		
		type.accept(new MemberBuilder(eClass, myTypeResolver, myDiagnostics, typeParameterIndex, myDeferredActions));
	}
	
	private void markNestedThings(final TypeDeclaration node, TypeDeclaration[] types) {
		for (int i = 1; i < types.length; i++) {
			myDiagnostics.reportError(types[i], ONLY_ONE_BODY_CLASS_IS_ALLOWED);
		}
		
		node.accept(new ASTVisitor() {
			@Override
			public boolean visit(FieldDeclaration node) {
				myDiagnostics.reportError(node, ALL_THE_FEATURES_MUST_BE_SPECIFIED_IN_A_BODY_CLASS);
				return true;
			}
			
			@Override
			public boolean visit(MethodDeclaration node) {
				myDiagnostics.reportError(node, ALL_THE_FEATURES_MUST_BE_SPECIFIED_IN_A_BODY_CLASS);
				return true;
			}
			
			@Override
			public boolean visit(EnumDeclaration node) {
				myDiagnostics.reportError(node, NESTED_TYPES_ARE_NOT_SUPPORTED_BY_ECORE);
				return false;
			}
			
			@Override
			public boolean visit(TypeDeclaration type) {
				return type == node;
			}
		});
	}

	private List<Type> getSupertypes(TypeDeclaration type) {
		List<Type> result = new ArrayList<Type>();
		Type superclass = (Type) type.getStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY);
		if (superclass != null) {
			result.add(superclass);
		}
		
		@SuppressWarnings("unchecked")
		List<Type> superinterfaces = (List<Type>) type.getStructuralProperty(TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
		result.addAll(superinterfaces);
		return result;
	}
	
	private void setUpEClassifier(final AbstractTypeDeclaration type, final EClassifier eClassifier) {
		eClassifier.setName(type.getName().getIdentifier());
		ITypeBinding declaringClass = type.resolveBinding().getDeclaringClass();
		EPackage ePackage = myTypeResolver.getEPackage(declaringClass);
		ePackage.getEClassifiers().add(eClassifier);
	}
}
