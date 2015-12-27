package util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCConnection {

  public Connection getConnection() {

	System.out.println("-------- MySQL JDBC Connection Testing ------------");

	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return null;
	}

	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;

	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://localhost:3306/insurance_broker","root", "root");

	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return null;
	}

	if (connection != null) {
		System.out.println("You made it, take control your database now!");
		return connection;
	} else {
		System.out.println("Failed to make connection!");
		return null;
	}
  }
}