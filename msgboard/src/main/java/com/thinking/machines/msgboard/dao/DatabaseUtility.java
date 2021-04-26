package com.thinking.machines.msgboard.dao;
import java.sql.*;
public class DatabaseUtility
{
private DatabaseUtility()
{
}
public static void createTables(String driver,String connectionString,String username,String password)throws DAOException
{
try
{
Class.forName(driver);
Connection connection=DriverManager.getConnection(connectionString,username,password);
Statement statement;
String sqlStatement;
sqlStatement="create table Administrator";
sqlStatement+="(";
sqlStatement+="username char(15) primary key,";
sqlStatement+="password char(100) not null,";
sqlStatement+="password_key char(100) not null";
sqlStatement+=");";
statement=connection.createStatement();
statement.executeUpdate(sqlStatement);
statement.close();
sqlStatement="create table branch";
sqlStatement+="(";
sqlStatement+="code int primary key auto_increment,";
sqlStatement+="title char(50) not null unique";
sqlStatement+=");";
statement=connection.createStatement();
statement.executeUpdate(sqlStatement);
statement.close();
sqlStatement="create table semester";
sqlStatement+="(";
sqlStatement+="code int primary key auto_increment,";
sqlStatement+="title char(25) not null unique";
sqlStatement+=");";
statement=connection.createStatement();
statement.executeUpdate(sqlStatement);
statement.close();
sqlStatement="create table student";
sqlStatement+="(";
sqlStatement+="roll_number char(15) primary key,";
sqlStatement+="first_name char(20) not null,";
sqlStatement+="last_name char(20) not null,";
sqlStatement+="email_id char(100) not null unique,";
sqlStatement+="password char(100) not null,";
sqlStatement+="password_key char(100) not null";
sqlStatement+=");";
statement=connection.createStatement();
statement.executeUpdate(sqlStatement);
statement.close();
sqlStatement="create table student_semester_mapping";
sqlStatement+="(";
sqlStatement+="roll_number char(15),";
sqlStatement+="semester_code int,";
sqlStatement+="primary key(roll_number,semester_code),";
sqlStatement+="foreign key (roll_number) references student(roll_number),";
sqlStatement+="foreign key (semester_code) references semester(code)";
sqlStatement+=");";
statement=connection.createStatement();
statement.executeUpdate(sqlStatement);
statement.close();
sqlStatement="create table student_branch_mapping";
sqlStatement+="(";
sqlStatement+="roll_number char(15),";
sqlStatement+="branch_code int,";
sqlStatement+="primary key(roll_number,branch_code),";
sqlStatement+="foreign key (roll_number) references student(roll_number),";
sqlStatement+="foreign key (branch_code) references branch(code)";
sqlStatement+=");";
statement=connection.createStatement();
statement.executeUpdate(sqlStatement);
statement.close();
connection.close();
}catch(Exception exception)
{
System.out.println(exception); //later on remove this and add to log
throw new DAOException("Unable to create tables");
}
}
}