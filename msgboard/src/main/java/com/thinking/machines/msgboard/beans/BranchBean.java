package com.thinking.machines.msgboard.beans;
public class BranchBean implements java.io.Serializable
{
private int code;
private String title;
public BranchBean()
{
this.code=0;
this.title="";
}
public void setCode(int code)
{
this.code=code;
}
public int getCode()
{
return this.code;
}
public void setTitle(String title)
{
this.title=title;
}
public String getTitle()
{
return this.title;
}
}