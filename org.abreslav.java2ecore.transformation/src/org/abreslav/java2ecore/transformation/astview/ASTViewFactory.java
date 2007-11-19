package org.abreslav.java2ecore.transformation.astview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ASTViewFactory {

	public static final ASTViewFactory INSTANCE = new ASTViewFactory();
	
	private ASTViewFactory() {
		
	}
	
	public AnnotationView createAnnotationView(Annotation annotation) {
		IAnnotationBinding binding = annotation.resolveAnnotationBinding();
		return createAnnotationView(binding);
	}
	
	public AnnotationView createAnnotationView(IAnnotationBinding binding) {
		AnnotationView view = new AnnotationView(binding.getAnnotationType().getQualifiedName());
		IMemberValuePairBinding[] allMemberValuePairs = binding.getAllMemberValuePairs();
		for (IMemberValuePairBinding pair : allMemberValuePairs) {
			view.getAttributes().put(pair.getName(), pair.getValue());
		}
		return view;
	}
	
	public Map<String, AnnotationView> collectAnnotationViews(ITypeBinding typeBinding) {
		Map<String, AnnotationView> annotationMap = new HashMap<String, AnnotationView>();
		IAnnotationBinding[] annotations = typeBinding.getAnnotations();
		for (IAnnotationBinding annotationBinding : annotations) {
			AnnotationView view = createAnnotationView(annotationBinding);
			annotationMap.put(view.getQualifiedName(), view);
		}
		return annotationMap;
	}
	
	public TypeView createTypeView(TypeDeclaration declaration) {
		TypeView view = new TypeView(
				declaration.getName().getIdentifier(), 
				declaration.resolveBinding().getQualifiedName(), 
				declaration.isInterface());
		
		List<?> modifiers = (List<?>) declaration.getStructuralProperty(TypeDeclaration.MODIFIERS2_PROPERTY);
		
		for (Object modifier : modifiers) {
			if (modifier instanceof Annotation) {
				Annotation annotation = (Annotation) modifier;
				view.addAnnotation(createAnnotationView(annotation));
			} else if (modifier instanceof Modifier) {
				Modifier mod = (Modifier) modifier;
				if (mod.isAbstract()) {
					view.setAbstract(true);
				}
			} 
		}
		return view;
	}
}
