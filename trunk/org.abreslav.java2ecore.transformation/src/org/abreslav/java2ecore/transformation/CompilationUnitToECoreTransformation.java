package org.abreslav.java2ecore.transformation;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.abreslav.java2ecore.annotations.types.NonModel;
import org.abreslav.java2ecore.transformation.astview.ASTViewFactory;
import org.abreslav.java2ecore.transformation.astview.AnnotatedView;
import org.abreslav.java2ecore.transformation.declarations.DeclarationStorage;
import org.abreslav.java2ecore.transformation.declarations.EClassDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EDataTypeDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EEnumDeclaration;
import org.abreslav.java2ecore.transformation.declarations.EPackageDeclaration;
import org.abreslav.java2ecore.transformation.declarations.IDeclaration;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationStorage;
import org.abreslav.java2ecore.transformation.declarations.IDeclarationVisitor;
import org.abreslav.java2ecore.transformation.deferred.DeferredActions;
import org.abreslav.java2ecore.transformation.deferred.IDeferredActions;
import org.abreslav.java2ecore.transformation.diagnostics.IDiagnostics;
import org.abreslav.java2ecore.transformation.impl.ItemStorage;
import org.abreslav.java2ecore.transformation.impl.DeclarationCollector;
import org.abreslav.java2ecore.transformation.impl.ContentBuilder;
import org.abreslav.java2ecore.transformation.impl.TypeResolver;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import static org.abreslav.java2ecore.transformation.impl.DiagnosticMessages.*;

public class CompilationUnitToECoreTransformation {

	public static EPackage transform(ICompilationUnit compilationUnit, IDiagnostics diagnostics) throws JavaModelException {
		CompilationUnit unitAST = createAST(compilationUnit);
		
		@SuppressWarnings("unchecked")
		List<AbstractTypeDeclaration> types = (List<AbstractTypeDeclaration>) unitAST.getStructuralProperty(CompilationUnit.TYPES_PROPERTY);

		if (types.isEmpty()) {
			diagnostics.reportError(unitAST, NO_PACKAGE_CLASS);
			return null;
		}

		AbstractTypeDeclaration firstType = types.get(0);
		if (types.size() > 1) {
			List<AbstractTypeDeclaration> otherTypes = types.subList(1, types.size());
			for (AbstractTypeDeclaration abstractTypeDeclaration : otherTypes) {
				diagnostics.reportError(abstractTypeDeclaration, ONE_TOPLEVEL_CLASS_PER_PACKAGE);
			}
		}
		
		IDeclarationStorage declarationStorage = new DeclarationStorage();
		EPackage rootEPackage = null;
		
		AnnotatedView annotatedView = ASTViewFactory.INSTANCE.createAnnotatedView(firstType);
		if (annotatedView.isAnnotationPresent(org.abreslav.java2ecore.annotations.EPackage.class)) {
			if (annotatedView.isAnnotationPresent(NonModel.class)) {
				diagnostics.reportError(annotatedView.getAnnotation(NonModel.class).getAnnotation(), ROOT_PACKAGE_TYPE_CANNOT_BE_NON_MODEL);
			}
			firstType.accept(new DeclarationCollector(firstType.resolveBinding(), declarationStorage, diagnostics));
		} else {
			diagnostics.reportError(firstType.getName(), TOP_LEVEL_TYPE_MUST_BE_EPACKAGE);
			return null;
		}

		Collection<EClassifier> unknownTypes = new ArrayList<EClassifier>();
		IItemStorage itemStorage = new ItemStorage(declarationStorage);
		ITypeResolver typeResolver = new TypeResolver(itemStorage, unknownTypes);
		rootEPackage = buildCollectedItems(diagnostics, typeResolver, declarationStorage);

		if (rootEPackage != null) {
			rootEPackage.getEClassifiers().addAll(unknownTypes);
		}
		return rootEPackage;
	}

	private static EPackage buildCollectedItems(IDiagnostics diagnostics,
			ITypeResolver typeResolver, IDeclarationStorage declarationStorage) {
		final EPackage[] rootEPackage = new EPackage[1];
		IDeferredActions deferredActions = new DeferredActions();
		final ContentBuilder typeBuilder = new ContentBuilder(typeResolver, diagnostics, deferredActions);

		for (final IDeclaration declaration : declarationStorage.getDeclarations()) {
			declaration.accept(new IDeclarationVisitor() {
				public void visit(EClassDeclaration declaration) {
					typeBuilder.buildEClass(declaration.getDeclaration(), declaration.getDeclaredElement());
				}

				public void visit(EDataTypeDeclaration declaration) {
					typeBuilder.buildEDataType(declaration.getDeclaration(), declaration.getDeclaredElement());
				}

				public void visit(EEnumDeclaration declaration) {
					typeBuilder.buildEEnum(declaration.getDeclaration(), declaration.getDeclaredElement());
				}

				public void visit(EPackageDeclaration declaration) {
					if (rootEPackage[0] == null) {
						rootEPackage[0] = declaration.getDeclaredElement();
					}
					typeBuilder.buildEPackage(declaration.getDeclaration(), declaration.getDeclaredElement());
				}
			});
		}
		deferredActions.performActions(diagnostics);
		return rootEPackage[0];
	}

	private static CompilationUnit createAST(ICompilationUnit compilationUnit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(compilationUnit);
		parser.setResolveBindings(true);
		CompilationUnit unitAST = (CompilationUnit) parser.createAST(null);
		return unitAST;
	}

}
