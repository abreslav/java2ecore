

import java.util.List;
import java.util.Map;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.sfeatures.Derived;
import org.abreslav.java2ecore.annotations.types.EClass;
import org.abreslav.java2ecore.annotations.types.EDataType;
import org.abreslav.java2ecore.annotations.types.NonModel;
import org.abreslav.java2ecore.multiplicities.ILowerBound;
import org.abreslav.java2ecore.multiplicities.IUpperBound;
import org.abreslav.java2ecore.multiplicities.Infinity;
import org.abreslav.java2ecore.multiplicities.MList;
import org.abreslav.java2ecore.multiplicities._4;
  
@EPackage(
		nsPrefix="some",   
		nsURI="http://sdfsad.com"
)
public class some {    
	interface I {}  

	@NonModel
	interface _239 extends IUpperBound, ILowerBound {
		
	}

	abstract interface MyIntf<T, S extends T> extends I  {
	}

	class Q<T> {	   
	} 
	   

	@EClass interface A<T extends MyIntf<T, ? extends T>> {
	class _<T extends MyIntf<T, ? extends T>> {
		List<MyIntf<Object, String>> intfs;
//		@ID List some1;
		Q<?> q;    
		int x = 0;
		int[] yy;
		Map<String, String> map;
		volatile MyIntf<MyIntf<T, ? super T>, MyIntf<T, ? super T>> y;
//		transient volatile X xx;
		MList<String, _4, Infinity> s;
	}}

	interface C extends MyIntf<C, C> {
	abstract class _{
		@Derived C a;
		abstract List<MyIntf<C, C>> a(String s, Q<? extends C> d);
	} 
	}
	    
	enum Enum {     
		A, B, C;  
	}      
	   
	@EDataType("java.lang.Exception")
	class Exception extends Throwable {		
	}
	
	@EDataType("java.lang.RuntimeException")
	class RuntimeException extends Throwable {		
	}
	
	@EDataType("java.io.IOException")
	class IOException extends Throwable {		
	}
	
	abstract @EClass interface D {
		void a() throws Exception, RuntimeException, IOException;
		<T, F extends C & D> MyIntf<T, T> t(Q<? super F> t) throws Exception;
	}
 
	@EDataType("java.util.Comparable<T>")
	interface Comparable<T> {
	}
	
	@EClass interface B<T extends MyIntf<T, ? extends T>> extends D, C, A<T> {
	class _<T extends MyIntf<T, ? extends T>> {
		Comparable<T> my;
		X<T> a;
	}   
	}

	@EDataType("javax.swing.JComponent")
	class X<T> {
	}
	
	@EPackage(
			nsPrefix="sdfsd",
			nsURI="dsfs"
	)
	interface newpack {
		class A {}
		
		class B implements C {
		
			Reader r;
		}
		
		@EDataType("java.io.Reader")
		class Reader {
			
		}
		
	}
	    
}  
