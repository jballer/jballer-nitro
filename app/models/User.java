package models;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;

import org.mindrot.jbcrypt.BCrypt;

import play.data.validation.Email;
import play.data.validation.Validation;
import play.db.jpa.Model;

@Entity
@Table(name="accounts")
public class User extends Model
{	
	@Email public String email;
	public String passwordHash;
	
	@OneToMany(mappedBy="uploadedBy", cascade=CascadeType.ALL)
	public List<Document> documents;
	
	@OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
	public Set<UserTagCount> tags;
	
	private User(String email, String password) {
		this.email = email;
		this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
		this.save();
	}
	
	public static User connect(String email, String password) {
		User user = User.find("byEmail", email).first(); 
		if(user != null) {
			if(BCrypt.checkpw(password, user.passwordHash)) {
				return user;
			}
		}
		return null;
	}
	
	public static User registerUser(String email, String password)
	{
		// lowercase it
		email = email.toLowerCase();
		
		// Check validity of username
		
		// Is it an email address?
		try
		{
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		}
		catch (AddressException ex)
		{
		      return null;
		}
		
		// If we made it here, assume it's valid.
		// Set the ivar and return true
		User user = new User(email, password);
		
		return user;
	}

	public Document addDocument(InputStream is, String fileName, String comment) {
		Document doc = new Document(this, is, fileName, comment);
		documents.add(doc);
		this.save();
		
		return doc;
	}
	
	public UserTagCount findOrCreateUserCountForTagNamed(String name) {
		Tag t = Tag.findOrCreateByName(name);
		UserTagCount ut = UserTagCount.findOrCreate(this, t);
		this.tags.add(ut);
		return ut;
	}

}