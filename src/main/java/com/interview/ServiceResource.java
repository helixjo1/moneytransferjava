package com.interview;


import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.auth.Auth;

import java.util.List;

/**
 * http://www.dropwizard.io/1.0.6/docs/manual/core.html#resources
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {

    private ServiceDAO dao;

    public ServiceResource(ServiceDAO dao) {
        this.dao = dao;
    }
    
    @RolesAllowed("ADMIN")
    @GET
    @Path("/admin/accounts")
    public List<Account> getAllAccounts() {
        return dao.getAllAccounts();
    }

    @RolesAllowed("ADMIN")
    @GET
    @Path("/admin/accounts/{id}")
    public Account getAccount(@PathParam("id") String id) {
        return dao.getAccount(id);
    }
    
    @RolesAllowed("USER")
    @GET
    @Path("/user/account")
    public Account getUserAccount(@Auth User user) {
        return dao.getAccount(user.getAccountID());
    }
    

}
