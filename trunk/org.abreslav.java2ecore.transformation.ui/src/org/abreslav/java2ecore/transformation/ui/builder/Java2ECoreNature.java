package org.abreslav.java2ecore.transformation.ui.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.abreslav.java2ecore.transformation.VariableResolver;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class Java2ECoreNature implements IProjectNature {

	public static final String PLUGIN_ID = "org.abreslav.java2ecore.transformation.ui";
	/**
	 * ID of this project nature
	 */
	public static final String NATURE_ID = PLUGIN_ID + ".nature";

	private IProject project;

	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Java2ECoreBuilder.BUILDER_ID)) {
				return;
			}
		}

		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		ICommand command = desc.newCommand();
		command.setBuilderName(Java2ECoreBuilder.BUILDER_ID);
		newCommands[newCommands.length - 1] = command;
		desc.setBuildSpec(newCommands);
		
		project.setDescription(desc, null);

		IJavaProject javaProject = JavaCore.create(project);
		List<IStatus> errors = new ArrayList<IStatus>();
		try {
			createClasspathEntryForOurSourceFolder(javaProject);
		} catch (CoreException e) {
			reportError(e, "Error while adding a source folder");
			errors.add(e.getStatus());
		}
		try {
			createClasspathEntryForOurJar(javaProject);
		} catch (CoreException e) {
			reportError(e, "Error while adding java2ecore.jar to classpath");
			errors.add(e.getStatus());
		}
		if (!errors.isEmpty()) {
			MultiStatus multiStatus = new MultiStatus(
					PLUGIN_ID, 0, 
					errors.toArray(new IStatus[errors.size()]), 
					"Errors occurred while configuring the project",
					null);
			throw new CoreException(multiStatus);
		}
	}

	private void reportError(CoreException e, String message) {
		Shell shell = new Shell();
		MessageDialog.openError(shell, message, e.getMessage());
	}

	private void createClasspathEntryForOurSourceFolder(IJavaProject javaProject) throws CoreException {
		IFolder ourSourceFolder = project.getFolder(Java2ECoreBuilder.SOURCE_FOLDER_NAME);
		if (!ourSourceFolder.exists()) {
			ourSourceFolder.create(true, false, null);
		}
		IJavaElement javaSourceFolder = JavaCore.create(ourSourceFolder);
		if (javaSourceFolder == null) {
			IClasspathEntry sourceEntry = JavaCore.newSourceEntry(ourSourceFolder.getFullPath());
			extendClasspath(javaProject, sourceEntry);
		}
	}

	private void createClasspathEntryForOurJar(IJavaProject javaProject) throws JavaModelException {
		IClasspathEntry[] classpath = javaProject.getRawClasspath();
		IPath path = Path.fromPortableString(VariableResolver.VARIABLE);
		for (IClasspathEntry classpathEntry : classpath) {
			if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_VARIABLE
					&& path.equals(classpathEntry.getPath())) {
				return;
			}
		}
		IClasspathEntry variableEntry = JavaCore.newVariableEntry(path, path, null);
		extendClasspath(javaProject, variableEntry);
	}

	private void extendClasspath(IJavaProject javaProject, IClasspathEntry newEntry)
			throws JavaModelException {
		List<IClasspathEntry> classpath = new ArrayList<IClasspathEntry>(Arrays.asList(javaProject.getRawClasspath()));
		ListIterator<IClasspathEntry> it = classpath.listIterator();
		// Add new entry after all the entries of that kind
		if (it.hasNext()) {
			IClasspathEntry entry;
			do {
				entry = it.next();
			} while (it.hasNext() && (entry.getEntryKind() != newEntry.getEntryKind()));
			while (it.hasNext() && (entry.getEntryKind() == newEntry.getEntryKind())) {
				entry = it.next();
			}
			it.previous();
		}
		it.add(newEntry);
		javaProject.setRawClasspath(classpath.toArray(new IClasspathEntry[classpath.size()]), null);
	}

	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Java2ECoreBuilder.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
						commands.length - i - 1);
				description.setBuildSpec(newCommands);
				return;
			}
		}
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
