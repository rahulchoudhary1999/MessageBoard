package com.thinking.machines.msgboard.dto;
public class Administrator
{
private String username;
private String password;
private String passwordKey;
public Administrator()
{
//do nothing
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
public void setPasswordKey(String passwordKey)
{
this.passwordKey=passwordKey;
}
public String getPasswordKey()
{
return this.passwordKey;
}
}