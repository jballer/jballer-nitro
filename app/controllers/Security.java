package controllers;

import models.Account;

public class Security extends controllers.Secure.Security {
	static boolean authenticate(String username, String password) {
		Account user = Account.find("byEmail", username).first();
		return user != null && user.password.equals(password);
	}
}
