package errors.enums;
import org.abreslav.java2ecore.annotations.EPackage;

@EPackage( 
		nsPrefix="sdf",
		nsURI="a"
)
public interface enum_default_value_fake {
	class X {
		E a = EE.X;
	}
	enum E {
		A;
	}
	class EE {
		public static final E X = E.A;
	}
}
