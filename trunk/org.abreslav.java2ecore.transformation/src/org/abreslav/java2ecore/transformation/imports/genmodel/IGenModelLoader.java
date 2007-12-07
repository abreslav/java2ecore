package org.abreslav.java2ecore.transformation.imports.genmodel;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;

public interface IGenModelLoader {
	GenModel loadGenModel(String path) throws ModelLoadingException;
}