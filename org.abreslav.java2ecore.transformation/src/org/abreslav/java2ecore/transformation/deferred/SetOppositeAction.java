package org.abreslav.java2ecore.transformation.deferred;

import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.ASTNode;

public class SetOppositeAction implements IAction {

	private final EReference myEReference;
	private final ASTNode myNode;
	private final EClass myFeatureType;
	private final String myOppositeFeatureName;
	private boolean myAuto;

	public SetOppositeAction(EReference eReference, ASTNode node, 
			EClass featureType, String oppositeFeatureName, boolean auto) {
		myEReference = eReference;
		myNode = node;
		myFeatureType = featureType;
		myOppositeFeatureName = oppositeFeatureName;
		myAuto = auto;
	}

	public void perform(IDiagnostics diagnostics) {
		EStructuralFeature oppositeFeature = myFeatureType.getEStructuralFeature(myOppositeFeatureName);
		if (oppositeFeature == null) {
			diagnostics.reportError("Opposite feature not found", myNode);
			return;
		}
		if (!oppositeFeature.getEType().equals(myEReference.getEContainingClass())) {
			diagnostics.reportError("Opposite feature has wrong type", myNode);
			return;
		}
		EReference oppositeReference = (EReference) oppositeFeature;
		myEReference.setEOpposite(oppositeReference);
		if (myAuto) {
			oppositeReference.setEOpposite(myEReference);
		}
	}
}
