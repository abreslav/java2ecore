package errors.forbidden_annotations.edatatypes;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EClass;
import org.abreslav.java2ecore.annotations.types.EDataType;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class eclass_on_edatatype {
	
	@EClass
	@EDataType("a.b.C")
	class A {
	}
}
