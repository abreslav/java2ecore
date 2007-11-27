package org.abreslav.java2ecore.transformation.impl.deferred;

import org.abreslav.java2ecore.transformation.deferred.IAction;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.ASTNode;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.*;

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
			diagnostics.reportError(myNode, OPPOSITE_FEATURE_NOT_FOUND);
			return;
		}
		if (!oppositeFeature.getEType().equals(myEReference.getEContainingClass())) {
			diagnostics.reportError(myNode, OPPOSITE_FEATURE_HAS_WRONG_TYPE);
			return;
		}
		EReference oppositeReference = (EReference) oppositeFeature;
		myEReference.setEOpposite(oppositeReference);
		if (myAuto) {
			if (oppositeReference.getEOpposite() != myEReference && oppositeReference.getEOpposite() != null) {
				diagnostics.reportError(myNode, OPPOSITE_ALREADY_HAS_ANOTHER_OPPOSITE);
			}
			oppositeReference.setEOpposite(myEReference);
		}
	}
}
