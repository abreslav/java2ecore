import org.abreslav.java2ecore.annotations.EPackage;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class feature_modifiers {    

	class A { 
	abstract class _{
		final int x = 0;
		transient int y;
		volatile int z;
	}
	}
		
	    
}  
