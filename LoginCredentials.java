import java.util.*;
public class LoginCredentials implements Comparable
{
	private String username;
	private String password;
	private String[] login;
	public LoginCredentials(String username, String password)
	{
		this.username = username;
		this.password = password;
		this.login = new String[2];
		this.login[0] = username;
		this.login[1] = password;
	}
	public int compareTo(Object other)
	{
		if (!(other instanceof LoginCredentials))
		{
			return 0;
		}
		LoginCredentials otherLogin = (LoginCredentials)other;
		char thisFirstLetter = this.username.charAt(0);
		char otherFirstCharacter = otherLogin.getUsername().charAt(0);
		if (thisFirstLetter > otherFirstCharacter)
		{
			return 1;
		}
		else if (thisFirstLetter < otherFirstCharacter)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	public String getUsername()
	{
		return this.username;
	}
	public void setUsername(String username)
	{
		this.username = username;
		this.login[0] = username;
	}
	public String getPassword()
	{
		return this.password;
	}
	public void setPassword(String password)
	{
		this.password = password;
		this.login[1] = password;
	}
	public String[] getLogin()
	{
		return this.login;
	}
	public boolean equals(LoginCredentials other)
	{
		if (Arrays.equals(this.login, other.getLogin()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
