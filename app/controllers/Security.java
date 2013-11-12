package controllers;

import models.User;

public class Security extends controllers.Secure.Security {
	
	static boolean authenticate(String username, String password) {
		return User.connect(username, password) != null;
	}
	
	static void onAuthenticated() {
		Application.index();
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
