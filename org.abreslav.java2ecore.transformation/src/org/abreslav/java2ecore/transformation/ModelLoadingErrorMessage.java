package org.abreslav.java2ecore.transformation;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnosticMessage;
import org.abreslav.java2ecore.transformation.imports.genmodel.ModelLoadingException;

public class ModelLoadingErrorMessage implements IDiagnosticMessage {

	private final String myMessage;
	
	public ModelLoadingErrorMessage(ModelLoadingException e) {
		if (e.getCause() == null) {
			myMessage = e.getMessage();
		} else {
			myMessage = e.getCause().getMessage();
		}
	}

	@Override
	public String toString() {
		return myMessage;
	}
	
	public String format(Object... args) {
		return toString();
	}

}
