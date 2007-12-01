package errors.eypedelement.bounds;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Bounds;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class bounds_with_less_values {
	class A {
		@Bounds({1})
		int a;
	}
}
