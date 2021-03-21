package bobby.com.thinking.machines.test;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;
@Path("/Indore")
//@GET
public class aaa{
//@AutoWired(name="/xyz")
//private bbb b1;
//@InjectRequestParameter("abc")
int code;
@FORWARD("/Pune")
@Path("/Ujjain")
//public int add(Customer c,@RequestParameter("xyx")int a,ApplicationScope as,RequestScope rs,SessionScope ss,ApplicationDirectory ad)
public int add(Customer c,SessionScope s,ApplicationScope as,RequestScope rs,SessionScope ss,ApplicationDirectory ad)
{
System.out.println(s);
System.out.println(as);
System.out.println(rs);
System.out.println(ss);
System.out.println(ad);
System.out.println(c.firstName);
System.out.println(c.lastName);
System.out.println(c.age);
return 50;
}
//@FORWARD("/Pune")
//@OnStartUp(priority=2)
/*public int add(@RequestParameter("pqr")int code,@RequestParameter("abc")String name,Student s,@RequestParameter("xyz")char gender,ApplicationScope as,RequestScope rs1,SessionScope ss,ApplicationDirectory ad)
{
System.out.println(code);
System.out.println(name);
System.out.println(gender);
System.out.println(as);
System.out.println(rs1);
System.out.println(ss);
System.out.println(ad.getDirectory());
return 10;
}
*/
//@OnStartUp(priority=1)
@Path("/Pune")
public void sub()
{
System.out.println("Code :"+code);
}
}

class Customer
{
public String firstName;
public String lastName;
public int age;
}

class Student
{
public int rollNumber;
public String name;
public boolean isIndian;
}