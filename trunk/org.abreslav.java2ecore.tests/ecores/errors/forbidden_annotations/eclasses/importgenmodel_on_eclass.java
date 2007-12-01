package errors.forbidden_annotations.eclasses;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.Import;
import org.abreslav.java2ecore.annotations.types.EClass;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class importgenmodel_on_eclass {
	
	@Import(importgenmodel_on_eclass.class)
	@EClass
	class A {
	}
}
