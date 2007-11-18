package org.abreslav.java2ecore.transformation;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;

public class TypeResolverFactory {

	public static IWritableTypeResolver createTypeReolver() {
		TypeResolver resolver = new TypeResolver();
		EList<EClassifier> classifiers = EcorePackage.eINSTANCE.getEClassifiers();
		for (EClassifier classifier : classifiers) {
			if (classifier instanceof EDataType) {
				EDataType dataType = (EDataType) classifier;
				resolver.addEDataType(dataType.getInstanceClassName(), dataType);
			}
		}
		return resolver;
	}
}
