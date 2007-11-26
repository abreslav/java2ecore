package errors.eypedelement;
import java.util.List;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Ordered;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class ordered_for_a_list {
	class A {
		@Ordered(false)
		List<A> x;
	}
}  
