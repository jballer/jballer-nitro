package controllers;

import models.Account;
import play.mvc.Controller;

public class Users extends Controller {
	
	public static void signup()
	{
		Boolean usernameError = false;
		Boolean passwordError = false;
		render(usernameError, passwordError);
	}
	
	public static void signup(Boolean usernameError, Boolean passwordError)
	{
		render(usernameError, passwordError);
	}
	
	public static void createUser(String username, String password)
	{
		Account account = new Account();
		
		Boolean validUsername = false;
		Boolean validPassword = false;
		
		String errorString = "";
		
		if(account.setUsername(username)) // returns false if invalid
		{
			System.out.println("Username validated.");
			validUsername = true;
		}
		else
		{
			errorString += "Username Invalid.\n";
		}
		
		if(account.setPassword(password))
		{
			System.out.println("Password validated.");
			validPassword = true;
		}
		else
		{
			errorString += "Password invalid.";
		}
		
		if(validUsername && validPassword)
		{
			// Store the new account
			account.save();
			
			// Log in with the new account
			try {
				Secure.authenticate(username, password, false);
				Files.listUserUploads();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
			
			Files.listUserUploads();
		}
		else
		{
			// FAIL
			account.delete();
			
			flash.error(errorString);
			
			signup(!validUsername, !validPassword);
		}
		
	}
	
}
