package errors.forbidden_annotations.edatatypes;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EDataType;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class instance_type_name_on_edatatype {
	
	@InstanceTypeName("a.b.C")
	@EDataType("a.b.C")
	class A {
	}
}
