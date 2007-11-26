package errors.eypedelement;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Bounds;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class bounds_negative_lower_bound {
	class A {
		@Bounds({-1, 1})    
		int a;
	}
}
