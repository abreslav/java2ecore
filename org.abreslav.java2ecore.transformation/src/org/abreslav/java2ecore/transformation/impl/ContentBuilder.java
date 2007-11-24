package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.java2ecore.annotations.types.Class;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;
import org.abreslav.java2ecore.transformation.ITypeResolver;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
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

public class ContentBuilder {

	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;

	public ContentBuilder(ITypeResolver typeResolver, IDiagnostics diagnostics) {
		myDiagnostics = diagnostics;
		myTypeResolver = typeResolver;
	}

	public void buildEPackage(TypeDeclaration type, EPackage ePackage) {
		AnnotatedView view = ASTViewFactory.INSTANCE.createAnnotatedView(type);
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

		if (eEnum == null) {
			throw new IllegalStateException("An enum is present but not collected");
		}
		
		@SuppressWarnings("unchecked")
		List<Type> superInterfaces = (List<Type>) type.getStructuralProperty(EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
		for (Type superInterface : superInterfaces) {
			myDiagnostics.reportError("Ecore enums do not implement interfaces", superInterface);
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
						myDiagnostics.reportError("Nothing but literals is allowed in Enums", node);
					}
				}
			}
		});	
	}

	public void buildEDataType(TypeDeclaration type, EDataType eDataType) {
		setUpEClassifier(type, eDataType);
		
		AnnotatedView view = ASTViewFactory.INSTANCE.createAnnotatedView(type);
		AnnotationView eDataTypeAnnotation = view.getAnnotation(org.abreslav.java2ecore.annotations.types.EDataType.class);
		eDataType.setInstanceTypeName((String) eDataTypeAnnotation.getAttribute("value"));
		myTypeResolver.createTypeParameters(eDataType, type.resolveBinding());
	}
	
	public void buildEClass(TypeDeclaration type, EClass eClass) {
		setUpEClassifier(type, eClass);

		AnnotatedView view = ASTViewFactory.INSTANCE.createAnnotatedView(type);
		AnnotationView instanceTypeName = view.getAnnotation(InstanceTypeName.class);
		if (instanceTypeName != null) {
			eClass.setInstanceTypeName((String) instanceTypeName.getAttribute("value"));
		}
		
		eClass.setAbstract((type.getModifiers() & Modifier.ABSTRACT) != 0);

		AnnotationView classAnnotation = view.getAnnotation(Class.class);
		if ((classAnnotation == null) || !type.isInterface()) {
			eClass.setInterface(type.isInterface());
		} else {
			eClass.setInterface(false);
		}
		
		ITypeBinding binding = type.resolveBinding();
		TypeParameterIndex typeParameterIndex = myTypeResolver.createTypeParameters(
				eClass, binding);
		
		List<ITypeBinding> supertypes = getSupertypes(binding, type);
		for (ITypeBinding typeBinding : supertypes) {
			if (typeBinding == null 
					|| Object.class.getCanonicalName().equals(typeBinding.getQualifiedName())) {
				continue;
			}
			EGenericType eSuperClass = myTypeResolver.resolveEGenericType(typeBinding, true, typeParameterIndex);
			eClass.getEGenericSuperTypes().add(eSuperClass);
		}
		TypeDeclaration[] types = type.getTypes();
		if (types.length > 0) {
			markNestedThings(type, types);
			type = types[0];
		}
		type.accept(new MemberBuilder(eClass, myTypeResolver, myDiagnostics, typeParameterIndex));
	}
	
	private void markNestedThings(final TypeDeclaration node, TypeDeclaration[] types) {
		for (int i = 1; i < types.length; i++) {
			myDiagnostics.reportError("Only one nested type is allowed", types[i]);
		}
		
		node.accept(new ASTVisitor() {
			@Override
			public boolean visit(FieldDeclaration node) {
				myDiagnostics.reportError("If nested type is present, all the features must be specified in this type", node);
				return true;
			}
			
			@Override
			public boolean visit(MethodDeclaration node) {
				myDiagnostics.reportError("If nested type is present, all the features must be specified in this type", node);
				return true;
			}
			
			@Override
			public boolean visit(TypeDeclaration type) {
				return type == node;
			}
		});
	}

	private List<ITypeBinding> getSupertypes(ITypeBinding binding, ASTNode node) {
		ArrayList<ITypeBinding> result = new ArrayList<ITypeBinding>();
		
		ITypeBinding superclass = binding.getSuperclass();
		if (superclass != null
				&& !Object.class.getCanonicalName().equals(superclass.getQualifiedName())) {
			result.add(superclass);
		}
		ITypeBinding[] interfaces = binding.getInterfaces();
		for (ITypeBinding typeBinding : interfaces) {
			result.add(typeBinding);
		}
		return result;
	}

	private void setUpEClassifier(final AbstractTypeDeclaration type, final EClassifier eClassifier) {
		eClassifier.setName(type.getName().getIdentifier());
		ITypeBinding declaringClass = type.resolveBinding().getDeclaringClass();
		EPackage ePackage = myTypeResolver.getEPackage(declaringClass);
		ePackage.getEClassifiers().add(eClassifier);
	}
}
