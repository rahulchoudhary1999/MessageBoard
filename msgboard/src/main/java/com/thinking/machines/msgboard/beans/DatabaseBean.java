package com.thinking.machines.msgboard.beans;
public class DatabaseBean implements java.io.Serializable
{
private String driver;
private String connectionString;
private String username;
private String password;
public DatabaseBean()
{
this.driver=null;
this.connectionString=null;
this.username=null;
this.password=null;
System.out.println("DatabaseBean Instantiated");
}
public void setDriver(String driver)
{
this.driver=driver;
}
public String getDriver()
{
return this.driver;
}
public void setConnectionString(String connectionString)
{
this.connectionString=connectionString;
}
public String getConnectionString()
{
return this.connectionString;
}
public void setUsername(String username)
{
this.username=username;
}
public String getUsername()
{
return this.username;
}
public void setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
}