package org.abreslav.java2ecore.transformation.astview;

import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;

public class ASTViewFactory {

	public static final ASTViewFactory INSTANCE = new ASTViewFactory();
	
	private ASTViewFactory() {
		
	}
	
	public AnnotationView createAnnotationView(Annotation annotation) {
		IAnnotationBinding binding = annotation.resolveAnnotationBinding();
		AnnotationView view = new AnnotationView(annotation);
		IMemberValuePairBinding[] allMemberValuePairs = binding.getAllMemberValuePairs();
		for (IMemberValuePairBinding pair : allMemberValuePairs) {
			view.getAttributes().put(pair.getName(), pair.getValue());
		}
		return view;
	}
	
	public AnnotatedView createAnnotatedView(BodyDeclaration declaration) {
		AnnotatedView view = new AnnotatedView(declaration);
		List<?> modifiers = (List<?>) declaration.getStructuralProperty(declaration.getModifiersProperty());
		
		for (Object modifier : modifiers) {
			if (modifier instanceof Annotation) {
				Annotation annotation = (Annotation) modifier;
				view.addAnnotation(createAnnotationView(annotation));
			} 
		}
		return view;
	}
}
