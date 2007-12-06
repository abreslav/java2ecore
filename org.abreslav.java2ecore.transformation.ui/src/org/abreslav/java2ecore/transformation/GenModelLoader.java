package org.abreslav.java2ecore.transformation;

import java.io.IOException;
import java.util.HashMap;

import org.abreslav.java2ecore.transformation.imports.genmodel.IGenModelLoader;
import org.abreslav.java2ecore.transformation.imports.genmodel.ModelLoadingException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;

public class GenModelLoader implements IGenModelLoader {

	private final IContainer myRoot;
	
	public GenModelLoader(IContainer root) {
		myRoot = root;
	}

	public ResourceSet createResourceSet() {
		ResourceSetImpl resourceSetImpl = new ResourceSetImpl();
		resourceSetImpl.setURIResourceMap(new HashMap<URI, Resource>());
		ResourceSet resourceSet = resourceSetImpl;
		resourceSet.setPackageRegistry(EPackage.Registry.INSTANCE);
		resourceSet.setResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
		
		resourceSet.setURIConverter(new URIConverterImpl() {
			@Override
			public URI normalize(URI uri) {
				String scheme = uri.scheme();
				if (scheme == null) {
					IFile file = myRoot.getFile(Path.fromPortableString(uri.toFileString()));
					java.net.URI locationURI = file.getLocationURI();
					URI fileURI = URI.createFileURI(locationURI.getPath().toString());
					return fileURI;
				}
				return super.normalize(uri);
			}
		});
		return resourceSet;
	}

	public GenModel loadGenModel(String path) throws ModelLoadingException {
		IFile file = myRoot.getFile(Path.fromPortableString(path));
		if (!file.exists()) {
			throw new ModelLoadingException("GenModel file does not exist");
		}
		ResourceSet resourceSet = createResourceSet();
		Resource resource = resourceSet.createResource(URI.createURI(path));
		try {
			resource.load(file.getContents(), null);
		} catch (IOException e) {
			throw new ModelLoadingException(e);
		} catch (CoreException e) {
			throw new ModelLoadingException(e);
		}
		if (resource.getContents().size() < 0) {
			throw new ModelLoadingException("GenModel file does not contain a model");
		}
		EObject object = resource.getContents().get(0);
		if (false == object instanceof GenModel) {
			throw new ModelLoadingException("This file does not contain an EMF GenModel");
		}
		return (GenModel) object;
	}

}
