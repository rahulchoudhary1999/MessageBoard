package com.thinking.machines.msgboard.dto;
import java.util.*;
public class BranchDTO
{
private int code;
private String title;
public BranchDTO()
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