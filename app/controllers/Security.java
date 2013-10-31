package controllers;

import models.Account;

public class Security extends controllers.Secure.Security {
	static boolean authenticate(String username, String password) {
		
		System.out.println("attempting to log in as "+username+":"+password);
		
		// Hard code a superuser so that we can create other accounts
		if (username.equals("admin") && password.equals("admin"))
		{
			System.out.println("Superuser!");
			
			return true;
		}
		else
		{
			Account account = Account.find("byUsername", username.toLowerCase()).first();
			
			return account != null && account.password.equals(password);
		}
	}
	
	static boolean isAdministrator()
	{
		String user = Security.connected();
		if (user == null){
			return false;
		}
		
		Account account = Account.find("byUsername", user).first();
		System.out.println("Account ID "+account.id+": "+ account.username);
		Boolean isAdmin = account.isAdmin;
		
		return isAdmin; 
	}
}
