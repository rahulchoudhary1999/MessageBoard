package com.thinking.machines.msgboard.dao;
import java.sql.*;
public class DAOConnection
{
public static String driver=null;
public static String connectionString=null;
public static String username=null;
public static String password=null;
public static Connection getConnection() throws DAOException
{
Connection connection=null;
try
{
Class.forName(driver);
connection=DriverManager.getConnection(connectionString,username,password);
}catch(Exception exception)
{
System.out.println(exception); //later on add to log and remove this
throw new DAOException("Unable to connect");
}
return connection;
}
}