package errors.opposite;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class opposite_already_has_another {
	class A {
		@Opposite("a")
		B c;
		@Opposite("a")
		B b;
	}
	
	class B {
		A a;
	}
}
