package errors.forbidden_annotations;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class resolveproxies_for_attribute {
	class A {
		@Opposite("some")
		int a;
	}
}  
