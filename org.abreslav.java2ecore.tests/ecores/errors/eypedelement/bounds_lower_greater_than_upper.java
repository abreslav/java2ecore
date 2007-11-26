package errors.eypedelement;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Bounds;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class bounds_lower_greater_than_upper {
	class A {
		@Bounds({2, 1})    
		int a;
	}
}
