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

import controllers.Secure.Security;;

public class Admin extends Controller {
	
	public static void listAccounts()
	  {
		String user = Security.connected();
		
		List<Account> accounts = Account.findAll();
		List<Document> docs = Document.findAll();
		
		render(accounts, user, docs);
	  }
	
	  public static void addAccount(String username, String password, Boolean isAdmin)
	  {
		final Account account = new Account();
	    account.username = username.toLowerCase();
	    account.password = password;
	    	    
	    account.save();
	    listAccounts();
	  }
	  
	  public static void deleteAccount(long id)
	  {
		  Account account = Account.findById(id);
		  if (account != null)
		  {
			  account.delete();
			  listAccounts();
		  }
	  }
	  
}
