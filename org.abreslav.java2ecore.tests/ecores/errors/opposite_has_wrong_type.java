package errors;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class opposite_has_wrong_type {
	class A {
		@Opposite("a")
		B b;
	}
	
	class B {
		int a; 
	}
}
