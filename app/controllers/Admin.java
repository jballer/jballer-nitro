package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import models.Document;
import play.libs.MimeTypes;
import play.modules.s3blobs.S3Blob;

import play.mvc.*;
import models.*;

@With(Secure.class)
public class Admin extends Controller {
	
	private static Boolean authenticate()
	{
		Boolean isAdmin = Security.isAdministrator();
		
		return isAdmin;
	}
	
	public static void listAccounts()
	  {
		String user = Security.connected();
		Boolean isAdmin = authenticate();
		
		List<Account> accounts = Account.findAll();
		
		if (isAdmin)
			render(accounts, user, isAdmin);
		else
			forbidden();
	  }
	
	  public static void addAccount(String username, String password, Boolean isAdmin)
	  {
		authenticate();
	    
		final Account account = new Account();
	    account.username = username.toLowerCase();
	    account.password = password;
	    if (isAdmin) {
	    	account.isAdmin = isAdmin;
	    }
	    else {
	    	isAdmin = false;
	    }
	    
	    account.save();
	    listAccounts();
	  }
	  
	  public static void deleteAccount(long id)
	  {
		  authenticate();
		  
		  Account account = Account.findById(id);
		  if (account != null)
		  {
			  account.delete();
			  listAccounts();
		  }
	  }
	  
}
