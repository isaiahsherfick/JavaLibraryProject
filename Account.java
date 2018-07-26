import java.util.*;
public class Account
{
	private String username;
	private String password;
	private String name;
	
	//Constructor for the Account class
	public Account(String username, String password, String name) 
	{
		this.username = username;
		this.password = password;
		this.name = name;
	}

	//Getter for username instance variable
	public String getUsername() 
	{
		return username;
	}

	//Setter for username instance variable
	public void setUsername(String username) 
	{
		username = username;
	}

	//Getter for password instance variable
	public String getPassword() 
	{
		return password;
	}

	//Setter for password instance variable
	public void setPassword(String password) 
	{
		password = password;
	}

	//Getter for name instance variable
	public String getName() 
	{
		return name;
	}

	//Setter for name instance variable
	public void setName(String name) 
	{
		name = name;
	}
	
	//Prints the contents of the library to the screen
	public void viewLibrary(Catalog c) 
	{
		c.printCatalog();
	}

}
