import java.util.*;
public class Librarian extends Account
{
	
	//Constructor for the Librarian class
	public Librarian(String username, String password, String name) 
	{
		super(username, password, name);
		
	}

	//Prompts the librarian to modify a book's information
	public void changeBookInfo(String fieldToChange, String toChange, Book book)
	{
		if (fieldToChange.equals("author"))
		{
			book.setAuthorName(toChange);
		}
		if (fieldToChange.equals("name"))
		{
			book.setBookName(toChange);
		}
		if (fieldToChange.equals("genre"))
		{
			book.setGenre(toChange);
		}
	}

	//Adds a book to the Catalog
	public void addBook(Book book, Catalog c) 
	{
		c.addBook(book);
	}

	//Removes a book from the Catalog
	public void removeBook(Book book, Catalog c) 
	{
		c.removeBook(book);
	}
	public String saveString()
	{
		String returnString = "Librarian " + this.getUsername() + " " + this.getPassword() + " " + this.getName();
		return returnString;
	}
}	
