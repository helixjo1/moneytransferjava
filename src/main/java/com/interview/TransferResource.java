package com.interview;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.auth.Auth;

import java.math.BigDecimal;
import java.util.List;

/**
 * http://www.dropwizard.io/1.0.6/docs/manual/core.html#resources
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferResource {

    private ServiceDAO dao;

    public TransferResource(ServiceDAO dao) {
        this.dao = dao;
    }

    @RolesAllowed("USER")
    @POST
    @Path("/user/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Transfer transferFromAccount(@Auth User user, Transfer t) {
    	Transfer transfer = t;
    	transfer.setUser(user.getName());
        transfer.setOriginatorId(user.getAccountID());
        
        if(transfer.originatorId().equals(transfer.destinationId())) {
        	transfer.setResult("error", "Operation not allowed");
    		return dao.logTransfer(transfer);
        }
        
    	Account originAcc = dao.getAccount(user.getAccountID());
    	Account destAcc =dao.getAccount(transfer.destinationId()); 
    	BigDecimal originBal = originAcc.getBalance();
    
    	
    	if(destAcc==null||originAcc==null) {
    		transfer.setResult("error", "One or more of the accounts specified does not exist");
    		return dao.logTransfer(transfer);
    	}
    	else if(originBal.compareTo(transfer.amount()) == -1) {
    		transfer.setResult("error", "Not enough funds in account");
    		return dao.logTransfer(transfer);
    	} 
    	else {
    	
    		try {
				dao.updateTransferBalances(t);
				transfer.setResult("success", "Transfer complete");
			} catch (Exception e) {
				transfer.setResult("error", "Something went wrong");
				transfer.setError(e.getMessage());
				e.printStackTrace();
			}		
    		return dao.logTransfer(transfer);
    	}

    }
    

    //Potential utility methods - Untested
    
//    @RolesAllowed("USER")
//    @GET
//    @Path("/transfers/in/{id}")
//    public List<Transfer> getTransfersToAccount(@Auth User user, @PathParam("id") String id) {
//    	if(user.getAccountID().equals(id)) {
//    		return dao.getTransfersByDestination(id);
//    	}
//    	List<Transfer> empty = null;
//    	return empty;
//    }
//    
//
//    @RolesAllowed("USER")
//    @GET
//    @Path("/transfers/out/{id}")
//    public List<Transfer> getTransfersFromAccount(@Auth User user, @PathParam("id") String id) {
//    	if(user.getAccountID().equals(id)) {
//    		return dao.getTransfersByOriginator(id);
//    	}
//    	List<Transfer> empty = null;
//    	return empty;
//    }
//    
//    @RolesAllowed("USER")
//    @GET
//    @Path("/user/transfers")
//    public List<Transfer> getTransfersByUser(@Auth User user) {
//    	
//        return dao.getTransfersByUser(user.getAccountID());
//    }
    
    @RolesAllowed("ADMIN")
    @GET
    @Path("/admin/transfers/all")
    public List<Transfer> getAllTransfers() {
    	
        return dao.getAllTransfers();
    }
}
