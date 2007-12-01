package errors.forbidden_annotations.epackages;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EClass;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class eclass_on_epackage {
	
	@EPackage(nsURI="", nsPrefix="")
	@EClass
	class A {
	}
}
