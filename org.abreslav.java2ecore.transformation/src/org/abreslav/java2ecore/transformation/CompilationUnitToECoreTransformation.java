package org.abreslav.java2ecore.transformation;


import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.impl.TypeBuilder;
import org.abreslav.java2ecore.transformation.impl.TypeCollector;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CompilationUnitToECoreTransformation {

	public static EPackage transform(ICompilationUnit compilationUnit, IDiagnostics diagnostics) throws JavaModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(compilationUnit);
		parser.setResolveBindings(true);
		CompilationUnit unitAST = (CompilationUnit) parser.createAST(null);
		TypeCollector typeCollector = new TypeCollector(diagnostics);
		unitAST.accept(typeCollector);
		unitAST.accept(new TypeBuilder(typeCollector.getTypeResolver(), diagnostics, typeCollector.getEPackage()));
		return typeCollector.getEPackage();
	}

}
