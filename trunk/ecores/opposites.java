import java.util.List;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;

@EPackage(
		nsPrefix="opp",
		nsURI="opposites"
)
public class opposites {
	class A {
		@Opposite("a") 
		List<B> b;
	} 
	
	class B {
		A a; 
		C c;
	}
	
	class C {
		@Opposite("c")
		B b;
		@Opposite("c")
		D d;
	}
	
	class D {
		@Opposite("d")
		C c;
	}
}
