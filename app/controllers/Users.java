package controllers;

import controllers.Secure.Security;
import models.User;
import play.data.validation.Error;
import play.mvc.Controller;

public class Users extends Controller {
	
	public static void signup(String email, String password) {
    	validation.email(email);
    	validation.required("Email address", email);
    	validation.required("Password", password);
    	
    	if(validation.hasErrors()) {
            for(Error error : validation.errors()) {
                System.out.println(error.message());
            }
        }
    	
    	if(validation.hasErrors()) {
			params.flash(); // add http parameters to the flash scope
			validation.keep(); // keep the errors for the next request
			render();
    	}
    	else {
    		// Log in and go to home page
    		Security.authenticate(email, password);
    		Application.home();
    	}
    }
    
    public static void login(String email, String password) {
    	validation.required(email);
    	validation.required(password);
    	validation.email(email);
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		validation.keep();
    		render("@index");
    	}
    }
}
