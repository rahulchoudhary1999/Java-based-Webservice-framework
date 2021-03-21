package bobby.com.thinking.machines.test;
import com.thinking.machines.webrock.annotations.*;
@Path("/student")

public class Student{
@FORWARD("/test1.html")
@Path("/addStudent")
public void addStudent()
{
System.out.println("add_student");
}

@Path("/deleteStudent")
public void deleteStudent()
{
System.out.println("delete_student");
}
}