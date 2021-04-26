package com.thinking.machines.msgboard.beans;
public class StudentViewBean implements java.io.Serializable
{
private String rollNumber;
private String firstName;
private String lastName;
private String emailId;
private SemesterBean semester;
private BranchBean branch;
public StudentViewBean()
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
public void setSemester(SemesterBean semester)
{
this.semester=semester;
}
public SemesterBean getSemester()
{
return this.semester;
}
public void setBranch(BranchBean branch)
{
this.branch=branch;
}
public BranchBean getBranch()
{
return this.branch;
}
}