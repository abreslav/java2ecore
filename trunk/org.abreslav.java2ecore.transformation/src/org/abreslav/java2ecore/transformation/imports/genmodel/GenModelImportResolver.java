package org.abreslav.java2ecore.transformation.imports.genmodel;

import java.util.List;

import org.abreslav.java2ecore.transformation.impl.typeresolver.ItemStorageWithStringKeys;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenEnum;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;

public class GenModelImportResolver implements IImportResolver {

	public static final GenModelImportResolver INSTANCE = new GenModelImportResolver();
	
	private GenModelImportResolver() {
		
	}
	
	public void resolveImports(GenModel genModel, ItemStorageWithStringKeys storage) {
		processGenPackages(storage, genModel.getGenPackages());
	}
 
	private void processGenPackages(ItemStorageWithStringKeys storage, List<GenPackage> genPackages) {
		for (GenPackage genPackage : genPackages) {
			processGenPackages(storage, genPackage.getSubGenPackages());
			for (GenClass genClass : genPackage.getGenClasses()) {
				storage.addEClass(genClass.getQualifiedInterfaceName(), genClass.getEcoreClass());
			}
			for (GenEnum genEnum : genPackage.getGenEnums()) {
				storage.addEEnum(genEnum.getQualifiedInstanceClassName(), genEnum.getEcoreEnum());
			}
		}
	}

}
