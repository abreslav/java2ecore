/**
 * 
 */
package org.abreslav.java2ecore.transformation.impl;

import org.abreslav.java2ecore.annotations.types.Class;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotationView;
import org.abreslav.java2ecore.transformation.astview.TypeView;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.diagnostics.NullDiagnostics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class TypeCollector extends ASTVisitor {
	private EPackage myEPackage;
	private final IDiagnostics myDiagnostics;
	private final IWritableTypeResolver myTypeResolver = new TypeResolver();
	
	public TypeCollector(IDiagnostics diagnostics) {
		super(false);
		myDiagnostics = diagnostics != null ? diagnostics : new NullDiagnostics();
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		TypeView type = ASTViewFactory.INSTANCE.createTypeView(node);
		AnnotationView annotation = type.getAnnotation(org.abreslav.java2ecore.annotations.EPackage.class.getCanonicalName());
		if (annotation != null) {
			myEPackage = EcoreFactory.eINSTANCE.createEPackage();
			myEPackage.setName(type.getSimpleName());
			myEPackage.setNsPrefix((String) annotation.getAttribute("nsPrefix"));
			myEPackage.setNsURI((String) annotation.getAttribute("nsURI"));
			// TODO: subpackages are not processed!!!
			return false;
		} 
		
		if (myEPackage == null) {
			myDiagnostics.reportError("EPackage must be specified first", node);
			return false;
		}
		
		createEClassifier(type, node);
		return false;
	}

	private void createEClassifier(TypeView type, TypeDeclaration node) {
		assert myEPackage != null;
		
		EClassifier eClassifier;
		AnnotationView eDataTypeAnnotation = type.getAnnotation(org.abreslav.java2ecore.annotations.types.EDataType.class.getCanonicalName());
		if (eDataTypeAnnotation != null) {
			EDataType eDataType = EcoreFactory.eINSTANCE.createEDataType();
			myTypeResolver.addEDataType(node.resolveBinding(), eDataType);
			eClassifier = eDataType;
			
			eDataType.setInstanceTypeName((String) eDataTypeAnnotation.getAttribute("value"));
		} else {
			EClass eClass = EcoreFactory.eINSTANCE.createEClass();
			myTypeResolver.addEClass(node.resolveBinding(), eClass);
			eClassifier = eClass;
			AnnotationView instanceTypeName = type.getAnnotation(InstanceTypeName.class.getCanonicalName());
			if (instanceTypeName != null) {
				eClassifier.setInstanceTypeName((String) instanceTypeName.getAttribute("value"));
			}

			eClass.setAbstract(type.isAbstract());
			
			AnnotationView classAnnotation = type.getAnnotation(Class.class.getCanonicalName());
			if ((classAnnotation == null) || !type.isInterface()) {
				eClass.setInterface(type.isInterface());
			} else {
				eClass.setInterface(false);
			}
		}
		eClassifier.setName(type.getSimpleName());
		
		myEPackage.getEClassifiers().add(eClassifier);
	}
	
	public EPackage getEPackage() {
		return myEPackage;
	}
	
	public IWritableTypeResolver getTypeResolver() {
		return myTypeResolver;
	}
}