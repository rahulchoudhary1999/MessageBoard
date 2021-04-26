package com.thinking.machines.msgboard.dao;
import com.thinking.machines.msgboard.beans.*;
import com.thinking.machines.msgboard.dto.*;
import com.thinking.machines.msgboard.utils.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.google.gson.*;
public class StudentDAO
{
private DatabaseBean databaseBean;
public StudentDAO() throws DAOException
{
this.databaseBean=null;
try
{
File file=new File("conf"+File.separator+"db.json");
if(file.exists())
{
Gson gson=new Gson();
this.databaseBean=gson.fromJson(new FileReader(file.getAbsolutePath()),DatabaseBean.class);
}
else
{
this.databaseBean=new DatabaseBean();
}
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public void add(StudentDTO studentDTO) throws DAOException
{
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String rollNumber=studentDTO.getRollNumber();
String firstName=studentDTO.getFirstName();
String lastName=studentDTO.getLastName();
String emailId=studentDTO.getEmailId();
String password=studentDTO.getPassword();
String passwordKey=EncryptionUtility.getKey();
String encryptedPassword=EncryptionUtility.encrypt(password,passwordKey);
int semesterCode=studentDTO.getSemesterCode();
int branchCode=studentDTO.getBranchCode();
String sqlStatement="insert into student (roll_number,first_name,last_name,email_id,password,password_key) values(?,?,?,?,?,?)";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
preparedStatement.setString(2,firstName);
preparedStatement.setString(3,lastName);
preparedStatement.setString(4,emailId);
preparedStatement.setString(5,encryptedPassword);
preparedStatement.setString(6,passwordKey);
preparedStatement.executeUpdate();
preparedStatement.close();
sqlStatement="insert into student_branch_mapping (roll_number,branch_code) values(?,?)";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
preparedStatement.setInt(2,branchCode);
preparedStatement.executeUpdate();
preparedStatement.close();
sqlStatement="insert into student_semester_mapping (roll_number,semester_code) values(?,?)";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
preparedStatement.setInt(2,semesterCode);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public void update(StudentDTO studentDTO) throws DAOException
{
try
{
//semestercode branchcode rollnumber not updatable
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String rollNumber=studentDTO.getRollNumber();
String firstName=studentDTO.getFirstName();
String lastName=studentDTO.getLastName();
String emailId=studentDTO.getEmailId();
String password=studentDTO.getPassword();
String passwordKey=EncryptionUtility.getKey();
String encryptedPassword=EncryptionUtility.encrypt(password,passwordKey);
String sqlStatement="update student set first_name=?,last_name=?,email_id=?,password=?,password_key=? where roll_number=?";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,firstName);
preparedStatement.setString(2,lastName);
preparedStatement.setString(3,emailId);
preparedStatement.setString(4,encryptedPassword);
preparedStatement.setString(5,passwordKey);
preparedStatement.setString(6,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
System.out.println("Updated");
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public void delete(String rollNumber) throws DAOException
{
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="delete from student_branch_mapping where roll_number=?";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
sqlStatement="delete from student_semester_mapping where roll_number=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
sqlStatement="delete from student where roll_number=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
System.out.println("Deleted");
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public List<StudentViewBean> getStudents() throws DAOException
{
List<StudentViewBean> studentViewBeanList=new LinkedList<>();
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="select * from student";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
ResultSet resultSet=preparedStatement.executeQuery();
String rollNumber;
String firstName;
String lastName;
String emailId;
StudentViewBean studentViewBean;
while(resultSet.next())
{
studentViewBean=new StudentViewBean();
rollNumber=resultSet.getString("roll_number");
firstName=resultSet.getString("first_name");
lastName=resultSet.getString("last_name");
emailId=resultSet.getString("email_id");
studentViewBean.setRollNumber(rollNumber);
studentViewBean.setFirstName(firstName);
studentViewBean.setLastName(lastName);
studentViewBean.setEmailId(emailId);
studentViewBeanList.add(studentViewBean);
}
resultSet.close();
preparedStatement.close();
int branchCode=0;
int semesterCode=0;
BranchBean branchBean;
SemesterBean semesterBean;
for(StudentViewBean svb:studentViewBeanList)
{
branchBean=new BranchBean();
semesterBean=new SemesterBean();
rollNumber=svb.getRollNumber();
sqlStatement="select branch_code from student_branch_mapping where roll_number=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
branchCode=resultSet.getInt("branch_code");
}
resultSet.close();
preparedStatement.close();

sqlStatement="select semester_code from student_semester_mapping where roll_number=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
semesterCode=resultSet.getInt("semester_code");
}
resultSet.close();
preparedStatement.close();

sqlStatement="select * from branch where code=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,branchCode);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
branchBean.setCode(resultSet.getInt("code"));
branchBean.setTitle(resultSet.getString("title"));
}
resultSet.close();
preparedStatement.close();


sqlStatement="select * from semester where code=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,semesterCode);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
semesterBean.setCode(resultSet.getInt("code"));
semesterBean.setTitle(resultSet.getString("title"));
}
resultSet.close();
preparedStatement.close();

svb.setSemester(semesterBean);
svb.setBranch(branchBean);
}

connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return studentViewBeanList;
}
public StudentViewBean getStudentByRollNumber(String rollNumber) throws DAOException
{
StudentViewBean studentViewBean=null;
try
{
int semesterCode=0;
int branchCode=0;
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="select branch_code from student_branch_mapping where roll_number=?";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
branchCode=resultSet.getInt("branch_code");
}
resultSet.close();
preparedStatement.close();
sqlStatement="select semester_code from student_semester_mapping where roll_number=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
semesterCode=resultSet.getInt("semester_code");
}
resultSet.close();
preparedStatement.close();
sqlStatement="select * from branch where code=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,branchCode);
resultSet=preparedStatement.executeQuery();
BranchBean branchBean=new BranchBean();
if(resultSet.next())
{
branchBean.setCode(resultSet.getInt("code"));
branchBean.setTitle(resultSet.getString("title"));
}
resultSet.close();
preparedStatement.close();

sqlStatement="select * from semester where code=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,semesterCode);
resultSet=preparedStatement.executeQuery();
SemesterBean semesterBean=new SemesterBean();
if(resultSet.next())
{
semesterBean.setCode(resultSet.getInt("code"));
semesterBean.setTitle(resultSet.getString("title"));
}
resultSet.close();
preparedStatement.close();

sqlStatement="select * from student where roll_number=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,rollNumber);
resultSet=preparedStatement.executeQuery();
studentViewBean=new StudentViewBean();
if(resultSet.next())
{
studentViewBean.setRollNumber(resultSet.getString("roll_number"));
studentViewBean.setFirstName(resultSet.getString("first_name"));
studentViewBean.setLastName(resultSet.getString("last_name"));
studentViewBean.setEmailId(resultSet.getString("email_id"));
studentViewBean.setSemester(semesterBean);
studentViewBean.setBranch(branchBean);
}
resultSet.close();
preparedStatement.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return studentViewBean;
}


public List<StudentViewBean> getStudentByBranch(int branchCode) throws DAOException
{
List<StudentViewBean> studentListByBranch=new LinkedList<>();
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="select roll_number from student_branch_mapping where branch_code=?";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,branchCode);
ResultSet resultSet=preparedStatement.executeQuery();
List<String> rollNumberListByBranch=new LinkedList<>();
while(resultSet.next())
{
rollNumberListByBranch.add(resultSet.getString("roll_number"));
}
resultSet.close();
preparedStatement.close();
for(String rollNumber:rollNumberListByBranch)
{
studentListByBranch.add(getStudentByRollNumber(rollNumber));
}
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return studentListByBranch;
}

public List<StudentViewBean> getStudentBySemester(int semesterCode) throws DAOException
{
List<StudentViewBean> studentListBySemester=new LinkedList<>();
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="select roll_number from student_semester_mapping where semester_code=?";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,semesterCode);
ResultSet resultSet=preparedStatement.executeQuery();
List<String> rollNumberListBySemester=new LinkedList<>();
while(resultSet.next())
{
rollNumberListBySemester.add(resultSet.getString("roll_number"));
}
resultSet.close();
preparedStatement.close();
for(String rollNumber:rollNumberListBySemester)
{
studentListBySemester.add(getStudentByRollNumber(rollNumber));
}
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return studentListBySemester;
}

public List<StudentViewBean> getStudentByBranchAndSemester(int branchCode,int semesterCode) throws DAOException
{
List<StudentViewBean> studentListByBranchAndSemester=new LinkedList<>();
try
{
List<StudentViewBean> studentListByBranch=getStudentByBranch(branchCode);
List<StudentViewBean> studentListBySemester=getStudentBySemester(semesterCode);
for(StudentViewBean s1:studentListByBranch)
{
for(StudentViewBean s2:studentListBySemester)
{
if(s1.getRollNumber().equals(s2.getRollNumber())) 
{
studentListByBranchAndSemester.add(s2);
break;
}
}
}
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return studentListByBranchAndSemester;
}
}