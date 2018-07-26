import java.util.*;
public class Book
{
	private String authorName;
	private String bookName;
	private String isbnNumber;
	private boolean isAvailable;
	private ArrayList<Student> checkOuts;
	private String genre;
	private int dateCheckedOut;

	//Constructor for the Book class
	public Book(String authorName, String bookName, String isbnNumber, String genre, boolean isAvailable, ArrayList<Student> checkOuts, int dateCheckedOut) 
	{
		this.authorName = authorName;
		this.bookName = bookName;
		this.isbnNumber = isbnNumber;
		this.genre = genre;
		this.isAvailable = true;
		this.isAvailable = isAvailable;
		this.checkOuts = checkOuts;
		this.dateCheckedOut = dateCheckedOut;
	}
	public int getDateCheckedOut()
	{
		return this.dateCheckedOut;
	}
	public void setDateCheckedOut(int dateCheckedOut)
	{
		this.dateCheckedOut = dateCheckedOut;
	}
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getIsbnNumber() {
		return isbnNumber;
	}

	public void setIsbnNumber(String isbnNumber) {
		this.isbnNumber = isbnNumber;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void toggleAvailability() 
	{
		if (this.isAvailable)
		{
			this.isAvailable = false;
		}
		else
		{
			this.isAvailable = true;
		}
	}

	public ArrayList<Student> getCheckOuts() {
		return checkOuts;
	}

	public void setCheckOuts(ArrayList<Student> checkOuts) {
		this.checkOuts = checkOuts;
	}
	public void addToCheckOuts(Student s)
	{
		this.checkOuts.add(s);
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String toString()
	{
		String returnString = "Title: " + this.bookName + "\n";
		returnString += "Author: " + this.authorName + "\n";
		returnString += "Genre: " + this.genre + "\n";
		returnString += "ISBN: " + this.isbnNumber + "\n";
		returnString += "Available: " + this.isAvailable() + "\n";
		if (!this.isAvailable())
		{
			returnString += "Checked out!"; 
		}
		return returnString;
	}
	public String saveString()
	{
		String returnString = this.authorName + " " +  this.bookName + " " + this.isbnNumber + " " + this.genre + " " + this.isAvailable + " ";
		returnString += this.dateCheckedOut + " ";
		for (Student s : this.checkOuts)
		{
			returnString += s.getUsername() + ",";
		}
		if (this.checkOuts.size() > 0)
		{
			returnString = returnString.substring(0,returnString.length() - 1);
		}
		else
		{
			returnString += " NO_CHECKOUTS";
		}
		//Gets rid of the last comma
		return returnString;
	}

}
