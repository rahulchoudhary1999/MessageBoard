package com.thinking.machines.msgboard.beans;
public class InstallationBean implements java.io.Serializable
{
private String driver;
private String connectionString;
private String username;
private String password;
private String administratorUsername;
private String administratorPassword;
public InstallationBean()
{
this.driver=null;
this.connectionString=null;
this.username=null;
this.password=null;
this.administratorUsername=null;
this.administratorPassword=null;
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
public void setAdministratorUsername(String administratorUsername)
{
this.administratorUsername=administratorUsername;
}
public String getAdministratorUsername()
{
return this.administratorUsername;
}
public void setAdministratorPassword(String administratorPassword)
{
this.administratorPassword=administratorPassword;
}
public String getAdministratorPassword()
{
return this.administratorPassword;
}
}