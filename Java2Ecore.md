# What is Java2Ecore? #

Java2Ecore is a translator from a subset of Java language to [EMF Ecore](http://www.eclipse.org/emf).

The main idea is that you can simply define some classes in Java and they will be transformed to an Ecore model.

# Advantages #

  * **Eclipse Java editor** is used for entering your code, this gives you all of it's features:
    * type checking
    * code completion
    * outline
    * on-line JavaDoc for Java2Ecore annotations
    * and so on...
  * **Full support for generics** - actually at first this was the main goal.
    * ll the generic-related things supported both in Java and EMF are supported by Java2Ecore.
  * **Clear syntax** - most constructs are expressed in very natural manner.
    * Unfortunately, there are some [exceptions](Problems.md).
See [New and noteworthy](NewAndNoteworthy.md) for more details.

# Example #
Let's have a look at the simplest example from Eclipse Help, [A Library](http://help.eclipse.org/help32/index.jsp?topic=/org.eclipse.emf.doc/tutorials/clibmod/clibmod.html):

![http://help.eclipse.org/help32/topic/org.eclipse.emf.doc/tutorials/clibmod/images/model.gif](http://help.eclipse.org/help32/topic/org.eclipse.emf.doc/tutorials/clibmod/images/model.gif)
```
@EPackage(
	nsPrefix = "library", 
	nsURI = "http:///example.com/library"
)
public interface library {

	class Library {
		String name;
		List<Writer> writers;
		List<Book> books;
	}

	class Writer {
		String name;
		@Opposite("author")
		List<Book> books;
	}

	class Book {
		String title;
		@Bounds(1)
		Writer author;
		int pages;
		BookCategory category = BookCategory.Mystery;
	}

	enum BookCategory {
		Mystery,
		ScienceFiction,
		Biography
	}
}
```
If you like it even a little bit, please, proceed to [Syntax](Syntax.md) page. :)

# Disadvantages #

  * Java2Ecore uses **JDT DOM** and thus can hardly work out of Eclipse
  * Some Ecore notions cannot be expressed in Java (see [Unsupported features](Problems.md))

# See also #
  * [Syntax](Syntax.md) description
  * User manual for [Eclipse integration](Integration.md) installation and use
  * [Problems](Problems.md), unsupported and not implemented features