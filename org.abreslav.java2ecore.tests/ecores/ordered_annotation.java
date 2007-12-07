import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Ordered;
import org.abreslav.java2ecore.multiplicities.MCollection;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities.MSet;
import org.abreslav.java2ecore.multiplicities._0;
import org.abreslav.java2ecore.multiplicities._5;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class ordered_annotation {    
 
	class A {
	abstract class _{ 
		int x;		
		@Ordered
		int ordx;		
		@Ordered(false) 
		int nordx;		
		
		int[] xarr;
		@Ordered
		int[] ordxarr;		
		@Ordered(false)
		int[] nordxarr;		
		
		Collection<Integer> xxcoll;		
		@Ordered
		Collection<Integer> ordxxcoll;		
		@Ordered(false)
		Collection<Integer> nordxxcoll;		
		
		List<Integer> xxlist;		
		@Ordered
		List<Integer> ordxxlist;		
		
		Set<Integer> xxset;		
		@Ordered
		Set<Integer> ordxxset;		
		@Ordered(false)
		Set<Integer> nordxxset;		
		
		MCollection<Integer, _0, _5> mcoll;		
		@Ordered
		MCollection<Integer, _0, _5> ordmcoll;		
		@Ordered(false)
		MCollection<Integer, _0, _5> nordmcoll;		
		
		MList<Integer, _0, _5> mlist;		
		@Ordered
		MList<Integer, _0, _5> ordmlist;		
		
		MSet<Integer, _0, _5> mset;		
		@Ordered
		MSet<Integer, _0, _5> ordmset;		
		@Ordered(false)
		MSet<Integer, _0, _5> nordmset;		
		
		abstract A a();
		@Ordered
		abstract A orda();
		@Ordered(false)
		abstract A norda();

		abstract A ap(int x);
		abstract A apord(@Ordered int x);
		abstract A apnord(@Ordered(false) int x);
	}
	}
		
	    
}  
