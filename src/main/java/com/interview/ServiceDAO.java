package com.interview;

import org.skife.jdbi.v2.Batch;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionIsolationLevel;
import org.skife.jdbi.v2.TransactionStatus;
import org.skife.jdbi.v2.Update;
import org.skife.jdbi.v2.VoidTransactionCallback;


import java.util.List;


/**
 * http://www.dropwizard.io/1.0.6/docs/manual/jdbi.html
 * http://jdbi.org/dbi_handle_and_statement/
 */
public class ServiceDAO {

    private DBI dbi;

    public ServiceDAO(DBI dbi) {
        this.dbi = dbi;
    }


    public List<Account> getAllAccounts() {
        Handle h  = dbi.open();
        try{
            return h.createQuery("SELECT * FROM accounts;")
                    .mapTo(Account.class)
                    .list();
        } finally {
            h.close();
        }
    }

    public Account getAccount(String id) {
        Handle h = dbi.open();
        try{
            return h.createQuery("SELECT * FROM accounts " +
                                " WHERE id=:id;")
                    .bind("id", id)
                    .mapTo(Account.class)
                    .first();
        } finally {
            h.close();
        }
    }

   //Handles actual transfer from one account to another
    public void updateTransferBalances(Transfer t) throws Exception{
         
    
        	dbi.inTransaction(TransactionIsolationLevel.SERIALIZABLE, new VoidTransactionCallback() {

    			@Override
    			protected void execute(Handle h, TransactionStatus status) throws Exception {
    			
    				 Account orig = h.createQuery("SELECT * FROM accounts " +
                             						" WHERE id=:id;")
    						 		.bind("id", t.originatorId())
    						 		.mapTo(Account.class)
    						 		.first();
    				 if(orig.getBalance().compareTo(t.amount()) == -1) {
    					 throw new IllegalArgumentException("Balance too low"); 
    				 }
    				 Batch batch = h.createBatch();
    				 
    				 
    				 batch.add("UPDATE accounts Set balance=balance-"+t.amount()+" WHERE id ='"+t.originatorId()+"';");
    				 batch.add("UPDATE accounts Set balance=balance+"+t.amount()+" WHERE id ='"+t.destinationId()+"';");
    				 batch.execute();
    			}
        	 
        	 });
        
    }
    
    public Transfer logTransfer(Transfer t) {
    	Handle h  = dbi.open();
    	try {
    		Update s = h.createStatement("INSERT into transfers(user_id, originatorId, destinationId, amount, result, error, message) values ( '"+t.user()+"', '"+t.originatorId()+"', '"+t.destinationId()+"', "+t.amount()+", '"+t.result()+"', '"+t.error()+"', '"+t.message()+"');");
        	s.execute();
    	}finally {
    		h.close();
    	}
    	
    	return t;
    }
    
    //returns all transfers, only accessible to admins 
    public List<Transfer> getAllTransfers() {
        Handle h  = dbi.open();
        try{
            return h.createQuery("SELECT * FROM transfers;")
                    .mapTo(Transfer.class)
                    .list();
        } finally {
            h.close();
        }
    }
    
    //returns all SUCCESSFUL transfers for a given user
    public List<Transfer> getTransfersByUser(String u) {
        Handle h  = dbi.open();
        try{
            return h.createQuery("SELECT * FROM transfers"+
                    " WHERE user=:user; AND result='SUCESS'")
            		.bind("user", u)
                    .mapTo(Transfer.class)
                    .list();
        } finally {
            h.close();
        }
    }
    
    //returns all SUCCESSFUL transfers for a given originator
    public List<Transfer> getTransfersByOriginator(String origin) {
        Handle h  = dbi.open();
        try{
            return h.createQuery("SELECT * FROM transfers"+
                    " WHERE originatorId=:originatorId; AND result='SUCESS'")
            		.bind("originator", origin)
                    .mapTo(Transfer.class)
                    .list();
        } finally {
            h.close();
        }
    }
    
    //returns all SUCCESSFUL transfers for a given destination
    public List<Transfer> getTransfersByDestination(String dest) {
        Handle h  = dbi.open();
        try{
            return h.createQuery("SELECT * FROM transfers"+
                    " WHERE destiantionId=:destinationId; AND result='SUCESS'")
            		.bind("destination", dest)
                    .mapTo(Transfer.class)
                    .list();
        } finally {
            h.close();
        }
    }

}
