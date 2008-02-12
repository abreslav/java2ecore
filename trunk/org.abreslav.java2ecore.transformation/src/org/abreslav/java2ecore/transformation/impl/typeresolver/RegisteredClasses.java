package org.abreslav.java2ecore.transformation.impl.typeresolver;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;

public class RegisteredClasses {

	public static ItemStorageWithStringKeys STORAGE = new ItemStorageWithStringKeys();
	static {
		importPackageContents(EcorePackage.eINSTANCE, STORAGE);
		// Some new packages may be added during a walk through
		// EPackage.Registry.INSTANCE.keySet()
		// So we have to avoid ConcurrentModificationException
		// Hack workaround (some packages will be missing)
		Set<String> nsURIs = new HashSet<String>(EPackage.Registry.INSTANCE.keySet());
		for (String nsURI : nsURIs) {
			EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(nsURI);
			importPackageContents(ePackage, STORAGE);
		}
	}
	
	private static void importPackageContents(EPackage ePackage, ItemStorageWithStringKeys storage) {
		for (EClassifier classifier : ePackage.getEClassifiers()) {
			if (classifier.getInstanceClassName() != null) {
				if (classifier instanceof EDataType) {
					storage.addEDataType(classifier.getInstanceClassName(), (EDataType) classifier);
				} else if (classifier instanceof EClass) {
					storage.addEClass(classifier.getInstanceClassName(), (EClass) classifier);
				}
			}
		}
	}


	private RegisteredClasses() {
		
	}
	
}
