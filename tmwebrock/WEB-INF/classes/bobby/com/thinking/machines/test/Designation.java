package bobby.com.thinking.machines.test;
import javax.servlet.*;
import javax.servlet.http.*;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;
@InjectSessionScope
@Path("/designation")
public class Designation
{
private SessionScope sessionScope;
public void setSessionScope(SessionScope sessionScope)
{
this.sessionScope=sessionScope;
System.out.println("Session object successfully set");
}
@Path("/addDataInSessionScope")
public void addDataInSessionScope()
{
this.sessionScope.setAttribute("title","Manager");
System.out.println("Title set");
}
@Path("/getDataSessionScope")
public void getDataInSessionScope()
{
System.out.println(this.sessionScope.getAttribute("title"));
}

}