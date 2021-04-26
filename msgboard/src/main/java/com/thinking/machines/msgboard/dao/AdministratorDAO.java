package com.thinking.machines.msgboard.dao;
import com.thinking.machines.msgboard.dto.*;
import java.sql.*;
public class AdministratorDAO
{
public void add(Administrator administrator) throws DAOException
{
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("insert into administrator values(?,?,?)");
preparedStatement.setString(1,administrator.getUsername());
preparedStatement.setString(2,administrator.getPassword());
preparedStatement.setString(3,administrator.getPasswordKey());
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
System.out.println(sqlException); //later on add to log and remove this
throw new DAOException("Unable to add administrator");
}
}
public Administrator getAdministrator() throws DAOException
{
Administrator administrator=null;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select * from administrator");
ResultSet rs=preparedStatement.executeQuery();
administrator=new Administrator();
if(rs.next())
{
administrator.setUsername(rs.getString("username"));
administrator.setPassword(rs.getString("password"));
administrator.setPasswordKey(rs.getString("password_key"));
}
rs.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
System.out.println(sqlException); //later on add to log and remove this
throw new DAOException("Unable to add administrator");
}
return administrator;
}
}