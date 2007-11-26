import java.util.List;

import org.abreslav.java2ecore.annotations.EPackage;
import org.abreslav.java2ecore.annotations.Import;
import org.abreslav.java2ecore.annotations.sfeatures.Opposite;

@Import(manypackages.class)
@EPackage(
	nsPrefix="library", 
	nsURI="http:///example.com/library"
)
public class library {
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
		Writer author;
		int pages = 100;
		BookCategory category;
	}

	enum BookCategory {
		Mystery,
		ScienceFiction,
		Biography
	}	
}

