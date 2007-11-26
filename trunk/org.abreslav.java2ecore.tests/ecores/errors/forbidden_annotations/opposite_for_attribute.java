package errors.forbidden_annotations;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class opposite_for_attribute {
	class A {
		@ResolveProxies
		int a;
	}
}  
