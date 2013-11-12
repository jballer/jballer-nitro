package models;

import javax.persistence.*;

import play.db.jpa.Model;

@Entity
public class UserTagCount extends Model {
	
	@ManyToOne
	public User	owner;
	
	@ManyToOne
	public Tag tag;
	
	public long	userTagCount;
	
	public static UserTagCount findOrCreate(User u, Tag t) {
		UserTagCount userTag = find("byAccountAndTag", u, t).first();
		
		if(userTag == null) {
			System.out.println("Creating new user count for this tag");
			userTag = new UserTagCount(u, t);
		}
		return userTag;
	}
	
	public long increment(long count) {
		userTagCount += count;
		return userTagCount;
	}
	
	private UserTagCount(User user, Tag tag) {
		this.owner = user;
		this.tag = tag;
	}
}
