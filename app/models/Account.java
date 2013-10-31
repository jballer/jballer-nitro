package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Account extends Model
{	
	public String username;
	public String password;	
	public boolean isAdmin;
}
