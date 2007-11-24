package org.abreslav.java2ecore.transformation.impl;

import java.util.HashMap;
import java.util.Map;

import org.abreslav.java2ecore.transformation.IItemCollector;
import org.abreslav.java2ecore.transformation.declarations.EClassDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EDataTypeDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EEnumDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EPackageDeclaration;
import org.abreslav.java2ecore.transformation.declarations.IDeclaration;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationCollector;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationVisitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ItemCollector implements IItemCollector {
	private final Map<String, EClass> myEClasses = new HashMap<String, EClass>();
	private final Map<String, EDataType> myEDataTypes = new HashMap<String, EDataType>();
	private final Map<String, EEnum> myEEnums = new HashMap<String, EEnum>();
	private final Map<String, EPackage> myEPackages = new HashMap<String, EPackage>();
	
	public ItemCollector(IDeclarationCollector declarationCollector) {
		EList<EClassifier> classifiers = EcorePackage.eINSTANCE.getEClassifiers();
		for (EClassifier classifier : classifiers) {
			if (classifier.getInstanceClassName() != null) {
				if (classifier instanceof EDataType) {
					myEDataTypes.put(classifier.getInstanceClassName(), (EDataType) classifier);
				} else if (classifier instanceof EClass) {
					myEClasses.put(classifier.getInstanceClassName(), (EClass) classifier);
				}
			}
		}

		for (final IDeclaration declaration : declarationCollector.getDeclarations()) {
			declaration.accept(new IDeclarationVisitor(){
				public void visit(EClassDeclaration declaration) {
					addEClass(declaration.getDeclaration().resolveBinding(), declaration.getDeclaredElement());
				}

				public void visit(EDataTypeDeclaration declaration) {
					addEDataType(declaration.getDeclaration().resolveBinding(), declaration.getDeclaredElement());
				}

				public void visit(EEnumDeclaration declaration) {
					addEEnum(declaration.getDeclaration().resolveBinding(), declaration.getDeclaredElement());
				}

				public void visit(EPackageDeclaration declaration) {
					addEPackage(declaration.getDeclaration().resolveBinding(), declaration.getDeclaredElement());
				}
			});
		}
	}
	
	public void addEClass(ITypeBinding type, EClass class1) {
		myEClasses.put(type.getErasure().getQualifiedName(), class1);
	}

	public void addEDataType(ITypeBinding type, EDataType dataType) {
		myEDataTypes.put(type.getErasure().getQualifiedName(), dataType);
	}

	public void addEEnum(ITypeBinding type, EEnum enum1) {
		myEEnums.put(type.getErasure().getQualifiedName(), enum1);
	}

	public void addEPackage(ITypeBinding type, EPackage package1) {
		myEPackages.put(type.getErasure().getQualifiedName(), package1);
	}

	public EClass getEClass(ITypeBinding type) {
		return myEClasses.get(type.getErasure().getQualifiedName());
	}

	public EDataType getEDataType(ITypeBinding type) {
		return myEDataTypes.get(type.getErasure().getQualifiedName());
	}

	public EEnum getEEnum(ITypeBinding type) {
		return myEEnums.get(type.getErasure().getQualifiedName());
	}

	public EPackage getEPackage(ITypeBinding type) {
		return myEPackages.get(type.getErasure().getQualifiedName());
	}

}
