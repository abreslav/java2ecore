import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.DefaultValueLiteral;
import org.abreslav.java2ecore.annotations.sfeatures.NoDefaultValue;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class default_value_literal {    
 
	class A { 
	abstract class _{
		A aa = null; 

		@DefaultValueLiteral("null")
		A a = null;

		int x1 = 1;
		double x2 = 2.0;    
		float x3 = 3.0f;
		byte b1 = 1;
		short s1 = -1;
		long l1 = 1;
		char c1 = '1';   
		boolean t = true;
		String s = "abc";
		
		@DefaultValueLiteral("239")
		int xann;

		@NoDefaultValue 
		final int xannundef = 0;
		
		@DefaultValueLiteral("A")
		E e;

		E eA = E.A;
	}
	}
	
	enum E {
		A;
	}
		 
	    
}  
