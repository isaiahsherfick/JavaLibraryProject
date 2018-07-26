import java.util.ArrayList;
public class Student extends Account 
{
	private String lendID;
	private int penalty;
	//This will be an int to avoid flont errors.
	
	private ArrayList<Book> listOfBooks;

	//Constructor for the Student class
	public Student(String username, String password, String name, String lendID, ArrayList<Book> listOfBooks) 
	{
		super(username, password, name);
		penalty = 0;
		this.lendID= lendID;
		this.listOfBooks = new ArrayList<Book>();
	}

	//Returns a book the student has checked out
	public void returnBook(Book book)
	{
		listOfBooks.remove(book);
		book.toggleAvailability();
	}

	//Checks out a book from the library
	public void checkOut(Book book)
	{
		listOfBooks.add(book);
		book.toggleAvailability();
	}
	public void addToListOfBooks(Book book)
	{
		listOfBooks.add(book);
	}

	//Lends a book to another Student
	public void lend(Book book)
	{
		this.returnBook(book);
		System.out.println("Please tell your friend to log in after you exit so they can check out the book.");
		listOfBooks.remove(book);	
	}

	//prints the contents of listOfBooks to the screen
	public void viewBooks()
	{
		for (Book each : listOfBooks)
		{
			System.out.print(each);
		}
	}
	
	public String getLendID()
	{
		return lendID;
	}
	
	public int getPenalty()
	{
		return penalty;
	}	
	public void increasePenalty(int amount)
	{
		this.penalty += amount;
	}
	public void resetPenalty()
	{
		this.penalty = 0;
	}
	public ArrayList<Book> getListOfBooks()
	{
		return this.listOfBooks;
	}
	public String saveString()
	{
		String returnString = "";
		returnString += "Student " + this.getUsername() + " " + this.getPassword() + " " + this.getName() + " " + this.lendID + " ";
		return returnString;
	}
}
