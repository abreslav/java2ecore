package errors.forbidden_annotations.eclasses;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.ImportGenModel;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class import_on_eclass {
	
	@ImportGenModel("")
	class A {
	}
}
