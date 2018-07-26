import java.util.*;
public class Catalog
{
	public ArrayList<Book> library;
	
	//Constructor for the Catalog class
	public Catalog() 
	{
		library = new ArrayList<Book>();
	}

	//Getter for the library
	public ArrayList<Book> getCheckedOutBooks()
	{
		ArrayList<Book> results = new ArrayList<>();
		for (Book b : this.library)
		{
			if (!b.isAvailable())
			{
				results.add(b);
			}
		}
		return results;
	}
	public ArrayList<Book> getLibrary()
	{
		return this.library;
	}
	//Adds a book to the Catalog's library instance variable
	public void addBook(Book book) 
	{
		library.add(book);
	}

	//Removes a book from the Catalog's library instance variable
	public void removeBook(Book book) 
	{
		library.remove(book);
	}
	//Prints the books in alphabetical order
	public void printCatalog()
	{
		//TODO
	}
	public Book getByISBN(String isbn)
	{
		isbn = isbn.toLowerCase();
		for (Book b : this.library)
		{
			if (b.getIsbnNumber().toLowerCase().equals(isbn))
			{
				return b;
			}
		}
		return null;
	}
	//Don't use searchBy___ functions. They return the first result, so authors with multiple books / books with the same name will still only return one result. Keeping the methods in the system in case we need that specific functionality later.
	public Book searchByAuthorName(String authorName)
	{
		authorName = authorName.toLowerCase();
		for (Book b : this.library)
		{
			if (b.getAuthorName().toLowerCase().equals(authorName))
			{
				return b;
			}
		}
		return null;
	}
	public Book searchByBookName(String bookName)
	{
		bookName = bookName.toLowerCase();
		for (Book b : this.library)
		{
			if (b.getBookName().toLowerCase().equals(bookName))
			{
				return b;
			}
		}
		return null;
	}
	public Book searchByGenre(String genre)
	{
		genre = genre.toLowerCase();
		for (Book b : this.library)
		{
			if (b.getGenre().toLowerCase().equals(genre))
			{
				return b;
			}
		}
		return null;
	}
	//Use this search function.
	public ArrayList<Book> search(String term)
	{
		ArrayList<Book> results = new ArrayList<>();
		for (Book b : this.library)
		{
			if (b.getAuthorName().equals(term) || b.getBookName().equals(term) || b.getIsbnNumber().equals(term) || b.getGenre().equals(term))
			{
				results.add(b);
			}
		}
		return results;
	}
	public String saveString()
	{
		String returnString = "";
		for (Book b : this.library)
		{
			returnString += b.saveString() + "\n";
		}
		return returnString;
	}
}	
