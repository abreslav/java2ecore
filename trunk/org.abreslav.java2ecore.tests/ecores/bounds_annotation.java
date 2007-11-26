import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Bounds;
import org.abreslav.java2ecore.multiplicities.MCollection;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities.MSet;
import org.abreslav.java2ecore.multiplicities._1;
import org.abreslav.java2ecore.multiplicities._5;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class bounds_annotation {    

	class A {
	abstract class _{ 
		@Bounds({1, 5})
		int x;		
		
		@Bounds({1, 5})
		int[] xx;		
		
		@Bounds({1, 5})
		List<Integer> xxlist;		
		
		@Bounds({1, 5})
		Set<Integer> xxset;		
		
		@Bounds({1, 5})
		Collection<Integer> xxcoll;	
		
		@Bounds({1, 5})
		MCollection<Integer, _1, _5> mcoll;
		@Bounds({1, 5})
		MList<Integer, _1, _5> mlist;
		@Bounds({1, 5})
		MSet<Integer, _1, _5> mset;
		
		@Bounds({1, -1})
		abstract A a();
	}
	}
		
	    
}  
