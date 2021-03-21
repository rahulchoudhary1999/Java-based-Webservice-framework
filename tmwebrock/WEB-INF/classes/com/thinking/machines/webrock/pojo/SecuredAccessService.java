package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
public class SecuredAccessService
{
private Class clazz;
private Method method;
private boolean isInjectRequestScope;
private boolean isInjectSessionScope;
private boolean isInjectApplicationScope;
private boolean isInjectApplicationDirectory;
public SecuredAccessService()
{
this.clazz=null;
this.method=null;
this.isInjectRequestScope=false;
this.isInjectSessionScope=false;
this.isInjectApplicationScope=false;
this.isInjectApplicationDirectory=false;
}
public void setClazz(Class clazz)
{
this.clazz=clazz;
}
public Class getClazz()
{
return this.clazz;
}
public void setMethod(Method method)
{
this.method=method;
}
public Method getMethod()
{
return this.method;
}
public void isInjectRequestScope(boolean isInjectRequestScope)
{
this.isInjectRequestScope=isInjectRequestScope;
}
public boolean isInjectRequestScope()
{
return this.isInjectRequestScope;
}
public void isInjectSessionScope(boolean isInjectSessionScope)
{
this.isInjectSessionScope=isInjectSessionScope;
}
public boolean isInjectSessionScope()
{
return this.isInjectSessionScope;
}
public void isInjectApplicationScope(boolean isInjectApplicationScope)
{
this.isInjectApplicationScope=isInjectApplicationScope;
}
public boolean isInjectApplicationScope()
{
return this.isInjectApplicationScope;
}
public void isInjectApplicationDirectory(boolean isInjectApplicationDirectory)
{
this.isInjectApplicationDirectory=isInjectApplicationDirectory;
}
public boolean isInjectApplicationDirectory()
{
return this.isInjectApplicationDirectory;
}
}