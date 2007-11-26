package errors.default_value_literal;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.DefaultValueLiteral;
import org.abreslav.java2ecore.annotations.sfeatures.NoDefaultValue;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class nodv_and_dvl {
	class A {
		@NoDefaultValue
		@DefaultValueLiteral("1")
		A a;
	}
}
