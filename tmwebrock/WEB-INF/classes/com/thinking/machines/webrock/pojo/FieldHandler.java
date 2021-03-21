package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
public class FieldHandler
{
private Field property;
private String name;
public FieldHandler()
{
property=null;
name="";
}
public void setProperty(Field property)
{
this.property=property;
}
public Field getProperty()
{
return this.property;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
}