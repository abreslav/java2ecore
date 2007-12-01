package errors.eypedelement.bounds;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Bounds;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class single_negative_lower_bound {
	class A {
		@Bounds(-1)    
		int a;
	}
}
