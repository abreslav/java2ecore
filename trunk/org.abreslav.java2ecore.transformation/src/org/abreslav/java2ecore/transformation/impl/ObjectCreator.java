package org.abreslav.java2ecore.transformation.impl;

import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationCollector;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.diagnostics.NullDiagnostics;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Traverses source to discover EPackages, EClasses, EDataTypes and EEnums
 * @author abreslav
 *
 */
public class ObjectCreator extends ASTVisitor {
	private final IDiagnostics myDiagnostics;
	private final IDeclarationCollector myDeclarationCollector;
	private final ITypeBinding myPackageSpecifier;
	
	public ObjectCreator(ITypeBinding packageSpecifier, IDeclarationCollector declarationCollector, IDiagnostics diagnostics) {
		super(false);
		myPackageSpecifier = packageSpecifier;
		myDeclarationCollector = declarationCollector;
		myDiagnostics = 
			diagnostics != null 
				? diagnostics 
				: new NullDiagnostics();
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding type = node.resolveBinding();
		if (type == myPackageSpecifier) {
			myDeclarationCollector.addEPackage(node, EcoreFactory.eINSTANCE.createEPackage());
			return true;
		}
		
		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(node);
		
		// Subpackage
		if (annotatedView.isAnnotationPresent(org.abreslav.java2ecore.annotations.EPackage.class)) {
			node.accept(new ObjectCreator(type, myDeclarationCollector, myDiagnostics));
			return false;
		} 

		// EDataType
		if (annotatedView.isAnnotationPresent(org.abreslav.java2ecore.annotations.types.EDataType.class)) {
			myDeclarationCollector.addEDataType(node, EcoreFactory.eINSTANCE.createEDataType());
			return false;
		}
		
		// EClass
		myDeclarationCollector.addEClass(node, EcoreFactory.eINSTANCE.createEClass());
		return false;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		myDeclarationCollector.addEEnum(node, EcoreFactory.eINSTANCE.createEEnum());
		return false;
	}
}