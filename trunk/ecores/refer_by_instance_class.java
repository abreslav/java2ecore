import java.io.Reader;
import java.io.Writer;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.types.EDataType;
import org.abreslav.java2ecore.annotations.types.InstanceTypeName;


@EPackage(
		nsPrefix="sdf",
		nsURI="..."
)
public class refer_by_instance_class {
	class C<T extends Reader> {
		Writer w; 
	}
	
	@InstanceTypeName("java.io.Reader")
	class X {
		
	}
	
	@EDataType("java.io.Writer")
	class Y {
		
	}
}
