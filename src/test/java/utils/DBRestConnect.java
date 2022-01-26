package utils;

import java.io.IOException;
import java.sql.*;

public class DBRestConnect {

	// Declaration of the variables

	private final String url = "jdbc:postgresql://localhost:5432/LMS_DB";
	private final String ID = "postgres";
	private final String password = "admin";
	public static String fname = null;
	public ResultSet rs = null;
	// Method to initalize connection to the database and execute query

	public String connect(String dbquery, String expectedAssertString) throws ClassNotFoundException {
		System.out.println("Inside connet");
		String actualassertString = null;

		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("set classforname");
			Connection conn = DriverManager.getConnection(url, ID, password);
			{
				if (conn != null) {

					// PreparedStatement pst = conn.prepareStatement("SELECT * FROM tbl_lms_user
					// where user_first_name='Maria'");
					PreparedStatement pst = conn.prepareStatement(dbquery);

					rs = pst.executeQuery();
					{
						while (rs.next()) {
							actualassertString = rs.getString(expectedAssertString);
						}
					}

				} else
					System.out.println("Failed to connect");
			}

		} catch (

		SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		return actualassertString;
	}

}
