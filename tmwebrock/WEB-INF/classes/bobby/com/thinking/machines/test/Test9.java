package bobby.com.thinking.machines.test;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.exceptions.*;
@Path("/test9")
//@InjectApplicationScope
public class Test9
{
public void efgh() throws ServiceException
{
System.out.println("Efgh executed");
throw new ServiceException("Not Accessible");
}
}

