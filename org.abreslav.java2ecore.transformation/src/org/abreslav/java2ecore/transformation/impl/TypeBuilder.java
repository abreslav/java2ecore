package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeBuilder extends ASTVisitor {

	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;

	public TypeBuilder(ITypeResolver typeResolver, IDiagnostics diagnostics) {
		myDiagnostics = diagnostics;
		myTypeResolver = typeResolver;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();
		if (processEDataType(binding)) {
			return false;
		}
		
		processEClass(node, binding);
		return false;
	}

	@Override
	public boolean visit(final EnumDeclaration enumNode) {
		final EEnum eEnum = myTypeResolver.getEEnum(enumNode.resolveBinding());
		if (eEnum == null) {
			throw new IllegalStateException("An enum is present but not collected");
		}
		
		@SuppressWarnings("unchecked")
		List<Type> superInterfaces = (List<Type>) enumNode.getStructuralProperty(EnumDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
		for (Type type : superInterfaces) {
			myDiagnostics.reportError("ECore enums do not implement interfaces", type);
		}
		enumNode.accept(new ASTVisitor() {
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
				if (node.getParent() == enumNode) {
					if (node instanceof BodyDeclaration 
							&& false == node instanceof EnumConstantDeclaration) {
						myDiagnostics.reportError("Nothing but literals is allowed in Enums", node);
					}
				}
			}
		});
		
		return false;
	}
	
	private void processEClass(TypeDeclaration node, ITypeBinding binding) {
		EClass eClass = myTypeResolver.getEClass(binding);
		if (eClass == null) {
			return;
		}
		
		TypeParameterIndex typeParameterIndex = myTypeResolver.createTypeParameters(
				eClass, binding);
		
		List<ITypeBinding> supertypes = getSupertypes(binding, node);
		for (ITypeBinding typeBinding : supertypes) {
			if (typeBinding == null 
					|| Object.class.getCanonicalName().equals(typeBinding.getQualifiedName())) {
				continue;
			}
			EGenericType eSuperClass = myTypeResolver.resolveEGenericType(typeBinding, true, typeParameterIndex);
			eClass.getEGenericSuperTypes().add(eSuperClass);
		}
		TypeDeclaration[] types = node.getTypes();
		if (types.length > 0) {
			markNestedThings(node, types);
			node = types[0];
		}
		node.accept(new MemberBuilder(eClass, myTypeResolver, myDiagnostics, typeParameterIndex));
	}

	private boolean processEDataType(ITypeBinding binding) {
		EDataType eDataType = myTypeResolver.getEDataType(binding);
		if (eDataType == null) {
			return false;
		}
		
		myTypeResolver.createTypeParameters(eDataType, binding);
		return true;
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

}
