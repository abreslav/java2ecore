package errors;
import java.io.Reader;

import org.abreslav.java2ecore.annotations.EPackage;


@EPackage(
		nsPrefix="sdf",
		nsURI="..."
)
public class unknown_upper_bound {
	class C<T extends Reader> {
		
	}
}
