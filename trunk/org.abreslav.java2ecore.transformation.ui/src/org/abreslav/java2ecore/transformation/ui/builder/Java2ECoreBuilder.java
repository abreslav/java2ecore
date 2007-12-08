package org.abreslav.java2ecore.transformation.ui.builder;

import java.io.IOException;
import java.util.Map;

import org.abreslav.java2ecore.transformation.TransformationPerformer;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

public class Java2ECoreBuilder extends IncrementalProjectBuilder {
	public static final String SOURCE_FOLDER_NAME = "ecores";
	public static final String MARKER_TYPE = Java2ECoreNature.PLUGIN_ID + ".j2EcoreProblem";

	class DeltaVisitor implements IResourceDeltaVisitor {

		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			IJavaElement element = JavaCore.create(resource);
			if (element == null) {
				return false;
			}
			if (element instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot srcFolder = (IPackageFragmentRoot) element;
				return SOURCE_FOLDER_NAME.equals(srcFolder.getElementName());
			}
			if (element instanceof ICompilationUnit) {
				ICompilationUnit compilationUnit = (ICompilationUnit) element;
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					perform(compilationUnit);
					break;
				case IResourceDelta.REMOVED:
					// handle removed resource
					break;
				case IResourceDelta.CHANGED:
					perform(compilationUnit);
					// handle changed resource
					break;
				}
			}
			return true;
		}
	}

	class ResourceVisitor implements IResourceVisitor {

		public boolean visit(IResource resource) throws CoreException {
			IJavaElement element = JavaCore.create(resource);
			if (element == null) {
				return false;
			}
			if (element instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot srcFolder = (IPackageFragmentRoot) element;
				return SOURCE_FOLDER_NAME.equals(srcFolder.getElementName());
			}
			if (element instanceof ICompilationUnit) {
				ICompilationUnit compilationUnit = (ICompilationUnit) element;
				perform(compilationUnit);
			}
			return true;
		}

	}

	public static final String BUILDER_ID = "org.abreslav.java2ecore.transformation.ui.builder";
	private static final String PLUGIN_ID = "org.abreslav.java2ecore.transformation.ui";

	@SuppressWarnings("unchecked")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new ResourceVisitor());
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		delta.accept(new DeltaVisitor());
	}

	private void perform(ICompilationUnit compilationUnit) throws CoreException {
		try {
			IMarker[] markers = compilationUnit.getResource().findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_ONE);
			for (IMarker marker : markers) {
				if (marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR) {
					return;
				}
			}
			TransformationPerformer.performTransformation(compilationUnit);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, e.getMessage(), e));
		} catch (RuntimeException e) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, "Exception in builder while processing " + compilationUnit.getElementName(), e));
		}
	}
	
	@Override
	protected void clean(final IProgressMonitor monitor) throws CoreException {
		IFolder modelFolder = getProject().getFolder(TransformationPerformer.MODEL_FOLDER_NAME);
		if (modelFolder.exists()) {
			modelFolder.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource instanceof IContainer) {
						return true;
					}
					resource.delete(true, monitor);
					return false;
				}
			});
		}
	}
}
