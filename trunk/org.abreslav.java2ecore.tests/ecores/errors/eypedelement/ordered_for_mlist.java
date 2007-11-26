package errors.eypedelement;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Ordered;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities._0;
import org.abreslav.java2ecore.multiplicities._1;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class ordered_for_mlist {
	class A {
		@Ordered(false)
		MList<A, _0, _1> x;
	}
}  
