package org.abreslav.java2ecore.transformation.impl;

import org.abreslav.java2ecore.transformation.IItemStorage;
import org.abreslav.java2ecore.transformation.declarations.EClassDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EDataTypeDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EEnumDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EPackageDeclaration;
import org.abreslav.java2ecore.transformation.declarations.IDeclaration;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationStorage;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationVisitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class ItemStorage implements IItemStorage {
	private final ItemStorageWithStringKeys myImpl;
	
	public ItemStorage(ItemStorageWithStringKeys impl, IDeclarationStorage declarationStorage) {
		myImpl = impl;
		importEcorePackageContents();
		importDeclarations(declarationStorage);
	}

	private void importEcorePackageContents() {
		for (EClassifier classifier : EcorePackage.eINSTANCE.getEClassifiers()) {
			if (classifier.getInstanceClassName() != null) {
				if (classifier instanceof EDataType) {
					myImpl.addEDataType(classifier.getInstanceClassName(), (EDataType) classifier);
				} else if (classifier instanceof EClass) {
					myImpl.addEClass(classifier.getInstanceClassName(), (EClass) classifier);
				}
			}
		}
	}

	private void importDeclarations(IDeclarationStorage declarationStorage) {
		for (final IDeclaration declaration : declarationStorage.getDeclarations()) {
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
		myImpl.addEClass(type.getErasure().getQualifiedName(), class1);
	}
	
	public void addEDataType(ITypeBinding type, EDataType dataType) {
		myImpl.addEDataType(type.getErasure().getQualifiedName(), dataType);
	}

	public void addEEnum(ITypeBinding type, EEnum enum1) {
		myImpl.addEEnum(type.getErasure().getQualifiedName(), enum1);
	}

	public void addEPackage(ITypeBinding type, EPackage package1) {
		myImpl.addEPackage(type.getErasure().getQualifiedName(), package1);
	}

	public EClass getEClass(ITypeBinding type) {
		return myImpl.getEClass(type.getErasure().getQualifiedName());
	}

	public EDataType getEDataType(ITypeBinding type) {
		return myImpl.getEDataType(type.getErasure().getQualifiedName());
	}

	public EEnum getEEnum(ITypeBinding type) {
		return myImpl.getEEnum(type.getErasure().getQualifiedName());
	}

	public EPackage getEPackage(ITypeBinding type) {
		return myImpl.getEPackage(type.getErasure().getQualifiedName());
	}

}
