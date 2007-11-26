package errors.forbidden_annotations;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Containment;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class containment_for_attribute {
	class A {
		@Containment
		int a;
	}
}  
