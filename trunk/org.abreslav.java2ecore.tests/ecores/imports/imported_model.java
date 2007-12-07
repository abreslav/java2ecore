package imports;
import org.abreslav.java2ecore.annotations.EPackage;
  
@EPackage(
		nsPrefix="imp",   
		nsURI="http://import.com"
)
public class imported_model {    

	class Library {
		Book[] books;
	}
	
	class Book {
		int pages;
	}
	
	enum E {
		A, B, C;
	}
	    
}  
