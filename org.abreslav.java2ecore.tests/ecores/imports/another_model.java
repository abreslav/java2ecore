package imports;
import org.abreslav.java2ecore.annotations.EPackage;
  
@EPackage(
		nsPrefix="imp",   
		nsURI="http://import.com"
)
public class another_model {    

	class SampleClass {
		int x;
	}
	
	enum SampleEnum {
		E1, E2, E3;
	}
	
	abstract interface SampleInterface {
		
	}
	
}  
