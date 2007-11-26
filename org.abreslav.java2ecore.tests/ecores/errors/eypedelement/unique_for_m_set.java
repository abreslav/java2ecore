package errors.eypedelement;
import java.util.Set;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Unique;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class unique_for_m_set {
	class A {
		@Unique(false)
		Set<A> x;
	}
}  
