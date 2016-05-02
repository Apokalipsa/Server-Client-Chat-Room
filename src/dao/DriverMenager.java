package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverMenager {

	// instance it self
	private static DriverMenager instance = null;

	// DB credentials
	private final String USERNAME = "root";
	private final String PASSWORD = "root";
	private final String CONN_STRING = "jdbc:mysql://localhost/server";

	// connection object
	private Connection connection = null;

	// private constructor - can only be constructed from within this class
	private DriverMenager() {

	}

	// check if instance is null, instantiate and return or return
	public static DriverMenager getInstance() {
		if (instance == null) {
			instance = new DriverMenager();
		}
		return instance;
	}

	private boolean openConnection() {
		try {
			connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			return true;
		} catch (SQLException e) {
			System.err.println(e);
			return false;
		}
	}

	public Connection getConnection() {
		if (connection == null) {
			if (openConnection()) {
				System.out.println("Connection opened");
				return connection;
			} else {
				return null;
			}
		}
		return connection;
	}

	public void close() {
		System.out.println("Closing connection");
		try {
			connection.close();
			connection = null;
		} catch (Exception e) {
		}
	}

}
