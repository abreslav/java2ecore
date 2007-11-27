import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Unique;
import org.abreslav.java2ecore.multiplicities.MCollection;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities.MSet;
import org.abreslav.java2ecore.multiplicities._0;
import org.abreslav.java2ecore.multiplicities._5;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class unique_annotation {    

	class A {
	abstract class _{ 
		int x;		
		@Unique
		int unx;		
		@Unique(false) 
		int nunx;		
		
		int[] xarr;
		@Unique
		int[] unxarr;		
		@Unique(false)
		int[] nunxarr;		
		
		Collection<Integer> xxcoll;		
		@Unique
		Collection<Integer> unxxcoll;		
		@Unique(false)
		Collection<Integer> nunxxcoll;		
		
		List<Integer> xxlist;		
		@Unique
		List<Integer> unxxlist;		
		@Unique(false)
		List<Integer> nunxxlist;		
		
		Set<Integer> xxset;		
		@Unique
		Set<Integer> unxxset;		
		
		MCollection<Integer, _0, _5> mcoll;		
		@Unique
		MCollection<Integer, _0, _5> unmcoll;		
		@Unique(false)
		MCollection<Integer, _0, _5> nunmcoll;		
		
		MList<Integer, _0, _5> mlist;		
		@Unique 
		MList<Integer, _0, _5> unmlist;		
		@Unique(false) 
		MList<Integer, _0, _5> nunmlist;		
		
		MSet<Integer, _0, _5> mset;		
		@Unique
		MSet<Integer, _0, _5> unmset;		
		
		abstract A a();
		@Unique
		abstract A una();
		@Unique(false)
		abstract A nuna();

		abstract A ap(int x);
		abstract A apun(@Unique int x);
		abstract A apnun(@Unique(false) int x);
	}
	}
		
	    
}  
