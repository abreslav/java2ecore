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
		int adef;
		A fdef;
		
		@Derived
		int ader; 
		
		@Derived
		A fder; 
		
		@ID int aid;
		 
		@ResolveProxies(true)
		A fres;
		@ResolveProxies(false)
		A fnres;
		
		@Unsettable
		A funs;
		@Unsettable
		int auns;
	}
	}
		 
	    
}  
