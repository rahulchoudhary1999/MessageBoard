package com.thinking.machines.msgboard.dto;
public class ActionResponse
{
private boolean isSuccess;
private String exception;
private Object result;
public void isSuccess(boolean isSuccess)
{
this.isSuccess=isSuccess;
}
public boolean isSuccess()
{
return this.isSuccess;
}
public void setException(String exception)
{
this.exception=exception;
}
public String getException()
{
return this.exception;
}
public void setResult(Object result)
{
this.result=result;
}
public Object getResult()
{
return this.result;
}
}