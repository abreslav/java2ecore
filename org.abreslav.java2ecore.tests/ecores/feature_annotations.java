import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Derived;
import org.abreslav.java2ecore.annotations.sfeatures.ID;
import org.abreslav.java2ecore.annotations.sfeatures.ResolveProxies;
import org.abreslav.java2ecore.annotations.sfeatures.Unsettable;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class feature_annotations {    

	class A {
	abstract class _{
		int x;
		
		@Derived
		int y; 
		
		@ID int z;
		 
		@ResolveProxies(true)
		A a;
		
		@ResolveProxies(false)
		A c;
		
		A d;
		
		@Unsettable
		A b;
	}
	}
		 
	    
}  
