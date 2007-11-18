package org.abreslav.java2ecore.transformation;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeBuilder extends ASTVisitor {

	private final ITypeResolver myTypeResolver;
	private final Set<Diagnostic> myDiagnostics;
	private final EPackage myEPackage;

	public TypeBuilder(ITypeResolver typeResolver, Set<Diagnostic> diagnostics,
			EPackage package1) {
		super();
		myTypeResolver = typeResolver;
		myDiagnostics = diagnostics;
		myEPackage = package1;
	}


	@Override
	public boolean visit(TypeDeclaration node) {
		EClass eClass = myTypeResolver.getEClass(node.resolveBinding().getQualifiedName());
		if (eClass == null) {
			return false;
		}
		node.accept(new MemberBuilder(eClass, myTypeResolver, myDiagnostics, myEPackage.getEClassifiers()));
		return false;
	}
}
