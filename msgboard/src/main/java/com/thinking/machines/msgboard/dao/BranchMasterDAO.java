package com.thinking.machines.msgboard.dao;
import com.thinking.machines.msgboard.beans.*;
import com.thinking.machines.msgboard.dto.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.google.gson.*;
public class BranchMasterDAO
{
private DatabaseBean databaseBean;
public BranchMasterDAO() throws DAOException
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
public void add(BranchDTO branchDTO) throws DAOException
{
String title;
try
{
title=branchDTO.getTitle();
Class.forName(databaseBean.getDriver());
Connection connection;
connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("insert into branch (title) values (?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,title);
preparedStatement.executeUpdate();
ResultSet resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int code=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
branchDTO.setCode(code);
System.out.println("Title :"+title+"and Code:"+branchDTO.getCode());
}
catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}
public void update(BranchDTO branchDTO) throws DAOException
{
try
{
int code=branchDTO.getCode();
String title=branchDTO.getTitle();
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
PreparedStatement preparedStatement;
String sqlStatement="update branch set title=? where code=?";
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
String sqlStatement="delete from branch where code=?";
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
public List<BranchDTO> getBranches() throws DAOException
{
List<BranchDTO> branches=new LinkedList<>();
try
{
Class.forName(databaseBean.getDriver());
Connection connection=DriverManager.getConnection(databaseBean.getConnectionString(),databaseBean.getUsername(),databaseBean.getPassword());
String sqlStatement="select * from branch";
PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement);
ResultSet resultSet=preparedStatement.executeQuery();
BranchDTO branch;
while(resultSet.next())
{
branch=new BranchDTO();
branch.setCode(resultSet.getInt("code"));
branch.setTitle(resultSet.getString("title"));
branches.add(branch);
}
resultSet.close();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
return branches;
}
}