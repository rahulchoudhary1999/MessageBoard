package com.thinking.machines.msgboard;
import java.io.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*; //bean ke liye hai
import com.google.gson.*;
import com.thinking.machines.msgboard.beans.*;
import com.thinking.machines.msgboard.dao.*;
import com.thinking.machines.msgboard.dto.*;
@SpringBootApplication
public class MsgboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsgboardApplication.class, args);
	}
@Bean
public DatabaseBean getDatabaseBean()
{
System.out.println("getDatabaseBean got called");
DatabaseBean databaseBean=null;
try
{
File file=new File("conf"+File.separator+"db.json");
if(file.exists())
{
Gson gson=new Gson();
databaseBean=gson.fromJson(new FileReader(file.getAbsolutePath()),DatabaseBean.class);
}
else
{
databaseBean=new DatabaseBean();
}
}catch(Exception exception)
{
System.out.println(exception); //later on it should be logged somewhere
}
return databaseBean;
}
@Bean
public MessageBoardBean getMessageBoardBean()
{
System.out.println("getMessageBoardBean got called");
MessageBoardBean messageBoardBean=null;
BranchMasterDAO branchMasterDAO;
SemesterMasterDAO semesterMasterDAO;
StudentDAO studentDAO;
try
{
messageBoardBean=new MessageBoardBean();
branchMasterDAO=new BranchMasterDAO();
semesterMasterDAO=new SemesterMasterDAO();
studentDAO=new StudentDAO();
messageBoardBean.branchList=branchMasterDAO.getBranches();
messageBoardBean.semesterList=semesterMasterDAO.getSemesters();
messageBoardBean.studentViewBeanList=studentDAO.getStudents();
}catch(Exception exception)
{
messageBoardBean=new MessageBoardBean();
System.out.println(exception); 
}
return messageBoardBean;
}
}
