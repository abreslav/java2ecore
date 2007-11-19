package org.abreslav.java2ecore.transformation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.java2ecore.transformation.utils.NullSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeResolver implements ITypeResolver {
	private final Map<String, EClass> myEClasses = new HashMap<String, EClass>();
	private final Map<String, EDataType> myEDataTypes = new HashMap<String, EDataType>();
	private Collection<? super EClassifier> myWrappedEClassifiers;
	
	public TypeResolver() {
		this(null);
	}
	
	public TypeResolver(Collection<? super EClassifier> wrappedClassifiers) {
		initWrappedClasssfiers(wrappedClassifiers);
		EList<EClassifier> classifiers = EcorePackage.eINSTANCE.getEClassifiers();
		for (EClassifier classifier : classifiers) {
			if (classifier instanceof EDataType) {
				myEDataTypes.put(classifier.getInstanceClassName(), (EDataType) classifier);
			}
		}
	}

	private void initWrappedClasssfiers(
			Collection<? super EClassifier> wrappedClassifiers) {
		myWrappedEClassifiers = wrappedClassifiers;
		if (myWrappedEClassifiers == null) {
			myWrappedEClassifiers = new NullSet<EClassifier>();
		}
	}

	public EClass getEClass(ITypeBinding type) {
		return myEClasses.get(type.getErasure().getQualifiedName());
	}

	public EDataType getEDataType(ITypeBinding type) {
		return myEDataTypes.get(type.getErasure().getQualifiedName());
	}

	public void addEClass(ITypeBinding type, EClass eClass) {
		myEClasses.put(type.getErasure().getQualifiedName(), eClass);
	}

	public void addEDataType(ITypeBinding type, EDataType eDataType) {
		myEDataTypes.put(type.getErasure().getQualifiedName(), eDataType);
	}
	
	public EGenericType resolveEGenericType(ITypeBinding binding, EClassTypeParameterIndex typeParameterIndex) {
		EGenericType eGenericType = EcoreFactory.eINSTANCE.createEGenericType(); 
		
		if (binding.isTypeVariable()) {
			eGenericType.setETypeParameter(typeParameterIndex.getETypeParameter(binding.getName()));
		} if (binding.isWildcardType()) { 
			ITypeBinding bound = binding.getBound();
			if (bound != null) {
				EGenericType eBound = resolveEGenericType(bound, typeParameterIndex);
				if (binding.isUpperbound()) {
					eGenericType.setEUpperBound(eBound);
				} else {
					eGenericType.setELowerBound(eBound);
				}
			}
		} else {
			processActualType(binding, eGenericType);
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		for (ITypeBinding typeArgument : typeArguments) {
			eGenericType.getETypeArguments().add(resolveEGenericType(typeArgument, typeParameterIndex));
		}
		
		return eGenericType;
	}

	private void processActualType(ITypeBinding binding,
			EGenericType eGenericType) {
		
		EClass eClass = getEClass(binding);
		if (eClass != null) {
			eGenericType.setEClassifier(eClass);
		} else {
			EDataType eDataType = resolveEDataType(binding);
			if (eDataType != null) {
				eGenericType.setEClassifier(eDataType);
			}
		}
	}
	
	public EClassTypeParameterIndex createTypeParameters(EClassifier eClassifier,
			ITypeBinding binding) {
		EClassTypeParameterIndex typeParameterIndex = new EClassTypeParameterIndex();
		if (binding.isGenericType()) {
			ITypeBinding[] typeParameters = binding.getTypeParameters();
			for (ITypeBinding typeParameter : typeParameters) {
				ETypeParameter eTypeParameter = EcoreFactory.eINSTANCE.createETypeParameter();
				eTypeParameter.setName(typeParameter.getName());
				typeParameterIndex.registerTypeParameter(eTypeParameter);
				
				ITypeBinding[] typeBounds = typeParameter.getTypeBounds();
				for (ITypeBinding typeBound : typeBounds) {
					// TODO: Strange hack :)
					if (Object.class.getName().equals(typeBound.getQualifiedName())) {
						continue;
					}
					EGenericType eTypeBound = resolveEGenericType(typeBound, typeParameterIndex);
					eTypeParameter.getEBounds().add(eTypeBound);
				}
				
				eClassifier.getETypeParameters().add(eTypeParameter);
			}
		}
		return typeParameterIndex;
	}

	public EDataType resolveEDataType(ITypeBinding type) {
		EDataType eDataType = getEDataType(type);
		if (eDataType == null) {
			eDataType = EcoreFactory.eINSTANCE.createEDataType();
			eDataType.setName(type.getErasure().getName());
			String qualifiedName = type.getErasure().getQualifiedName();
			eDataType.setInstanceTypeName(qualifiedName);

			createTypeParameters(eDataType, type.getTypeDeclaration());
			
			myEDataTypes.put(qualifiedName, eDataType);
			myWrappedEClassifiers.add(eDataType);
		}
		return eDataType;
	}


}
