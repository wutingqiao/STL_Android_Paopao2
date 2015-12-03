package com.example.config;

public class URLs {
   public static final String BASIC_URL="http://localhost:8080/STL_Android_Paopao";
   public static final String LOGIN= BASIC_URL+"/users/users?action=validate";
   public static final String REGISTER= BASIC_URL+"/users/users?action=add";
   public static final String RESETPWD= BASIC_URL+"/users/users?action=forgotpwd";
   public static final String JOBLIST= BASIC_URL+"/joblist/joblist?action=list";
   public static final String JOBVIEW= BASIC_URL+"/joblist/joblist?action=view&jobID=%s";
}
