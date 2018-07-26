import java.util.*;
import java.io.*;
public class LibraryDriver
{
	private Account currentUser;
	private Catalog catalog;
	private TreeMap<LoginCredentials, Account>accountMap;
	private ArrayList<String> availableUserOptions;
	private int currentDay;

	//Runs automatically, adding penalties to offending Students with overdue books checked out.
	public void addPenalty()
       	{
		for (Book b : this.catalog.getLibrary())
		{
			Student offendingStudent;
			if (!b.isAvailable())
			{
				if (currentDay - b.getDateCheckedOut() > 14)
				{
					offendingStudent = b.getCheckOuts().get(b.getCheckOuts().size() - 1);
					offendingStudent.increasePenalty(currentDay - b.getDateCheckedOut());
				}
			}
		}	
	}
	public String saveAccounts()
	{
		String returnString = "";
		for (Map.Entry<LoginCredentials, Account> entry : this.accountMap.entrySet())
		{
			Account currentSaveAccount = entry.getValue();
			if (currentSaveAccount instanceof Student)
			{
				Student currentStudent = (Student)currentSaveAccount;
				returnString += currentStudent.saveString() + "\n";
			}
			else if (currentSaveAccount instanceof Librarian)
			{
				Librarian currentLibrarian = (Librarian)currentSaveAccount;
				returnString += currentLibrarian.saveString() + "\n";
			}
			else if (currentSaveAccount instanceof Admin)
			{
				Admin currentAdmin = (Admin)currentSaveAccount;
				returnString += currentAdmin.saveString() + "\n";
			}
		}
		return returnString;
	}
	//Writes the state of the system to a file. Runs on exit.
	public void writeFiles()
       	{
		BufferedWriter bW = null;
		FileWriter fW = null;
		try
		{
			String catalogToFile = "";
			catalogToFile += this.catalog.saveString();
			fW = new FileWriter("SavedCatalog.txt");
			bW = new BufferedWriter(fW);
			bW.write(catalogToFile);
			bW.close();
			fW.close();
			fW = new FileWriter("SavedAccounts.txt");
			bW = new BufferedWriter(fW);
			bW.write(this.saveAccounts() + "Day " + this.currentDay);
			bW.close();
			fW.close();
			System.out.println("State saved.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//Prints the current user's options to the console.
	public void displayOptions() 
	{
		System.out.println("Press the corresponding key and press <Enter> to execute a command.");
		if (currentUser instanceof Student)
		{
			//All the Student stuff
			System.out.println("1. View late fees");
			System.out.println("2. Check out a book");
			System.out.println("3. Return a book");
			System.out.println("4. Lend a book to another student");
			System.out.println("5. View my checked out books");
			System.out.println("6. Search for a book");
		}
		else if (currentUser instanceof Librarian)
		{
			//All the Librarian stuff
			System.out.println("1. View checked out books");
			System.out.println("2. View books checked out by a specific student");
			System.out.println("3. Remove a student's fees");
		        System.out.println("4. Add a book to the catalog");
			System.out.println("5. Remove a book from the catalog");
			System.out.println("6. Modify an existing book's information");
			System.out.println("7. Search the catalog");
		}	
		else if (currentUser instanceof Admin)
		{
			//All the Admin stuff
			System.out.println("1. Add an account to the system");
			System.out.println("2. Remove an account to the system");
			System.out.println("3. Search the catalog");
			System.out.println("4. Modify an existing book's information");
			System.out.println("5. Start a new day");
		}
	}
	//Starts the system, reading all state information
	public void startSystem()
	{
		try
		{
			File accountsFile = new File("SavedAccounts.txt");
			File catalogFile = new File("SavedCatalog.txt");
			Scanner accountScanner = new Scanner(accountsFile);
			Scanner catalogScanner = new Scanner(catalogFile);
			while (accountScanner.hasNextLine())
			{
				String line = accountScanner.nextLine();
				Scanner lineBreaker = new Scanner(line);
				String identifier = lineBreaker.next();
				if (identifier.equals("Student"))
				{
					//Student info
					String username = lineBreaker.next();
					String password = lineBreaker.next();
					String name = lineBreaker.next();
					String lendID = lineBreaker.next();
					Student newStudent = new Student(username, password, name, lendID, new ArrayList<Book>());
					LoginCredentials newStudentLogin = new LoginCredentials(username, password);
					this.accountMap.put(newStudentLogin, newStudent);
				}
				else if (identifier.equals("Librarian"))
				{
					//Librarian info
					String username = lineBreaker.next();
					String password = lineBreaker.next();
					String name = lineBreaker.next();
					Librarian newLibrarian = new Librarian(username, password, name);
					LoginCredentials newLibrarianLogin = new LoginCredentials(username, password);
					this.accountMap.put(newLibrarianLogin, newLibrarian);
				}
				else if (identifier.equals("Admin"))
				{
					//Admin stuff
					String username = lineBreaker.next();
					String password = lineBreaker.next();
					String name = lineBreaker.next();
					Admin newAdmin = new Admin(username, password, name);
					LoginCredentials newAdminLogin = new LoginCredentials(username, password);
					this.accountMap.put(newAdminLogin, newAdmin);
				}
				else
				{
					this.currentDay = lineBreaker.nextInt();
				}
				lineBreaker.close();
			}
			while (catalogScanner.hasNextLine())
			{
				String line = catalogScanner.nextLine();
				Scanner lineBreaker = new Scanner(line);
				String author = lineBreaker.next();
				String title = lineBreaker.next();
				String isbn = lineBreaker.next();
				String genre = lineBreaker.next();
				String availabilityString = lineBreaker.next();
				boolean available = false;
				if (availabilityString.equals("true"))	
				{
					available = true;
				}
				int dateCheckedOut = lineBreaker.nextInt();
				String checkOutsString = lineBreaker.next();
				List<String> checkOutsList = Arrays.asList(checkOutsString.split("\\s*,\\s*"));
				ArrayList<Student> checkOuts = new ArrayList<>();
				for (String s : checkOutsList)
				{
					for (Map.Entry<LoginCredentials, Account> entry : this.accountMap.entrySet())
					{
						Account currentAccount = entry.getValue();
						String currentUsername = currentAccount.getUsername().toLowerCase();
						if (s.toLowerCase().equals(currentUsername))
						{
							checkOuts.add((Student)currentAccount);
						}
					}
				}
				Book newBook = new Book(author, title, isbn, genre, available, checkOuts, dateCheckedOut);
				this.catalog.addBook(newBook);
				if (!available)
				{
					newBook.getCheckOuts().get(newBook.getCheckOuts().size() - 1).addToListOfBooks(newBook);
				}
				lineBreaker.close();
			}
			accountScanner.close();
			catalogScanner.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Save information not fonud. Loading default information.");
		}	
	}
	//Constructor for the LibraryDriver class. Only needs instantiated once.
	public LibraryDriver(String accountFileName, String bookFileName)
	{
		this.catalog = new Catalog();
		//Make sure we remove this ^^^
		this.accountMap = new TreeMap<LoginCredentials, Account>();
		Admin isaiah = new Admin("admin","1234","Admin");
		LoginCredentials isaiahLoginCredentials = new LoginCredentials("isherfic","1234");
		this.accountMap.put(isaiahLoginCredentials, isaiah);
		this.startSystem();
	}
	//Prints 40 blank lines, just makes it look nice whenever something is printed out.
	public void clearScreen()
	{
		for (int i = 0; i < 40; i++)
		{
			System.out.println("");
		}
	}
	//The main loop for the system
	public static void main(String[] args)
	{
		LibraryDriver mainDriver = new LibraryDriver("librarySave", "accountSave");
		Scanner in = new Scanner(System.in);
		boolean validLogin = false;
		String username = "";
		String password = "";
		LoginCredentials userLogin = new LoginCredentials("null","null");
		/**
		 * Input validation
		 */
		while (!validLogin)
		{
			System.out.print("Username: ");
			username = in.nextLine();
			System.out.print("Password: ");
			password = in.nextLine();
			userLogin.setUsername(username);
			userLogin.setPassword(password);
			for (Map.Entry<LoginCredentials, Account> entry : mainDriver.accountMap.entrySet())
			{
				LoginCredentials key = entry.getKey();
				if (userLogin.equals(key))
				{
					mainDriver.currentUser = mainDriver.accountMap.get(userLogin);
					validLogin = true;
				}
			}
			if (!validLogin)
			{
				System.out.println("Invalid login. Please trying again.");
			}
		}
		/**
		 * Main loop for each individual account type
		 */
		boolean userExit = false;
		//Main loop
		while (!userExit)
		{
			if (mainDriver.currentUser instanceof Student)
			{
				Student currentStudent = (Student)mainDriver.currentUser;
				mainDriver.displayOptions();
				String option = in.nextLine();
				if (option.equals("1"))
				{
					//View late fees
					System.out.println(currentStudent.getName() + "'s late fees: " + currentStudent.getPenalty() + " dollars.");
				}
				else if (option.equals("2"))
				{
					//Check out a book
					System.out.println("Please enter the desired book's ISBN number. If you don't know it, please search the catalog for your desired book and write it down. Enter \"E\" to exit.");
					String isbn = in.nextLine();
					if (mainDriver.catalog.getByISBN(isbn) != null)
					{
						Book bookToCheckOut = mainDriver.catalog.getByISBN(isbn);
						if (bookToCheckOut.isAvailable())
						{
							currentStudent.checkOut(bookToCheckOut);
							bookToCheckOut.addToCheckOuts(currentStudent);
							System.out.println("Checked out \n" + bookToCheckOut + "\n");
						}	
						else
						{
							System.out.println("Book is currently unavailable.");
						}
					}
					else if (isbn.equals("E"))
					{
						System.out.println("Exiting.");
					}
					else
					{
						System.out.println("No results.");
					}
				}
				else if (option.equals("3"))
				{
					//Return a book
					currentStudent.viewBooks();
					System.out.println("Please enter the ISBN of the desirerd book to return.");
					String isbn = in.nextLine();
					boolean bookReturned = false;
					Book bookToReturn = new Book("","","","",false, new ArrayList<Student>(),0);
					for (Book b : currentStudent.getListOfBooks())
					{
						if (b.getIsbnNumber().equals(isbn))
						{
							bookToReturn = b;
							System.out.println("Returned \n" + b);
							bookReturned = true;
						}
					}
					if (!bookReturned)
					{
						System.out.println("Something went wrong. No book returned. Please double check your input.");
					}
					else
					{
						currentStudent.returnBook(bookToReturn);
					}
				}
				else if (option.equals("4"))
				{
					System.out.println("What is the ISBN of the book you want to lend?");
					String isbn = in.nextLine();
					Book bookToLend = mainDriver.catalog.getByISBN(isbn);
					if (bookToLend == null)
					{
						System.out.println("That book does not exist.");
					}
					else
					{
						if (currentStudent.getListOfBooks().contains(bookToLend))
						{
							currentStudent.lend(bookToLend);
						}
						else
						{
							System.out.println("You don't have that book.");
						}
					}
				}
				else if (option.equals("5"))
				{
					//View my checked out books
					System.out.println(currentStudent.getName() + "'s checked out books:\n");
					currentStudent.viewBooks();
				}
				else if (option.equals("6"))
				{
					//Search for a book
					System.out.println("Please enter the ISBN, Title, Author, or Genre of the book.\n");
					String searchTerm = in.nextLine();
					ArrayList<Book> results = mainDriver.catalog.search(searchTerm);
					if (results.size() == 0)
					{
						System.out.println("No results.\n");
					}
					else
					{
						for (Book b : results)
						{
							System.out.println(b);
						}
					}
				}
				else if (option.equals("exit"))
				{
					mainDriver.writeFiles();
					userExit = true;
				}
				else
				{
					System.out.println("Unrecognized input. Please try again.");
				}
			}
			else if (mainDriver.currentUser instanceof Librarian)
			{
				//All the Librarian options
				mainDriver.displayOptions();
				String option = in.nextLine();
				if (option.equals("1"))
				{
					//View checked out books
					ArrayList<Book> checkedOutBooks = mainDriver.catalog.getCheckedOutBooks();
					for (Book b : checkedOutBooks)
					{
						System.out.println(b);
					}
				}
				else if (option.equals("2"))
				{
					//View books checked out by a specific student	
					System.out.println("Please input the name of the student whose checkouts you wish to view.");
					String desiredStudent = in.nextLine().toLowerCase();
					ArrayList<Book> checkedOutBooks = new ArrayList<>();
					for (Book b : mainDriver.catalog.getLibrary())
					{
						if (!b.isAvailable())
						{
							if (b.getCheckOuts().get(b.getCheckOuts().size() - 1).getName().toLowerCase().equals(desiredStudent))
							{
								checkedOutBooks.add(b);
							}	
						}
					}
					if (checkedOutBooks.size() == 0)
					{
						System.out.println("Student has no checked out books or does not exist.");
					}
					else
					{
						for (Book b : checkedOutBooks)
						{
							System.out.println(b);
						}
					}
				}
				else if (option.equals("3"))
				{
					//Remove a student's fees
					System.out.println("Please input the student's name.");
					String desiredStudent = in.nextLine().toLowerCase();
					boolean studentExists = false;
					for (Map.Entry<LoginCredentials, Account> entry : mainDriver.accountMap.entrySet())
					{
						Account currentAccount = entry.getValue();
						if (currentAccount.getName().toLowerCase().equals(desiredStudent))
						{
						Student currentStudent = (Student)currentAccount;
							currentStudent.resetPenalty();
							System.out.println("Fees reset.");
							studentExists = true;
						}
					}
					if (!studentExists)
					{
						System.out.println("Student does not exist.");
					}
				}
				else if (option.equals("4"))
				{
					//Add a book to the catalog
					System.out.println("Please input the title of the book.");
					String bookName = in.nextLine();
					System.out.println("Please input the author's name.");
					String authorName = in.nextLine();
					System.out.println("Please input the book's ISBN.");
					String isbn = in.nextLine();
					boolean isAvailable = true;
					ArrayList<Student> checkOuts = new ArrayList<>();
					System.out.println("Please input the genre of the book.");
					String genre = in.nextLine();
					Book newBook = new Book(authorName, bookName, isbn, genre, isAvailable, checkOuts,0);
					mainDriver.catalog.addBook(newBook);
					System.out.println("Added!");
				}
				else if (option.equals("5"))
				{
					System.out.println("Enter the book's ISBN.");
					String removalISBN = in.nextLine();
					Book bookToBeRemoved = mainDriver.catalog.getByISBN(removalISBN);
					if(bookToBeRemoved == null)
					{
						System.out.println("That was not in the catalog.");
					}
					else
					{
						System.out.println("Removed " + bookToBeRemoved);
						mainDriver.catalog.removeBook(bookToBeRemoved);
					}

				}
				else if (option.equals("6"))
				{
					//Modify an existing book's information
					System.out.println("Please input the ISBN of the book whose information you wish to modify.");
					String isbn = in.nextLine();
					Book bookToModify = mainDriver.catalog.getByISBN(isbn);
					if (bookToModify == null)
					{
						System.out.println("Book not found.");
					}
					else
					{
						System.out.println(bookToModify);
						System.out.println("Please input the field you would wish to modify.");
						System.out.println("1. Author name");
						System.out.println("2. Book name");
						System.out.println("3. ISBN");
						System.out.println("4. Genre");
						boolean validInput = false;
						while (!validInput)
						{
							String changeOption = in.nextLine();
							if (changeOption.equals("1"))
							{
								System.out.println("Please input the new author name.");
								String newAuthorName = in.nextLine();
								bookToModify.setAuthorName(newAuthorName);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("2"))
							{
								System.out.println("Please input the new book name.");
								String newBookName = in.nextLine();
								bookToModify.setBookName(newBookName);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("3"))
							{
								System.out.println("Please input the new ISBN.");
								String newISBN = in.nextLine();
								bookToModify.setIsbnNumber(newISBN);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("4"))
							{
								System.out.println("Please input the new genre.");
								String newGenre = in.nextLine();
								bookToModify.setGenre(newGenre);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else
							{
								System.out.println("Invalid input.");
							}
						}
					}
				}
				else if (option.equals("7"))
				{
					//Search for a book
					System.out.println("Please enter the ISBN, Title, Author, or Genre of the book.\n");
					String searchTerm = in.nextLine();
					ArrayList<Book> results = mainDriver.catalog.search(searchTerm);
					if (results.size() == 0)
					{
						System.out.println("No results.\n");
					}
					else
					{
						for (Book b : results)
						{
							System.out.println(b);
						}
					}
				}
				else if (option.equals("exit"))
				{
					mainDriver.writeFiles();
					userExit = true;
				}
				else
				{
					System.out.println("Unrecognized input.");
				}
			}
			else if (mainDriver.currentUser instanceof Admin)
			{
				//All the Admin options
				mainDriver.displayOptions();
				String option = in.nextLine();
				if (option.equals("1"))
				{
					//Add an account to the system
					System.out.println("What kind of account are you creating?");
					String accountType = in.nextLine().toLowerCase();
					if (accountType.equals("student"))
					{
						System.out.println("Please input the student's username.");
						String newUsername = in.nextLine();
						System.out.println("Please input the student's password.");
						String newPassword = in.nextLine();
						System.out.println("Please input the student's full name.");
						String name = in.nextLine();
						System.out.println("Please input the student's lendID.");
						String lendID = in.nextLine();
						Student newStudent = new Student(newUsername, newPassword, name, lendID, new ArrayList<Book>());
						LoginCredentials newStudentLoginCredentials = new LoginCredentials(newUsername, newPassword);
						mainDriver.accountMap.put(newStudentLoginCredentials, newStudent);
						System.out.println("Done!");
					}
					else if (accountType.equals("librarian"))
					{
						System.out.println("Please input the librarian's username.");
						String newUsername = in.nextLine();
						System.out.println("Please input the librarian's password.");
						String newPassword = in.nextLine();
						System.out.println("Please input the librarian's full name.");
						String name = in.nextLine();
						Librarian newLibrarian= new Librarian(newUsername, newPassword, name);
						LoginCredentials newLibrarianLoginCredentials = new LoginCredentials(newUsername, newPassword);
						mainDriver.accountMap.put(newLibrarianLoginCredentials, newLibrarian);
						System.out.println("Done!");
					}
					else if (accountType.equals("admin"))
					{
						System.out.println("Please input the admin's username.");
						String newUsername = in.nextLine();
						System.out.println("Please input the admin's password.");
						String newPassword = in.nextLine();
						System.out.println("Please input the admin's full name.");
						String name = in.nextLine();
						Admin newAdmin = new Admin(newUsername, newPassword, name);
						LoginCredentials newAdminLoginCredentials = new LoginCredentials(newUsername, newPassword);
						mainDriver.accountMap.put(newAdminLoginCredentials, newAdmin);
						System.out.println("Done!");
					}
					else
					{
						System.out.println("Unrecognized input. Please try again and input either 'student', 'librarian', or 'admin'.");
					}
				}
				else if (option.equals("2"))
				{
					//Remove an account from the system
					System.out.println("What is the username?");
					String removalUsername = in.nextLine();
					LoginCredentials credentialsForRemoval = null;
					for (Map.Entry<LoginCredentials, Account> entry :mainDriver.accountMap.entrySet())
					{
						Account checkingAccount = entry.getValue();
						if( checkingAccount.getUsername().equals(removalUsername))
						{
							credentialsForRemoval = new LoginCredentials(checkingAccount.getUsername(),checkingAccount.getPassword());
						}
					}
					if (credentialsForRemoval == null)
					{
						System.out.println("That isn't an account");
					}
					else
					{
						mainDriver.accountMap.remove(credentialsForRemoval);
						System.out.println("Removed!");
					}
				}
				else if (option.equals("3"))
				{
					//Search the library
					System.out.println("Please enter the ISBN, Title, Author, or Genre of the book.\n");
					String searchTerm = in.nextLine();
					ArrayList<Book> results = mainDriver.catalog.search(searchTerm);
					if (results.size() == 0)
					{
						System.out.println("No results.\n");
					}
					else
					{
						for (Book b : results)
						{
							System.out.println(b);
						}
					}
				}
				else if (option.equals("4"))
				{
					//Modify an existing book's information
					System.out.println("Please input the ISBN of the book whose information you wish to modify.");
					String isbn = in.nextLine();
					Book bookToModify = mainDriver.catalog.getByISBN(isbn);
					if (bookToModify == null)
					{
						System.out.println("Book not found.");
					}
					else
					{
						System.out.println(bookToModify);
						System.out.println("Please input the field you would wish to modify.");
						System.out.println("1. Author name");
						System.out.println("2. Book name");
						System.out.println("3. ISBN");
						System.out.println("4. Genre");
						boolean validInput = false;
						while (!validInput)
						{
							String changeOption = in.nextLine();
							if (changeOption.equals("1"))
							{
								System.out.println("Please input the new author name.");
								String newAuthorName = in.nextLine();
								bookToModify.setAuthorName(newAuthorName);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("2"))
							{
								System.out.println("Please input the new book name.");
								String newBookName = in.nextLine();
								bookToModify.setBookName(newBookName);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("3"))
							{
								System.out.println("Please input the new ISBN.");
								String newISBN = in.nextLine();
								bookToModify.setIsbnNumber(newISBN);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("4"))
							{
								System.out.println("Please input the new genre.");
								String newGenre = in.nextLine();
								bookToModify.setGenre(newGenre);
								System.out.println("Changes saved.");
								validInput = true;
							}
							else if (changeOption.equals("5"))
							{
								mainDriver.currentDay +=1;
								System.out.println("updated. No Restart the System.");
								validInput = true;							
							}
							else
							{
								System.out.println("Invalid input.");
							}
						}
					}

				}
				else if (option.equals("exit"))
				{
					mainDriver.writeFiles();
					userExit = true;
				}
				else
				{
					System.out.println("Invalid input.");
				}
			}
		}
	}
}
