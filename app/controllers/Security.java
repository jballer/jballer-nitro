package controllers;

import models.Account;

public class Security extends controllers.Secure.Security {
	static boolean authenticate(String username, String password) {
		
		System.out.println("attempting to log in as "+username);
		
		// Hard code a superuser so that we can create other accounts
		if (username.equals("admin") && password.equals("admin"))
		{
			System.out.println("Superuser!");
			Admin.listAccounts();
			return false;
		}
		else
		{
			Account account = null;
			try {
				account = Account.find("username = ?", username.toLowerCase()).first();
			}
			catch(Exception e)
			{
				System.out.println("EXCEPTION:\n"+e);
			}
			
			return account != null && account.password.equals(password);
		}
	}
	
//	static boolean isAdministrator()
//	{
//		String username = Security.connected();
//		if (username == null){
//			return false;
//		}
//		
//		Account account = Account.find("byUsername", username).first();
//		System.out.println("Account ID "+account.id+": "+ account.username);
//		Boolean isAdmin = account.isAdmin;
//		
//		return isAdmin; 
//	}
}
