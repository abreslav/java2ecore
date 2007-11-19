package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
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

	private void processEClass(TypeDeclaration node, ITypeBinding binding) {
		EClass eClass = myTypeResolver.getEClass(binding);
		if (eClass == null) {
			return;
		}
		
		EClassTypeParameterIndex typeParameterIndex = myTypeResolver.createTypeParameters(
				eClass, binding);
		
		List<ITypeBinding> supertypes = getSupertypes(binding, node);
		for (ITypeBinding typeBinding : supertypes) {
			if (typeBinding == null 
					|| Object.class.getCanonicalName().equals(typeBinding.getQualifiedName())) {
				continue;
			}
			EGenericType eSuperClass = myTypeResolver.resolveEGenericType(typeBinding, typeParameterIndex);
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
