package com.thinking.machines.msgboard.services;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.thinking.machines.msgboard.beans.*;
import com.thinking.machines.msgboard.dao.*;
import com.thinking.machines.msgboard.dto.*;
import com.thinking.machines.msgboard.utils.*;
import com.google.gson.*;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
@Controller
public class Administration 
{
@Autowired
private DatabaseBean databaseBean;
@Autowired
private MessageBoardBean messageBoardBean;
@GetMapping("/admin")
public String adminIndex()
{
if(databaseBean.getDriver()==null) return "Installer";
return "AdminIndex";
}
@PostMapping("/install")
public String installMessageBoard(InstallationBean installationBean)
{
//You need to write the code to get the tables created 
//if created,then set driver etc. in DAOConnection
//call add of AdministratorDAO
//if all is well then return InstallationSuccessful

String driver=installationBean.getDriver();
String connectionString=installationBean.getConnectionString();
String username=installationBean.getUsername();
String password=installationBean.getPassword();
String administratorUsername=installationBean.getAdministratorUsername();
String administratorPassword=installationBean.getAdministratorPassword();
try
{
DatabaseUtility.createTables(driver,connectionString,username,password);
DAOConnection.driver=driver;
DAOConnection.connectionString=connectionString;
DAOConnection.username=username;
DAOConnection.password=password;
Administrator administrator=new Administrator();
String passwordKey=EncryptionUtility.getKey();
String encryptedPassword=EncryptionUtility.encrypt(administratorPassword,passwordKey);
administrator.setUsername(administratorUsername);
administrator.setPassword(encryptedPassword);
administrator.setPasswordKey(passwordKey);
AdministratorDAO administratorDAO=new AdministratorDAO();
administratorDAO.add(administrator);
//populate Database bean
databaseBean.setDriver(driver);
databaseBean.setConnectionString(connectionString);
databaseBean.setUsername(username);
databaseBean.setPassword(password);
String jsonString=new Gson().toJson(databaseBean);
File file=new File("conf"+File.separator+"db.json");
BufferedWriter bwr=new BufferedWriter(new FileWriter(file));
bwr.write(jsonString);
bwr.flush();
bwr.close();
}catch(Exception e)
{
System.out.println(e);
return "InstallationFailed";
}
return "InstallationSuccessful";
}
@PostMapping("/addBranch")
@ResponseBody
public ActionResponse addBranch(BranchBean branchBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(branchBean==null) throw new DAOException("BranchBean required,null not allowed");
int vCode=branchBean.getCode();
String vTitle=branchBean.getTitle();
if(vCode!=0) throw new DAOException("Code should be zero as it is auto generated");
if(vTitle==null) throw new DAOException("Title Required");
vTitle=vTitle.trim();
if(vTitle.length()==0)throw new DAOException("Title length should be greater than zero");
if(vTitle.length()>50) throw new DAOException("Title cannot exceed 50 characters.");
//code to check that the title should not exist in branchList
List<BranchDTO> branchList=messageBoardBean.branchList;
String dsTitle;
for(BranchDTO branchDTO:branchList)
{
dsTitle=branchDTO.getTitle();
if(dsTitle.equalsIgnoreCase(vTitle))
{
throw new DAOException("Title ("+vTitle+") already exists");
}
}
//code to add in database
BranchDTO branchDTO=new BranchDTO();
branchDTO.setTitle(vTitle);
BranchMasterDAO branchMasterDAO=new BranchMasterDAO();
 branchMasterDAO.add(branchDTO);
//add in ds (branchList)
branchList.add(branchDTO);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(branchDTO.getCode());
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);//later on remove it
}
return actionResponse;
}
@PostMapping("/updateBranch")
@ResponseBody
public ActionResponse update(BranchBean branchBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(branchBean==null) throw new DAOException("BranchBean required,null not allowed");
int vCode=branchBean.getCode();
String vTitle=branchBean.getTitle();
if(vCode==0) throw new DAOException("Code should not be zero");
if(vTitle==null) throw new DAOException("Title Required");
vTitle=vTitle.trim();
if(vTitle.length()==0)throw new DAOException("Title length should be greater than zero");
if(vTitle.length()>50) throw new DAOException("Title cannot exceed 50 characters.");
//code to check code in ds if exist then ok if not throw exception
List<BranchDTO> branchList=messageBoardBean.branchList;
int dsCode=0;
String dsTitle=null;
boolean codeExists=false;
boolean titleExists=false;
for(BranchDTO branchDTO:branchList)
{
dsCode=branchDTO.getCode();
if(dsCode==vCode)
{
codeExists=true;
break;
}
}
if(codeExists==false) throw new DAOException("Invalid code");
for(BranchDTO branchDTO:branchList)
{
dsCode=branchDTO.getCode();
dsTitle=branchDTO.getTitle();
if(dsTitle.equalsIgnoreCase(vTitle) && dsCode!=vCode)
{
titleExists=true;
break;
}
}
if(titleExists) throw new DAOException("Title already exists against code = "+dsCode);
//code to update in database
BranchDTO branch=new BranchDTO();
branch.setCode(vCode);
branch.setTitle(vTitle);
BranchMasterDAO branchMasterDAO=new BranchMasterDAO();
branchMasterDAO.update(branch);
//update in ds (branchList)
BranchDTO b=null;
for(BranchDTO branchDTO:branchList)
{
dsCode=branchDTO.getCode();
if(dsCode==vCode)
{
b=branchDTO;
break;
}
}
b.setTitle(vTitle);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}
catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/deleteBranch")
@ResponseBody
public ActionResponse delete(BranchBean branchBean) 
{
ActionResponse actionResponse=new ActionResponse();
try
{
int code=branchBean.getCode();
if(code==0) throw new DAOException("Invalid code zero");
//code to check if code exists in ds or not
List<BranchDTO> branchList=messageBoardBean.branchList;
boolean codeExists=false;
BranchDTO b=null;
for(BranchDTO branchDTO:branchList)
{
if(code==branchDTO.getCode())
{
codeExists=true;
b=branchDTO;
break;
}
}
if(codeExists==false) throw new DAOException("Invalid code:"+code);
//code to delete from db
BranchMasterDAO branchMasterDAO=new BranchMasterDAO();
if(new StudentDAO().getStudentByBranch(code).size()>0) throw new DAOException("Cannot delete branch code exists against another student");
branchMasterDAO.delete(code);
//code to delete/remove from ds
branchList.remove(b);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}
@PostMapping("/getBranch")
@ResponseBody
public ActionResponse getBranch()
{
ActionResponse actionResponse=new ActionResponse();
try
{
List<BranchDTO> branchList=messageBoardBean.branchList;
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(branchList);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}


@PostMapping("/addSemester")
@ResponseBody
public ActionResponse addSemester(SemesterBean semesterBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(semesterBean==null) throw new DAOException("SemesterBean required,null not allowed");
int vCode=semesterBean.getCode();
String vTitle=semesterBean.getTitle();
if(vCode!=0) throw new DAOException("Code should be zero as it is auto generated");
if(vTitle==null) throw new DAOException("Title Required");
vTitle=vTitle.trim();
if(vTitle.length()==0)throw new DAOException("Title length should be greater than zero");
if(vTitle.length()>25) throw new DAOException("Title cannot exceed 25 characters.");
//code to check that the title should not exist in branchList
List<SemesterDTO> semesterList=messageBoardBean.semesterList;
String dsTitle;
for(SemesterDTO semesterDTO:semesterList)
{
dsTitle=semesterDTO.getTitle();
if(dsTitle.equalsIgnoreCase(vTitle))
{
throw new DAOException("Title ("+vTitle+") already exists");
}
}
//code to add in database
SemesterDTO semesterDTO=new SemesterDTO();
semesterDTO.setTitle(vTitle);
SemesterMasterDAO semesterMasterDAO=new SemesterMasterDAO();
semesterMasterDAO.add(semesterDTO);
//add in ds (branchList)
semesterList.add(semesterDTO);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(semesterDTO.getCode());
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);//later on remove it
}
return actionResponse;
}


@PostMapping("/updateSemester")
@ResponseBody
public ActionResponse update(SemesterBean semesterBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(semesterBean==null) throw new DAOException("SemesterBean required,null not allowed");
int vCode=semesterBean.getCode();
String vTitle=semesterBean.getTitle();
if(vCode==0) throw new DAOException("Code should not be zero");
if(vTitle==null) throw new DAOException("Title Required");
vTitle=vTitle.trim();
if(vTitle.length()==0)throw new DAOException("Title length should be greater than zero");
if(vTitle.length()>25) throw new DAOException("Title cannot exceed 25 characters.");
//code to check code in ds if exist then ok if not throw exception
List<SemesterDTO> semesterList=messageBoardBean.semesterList;
int dsCode=0;
String dsTitle=null;
boolean codeExists=false;
boolean titleExists=false;
for(SemesterDTO semesterDTO:semesterList)
{
dsCode=semesterDTO.getCode();
if(dsCode==vCode)
{
codeExists=true;
break;
}
}
if(codeExists==false) throw new DAOException("Invalid code");
for(SemesterDTO semesterDTO:semesterList)
{
dsCode=semesterDTO.getCode();
dsTitle=semesterDTO.getTitle();
if(dsTitle.equalsIgnoreCase(vTitle) && dsCode!=vCode)
{
titleExists=true;
break;
}
}
if(titleExists) throw new DAOException("Title already exists against code = "+dsCode);
//code to update in database
SemesterDTO semester=new SemesterDTO();
semester.setCode(vCode);
semester.setTitle(vTitle);
SemesterMasterDAO semesterMasterDAO=new SemesterMasterDAO();
semesterMasterDAO.update(semester);
//update in ds (branchList)
SemesterDTO s=null;
for(SemesterDTO semesterDTO:semesterList)
{
dsCode=semesterDTO.getCode();
if(dsCode==vCode)
{
s=semesterDTO;
break;
}
}
s.setTitle(vTitle);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}
catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/deleteSemester")
@ResponseBody
public ActionResponse delete(SemesterBean semesterBean) 
{
ActionResponse actionResponse=new ActionResponse();
try
{
int code=semesterBean.getCode();
if(code==0) throw new DAOException("Invalid code zero");
//code to check if code exists in ds or not
List<SemesterDTO> semesterList=messageBoardBean.semesterList;
boolean codeExists=false;
SemesterDTO s=null;
for(SemesterDTO semesterDTO:semesterList)
{
if(code==semesterDTO.getCode())
{
codeExists=true;
s=semesterDTO;
break;
}
}
if(codeExists==false) throw new DAOException("Invalid code:"+code);
//code to delete from db
SemesterMasterDAO semesterMasterDAO=new SemesterMasterDAO();
if(new StudentDAO().getStudentBySemester(code).size()>0) throw new DAOException("Cannot delete record beacuse semester code exists against another student");
semesterMasterDAO.delete(code);
//code to delete/remove from ds
semesterList.remove(s);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/getSemester")
@ResponseBody
public ActionResponse getSemester()
{
ActionResponse actionResponse=new ActionResponse();
try
{
List<SemesterDTO> semesterList=messageBoardBean.semesterList;
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(semesterList);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}


@PostMapping("/getStudentByRollNumber")
@ResponseBody
public ActionResponse getStudentByRollNumber(String rollNumber)
{
System.out.println(rollNumber);
ActionResponse actionResponse=new ActionResponse();
try
{
StudentViewBean svb=new StudentDAO().getStudentByRollNumber(rollNumber);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(svb);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/getStudents")
@ResponseBody
public ActionResponse getStudents()
{
ActionResponse actionResponse=new ActionResponse();
try
{
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(new StudentDAO().getStudents());
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/getStudentByBranch")
@ResponseBody
public ActionResponse getStudentByBranch(int branchCode)
{
ActionResponse actionResponse=new ActionResponse();
try
{
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(new StudentDAO().getStudentByBranch(branchCode));
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/getStudentBySemester")
@ResponseBody
public ActionResponse getStudentBySemester(int semesterCode)
{
ActionResponse actionResponse=new ActionResponse();
try
{
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(new StudentDAO().getStudentBySemester(semesterCode));
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/getStudentByBranchAndSemester")
@ResponseBody
public ActionResponse getStudentByBranch(int branchCode,int semesterCode)
{
ActionResponse actionResponse=new ActionResponse();
try
{
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(new StudentDAO().getStudentByBranchAndSemester(branchCode,semesterCode));
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/addStudent")
@ResponseBody
public ActionResponse addStudent(StudentBean studentBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(studentBean==null) throw new DAOException("StudentBean required");
List<StudentViewBean> studentViewBeanList=messageBoardBean.studentViewBeanList;
List<BranchDTO> branchList=messageBoardBean.branchList;
List<SemesterDTO> semesterList=messageBoardBean.semesterList;
String rollNumber=studentBean.getRollNumber();
String emailId=studentBean.getEmailId();
int semesterCode=studentBean.getSemesterCode();
int branchCode=studentBean.getBranchCode();
String password=studentBean.getPassword();
System.out.println(rollNumber);
System.out.println(emailId);
System.out.println(semesterCode);
System.out.println(branchCode);
boolean rollNumberFound=false;
boolean emailFound=false;
boolean branchCodeFound=false;
boolean semesterCodeFound=false;
for(StudentViewBean svb:studentViewBeanList)
{
if(rollNumberFound==false && rollNumber.equals(svb.getRollNumber()))
{
rollNumberFound=true;
}
if(emailFound==false && emailId.equals(svb.getEmailId()))
{
emailFound=true;
}
if(emailFound==true && rollNumberFound==true)
{
break;
}
}
if(rollNumberFound==false && emailFound==true)
{
throw new DAOException("Email id : "+emailId+" already exists");
}
if(rollNumberFound==true && emailFound==false)
{
throw new DAOException("Rollnumber : "+rollNumber+" exists against another student");
}
if(rollNumberFound==true && emailFound==true)
{
throw new DAOException("Rollnumber : "+rollNumber+" and email id : "+emailId+" exists against another student");
}
for(BranchDTO branchDTO:branchList)
{
if(branchDTO.getCode()==branchCode)
{
branchCodeFound=true;
break;
}
}
for(SemesterDTO semesterDTO:semesterList)
{
if(semesterDTO.getCode()==semesterCode)
{
semesterCodeFound=true;
break;
}
}
if(branchCodeFound==false && semesterCodeFound==true)
{
throw new DAOException("Invalid branch code");
}
if(branchCodeFound==true && semesterCodeFound==false)
{
throw new DAOException("Invalid semester code");
}
if(branchCodeFound==false && semesterCodeFound==false)
{
throw new DAOException("Invalid branch code and semester code");
}
if(password==null) throw new DAOException("Password Required");
StudentDTO s=new StudentDTO();
s.setRollNumber(studentBean.getRollNumber());
s.setFirstName(studentBean.getFirstName());
s.setLastName(studentBean.getLastName());
s.setEmailId(studentBean.getEmailId());
s.setPassword(studentBean.getPassword());
s.setSemesterCode(studentBean.getSemesterCode());
s.setBranchCode(studentBean.getBranchCode());
//add to database
StudentDAO studentDAO=new StudentDAO();
studentDAO.add(s);
//add to ds
studentViewBeanList.add(studentDAO.getStudentByRollNumber(studentBean.getRollNumber()));
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}


@PostMapping("/updateStudent")
@ResponseBody
public ActionResponse updateStudent(StudentBean studentBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(studentBean==null) throw new DAOException("StudentBean required");
List<StudentViewBean> studentViewBeanList=messageBoardBean.studentViewBeanList;
String rollNumber=studentBean.getRollNumber();
String emailId=studentBean.getEmailId();
boolean rollNumberFound=false;
boolean emailFound=false;
for(StudentViewBean svb:studentViewBeanList)
{
if(rollNumberFound==false && rollNumber.equals(svb.getRollNumber()))
{
rollNumberFound=true;
}
if(emailFound==false && emailId.equals(svb.getEmailId()) && !rollNumber.equals(svb.getRollNumber()))
{
emailFound=true;
}
if(emailFound==true && rollNumberFound==true)
{
break;
}
}
if(rollNumberFound==false)
{
throw new DAOException("Invalid roll number");
}
if(emailFound==true)
{
throw new DAOException("Email Id already exists");
}
StudentDTO s=new StudentDTO();
s.setRollNumber(studentBean.getRollNumber());
s.setFirstName(studentBean.getFirstName());
s.setLastName(studentBean.getLastName());
s.setEmailId(studentBean.getEmailId());
s.setPassword(studentBean.getPassword());
StudentDAO studentDAO=new StudentDAO();
StudentViewBean studentViewBean=studentDAO.getStudentByRollNumber(studentBean.getRollNumber());
//update to database
studentDAO.update(s);
//update to ds
studentViewBeanList.remove(studentViewBean);
studentViewBeanList.add(studentDAO.getStudentByRollNumber(studentBean.getRollNumber()));
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@PostMapping("/deleteStudent")
@ResponseBody
public ActionResponse deleteStudent(StudentBean studentBean)
{
ActionResponse actionResponse=new ActionResponse();
try
{
if(studentBean==null) throw new DAOException("StudentBean required");
List<StudentViewBean> studentViewBeanList=messageBoardBean.studentViewBeanList;
String rollNumber=studentBean.getRollNumber();
boolean rollNumberFound=false;
for(StudentViewBean svb:studentViewBeanList)
{
if(rollNumber.equals(svb.getRollNumber()))
{
rollNumberFound=true;
break;
}
}
if(rollNumberFound==false)
{
throw new DAOException("Invalid roll number");
}
StudentDAO studentDAO=new StudentDAO();
StudentViewBean studentViewBean=studentDAO.getStudentByRollNumber(rollNumber);
//delete to database
studentDAO.delete(rollNumber);
//delete to ds
studentViewBeanList.remove(studentViewBean);
actionResponse.isSuccess(true);
actionResponse.setException(null);
actionResponse.setResult(null);
}catch(Exception exception)
{
actionResponse.isSuccess(false);
actionResponse.setException(exception.getMessage());
actionResponse.setResult(null);
System.out.println(exception);
}
return actionResponse;
}

@GetMapping("/testA")
@ResponseBody
public String methodA(@RequestParam("aaa")String g,HttpServletRequest request)
{
HttpSession ss=request.getSession();
ss.setAttribute("aaaaa",g);
return (String)ss.getAttribute("aaaaa");
}

@GetMapping("/testB")
@ResponseBody
public String methodB(HttpSession session)
{
String k=(String)session.getAttribute("username");
System.out.println(k);
return k;
}

@GetMapping("/testC")
@ResponseBody
public String methodC(HttpSession ss)
{
ss.invalidate();
return "Successfully invalidate";
}

@PostMapping("/authenticateAdministrator")
public String isAuthenticateAdministrator(AdministratorBean administratorBean,HttpServletRequest request)
{
try
{
String username=administratorBean.getUsername();
String password=administratorBean.getPassword();
DAOConnection.driver=databaseBean.getDriver();
DAOConnection.connectionString=databaseBean.getConnectionString();
DAOConnection.username=databaseBean.getUsername();
DAOConnection.password=databaseBean.getPassword();
Administrator administrator=new AdministratorDAO().getAdministrator();
if(!username.equals(administrator.getUsername())) throw new DAOException("Invalid username: "+username);
String secretKey=administrator.getPasswordKey();
String encryptPassword=administrator.getPassword();
String decryptPassword=EncryptionUtility.decrypt(encryptPassword,secretKey);
if(!decryptPassword.equals(password)) throw new DAOException("Invalid password: "+password);
request.getSession().setAttribute("username",username);
return "AdminHome";
}
catch(Exception exception)
{
System.out.println(exception);
return "AdminIndex";
}
}
@GetMapping("/logout")
public String logout(HttpSession session)
{
session.invalidate();
return "AdminIndex";
}
}