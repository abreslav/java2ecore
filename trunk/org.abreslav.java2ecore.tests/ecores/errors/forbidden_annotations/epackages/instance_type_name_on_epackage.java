package errors.forbidden_annotations.epackages;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class instance_type_name_on_epackage {
	
	@InstanceTypeName("a.b.C")
	@EPackage(nsPrefix="",nsURI="")
	class A {
	}
}
