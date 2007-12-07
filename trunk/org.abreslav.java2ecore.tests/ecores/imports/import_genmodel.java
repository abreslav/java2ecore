package imports;
import imported_model.E;
import imported_model.Library;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.ImportGenModel;
import org.abreslav.java2ecore.annotations.types.EClass;

import another_model.SampleClass;
import another_model.SampleEnum;
import another_model.SampleInterface;
  
@ImportGenModel({
	"imported_model.genmodel",
	"another_model.genmodel"
}) 
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"  
)
public class import_genmodel {     
    
	@EClass interface X extends SampleInterface {     
	class _{ 
		Library l;
		E e = E.B; 
		SampleClass s;
		SampleEnum ee;
	}
	}
	    
}  
