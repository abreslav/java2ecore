package errors.eypedelement.bounds;
import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Bounds;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities._0;
import org.abreslav.java2ecore.multiplicities._1;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public class single_bounds_for_m_collection {
	class A {
		@Bounds(1)
		MList<Integer, _0, _1> a;
	}
}
