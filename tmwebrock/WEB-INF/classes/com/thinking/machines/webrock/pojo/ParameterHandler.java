package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
public class ParameterHandler
{
private String type;
private String name;
private boolean isRequestParameter;
private Class parameter;
public ParameterHandler()
{
type="";
name="";
isRequestParameter=false;
parameter=null;
}
public void setParameter(Class parameter)
{
this.parameter=parameter;
}
public Class getParameter()
{
return this.parameter;
}
public void setType(String type)
{
this.type=type;
}
public String getType()
{
return this.type;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void isRequestParameter(boolean isRequestParameter)
{
this.isRequestParameter=isRequestParameter;
}
public boolean isRequestParameter()
{
return this.isRequestParameter;
}
}