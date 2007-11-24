package org.abreslav.java2ecore.transformation.deferred;

import java.util.ArrayList;
import java.util.List;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;

public class DeferredActions implements IDeferredActions {

	private List<IAction> myActions = new ArrayList<IAction>();
	
	public void addAction(IAction action) {
		myActions.add(action);
	}

	public void performActions(IDiagnostics diagnostics) {
		for (IAction action : myActions) {
			action.perform(diagnostics);
		}
	}

}
