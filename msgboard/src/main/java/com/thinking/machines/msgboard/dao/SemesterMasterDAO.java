package com.thinking.machines.msgboard.dao;
import com.thinking.machines.msgboard.beans.*;
import com.thinking.machines.msgboard.dto.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.google.gson.*;
public class SemesterMasterDAO
{
private DatabaseBean databaseBean;
public SemesterMasterDAO() throws DAOException
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
System.out.println(exception.getMessage()); //later on it should be logged somewhere
}
}
public void add(SemesterDTO semesterDTO) throws DAOException
{
String title;
try
{
title=semesterDTO.getTitle();
Class.forName(databaseBean.getDriver());
Connection connection;
connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("insert into semester (title) values (?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,title);
preparedStatement.executeUpdate();
ResultSet resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int code=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
semesterDTO.setCode(code);
System.out.println("Title :"+title+"and Code:"+semesterDTO.getCode());
}
catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}
public void update(SemesterDTO semesterDTO) throws DAOException
{
try
{
int code=semesterDTO.getCode();
String title=semesterDTO.getTitle();
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
PreparedStatement preparedStatement;
String sqlStatement="update semester set title=? where code=?";
preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setString(1,title);
preparedStatement.setInt(2,code);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
System.out.println("Updated");
}
catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}
public void delete(int code) throws DAOException
{
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="delete from semester where code=?";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
preparedStatement.setInt(1,code);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}
catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}
public List<SemesterDTO> getSemesters() throws DAOException
{
List<SemesterDTO> semesters=new LinkedList<>();
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="select * from semester";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
ResultSet resultSet=preparedStatement.executeQuery();
SemesterDTO semester;
while(resultSet.next())
{
semester=new SemesterDTO();
semester.setCode(resultSet.getInt("code"));
semester.setTitle(resultSet.getString("title"));
semesters.add(semester);
}
resultSet.close();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return semesters;
}
}