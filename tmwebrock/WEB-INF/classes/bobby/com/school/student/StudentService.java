package bobby.com.school.student;
import com.thinking.machines.webrock.annotations.*;
import java.util.*;
import java.sql.*;
import com.google.gson.*;
@Path("/studentService")
public class StudentService
{
@FORWARD("/getAll")
@Path("/add")
@POST
public void addStudent(Student student) throws Exception
{
try
{
Connection connection;
Class.forName("com.mysql.cj.jdbc.Driver");
connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/stddb","std","std");
int rollNumber=student.getRollNumber();
String name=student.getName();
String gender=student.getGender();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("Select gender from student where roll_number=?");
preparedStatement.setInt(1,rollNumber);
ResultSet resultSet;
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new Exception("Roll number exists.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("insert into student (roll_number,name,gender) values(?,?,?)");
preparedStatement.setInt(1,rollNumber);
preparedStatement.setString(2,name);
preparedStatement.setString(3,gender);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
throw new Exception(exception.getMessage());
}
}
@Path("/update")
@POST
public void updateStudent(Student student) throws Exception
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/stddb","std","std");
int rollNumber=student.getRollNumber();
String name=student.getName();
String gender=student.getGender();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select gender from student where roll_number=?");
preparedStatement.setInt(1,rollNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(!resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new Exception("Invalid rollNumber");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("update student set name=?,gender=? where roll_number=?");
preparedStatement.setString(1,name);
preparedStatement.setString(2,gender);
preparedStatement.setInt(3,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
throw new Exception(exception.getMessage());
}
}
@Path("/delete")
public void deleteStudent(@RequestParameter("rollNumber") int rollNumber) throws Exception
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/stddb","std","std");
PreparedStatement preparedStatement=connection.prepareStatement("select gender from student where roll_number=?");
preparedStatement.setInt(1,rollNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(!resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new Exception("Invalid roll number.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from student where roll_number=?");
preparedStatement.setInt(1,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception exception)
{
throw new Exception(exception.getMessage());
}
}
@Path("/getByRollNumber")
@GET
public Student getByRollNumber(@RequestParameter("rollNumber") int rollNumber) throws Exception
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/stddb","std","std");
PreparedStatement preparedStatement=connection.prepareStatement("select * from student where roll_number=?");
preparedStatement.setInt(1,rollNumber);
ResultSet resultSet=preparedStatement.executeQuery();
Student student;
if(resultSet.next())
{
student=new Student();
student.setRollNumber(resultSet.getInt("roll_number"));
student.setName(resultSet.getString("name"));
student.setGender(resultSet.getString("gender"));
}
else
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new Exception("Invalid roll number.");
}
resultSet.close();
preparedStatement.close();
connection.close();
return student;
}catch(Exception exception)
{
throw new Exception(exception.getMessage());
}
}
@Path("/getAll")
public String getAll() throws Exception
{
try
{
List<Student> students=new LinkedList<>();
Class.forName("com.mysql.cj.jdbc.Driver");
Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/stddb","std","std");
PreparedStatement preparedStatement=connection.prepareStatement("select * from student");
ResultSet resultSet=preparedStatement.executeQuery();
Student student;
while(resultSet.next())
{
student=new Student();
student.setRollNumber(resultSet.getInt("roll_number"));
student.setName(resultSet.getString("name"));
student.setGender(resultSet.getString("gender"));
students.add(student);
}
resultSet.close();
preparedStatement.close();
connection.close();
Gson gson=new Gson();
return gson.toJson(students);
}catch(Exception exception)
{
throw new Exception(exception.getMessage());
}
}
}