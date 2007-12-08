import org.abreslav.java2ecore.annotations.EPackage;

@EPackage(
		nsPrefix="a",
		nsURI="a"
)  
public class generic_parameters_reverse_order {
	
	class A<T> {
		
	}
	
	class B<E extends A<T>, T> {
		
	}   
	
	class C {
		<E extends A<T>, T> void x() {}
	} 
	    
}
