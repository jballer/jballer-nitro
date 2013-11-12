package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.Error;

import controllers.Secure.Security;

import java.util.*;

import models.*;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
    	String user = Security.connected();
    	if(user != null) {
    		home();
    	}
    	render();
    }
    
    public static void signup() {
    	render();
    }
    
    public static void home() {
    	User user = User.find("byEmail", Security.connected()).first();
    	if(user == null) {
    		forbidden();
    	}
    	render(user);
    }
}