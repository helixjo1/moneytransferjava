package com.interview;


import java.security.Principal;

public class User implements Principal {
   private final String username;
   private final String pass;
   private final String role;
   private final String account_id;

   public User(String name) {
       this.username = name;
       this.role = null;
       this.pass = null;
       this.account_id = null;
   }

   public User(String name, String pass, String role, String account_id) {
       this.username = name;
       this.pass = pass;
       this.role = role;
       this.account_id = account_id;
   }

   public String getName() {
       return username;
   }

   public String getAccountID() {
	   return account_id;
   }
   
   public int getId() {
       return (int) (Math.random() * 100);
   }

   public String getRole() {
       return role;
   }
   
   public boolean checkPass(String check) {
	   if(check.equals(pass)) {
		   return true;
	   }else {
		   return false; 
	   }
   }
}