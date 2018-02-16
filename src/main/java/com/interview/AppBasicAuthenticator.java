package com.interview;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;





import java.util.Optional;

 
public class AppBasicAuthenticator implements Authenticator<BasicCredentials, User>
{
	 UserDAO userDAO;
	 public AppBasicAuthenticator(UserDAO userDAO) {
	        this.userDAO = userDAO;
	    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
    
    	User user = this.userDAO.getUser(credentials.getUsername());
    	if(user.checkPass(credentials.getPassword())) {
    		return Optional.of(user);
    	}
    	return Optional.empty();
    }
    
}