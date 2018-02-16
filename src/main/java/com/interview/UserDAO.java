package com.interview;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;



/**
 * http://www.dropwizard.io/1.0.6/docs/manual/jdbi.html
 * http://jdbi.org/dbi_handle_and_statement/
 */
public class UserDAO {

    private DBI dbi;

    public UserDAO(DBI dbi) {
        this.dbi = dbi;
    }


    public User getUser(String username) {
        Handle h = dbi.open();
        try{
            return h.createQuery("SELECT * FROM users " +
                                " WHERE username=:username;")
                    .bind("username", username)
                    .mapTo(User.class)
                    .first();
        } finally {
            h.close();
        }
    }


}
