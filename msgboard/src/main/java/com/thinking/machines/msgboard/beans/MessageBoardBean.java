package com.thinking.machines.msgboard.beans;
import com.thinking.machines.msgboard.dto.*;
import java.util.*;
public class MessageBoardBean implements java.io.Serializable
{
public static List<BranchDTO> branchList;
public static List<SemesterDTO> semesterList;
public static List<StudentViewBean> studentViewBeanList;
public MessageBoardBean()
{
branchList=new LinkedList<>();
semesterList=new LinkedList<>();
studentViewBeanList=new LinkedList<>();
}
}