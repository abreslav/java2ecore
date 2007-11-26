package errors.default_value_literal;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.DefaultValueLiteral;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class initializer_and_annotation {
	class A {
		@DefaultValueLiteral("1")
		int a = 1;
	}
}
