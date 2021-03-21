package bobby.com.thinking.machines.test;
import com.thinking.machines.webrock.annotations.*;
@Path("/test8")
public class Test8
{

@SecuredAccess(checkPost="bobby.com.thinking.machines.test.Test9",guard="efgh")
@Path("/getByCode")
public void getByCode()
{
System.out.println("Executed");
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

