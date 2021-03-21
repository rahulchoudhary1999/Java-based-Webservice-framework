package bobby.com.thinking.machines.test;
import javax.servlet.*;
import javax.servlet.http.*;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.pojo.*;

@Path("/test5")
public class Test5
{
@AutoWired(name="city")
private String city;
@AutoWired(name="country")
private String country;
public void setCity(String city)
{
this.city=city;
}
public void setCountry(String country)
{
this.country=country;
}
@Path("/getCity")
public void getCity()
{
System.out.println(this.city);
}
@Path("/getCountry")
public void getCountry()
{
System.out.println(this.country);
}
}