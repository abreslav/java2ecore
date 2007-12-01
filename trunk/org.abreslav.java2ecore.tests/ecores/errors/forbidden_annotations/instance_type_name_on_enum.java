package errors.forbidden_annotations;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class instance_type_name_on_enum {
	
	@InstanceTypeName("a.b.C")
	enum A {
		X;
	}
	
}
