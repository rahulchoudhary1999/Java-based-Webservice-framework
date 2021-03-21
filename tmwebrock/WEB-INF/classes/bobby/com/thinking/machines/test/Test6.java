package bobby.com.thinking.machines.test;
import com.thinking.machines.webrock.annotations.*;
//import com.thinking.machines.webrock.annotations.*;
@Path("/test6")
public class Test6
{
@Path("/getByCode")
@FORWARD("/delete")
public Student getByCode()
{
Student s=new Student();
s.rollNumber=101;
s.name="Rahul Choudhary";
System.out.println("Executed");
return s;
}

@Path("/delete")
//public void delete(int code,String name)
public void delete(Student s)
{
System.out.println("Delete done");
System.out.println("RollNumber: "+s.rollNumber+",Name :"+s.name);
return;
}
}

class Student
{
public int rollNumber;
public int name;
}