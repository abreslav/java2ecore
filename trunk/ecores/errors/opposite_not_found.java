package errors;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class opposite_not_found {
	class A {
		@Opposite("a")
		B b;
	}
	
	class B {
		
	}
}
