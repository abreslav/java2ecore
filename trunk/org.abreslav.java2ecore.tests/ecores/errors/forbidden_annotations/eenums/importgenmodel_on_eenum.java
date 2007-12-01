package errors.forbidden_annotations.eenums;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.Import;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class importgenmodel_on_eenum {
	
	@Import(importgenmodel_on_eenum.class)
	enum A {
	}
}
