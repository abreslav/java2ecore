package errors.forbidden_annotations.edatatypes;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.ImportGenModel;
import org.abreslav.java2ecore.annotations.types.EDataType;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class import_on_edatatype {
	
	@ImportGenModel("")
	@EDataType("a.b.C")
	class A {
	}
}
