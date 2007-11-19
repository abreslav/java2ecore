package test.popup.actions;

import java.io.IOException;

import org.abreslav.java2ecore.transformation.TransformationPerformer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


public class GenerateEcoreAction implements IObjectActionDelegate {

	private ICompilationUnit myCompilationUnit;

	/**
	 * Constructor for Action1.
	 */
	public GenerateEcoreAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		performTransformation();
		Shell shell = new Shell();
		MessageDialog.openInformation(
			shell,
			"Test Plug-in",
			"Generate ECore was executed.");
	}

	private void performTransformation() {
		try {
			TransformationPerformer.performTransformation(myCompilationUnit);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		myCompilationUnit = (ICompilationUnit) ((IStructuredSelection) selection).getFirstElement();
	}

}
