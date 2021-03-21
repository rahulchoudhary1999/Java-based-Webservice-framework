package bobby.com.thinking.machines.test;
import javax.servlet.*;
import javax.servlet.http.*;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;
@InjectSessionScope
@Path("/test2")
public class Test2
{
private SessionScope sessionScope;
public void setSessionScope(SessionScope sessionScope)
{
this.sessionScope=sessionScope;
}
@Path("/setDataInSessionScope")
public void setDataInSessionScope()
{
this.sessionScope.setAttribute("city","Ujjain");
}
@Path("/getDataFromSessionScope")
public void getDataInSessionScope()
{
System.out.println(this.sessionScope.getAttribute("city"));
}
}