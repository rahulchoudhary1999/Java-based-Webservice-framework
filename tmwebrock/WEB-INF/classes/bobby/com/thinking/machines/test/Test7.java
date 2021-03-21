package bobby.com.thinking.machines.test;
import com.thinking.machines.webrock.annotations.*;
@Path("/test7")
public class Test7
{
@InjectRequestParameter("xyz")
char name;
public void setName(char name)
{
this.name=name;
}

@Path("/getByCode")
public void getByCode()
{
System.out.println("Executed");
System.out.println("Name-->"+name);
return ;
}

@Path("/delete")
public void delete(int code,String name)
{
System.out.println("Delete done");
//System.out.println("RollNumber: "+s.rollNumber+",Name :"+s.name);
return;
}
}

