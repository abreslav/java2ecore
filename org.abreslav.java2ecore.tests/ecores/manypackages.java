import org.abreslav.java2ecore.annotations.EPackage;

@EPackage(
		nsPrefix="a",
		nsURI="a"
)  
public class manypackages {
	@EPackage(
			nsPrefix="a",
			nsURI="a"
	)   
	interface a { 
		class A {}
		@EPackage(
				nsPrefix="a",
				nsURI="a"
		)  
		interface b { 
			class A {}
			
		}
		@EPackage(
				nsPrefix="a",
				nsURI="a"
		)  
		interface c { 
			class A {}
			
		}
		
	}
	@EPackage(
			nsPrefix="a",
			nsURI="a"
	)  
	interface b { 
		class A {}
		
	}

}
