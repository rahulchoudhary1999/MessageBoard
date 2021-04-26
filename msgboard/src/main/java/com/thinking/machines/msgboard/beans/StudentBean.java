package com.thinking.machines.msgboard.beans;
public class StudentBean implements java.io.Serializable
{
private String rollNumber;
private String firstName;
private String lastName;
private String emailId;
private String password;
private int semesterCode;
private int branchCode;
public StudentBean()
{

}
public void setRollNumber(String rollNumber)
{
this.rollNumber=rollNumber;
}
public String getRollNumber()
{
return this.rollNumber;
}
public void setFirstName(String firstName)
{
this.firstName=firstName;
}
public String getFirstName()
{
return this.firstName;
}
public void setLastName(String lastName)
{
this.lastName=lastName;
}
public String getLastName()
{
return this.lastName;
}
public void setEmailId(String emailId)
{
this.emailId=emailId;
}
public String getEmailId()
{
return this.emailId;
}
public void setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
public void setSemesterCode(int semesterCode)
{
this.semesterCode=semesterCode;
}
public int getSemesterCode()
{
return this.semesterCode;
}
public void setBranchCode(int branchCode)
{
this.branchCode=branchCode;
}
public int getBranchCode()
{
return this.branchCode;
}
}