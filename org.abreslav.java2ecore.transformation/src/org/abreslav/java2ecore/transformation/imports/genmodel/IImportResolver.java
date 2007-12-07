package org.abreslav.java2ecore.transformation.imports.genmodel;

import org.abreslav.java2ecore.transformation.impl.ItemStorageWithStringKeys;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;

public interface IImportResolver {
	void resolveImports(GenModel genModel, ItemStorageWithStringKeys storage);
}
