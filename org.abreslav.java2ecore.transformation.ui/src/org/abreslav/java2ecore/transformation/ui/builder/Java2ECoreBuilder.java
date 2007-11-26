package org.abreslav.java2ecore.transformation.ui.builder;

import java.io.IOException;
import java.util.Map;

import org.abreslav.java2ecore.transformation.TransformationPerformer;
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
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

public class Java2ECoreBuilder extends IncrementalProjectBuilder {

	class DeltaVisitor implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			IJavaElement element = JavaCore.create(resource);
			if (element == null) {
				return false;
			}
			if (element instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot srcFolder = (IPackageFragmentRoot) element;
				return "ecores".equals(srcFolder.getElementName());
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
				return "ecores".equals(srcFolder.getElementName());
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
			TransformationPerformer.performTransformation(compilationUnit);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, e.getMessage(), e));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
