package errors.forbidden_annotations.features;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.ID;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class id_for_reference {
	class A {
		@ID
		A a;
	}
}  
