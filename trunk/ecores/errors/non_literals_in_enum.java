package errors;
import org.abreslav.java2ecore.annotations.EPackage;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class non_literals_in_enum {
	
	enum A {
		A;
		public int x;
	}
	
}
