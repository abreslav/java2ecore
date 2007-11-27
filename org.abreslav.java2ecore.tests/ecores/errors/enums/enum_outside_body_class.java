package errors.enums;
import org.abreslav.java2ecore.annotations.EPackage;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class enum_outside_body_class {
	
	static class A {
		static class a {   
		}
		enum E {
			
		}
	}
	
}
