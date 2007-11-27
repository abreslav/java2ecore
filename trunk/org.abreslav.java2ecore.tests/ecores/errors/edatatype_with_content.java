package errors;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EDataType;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class edatatype_with_content {
	@EDataType("")
	class A {
		int a[];
		void a() {}
		class B {
			
		}
	}
}
