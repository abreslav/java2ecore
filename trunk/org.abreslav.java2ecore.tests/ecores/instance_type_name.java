import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EDataType;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;

@EPackage(
		nsPrefix="a",
		nsURI="a"
)  
public class instance_type_name {
	@EDataType("a.b.C")
	class A {
	} 

	@InstanceTypeName("c.d.E")
	class B {
		
	}
}
