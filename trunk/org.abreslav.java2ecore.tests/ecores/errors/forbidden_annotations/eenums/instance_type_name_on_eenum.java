package errors.forbidden_annotations.eenums;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class instance_type_name_on_eenum {
	
	@InstanceTypeName("a.b.C")
	enum A {
	}
}
