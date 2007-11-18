package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.java2ecore.multinh._;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeBuilder extends ASTVisitor {

	private final ITypeResolver myTypeResolver;
	private final IDiagnostics myDiagnostics;
	private final EPackage myEPackage;

	public TypeBuilder(ITypeResolver typeResolver, IDiagnostics diagnostics,
			EPackage package1) {
		myEPackage = package1;
		myDiagnostics = diagnostics;
		myTypeResolver = new WrappingTypeResolver(typeResolver, myEPackage.getEClassifiers());
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		EClass eClass = myTypeResolver.getEClass(node.resolveBinding().getQualifiedName());
		if (eClass == null) {
			return false;
		}
		
		ITypeBinding binding = node.resolveBinding();
		EClassTypeParameterIndex typeParameterIndex = new EClassTypeParameterIndex();
		if (binding.isGenericType()) {
			ITypeBinding[] typeParameters = binding.getTypeParameters();
			for (ITypeBinding typeParameter : typeParameters) {
				ETypeParameter eTypeParameter = EcoreFactory.eINSTANCE.createETypeParameter();
				eTypeParameter.setName(typeParameter.getName());
				typeParameterIndex.registerTypeParameter(eTypeParameter);
				
				ITypeBinding[] typeBounds = typeParameter.getTypeBounds();
				for (ITypeBinding typeBound : typeBounds) {
					EGenericType eTypeBound = myTypeResolver.resolveEGenericType(typeBound, typeParameterIndex);
					eTypeParameter.getEBounds().add(eTypeBound);
				}
				
				eClass.getETypeParameters().add(eTypeParameter);
			}
		}
		
		List<ITypeBinding> supertypes = getSupertypes(binding, node);
		for (ITypeBinding typeBinding : supertypes) {
			if (typeBinding == null 
					|| Object.class.getCanonicalName().equals(typeBinding.getQualifiedName())) {
				continue;
			}
			EGenericType eSuperClass = myTypeResolver.resolveEGenericType(typeBinding, typeParameterIndex);
			eClass.getEGenericSuperTypes().add(eSuperClass);
		}
		
		node.accept(new MemberBuilder(eClass, myTypeResolver, myDiagnostics, typeParameterIndex));
		return false;
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
			unwrapToList(typeBinding, result, 0, node);
		}
		return result;
	}

	private void unwrapToList(ITypeBinding typeBinding, ArrayList<ITypeBinding> list, int depth, ASTNode node) {
		if (_.class.getCanonicalName().equals(typeBinding.getErasure().getQualifiedName())) {
			ITypeBinding[] typeArguments = typeBinding.getTypeArguments();
			unwrapToList(typeArguments[0], list, depth + 1, node);
			unwrapToList(typeArguments[1], list, depth + 1, node);
		} else {
			if (depth > 0 && typeBinding.isInterface()) {
				myDiagnostics.reportWarning("Don't use interface-wrapper operator '_' for interfaces. It is unnecesssary", node);
			}
			list.add(typeBinding);
		}
	}
}
