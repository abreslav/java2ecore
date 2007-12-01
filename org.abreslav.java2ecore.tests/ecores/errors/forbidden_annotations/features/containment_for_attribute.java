package errors.forbidden_annotations.features;
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
