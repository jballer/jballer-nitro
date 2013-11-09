package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.*;
import java.io.FileNotFoundException;
import java.util.List;

import javax.persistence.*;

import models.Document;
import models.Account;
import models.Tag;
import models.UserTag;
import play.db.jpa.JPA;
import play.libs.MimeTypes;
import play.modules.s3blobs.S3Blob;
import play.mvc.Controller;
import play.mvc.Util;

public class Files extends Controller
{

  public static void uploadForm()
  {
    render();
  }

  public static void doUpload(File file, String comment, int userID) throws FileNotFoundException
  {
	  if(file == null)
	  {
		  uploadForm();
	  }
	  else
	  {
	    final Document doc = new Document();
	    doc.uploadedBy = Account.accountForUsername(Security.connected());
	    doc.fileName = file.getName();
	    doc.comment = comment;
	    doc.file = new S3Blob();
	    doc.file.set(new FileInputStream(file), MimeTypes.getContentType(file.getName()));
	    doc.save();
    
	    parseTags(file, doc);
	  }
    
	  listUserUploads();
  }

  public static void listAllUploads()
  {
    List<Document> docs = Document.findAll();
    render(docs);
  }
  
  public static void listUserUploads()
  {
	Account account = Account.accountForUsername(Security.connected());
    List<Document> docs = new ArrayList<Document>();
    try {
    	Document.find("uploadedby = ?", account.id).fetch();
    } catch(Exception e) {}
    renderTemplate("/Files/listUploads.html", docs, account.username);
  }

  public static void downloadFile(long id)
  {
    final Document doc = Document.findById(id);
    notFoundIfNull(doc);
    response.setContentTypeIfNotSet(doc.file.type());
    renderBinary(doc.file.get(), doc.fileName);
  }
  
  public static Document deleteFile(long id)
  {
	  Document doc = Document.findById(id);
	  return doc.delete();
  }

  @Util
  public static void parseTags(File file, Document document)
  {
	  //String for entire book
	  String fullText = "";

	  //Read File to string
	  FileInputStream fileStream = null;
	  BufferedInputStream bufferStream = null;
	  DataInputStream dataStream = null;
	  
	  try
	  {
		  fileStream = new FileInputStream(file);
		  bufferStream = new BufferedInputStream(fileStream);
		  dataStream = new DataInputStream(bufferStream);
	  
		  while (dataStream.available() != 0)
		  {
			  //populate string from file
			  fullText+=dataStream.readLine();
		  }
		  fileStream.close();
		  bufferStream.close();
		  dataStream.close();
	  }
	  catch (Exception x)
	  {}

	  //remove punctuation and numbers
	  fullText = fullText.replaceAll("\\.|\\]|\\[|[0-9]|,|\\?|:|\\(|\\)|;|-|!","");
	  
	  //lower case all words
	  fullText = fullText.toLowerCase();
	  
	  //create pattern
	  Pattern word = Pattern.compile("[\\w]+");
	  
	  //find pattern matches within file string
	  Matcher m = word.matcher(fullText);
	  
	  //create total word elementList, a unique word list, and a hash table
	  ArrayList elementList = new ArrayList();
	  Hashtable frequencyHash = new Hashtable();
	  ArrayList uniqueList = new ArrayList();
	  
	  //for every match found populate total word array list
	  while (m.find())
	  {
		  if (m.group().length() > 4)
		  {
			  elementList.add(m.group());
		  }
	  }
	  //for every word found in total word list
	  for( int i = 0; i < elementList.size(); i++){
		  //first see if your word exists in your hashtable
		  //if it doesn’t add it to your hash table as the key and setvalue to 1
		  //if the word exists in your hash table increment the value
		  if (uniqueList.contains(elementList.get(i))){
			  int elementCount =
			  Integer.parseInt(frequencyHash.get(elementList.get(i)).toString());
			  elementCount++;
			  frequencyHash.put(elementList.get(i), elementCount);
		  }
		  else{
		  uniqueList.add(elementList.get(i));
		  frequencyHash.put(elementList.get(i),1);
		  }
	  }
	  
	  //output word lists
	  System.out.println("unique words : " + uniqueList.size());
	  System.out.println("total words : " + elementList.size());
	  
	  //create enumerators to go through hash table
	  Enumeration numerator1;
	  Enumeration numerator2;
	  
	  //set enums to desired content
	  numerator1 = frequencyHash.keys();
	  numerator2 = frequencyHash.elements();
	  
	  // TODO: Create Tags
	  while (numerator1.hasMoreElements ()){
		  String key = numerator1.nextElement().toString();
		  Integer count = Integer.parseInt(numerator2.nextElement().toString()); 
		  
		  if(count > 3) // word appears more than 3 times in the file
		  {
			  // Does the tag exist?
			  // If not, make it.
			  
			  Tag tag = Tag.find("byTagString", key).first();
			  
			  if (tag == null)
			  {
				  System.out.println("Creating new tag: " + key);
				  
				  tag = new Tag();
				  tag.tagString = key; 
			  }
			  else
			  {
				  System.out.println("Tag exists. Updating count from "+ tag.count + " to " + (tag.count+count));
			  }
			  
			  tag.count += count;
			  tag.save();
			  
			  
			  // Now handle User Tag
			  
			  
			  long account_id = document.uploadedBy.id;
			  
			  Query q = JPA.em().createNativeQuery("select count(*) from usertag where account_id = :acId and where tag_id = :tgId");
			  q.setParameter("acId", account_id);
			  q.setParameter("tgId", tag.id);
			  UserTag userTag = (UserTag) q.getResultList().get(0);
			  
			  if(userTag != null)
			  {
				  System.out.println("Fetching existing User tag.");
				  userTag = UserTag.find("account_id = ? and tag_id = ?", Account.accountForUsername(Security.connected()), tag).first();
				  System.out.println("Fetched with count "+ userTag.userTagCount);
			  }
			  else
			  {
				  System.out.println("Creating User tag for " + tag.tagString);
				  userTag = new UserTag();
				  userTag.account = Account.accountForUsername(Security.connected());
				  userTag.tag = tag;
			  }
			  
			  userTag.userTagCount += count;
			  userTag.save();
			  
		  }
	  }
  }
}