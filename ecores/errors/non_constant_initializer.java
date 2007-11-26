package errors;
import org.abreslav.java2ecore.annotations.EPackage;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class non_constant_initializer {
	
	class A {
		int y;
		int x = y;
	}
	
}
