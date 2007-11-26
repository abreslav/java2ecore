import java.math.BigInteger;

import org.abreslav.java2ecore.annotations.EPackage;
import org.eclipse.emf.ecore.EClass;

@EPackage(
		nsPrefix="a",
		nsURI="a"
)  
public class use_ecore_classes {
	class A {
		EClass e;
		int x;
		Integer y;
		BigInteger z;
	}
	
	@org.abreslav.java2ecore.annotations.types.EClass interface B extends org.eclipse.emf.ecore.EPackage {
		
	}
}
