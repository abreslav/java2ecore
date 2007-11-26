package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.resource.Resource;

/*package*/ class MyTypedElement implements ETypedElement {

	private EGenericType myGenericType;
	private int myLowerBound;
	private int myUpperBound;
	private boolean myOrdered;
	private boolean myUnique;

	public EGenericType getEGenericType() {
		return myGenericType;
	}

	public EClassifier getEType() {
		throw new UnsupportedOperationException();
	}

	public int getLowerBound() {
		return myLowerBound;
	}

	public int getUpperBound() {
		return myUpperBound;
	}

	public boolean isMany() {
		throw new UnsupportedOperationException();
	}

	public boolean isOrdered() {
		return myOrdered;
	}

	public boolean isRequired() {
		throw new UnsupportedOperationException();
	}

	public boolean isUnique() {
		return myUnique;
	}

	public void setEGenericType(EGenericType value) {
		myGenericType = value;
	}

	public void setEType(EClassifier value) {
		throw new UnsupportedOperationException();
	}

	public void setLowerBound(int value) {
		myLowerBound = value;
	}

	public void setOrdered(boolean value) {
		myOrdered = value;
	}

	public void setUnique(boolean value) {
		myUnique = value;
	}

	public void setUpperBound(int value) {
		myUpperBound = value;
	}

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(String value) {
		throw new UnsupportedOperationException();
	}

	public EAnnotation getEAnnotation(String source) {
		throw new UnsupportedOperationException();
	}

	public EList<EAnnotation> getEAnnotations() {
		throw new UnsupportedOperationException();
	}

	public TreeIterator<EObject> eAllContents() {
		throw new UnsupportedOperationException();
	}

	public EClass eClass() {
		throw new UnsupportedOperationException();
	}

	public EObject eContainer() {
		throw new UnsupportedOperationException();
	}

	public EStructuralFeature eContainingFeature() {
		throw new UnsupportedOperationException();
	}

	public EReference eContainmentFeature() {
		throw new UnsupportedOperationException();
	}

	public EList<EObject> eContents() {
		throw new UnsupportedOperationException();
	}

	public EList<EObject> eCrossReferences() {
		throw new UnsupportedOperationException();
	}

	public Object eGet(EStructuralFeature feature) {
		throw new UnsupportedOperationException();
	}

	public Object eGet(EStructuralFeature feature, boolean resolve) {
		throw new UnsupportedOperationException();
	}

	public boolean eIsProxy() {
		throw new UnsupportedOperationException();
	}

	public boolean eIsSet(EStructuralFeature feature) {
		throw new UnsupportedOperationException();
	}

	public Resource eResource() {
		throw new UnsupportedOperationException();
	}

	public void eSet(EStructuralFeature feature, Object newValue) {
		throw new UnsupportedOperationException();
	}

	public void eUnset(EStructuralFeature feature) {
		throw new UnsupportedOperationException();
	}

	public EList<Adapter> eAdapters() {
		throw new UnsupportedOperationException();
	}

	public boolean eDeliver() {
		throw new UnsupportedOperationException();
	}

	public void eNotify(Notification notification) {
		throw new UnsupportedOperationException();
	}

	public void eSetDeliver(boolean deliver) {
		throw new UnsupportedOperationException();
	}
}
