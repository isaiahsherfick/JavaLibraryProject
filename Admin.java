import java.util.*;
public class Admin extends Account
{

	//Constructor for the Account class
	public Admin(String username, String password, String name) 
	{
		super(username, password, name);
	//	c = library name;         Library is same no matter what so make it in constructor
	}

	//Removes an account from the system
	public void removeAccount(String username) 
	{
		
	}

	//Adds a book to the Catalog
	public void addBook(Book book, Catalog c) 
	{
		c.addBook(book);
	}

	//removes a book from the Catalog
	public void removeBook(Book book, Catalog c) 
	{
		c.removeBook(book);
	}
	
	public void createAccount(String username, String password, String name, String type) //makes an account
	{
		Account toCreate = new Account(username, password, name); //TO DO     CHANGE TYPE 
	}
	
	

	//Prompts the user to change a book's info
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
	public String saveString()
	{
		String returnString = "";
		returnString += "Admin " + this.getUsername()  + " " + this.getPassword() + " " + this.getName();
		return returnString;
	}
}
