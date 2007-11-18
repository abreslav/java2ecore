package org.abreslav.java2ecore.transformation.impl;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
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
		
		ITypeBinding[] supertypes = getSupertypes(binding);
		for (ITypeBinding typeBinding : supertypes) {
			if (typeBinding == null 
					|| Object.class.getName().equals(typeBinding.getQualifiedName())) {
				continue;
			}
			EGenericType eSuperClass = myTypeResolver.resolveEGenericType(typeBinding, typeParameterIndex);
			eClass.getEGenericSuperTypes().add(eSuperClass);
		}
		
		node.accept(new MemberBuilder(eClass, myTypeResolver, myDiagnostics, typeParameterIndex));
		return false;
	}

	private ITypeBinding[] getSupertypes(ITypeBinding binding) {
		ITypeBinding superclass = binding.getSuperclass();
		ITypeBinding[] interfaces = binding.getInterfaces();
		ITypeBinding[] supertypes = new ITypeBinding[1 + interfaces.length];
		supertypes[0] = superclass;
		for (int i = 1; i < supertypes.length; i++) {
			supertypes[i] = interfaces[i - 1];
		}
		return supertypes;
	}
}
