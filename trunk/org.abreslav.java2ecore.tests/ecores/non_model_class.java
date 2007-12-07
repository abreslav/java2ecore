

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.NonModel;
import org.abreslav.java2ecore.multiplicities.ILowerBound;
import org.abreslav.java2ecore.multiplicities.IUpperBound;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities._0;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class non_model_class {    
 
	@NonModel
	interface _239 extends IUpperBound, ILowerBound {
	}


	class A {
		MList<Integer, _0, _239> ints;		
	}
		
	    
}   
