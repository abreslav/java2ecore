package errors.forbidden_annotations.edatatypes;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.Import;
import org.abreslav.java2ecore.annotations.types.EDataType;

@EPackage( 
		nsPrefix="sdf",
		nsURI="..."
)
public class importgenmodel_on_edatatype {
	
	@Import(importgenmodel_on_edatatype.class)
	@EDataType("a.b.C")
	class A {
	}
}
