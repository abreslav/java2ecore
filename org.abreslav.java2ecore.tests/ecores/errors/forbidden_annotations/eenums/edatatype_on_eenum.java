package errors.forbidden_annotations.eenums;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EDataType;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class edatatype_on_eenum {
	
	@EDataType("")
	enum A {
	}
}
