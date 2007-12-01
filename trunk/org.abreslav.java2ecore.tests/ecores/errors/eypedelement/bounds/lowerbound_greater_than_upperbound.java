package errors.eypedelement.bounds;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities._1;
import org.abreslav.java2ecore.multiplicities._2;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class lowerbound_greater_than_upperbound {
	class A {
		MList<Integer, _2, _1> a;
	}
	
}
