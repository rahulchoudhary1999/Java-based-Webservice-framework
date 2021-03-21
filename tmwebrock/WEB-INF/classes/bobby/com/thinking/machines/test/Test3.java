package bobby.com.thinking.machines.test;
import javax.servlet.*;
import javax.servlet.http.*;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;
@InjectApplicationScope
@Path("/test3")
public class Test3
{
private ApplicationScope applicationScope;
public void setApplicationScope(ApplicationScope applicationScope)
{
this.applicationScope=applicationScope;
System.out.println("Application scope object successfully set");
}
@Path("/setDataInApplicationScope")
public void setDataInApplicationScope()
{
this.applicationScope.setAttribute("country","India");
System.out.println("Title set");
}
@Path("/getDataFromApplicationScope")
public void getDataFromApplicationScope()
{
System.out.println(this.applicationScope.getAttribute("country"));
}

}