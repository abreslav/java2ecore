package org.abreslav.java2ecore.transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.abreslav.java2ecore.transformation.diagnostics.Diagnostic;
import org.abreslav.java2ecore.transformation.diagnostics.Diagnostics;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.diagnostics.Severity;
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

public class TransformationPerformer {
	private static final String MARKER_TYPE = "org.abreslav.java2ecore.transformation.ui.j2EcoreProblem";

	public static void performTransformation(ICompilationUnit compilationUnit) throws CoreException, IOException {
		compilationUnit.getResource().deleteMarkers(MARKER_TYPE, false,
				IResource.DEPTH_ZERO);
		IDiagnostics diagnostics = new Diagnostics();
		EPackage ePackage = CompilationUnitToECoreTransformation.transform(
				compilationUnit, diagnostics);

		IProject project = compilationUnit.getJavaProject().getProject();
		if (!diagnostics.hasErrors()) {
			String fileName = ePackage.getName() + ".ecore";
			Resource res = new XMIResourceFactoryImpl().createResource(URI
					.createURI(fileName));
			res.getContents().add(ePackage);

			IFolder folder = project.getFolder("model");
			if (!folder.exists()) {
				folder.create(true, true, null);
			}
			IFile file = folder.getFile(fileName);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			res.save(byteArrayOutputStream, Collections.EMPTY_MAP);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			if (file.exists()) {
				file.setContents(byteArrayInputStream, true, false, null);
			} else {
				file.create(byteArrayInputStream, true, null);
			}
		}
		for (Diagnostic diagnostic : diagnostics) {
			IMarker marker = compilationUnit.getResource().createMarker(
					MARKER_TYPE);
			marker
					.setAttribute(
							IMarker.SEVERITY,
							diagnostic.getSeverity() == Severity.ERROR ? IMarker.SEVERITY_ERROR
									: IMarker.SEVERITY_WARNING);
			marker.setAttribute(IMarker.MESSAGE, diagnostic.getMessage());
			marker.setAttribute(IMarker.CHAR_START, diagnostic.getNode()
					.getStartPosition());
			marker.setAttribute(IMarker.CHAR_END, diagnostic.getNode()
					.getStartPosition()
					+ diagnostic.getNode().getLength());
		}
		project.refreshLocal(
				IResource.DEPTH_INFINITE, null);
	}

}
