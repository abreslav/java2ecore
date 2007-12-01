package errors.forbidden_annotations.eenums;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EClass;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class eclass_on_eenum {
	
	@EClass
	enum A {
	}
}
