package org.abreslav.java2ecore.transformation.deferred;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;

public interface IAction {

	void perform(IDiagnostics diagnostics);

}
