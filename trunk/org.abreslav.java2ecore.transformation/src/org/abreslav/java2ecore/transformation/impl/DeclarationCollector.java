package org.abreslav.java2ecore.transformation.impl;

import org.abreslav.java2ecore.annotations.types.NonModel;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationStorage;
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
public class DeclarationCollector extends ASTVisitor {
	private final IDiagnostics myDiagnostics;
	private final IDeclarationStorage myDeclarationStorage;
	private final ITypeBinding myPackageSpecifier;
	
	public DeclarationCollector(ITypeBinding packageSpecifier, IDeclarationStorage declarationStorage, IDiagnostics diagnostics) {
		super(false);
		myPackageSpecifier = packageSpecifier;
		myDeclarationStorage = declarationStorage;
		myDiagnostics = 
			diagnostics != null 
				? diagnostics 
				: new NullDiagnostics();
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(node);
		
		// @Ignore
		if (annotatedView.isAnnotationPresent(NonModel.class)) {
			return false;
		}

		ITypeBinding type = node.resolveBinding();
		if (type == myPackageSpecifier) {
			myDeclarationStorage.addEPackage(node, EcoreFactory.eINSTANCE.createEPackage());
			return true;
		}
				
		// Subpackage
		if (annotatedView.isAnnotationPresent(org.abreslav.java2ecore.annotations.EPackage.class)) {
			node.accept(new DeclarationCollector(type, myDeclarationStorage, myDiagnostics));
			return false;
		} 

		// EDataType
		if (annotatedView.isAnnotationPresent(org.abreslav.java2ecore.annotations.types.EDataType.class)) {
			myDeclarationStorage.addEDataType(node, EcoreFactory.eINSTANCE.createEDataType());
			return false;
		}
		
		// EClass
		myDeclarationStorage.addEClass(node, EcoreFactory.eINSTANCE.createEClass());
		return false;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		myDeclarationStorage.addEEnum(node, EcoreFactory.eINSTANCE.createEEnum());
		return false;
	}
}