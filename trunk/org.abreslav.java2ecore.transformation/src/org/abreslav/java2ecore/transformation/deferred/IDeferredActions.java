package org.abreslav.java2ecore.transformation.deferred;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;

public interface IDeferredActions {

	void addAction(IAction action);
	
	void performActions(IDiagnostics diagnostics);

}
