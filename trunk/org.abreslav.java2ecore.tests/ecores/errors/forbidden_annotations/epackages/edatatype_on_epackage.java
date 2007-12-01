package errors.forbidden_annotations.epackages;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EDataType;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class edatatype_on_epackage {
	
	@EPackage(nsURI="", nsPrefix="")
	@EDataType("a.b.C")
	class A {
	}
}
