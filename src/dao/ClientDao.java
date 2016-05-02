package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDao {
	private Connection connectToDB() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/server", "root", "root");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public Client registerClient() throws SQLException {
		Client registar = null;
		String sql = "INSERT INTO client ( userName, password)" + " VALUES ( ?, ?)";
		java.util.Scanner input = new java.util.Scanner(System.in);
		System.out.print(" Enter your username: ");
		String username = input.next();

		System.out.print(" Enter your password: ");
		String password = input.next();

		try (Connection conn = connectToDB(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			input.close();
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					registar = new Client();

					registar.setUserName(rs.getString("userName"));
					registar.setPassword(rs.getString("password"));

				}

			}
			return registar;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return registar;
	}

	public Client loginClient(String userName, String password) {
		Client login = null;
		String sql = "SELECT * FROM client WHERE username = ? AND password = ?";
		try (Connection conn = connectToDB(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, userName);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					login = new Client();
					login.setId(rs.getInt("id"));
					login.setUserName(rs.getString("userName"));
					login.setPassword(rs.getString("password"));

				}
			}
			return login;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return login;
	}

	public boolean itClientExist(String username) {
		String query = "SELECT * FROM client WHERE username = ?";
		boolean existing = false;
		try (Connection conn = connectToDB(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				existing = rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existing;
	}

	public boolean isLoginClient(String username, String password) {
		String prep = "SELECT * FROM client WHERE userName = ? AND password = ?";
		boolean isLogin = false;
		try (Connection conn = connectToDB(); PreparedStatement ps = conn.prepareStatement(prep)) {

			ps.setString(1, username);
			ps.setString(2, password);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					isLogin = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isLogin;
	}

	public void validateUsername(String userName) {
		if (userName.length() < 4) {
			System.out.println("Username must be at least 4 character long!");
		}
	}

	public void validatePassword(String password) {
		if (password.length() < 4) {
			System.out.println("Password must be at least 4 character long!");
		}

	}
}
