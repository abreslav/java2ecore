package errors.eypedelement;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Unique;
import org.abreslav.java2ecore.multiplicities.MSet;
import org.abreslav.java2ecore.multiplicities._0;
import org.abreslav.java2ecore.multiplicities._1;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class unique_for_a_set {
	class A {
		@Unique(false)
		MSet<A, _0, _1> x;
	}
}  
