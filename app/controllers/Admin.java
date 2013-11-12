package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
		
		List<User> accounts = User.findAll();
		List<Document> docs = new ArrayList<Document>();
		try {
			Document.findAll();
		}
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		
		render(accounts, user, docs);
	  }
	
	  public static void addAccount(String email, String password, Boolean isAdmin)
	  {
		User.registerUser(email, password);
		listAccounts();
	  }
	  
	  public static void deleteAccount(long id)
	  {
		  User account = User.findById(id);
		  if (account != null)
		  {
			  account.delete();
			  listAccounts();
		  }
	  }
	  
}
