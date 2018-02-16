package com.interview;

import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<User>{

    @Override
    public boolean authorize(User user, String role) {
    	return user.getRole() != null && user.getRole().equals(role);
    }
}
