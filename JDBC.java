import java.sql.*;

import java.util.*;

class Main {

public static void main(String[] args) {

Scanner sc = new Scanner(System.in);

try {

Class.forName("com.mysql.cj.jdbc.Driver");

Connection con = DriverManager.getConnection(

"jdbc:mysql://localhost:3306/company", "root", "root");

Statement st = con.createStatement();

st.executeUpdate(

"CREATE TABLE IF NOT EXISTS employee(" +

"id INT PRIMARY KEY, name VARCHAR(30), salary DOUBLE)");

// CREATE

System.out.print("Enter ID: ");

int id = sc.nextInt();

System.out.print("Enter Name: ");

String name = sc.next();

System.out.print("Enter Salary: ");

double salary = sc.nextDouble();

PreparedStatement p = con.prepareStatement(

"INSERT INTO employee VALUES(?,?,?)");

p.setInt(1, id);

p.setString(2, name);

p.setDouble(3, salary);

p.executeUpdate();

System.out.println("Employee Added");

// READ

System.out.println("\nEmployee Records:");

ResultSet rs = st.executeQuery("SELECT * FROM employee");

while (rs.next())

System.out.println(rs.getInt(1) + " " +

rs.getString(2) + " " +

rs.getDouble(3));

// UPDATE

System.out.print("\nEnter ID to update: ");

id = sc.nextInt();

System.out.print("Enter new salary: ");

salary = sc.nextDouble();

p = con.prepareStatement(

"UPDATE employee SET salary=? WHERE id=?");

p.setDouble(1, salary);

p.setInt(2, id);

p.executeUpdate();

System.out.println("Employee Updated");

// DELETE

System.out.print("\nEnter ID to delete: ");

id = sc.nextInt();

p = con.prepareStatement(

"DELETE FROM employee WHERE id=?");

p.setInt(1, id);

p.executeUpdate();

System.out.println("Employee Deleted");

con.close();

} catch (Exception e) {

System.out.println(e)
}
}
}
