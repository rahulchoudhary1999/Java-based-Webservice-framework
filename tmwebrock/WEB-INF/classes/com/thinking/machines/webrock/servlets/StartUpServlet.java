package com.thinking.machines.webrock.servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.annotations.*;
public class StartUpServlet extends HttpServlet
{
private List<String> classList; //packages name like bobby.com.thinking.machines.Student
private List<Service> onStartUpServicesList; //List of Service class Object having priority annotation applied on methods
private String startPackagePrefix;
private Map<String,Service> map;
public void init()
{
try{
this.map=new HashMap<>();
this.classList=new LinkedList<>();
this.onStartUpServicesList=new ArrayList<>();
this.startPackagePrefix=getServletConfig().getInitParameter("SERVICE_PACKAGE_PREFIX");
String maindirpath=getServletContext().getRealPath("")+File.separator+"WEB-INF"+File.separator+"classes"+File.separator+this.startPackagePrefix;
File maindir = new File(maindirpath); 
if(maindir.exists() && maindir.isDirectory()) 
{ 
File arr[] = maindir.listFiles(); 
recursivePrint(arr,0,0);
}
//list pe iterate krenege
Class c;
Method methods[];
Field fields[];
String url;
Path a,b;
Service service;
GET getAnnotationFoundOnClass;
POST postAnnotationFoundOnClass;
GET getAnnotationFoundOnMethod;
POST postAnnotationFoundOnMethod;
FORWARD forwardToAnnotation;
OnStartUp onStartUpAnnotation;
InjectApplicationDirectory injectApplicationDirectory;
InjectSessionScope injectSessionScope;
InjectApplicationScope injectApplicationScope;
InjectRequestScope injectRequestScope;
InjectRequestParameter injectRequestParameter;
AutoWired autoWired;
RequestParameter requestParameter;
ArrayList<FieldHandler> fieldHandlerList;
ArrayList<ParameterHandler> parameterHandlerList;
ArrayList<FieldHandler> requestParameterInjectInFieldList;
SecuredAccess securedAccessAnnotation=null;
for(String className:classList)
{
c=Class.forName(className);
a=(Path)c.getAnnotation(com.thinking.machines.webrock.annotations.Path.class);
getAnnotationFoundOnClass=(GET)c.getAnnotation(com.thinking.machines.webrock.annotations.GET.class);
postAnnotationFoundOnClass=(POST)c.getAnnotation(com.thinking.machines.webrock.annotations.POST.class);
injectApplicationDirectory=(InjectApplicationDirectory)c.getAnnotation(com.thinking.machines.webrock.annotations.InjectApplicationDirectory.class);
injectSessionScope=(InjectSessionScope)c.getAnnotation(com.thinking.machines.webrock.annotations.InjectSessionScope.class);
injectApplicationScope=(InjectApplicationScope)c.getAnnotation(com.thinking.machines.webrock.annotations.InjectApplicationScope.class);
injectRequestScope=(InjectRequestScope)c.getAnnotation(com.thinking.machines.webrock.annotations.InjectRequestScope.class);
securedAccessAnnotation=(SecuredAccess)c.getAnnotation(com.thinking.machines.webrock.annotations.SecuredAccess.class);
fields=c.getDeclaredFields();
FieldHandler fieldHandler;
fieldHandlerList=new ArrayList<>();
requestParameterInjectInFieldList=new ArrayList<>();
for(Field field:fields)
{
autoWired=(AutoWired)field.getAnnotation(com.thinking.machines.webrock.annotations.AutoWired.class);
injectRequestParameter=(InjectRequestParameter)field.getAnnotation(com.thinking.machines.webrock.annotations.InjectRequestParameter.class);
if(autoWired!=null)
{
fieldHandler=new FieldHandler();
fieldHandler.setProperty(field);
fieldHandler.setName(autoWired.name());
fieldHandlerList.add(fieldHandler);
}
if(injectRequestParameter!=null)
{
fieldHandler=new FieldHandler();
fieldHandler.setProperty(field);
fieldHandler.setName(injectRequestParameter.value());
requestParameterInjectInFieldList.add(fieldHandler);
}
}
methods=c.getDeclaredMethods();
for(Method m:methods)
{
b=(Path)m.getAnnotation(com.thinking.machines.webrock.annotations.Path.class);
if(b!=null)
{
url=a.value()+b.value();
Parameter parameters[]=m.getParameters();
parameterHandlerList=new ArrayList<>();
ParameterHandler parameterHandler;
for(Parameter parameter:parameters)
{
requestParameter=(RequestParameter)parameter.getAnnotation(com.thinking.machines.webrock.annotations.RequestParameter.class);
parameterHandler=new ParameterHandler();
if(requestParameter!=null)
{
parameterHandler.setType(parameter.getType().getSimpleName());
parameterHandler.setParameter(parameter.getType());
parameterHandler.setName(requestParameter.value());
parameterHandler.isRequestParameter(true);
parameterHandlerList.add(parameterHandler);
}
else
{
parameterHandler.setType(parameter.getType().getSimpleName());
parameterHandler.setParameter(parameter.getType());
parameterHandler.setName("");
parameterHandler.isRequestParameter(false);
parameterHandlerList.add(parameterHandler);
}
}
service=new Service();
service.setPath(url);
service.setServiceClass(c);
service.setServiceMethod(m); 
service.setFieldHandlerList(fieldHandlerList);
service.setParameterHandlerList(parameterHandlerList);
service.setRequestParameterInjectInFieldList(requestParameterInjectInFieldList);
if(securedAccessAnnotation==null)
{
securedAccessAnnotation=(SecuredAccess)m.getAnnotation(com.thinking.machines.webrock.annotations.SecuredAccess.class);
}
if(securedAccessAnnotation==null)
{
service.isSecuredAccess(false);
service.setSecuredAccessService(null);
}
else
{
SecuredAccessService securedAccessService=new SecuredAccessService();
Class clazz=Class.forName(securedAccessAnnotation.checkPost());
Method m2=null;
Method m3[]=clazz.getMethods();
for(Method m1:m3)
{
if(m1.getName().equals(securedAccessAnnotation.guard()))
{
m2=m1;
break;
}
}
securedAccessService.setClazz(clazz);
securedAccessService.setMethod(m2);
if(clazz!=null)
{
if(clazz.getAnnotation(com.thinking.machines.webrock.annotations.InjectRequestScope.class)!=null) securedAccessService.isInjectRequestScope(true);
if(clazz.getAnnotation(com.thinking.machines.webrock.annotations.InjectSessionScope.class)!=null)securedAccessService.isInjectSessionScope(true);
if(clazz.getAnnotation(com.thinking.machines.webrock.annotations.InjectApplicationScope.class)!=null)securedAccessService.isInjectApplicationScope(true);
if(clazz.getAnnotation(com.thinking.machines.webrock.annotations.InjectApplicationDirectory.class)!=null)securedAccessService.isInjectApplicationDirectory(true);
}
service.isSecuredAccess(true);
service.setSecuredAccessService(securedAccessService);
}//else ends
if(injectApplicationDirectory!=null)service.setInjectApplicationDirectory(true);
if(injectSessionScope!=null)service.setInjectSessionScope(true);   
if(injectApplicationScope!=null)service.setInjectApplicationScope(true);   
if(injectRequestScope!=null)service.setInjectRequestScope(true);                                                      
forwardToAnnotation=(FORWARD)m.getAnnotation(com.thinking.machines.webrock.annotations.FORWARD.class);
if(forwardToAnnotation!=null)
{
service.setForwardTo(a.value()+forwardToAnnotation.value());
}
if(getAnnotationFoundOnClass==null && postAnnotationFoundOnClass==null)
{
getAnnotationFoundOnMethod=(GET)m.getAnnotation(com.thinking.machines.webrock.annotations.GET.class);
postAnnotationFoundOnMethod=(POST)m.getAnnotation(com.thinking.machines.webrock.annotations.POST.class);
if(getAnnotationFoundOnMethod!=null)
{
service.setIsGetAllowed(true);
}
if(postAnnotationFoundOnMethod!=null)
{
service.setIsPostAllowed(true);
}
}
else
{
if(getAnnotationFoundOnClass!=null)
{
service.setIsGetAllowed(true);
service.setIsPostAllowed(false);
}
if(postAnnotationFoundOnClass!=null)
{
service.setIsPostAllowed(true);
service.setIsGetAllowed(false);
}
}
onStartUpAnnotation=(OnStartUp)m.getAnnotation(com.thinking.machines.webrock.annotations.OnStartUp.class); 
if(onStartUpAnnotation!=null)
{
Class returnType=m.getReturnType();
Parameter params[]=m.getParameters();
if(returnType.getName().equals("void") && params.length==0)
{
service.setRunOnStartUp(url);
service.setPriority(onStartUpAnnotation.priority());
onStartUpServicesList.add(service);
}
else
{
System.out.println("Invalid return type: "+returnType.getName()+" , invalid parameter value: "+params.length);
}
}
map.put(url,service);
}
}
}
//put map in appicationscope
getServletContext().setAttribute("model",map);
int z;

//list ko sort krdiya ascending order mai
Service ss;
for(int y=1;y<onStartUpServicesList.size();y++)
{
ss=onStartUpServicesList.get(y);
z=y-1;
while(z>=0 && ss.getPriority()<onStartUpServicesList.get(z).getPriority())
{
onStartUpServicesList.set(z+1,onStartUpServicesList.get(z));
z--;
}
onStartUpServicesList.set(z+1,ss);	
}

for(Service s:onStartUpServicesList)
{
Class serviceClass=s.getServiceClass();
Method serviceMethod=s.getServiceMethod();
serviceMethod.invoke(serviceClass.newInstance());
}
}catch(Exception e)
{
System.out.println("Exception"+e);
}
}
public void recursivePrint(File[] arr,int index,int level)
{
// terminate condition 
if(index==arr.length) return; 
if(arr[index].isFile()) 
{
if(arr[index].getName().endsWith(".class") )
{
String packageName=arr[index].getAbsolutePath();
int index1=packageName.indexOf(this.startPackagePrefix);
packageName=packageName.substring(index1,packageName.length()-6).replace("\\",".");
classList.add(packageName);
}
}       
else if(arr[index].isDirectory()) 
{ 
recursivePrint(arr[index].listFiles(), 0, level + 1); 
} 
recursivePrint(arr,++index, level); 
}
}