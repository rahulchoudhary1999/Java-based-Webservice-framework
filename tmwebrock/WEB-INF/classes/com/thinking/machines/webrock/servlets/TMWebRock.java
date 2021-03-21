package com.thinking.machines.webrock.servlets;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.exceptions.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;	
import com.google.gson.*;
public class TMWebRock extends HttpServlet
{
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
try
{
String URI=request.getRequestURI();
URI=URI.substring(1);
URI=URI.substring(URI.indexOf("/"));
URI=URI.substring(1);
String entityName=URI.split("/")[0];
URI=URI.substring(URI.indexOf("/"));
Map<String,Service> map=(Map<String,Service>)getServletContext().getAttribute("model");
if(map.containsKey(URI)) //url mai agar kuch aayega data toh frr change required rahega /service/student/addStudent?rollnumber=101
{
Service service=map.get(URI);
if(service.getIsGetAllowed()!=false || service.getIsPostAllowed()!=false)
{
if(service.getIsGetAllowed()==false)
{
//send error
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
return;
}
}
requestProcessor(service,request,response,entityName);
}
else
{
response.sendError(HttpServletResponse.SC_NOT_FOUND);
}
}catch(Exception e)
{
System.out.println(e);
}
}

public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
String URI=request.getRequestURI();
URI=URI.substring(1);
URI=URI.substring(URI.indexOf("/"));
URI=URI.substring(1);
String entityName=URI.split("/")[0];
URI=URI.substring(URI.indexOf("/"));
Map<String,Service> map=(Map<String,Service>)getServletContext().getAttribute("model");
if(map.containsKey(URI))
{
Service service=map.get(URI);
if(service.getIsGetAllowed()!=false || service.getIsPostAllowed()!=false)
{
if(service.getIsPostAllowed()==false)
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
return;
}
}
requestProcessor(service,request,response,entityName);
}
else
{
response.sendError(HttpServletResponse.SC_NOT_FOUND);
}
}catch(Exception e)
{
System.out.println(e);
}
}

public void requestProcessor(Service service,HttpServletRequest request,HttpServletResponse response,String entityName)
{
Gson gg=new Gson();
try
{
String contentType=request.getContentType();
Map<String,Service> map=(Map<String,Service>)getServletContext().getAttribute("model");
Class serviceClass=service.getServiceClass();
Method serviceMethod=service.getServiceMethod();
Class property;
Object object;
String methodName;
String propertyName;
String firstLetter;
String remainingLetter;
Method setterMethod;
Object instance=serviceClass.newInstance();
ArrayList<FieldHandler> fieldHandlerList=service.getFieldHandlerList(); 
ArrayList<FieldHandler> requestParameterInjectInFieldList=service.getRequestParameterInjectInFieldList();
if(service.getInjectSessionScope())
{
HttpSession session=request.getSession();
SessionScope sessionScope=new SessionScope(session);
Method setSessionScope=serviceClass.getMethod("setSessionScope",SessionScope.class);
if(setSessionScope!=null) setSessionScope.invoke(instance,sessionScope);
}

if(service.getInjectApplicationScope())
{
ServletContext servletContext=getServletContext();
ApplicationScope applicationScope=new ApplicationScope(servletContext);
Method setApplicationScope=serviceClass.getMethod("setApplicationScope",ApplicationScope.class);
if(setApplicationScope!=null) setApplicationScope.invoke(instance,applicationScope);
}

if(service.getInjectRequestScope())
{
RequestScope requestScope=new RequestScope(request);
Method setRequestScope=serviceClass.getMethod("setRequestScope",RequestScope.class);
if(setRequestScope!=null) setRequestScope.invoke(instance,requestScope);
}

if(service.getInjectApplicationDirectory())
{
File directory= new File(getServletContext().getRealPath(""));
ApplicationDirectory applicationDirectory=new ApplicationDirectory(directory);
Method setApplicationDirectory=serviceClass.getMethod("setApplicationDirectory",ApplicationDirectory.class);
setApplicationDirectory.invoke(instance,applicationDirectory);
}

//AutoWiring
for(FieldHandler fieldHandler:fieldHandlerList)
{
property=fieldHandler.getProperty().getType();
object=request.getAttribute(fieldHandler.getName());
if(object==null) object=request.getSession().getAttribute(fieldHandler.getName());
if(object==null) object=getServletContext().getAttribute(fieldHandler.getName());
if(object!=null)
{
if(property.isInstance(object))
{
propertyName=fieldHandler.getProperty().getName();
firstLetter=propertyName.substring(0,1).toUpperCase();
remainingLetter=propertyName.substring(1);
methodName="set"+firstLetter+remainingLetter;
Class params[]=new Class[1];
params[0]=property;
setterMethod=serviceClass.getMethod(methodName,params);
setterMethod.invoke(instance,object);//replaced with instance if required
}
}
}

//InjectRequestParameter
for(FieldHandler fieldHandler:requestParameterInjectInFieldList)
{
String v=request.getParameter(fieldHandler.getName());
if(v!=null)
{
property=fieldHandler.getProperty().getType();
propertyName=fieldHandler.getProperty().getName();
firstLetter=propertyName.substring(0,1).toUpperCase();
remainingLetter=propertyName.substring(1);
methodName="set"+firstLetter+remainingLetter;
Class params[]=new Class[1];
params[0]=property;
setterMethod=serviceClass.getMethod(methodName,params);
Object obj;
String type=property.getSimpleName();
if(type.equalsIgnoreCase("INT"))
{
obj=Integer.parseInt(v);
}
else if(type.equalsIgnoreCase("LONG"))
{
obj=Long.parseLong(v);
}
else if(type.equalsIgnoreCase("SHORT"))
{
obj=Short.parseShort(v);
}
else if(type.equalsIgnoreCase("BYTE"))
{
obj=Byte.parseByte(v);
}
else if(type.equalsIgnoreCase("FLOAT"))
{
obj=Float.parseFloat(v);
}
else if(type.equalsIgnoreCase("DOUBLE"))
{
obj=Double.parseDouble(v);
}
else if(type.equalsIgnoreCase("CHAR"))
{
obj=v.charAt(0);
}
else if(type.equalsIgnoreCase("BOOLEAN"))
{
obj=Boolean.parseBoolean(v);
}
else if(type.equalsIgnoreCase("String"))
{
obj=v;
}
else
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("InjectRequestParameter should be applied on properties having types --> int,long,short,byte,float,double,char,boolean,String");
}
setterMethod.invoke(instance,obj);
}
else
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("RequestParameter that is supposed to be inject in field not found in querystring");
}
}

//SecuredAccess
if(service.isSecuredAccess())
{
SecuredAccessService sas=service.getSecuredAccessService();
Class clazz=sas.getClazz();
Method method=sas.getMethod();
Parameter parameters[]=method.getParameters();
Object i=clazz.newInstance();
if(sas.isInjectApplicationScope())
{
ServletContext servletContext=getServletContext();
ApplicationScope applicationScope=new ApplicationScope(servletContext);
Method setApplicationScope=clazz.getMethod("setApplicationScope",ApplicationScope.class);
if(setApplicationScope!=null) setApplicationScope.invoke(i,applicationScope);
}
if(sas.isInjectRequestScope())
{
RequestScope requestScope=new RequestScope(request);
Method setRequestScope=clazz.getMethod("setRequestScope",RequestScope.class);
if(setRequestScope!=null) setRequestScope.invoke(i,requestScope);
}
if(sas.isInjectSessionScope())
{
HttpSession session=request.getSession();
SessionScope sessionScope=new SessionScope(session);
Method setSessionScope=clazz.getMethod("setSessionScope",SessionScope.class);
if(setSessionScope!=null) setSessionScope.invoke(i,sessionScope);
}
if(sas.isInjectApplicationDirectory())
{
File directory= new File(getServletContext().getRealPath(""));
ApplicationDirectory applicationDirectory=new ApplicationDirectory(directory);
Method setApplicationDirectory=clazz.getMethod("setApplicationDirectory",ApplicationDirectory.class);
setApplicationDirectory.invoke(i,applicationDirectory);
}
if(parameters.length==0)
{
try
{
method.invoke(i);
}catch(Exception e)
{
//response.sendError(HttpServletResponse.SC_NOT_FOUND); //404 
throw new ServiceException("Not accessible");
}
}
else
{
Object oo[]=new Object[parameters.length];
int e=0;
for(Parameter p:parameters)
{
if(p.getAnnotation(com.thinking.machines.webrock.annotations.RequestParameter.class)!=null)
{
//response.sendError(HttpServletResponse.SC_NOT_FOUND); //404 
throw new ServiceException("You cannot use RequestParameter over parameter in the method which you specified against guard in SecuredAccess");
}
else if(p.getType().getSimpleName().equals("ApplicationScope"))
{
oo[e]=new ApplicationScope(getServletContext());
}
else if(p.getType().getSimpleName().equals("SessionScope"))
{
oo[e]=new SessionScope(request.getSession());
}
else if(p.getType().getSimpleName().equals("RequestScope"))
{
oo[e]=new RequestScope(request);
}
else if(p.getType().getSimpleName().equals("ApplicationDirectory"))
{
File dir= new File(getServletContext().getRealPath(""));
oo[e]=new ApplicationDirectory(dir);
}
else
{
//response.sendError(HttpServletResponse.SC_NOT_FOUND); //404 
throw new ServiceException("You can use only these types ApplicationScope,SessionScope,RequestScope,ApplicationDirectory as parameter in the method which you specified against guard in SecuredAccess");
}
e++;
}//for ends
try
{
method.invoke(i,oo);
}catch(Exception ee)
{
//response.sendError(HttpServletResponse.SC_NOT_FOUND); //404 
throw new ServiceException("Not accessible");
}
}
}

Object result=null;
Object value=request.getAttribute("$");
Object params[];
if(value!=null)
{
if(serviceMethod.getParameters().length==0)
{
result=serviceMethod.invoke(instance);
}
else if(serviceMethod.getParameters().length==1)
{
String type=serviceMethod.getParameters()[0].getType().getName();
if(type.equalsIgnoreCase("INT")||type.equalsIgnoreCase("LONG")||type.equalsIgnoreCase("SHORT")||type.equalsIgnoreCase("FLOAT")||type.equalsIgnoreCase("DOUBLE")||type.equalsIgnoreCase("BYTE")||type.equalsIgnoreCase("CHAR")||type.equalsIgnoreCase("BOOLEAN")||type.equalsIgnoreCase("STRING"))
{
result=serviceMethod.invoke(instance,value);
}
else if((serviceMethod.getParameters()[0]).getType().isInstance(value))
{
result=serviceMethod.invoke(instance,value);
}
else
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("Invalid type");
}
}
else 
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("More than one parameter");
}
}
else
{
//apply changes   application/json
ArrayList<ParameterHandler> parameterHandlerList=service.getParameterHandlerList();
if(contentType==null || contentType.equalsIgnoreCase("application/x-www-form-urlencoded"))
{
if(parameterHandlerList.size()==0)
{
result=serviceMethod.invoke(instance);
}
else
{
params=new Object[parameterHandlerList.size()];
int i=0;
for(ParameterHandler ph:parameterHandlerList)
{
String name=ph.getName();
String type=ph.getType();

boolean isRequestParameter=ph.isRequestParameter();
if(isRequestParameter==true)
{

String val=request.getParameter(name);
if(val!=null)
{
if(type.equalsIgnoreCase("INT"))
{
params[i]=Integer.parseInt(val);
}
else if(type.equalsIgnoreCase("LONG"))
{
params[i]=Long.parseLong(val);
}
else if(type.equalsIgnoreCase("SHORT"))
{
params[i]=Short.parseShort(val);
}
else if(type.equalsIgnoreCase("BYTE"))
{
params[i]=Byte.parseByte(val);
}
else if(type.equalsIgnoreCase("FLOAT"))
{
params[i]=Float.parseFloat(val);
}
else if(type.equalsIgnoreCase("DOUBLE"))
{
params[i]=Double.parseDouble(val);
}
else if(type.equalsIgnoreCase("CHAR"))
{
params[i]=val.charAt(0);
}
else if(type.equalsIgnoreCase("BOOLEAN"))
{
params[i]=Boolean.parseBoolean(val);
}
else if(type.equalsIgnoreCase("String"))
{
params[i]=val;
}
else
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("RequestParameter annotation should be applied on type int,long,short,byte,float,double,char,boolean,String");
}
}
else
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("name not found in querystring");
}

}
else
{
if(type.equals("ApplicationScope"))
{
params[i]=new ApplicationScope(getServletContext());
}
else if(type.equals("SessionScope"))
{
params[i]=new SessionScope(request.getSession());
}
else if(type.equals("RequestScope"))
{
params[i]=new RequestScope(request);
}
else if(type.equals("ApplicationDirectory"))
{
File dir= new File(getServletContext().getRealPath(""));
//ApplicationDirectory applicationDirectory=new ApplicationDirectory(directory);
params[i]=new ApplicationDirectory(dir);
}
else
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("ApplicationScope,RequestScope,SessionScope,ApplicationDirectory only these types are allowed without RequestParameter annotation");
}
}
i++;
}//for ends
result=serviceMethod.invoke(instance,params);
}//else ends
}//new if content type
else
{
//json wala kaam
BufferedReader br;
StringBuffer sb;
String d;
String rawData;
Gson gson;
Object o;
br=request.getReader();
sb=new StringBuffer();
while(true)
{
d=br.readLine();
if(d==null) break;
sb.append(d);
}
rawData=sb.toString();
gson=new Gson();
o=gson.fromJson(rawData,parameterHandlerList.get(0).getParameter());


if(parameterHandlerList.size()==0)
{
result=serviceMethod.invoke(instance);
}
else if(parameterHandlerList.size()==1)
{
result=serviceMethod.invoke(instance,o);
}
else
{
int count=0;
int size;
for(ParameterHandler ph:parameterHandlerList)
{
if(ph.isRequestParameter())
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("RequestParameter annotation not allowed in such scenario");
}
if(ph.getType().equals("RequestScope")) count++;
if(ph.getType().equals("ApplicationScope")) count++;
if(ph.getType().equals("SessionScope")) count++;
if(ph.getType().equals("ApplicationDirectory")) count++;
}
size=parameterHandlerList.size();
if(size-count!=1)
{
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
throw new ServiceException("Only one extra type is allowed with ApplicationScope,RequestScope,SessionScope,ApplicationDirectory");
}
params=new Object[size];
int i=0;
for(ParameterHandler ph:parameterHandlerList)
{
if(ph.getType().equals("ApplicationScope"))
{
params[i]=new ApplicationScope(getServletContext());
}
else if(ph.getType().equals("SessionScope"))
{
params[i]=new SessionScope(request.getSession());
}
else if(ph.getType().equals("RequestScope"))
{
params[i]=new RequestScope(request);
}
else if(ph.getType().equals("ApplicationDirectory"))
{
File dir= new File(getServletContext().getRealPath(""));
//ApplicationDirectory applicationDirectory=new ApplicationDirectory(directory);
params[i]=new ApplicationDirectory(dir);
}
else
{
params[i]=o;
}
i++;
}
result=serviceMethod.invoke(instance,params);
}
}



}//apply change else

int flag=0;
if(service.getForwardTo()!=null)
{
String forwardURL=request.getRequestURL().toString().substring(0,32);
String requestURL[]=service.getForwardTo().split("/");
if(map.containsKey(service.getForwardTo()))
{
if(result!=null)
{
request.setAttribute("$",result);
}
else
{
//request.setAttribute("$","$");
}
//RequestDispatcher requestDispatcher=request.getRequestDispatcher("/service"+service.getForwardTo());
RequestDispatcher requestDispatcher=request.getRequestDispatcher("/"+entityName+service.getForwardTo());
requestDispatcher.forward(request,response);
}
else
{
flag=1;
RequestDispatcher requestDispatcher=request.getRequestDispatcher("/"+service.getForwardTo().split("/")[2]);
requestDispatcher.forward(request,response);
}
}

if(flag==0)
{
PrintWriter pw=response.getWriter();
response.setContentType("application/json");
ServiceResponse serviceResponse=new ServiceResponse();
serviceResponse.setIsSuccess(true);
serviceResponse.setException(null);
serviceResponse.setResult(result);
pw.println(gg.toJson(serviceResponse));
pw.flush();
}

}catch(Exception e)
{
try
{
PrintWriter pw=response.getWriter();
response.setContentType("application/json");
ServiceResponse serviceResponse=new ServiceResponse();
serviceResponse.setIsSuccess(false);
serviceResponse.setException(e);
serviceResponse.setResult(null);
pw.println(gg.toJson(serviceResponse));
pw.flush();
}catch(Exception ee)
{
//do nothing
}
}
}
}