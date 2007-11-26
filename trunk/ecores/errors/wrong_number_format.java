package errors;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.NonModel;
import org.abreslav.java2ecore.multiplicities.IUpperBound;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities._2;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class wrong_number_format {
	
	@NonModel
	interface _abc extends IUpperBound {
		
	}
	
	class A {
		MList<Integer, _2, _abc> a;
	}
	
}
