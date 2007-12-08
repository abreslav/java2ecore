package errors;
import java.io.Reader;

import org.abreslav.java2ecore.annotations.EPackage;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class unknown_superclass {
	class A { 
		Reader r; 
	} 
	  
	abstract class B extends Reader {
		
	}
	
	class C<T extends Reader> {
		
	}
}
