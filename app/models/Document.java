package models;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;
import play.libs.MimeTypes;
import play.modules.s3blobs.S3Blob;

@Entity
public class Document extends Model
{
	@Column(nullable=true)
	public User uploadedBy;
    public String fileName;
    public S3Blob file;
    public String comment;
    
    public Document(User uploadedBy, InputStream is, String fileName, String comment) {
    	// Set properties
    	this.uploadedBy = uploadedBy;
    	this.fileName = fileName;
    	this.comment = comment;
    	
    	// If there's a file, save it as an S3Blob
    	if(is != null) {
    		this.file = new S3Blob();
    		this.file.set(is, MimeTypes.getContentType(fileName));
    	}
    }
}
