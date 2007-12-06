package org.abreslav.java2ecore.transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnosticMessage;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.ui.builder.Java2ECoreBuilder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;

public class TransformationPerformer {
	public static final String MODEL_FOLDER_NAME = "model";

	@SuppressWarnings("serial")
	private static class ExceptionWrapper extends RuntimeException {
		public ExceptionWrapper(CoreException cause) {
			super(cause);
		}
	}
	
	public static void performTransformation(final ICompilationUnit compilationUnit)
			throws CoreException, IOException {
		compilationUnit.getResource().deleteMarkers(Java2ECoreBuilder.MARKER_TYPE, false,
				IResource.DEPTH_ZERO);
		final boolean[] hasErrors = new boolean[1];
		IDiagnostics diagnostics = new IDiagnostics() {
			public void reportError(ASTNode node, IDiagnosticMessage message) {
				createMarker(compilationUnit, IMarker.SEVERITY_ERROR, message.toString(), node);
				hasErrors[0] = true;
			}

			public void reportErrorFormatted(ASTNode node,
					IDiagnosticMessage message, Object... args) {
				createMarker(compilationUnit, IMarker.SEVERITY_ERROR, message.format(args), node);
				hasErrors[0] = true;
			}

			public void reportWarning(ASTNode node, IDiagnosticMessage message) {
				createMarker(compilationUnit, IMarker.SEVERITY_WARNING, message.toString(), node);
			}

			public void reportWarningFormatted(ASTNode node,
					IDiagnosticMessage message, Object... args) {
				createMarker(compilationUnit, IMarker.SEVERITY_WARNING, message.format(args), node);
			}
		};
		try {
			IProject project = compilationUnit.getJavaProject().getProject();
			EPackage ePackage = CompilationUnitToECoreTransformation.transform(
					compilationUnit, new GenModelLoader(project), diagnostics);

			if (!hasErrors[0]) {
				String fileName = ePackage.getName() + ".ecore";
				Resource res = new XMIResourceFactoryImpl().createResource(URI
						.createURI(fileName));
				res.getContents().add(ePackage);
	
				IFolder folder = project.getFolder(MODEL_FOLDER_NAME);
				if (!folder.exists()) {
					folder.create(true, true, null);
				}
				IFile file = folder.getFile(fileName);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				res.save(byteArrayOutputStream, Collections.EMPTY_MAP);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						byteArrayOutputStream.toByteArray());
				if (file.exists()) {
					file.setContents(byteArrayInputStream, true, false, null);
				} else {
					file.create(byteArrayInputStream, true, null);
				}
			}
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (ExceptionWrapper e) {
			throw (CoreException) e.getCause();
		}
	}

	private static void createMarker(ICompilationUnit compilationUnit,
			int severity, String message, ASTNode node) {
		try {
			IMarker marker = compilationUnit.getResource().createMarker(Java2ECoreBuilder.MARKER_TYPE);
			marker.setAttribute(IMarker.SEVERITY, severity);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.CHAR_START, node.getStartPosition());
			marker.setAttribute(IMarker.CHAR_END, node.getStartPosition() + node.getLength());
		} catch (CoreException e) {
			throw new ExceptionWrapper(e);
		}
	}

}
